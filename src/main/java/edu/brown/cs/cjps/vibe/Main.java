/**
 * Package of things specific to this project
 */
package edu.brown.cs.cjps.vibe;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.SparkBase;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.User;

import edu.brown.cs.cjps.calendar.CalendarEvent;
import edu.brown.cs.cjps.music.PlaylistHQ;
import edu.brown.cs.cjps.music.SpotifyConverter;
import edu.brown.cs.cjps.music.VibePlaylist;
import freemarker.template.Configuration;

/**
 * Main class just accepts the arguments and creates an InputHandler to deal
 * with them. If the gui is started, Main handles the Spark logistics.
 *
 * @author smayfiel
 *
 */
public final class Main {

  /**
   * Launching point of program.
   *
   * @param args
   *          from command line
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  /**
   * Arguments to the command line.
   */
  private String[] args;
  /**
   * The user's clientID.
   */
  private String clientID;
  /**
   * The secret key for the application.
   */
  private String clientSecret;
  /**
   * The redirection URI for after Spotify authenticates a user.
   */
  private String redirectURI;
  /**
   * An instance variable for the Spotify API.
   */
  private Api api;

  /**
   * Current spotify user
   */
  private User currentUser;

  private PlaylistHQ hq;

  private String accessToken;
  private String refreshToken;
  private EventProcessor eventProcessor;

  /**
   * Google's GSON instance variable that helps with sending and retrieving
   * front-end and back-end requests.
   */
  private final static Gson GSON = new Gson();

  // private final boolean madeRequest = false;

  /**
   * Constructor for our Main class.
   *
   * @param args
   *          - the command line arguments
   */
  private Main(String[] args) {
    this.args = args;
  }

  /**
   * Runs the program.
   */
  private void run() {
    // Instantiate HQ
    hq = new PlaylistHQ();
    eventProcessor = new EventProcessor();

    System.out.println("Starting Vibe...");
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    OptionSet options = parser.parse(args);

    int port = 5555;
    Spark.setPort(port);
    clientSecret = "9b79b8ae6c2a453588a6be84ca9de659";
    clientID = "bfd53bc39d2c46f081fa7951a5a88ea8";
    redirectURI = String.format("http://localhost:%s/playlists", port);
    api = Api.builder().clientId(clientID).clientSecret(clientSecret)
        .redirectURI(redirectURI).build();

    // SOME TEST STUFF
    // this.generatePlaylist();

    // END TEST STUFF

    if (options.has("gui")) {
      // Runs the GUI
      runSparkServer();
    }
  }

  // TODO: Call this method from some sort of handler
  private String generatePlaylist() {

    System.out.println("In g playlist");
    SpotifyConverter spotconv = new SpotifyConverter();

    // NewSpotifyTester t = new NewSpotifyTester(api, currentUser, accessToken);
    // String track = null;
    // List<String> list = new ArrayList<>();
    hq.generateFromTag("test", api, currentUser, accessToken);
    System.out.println("survived generate from tag");
    VibePlaylist p1 = VibeCache.getPlaylistCache().get("test");
    System.out.println("~~~THE TRACKS~~~");
    System.out.println(p1.getTracks());
    // try {
    // list = t.recommendations();
    // } catch (MalformedURLException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // System.out.println("Track from main : " + track.toString());

    // String playlistURI = spotconv.makeSpotifyPlaylist(api, currentUser,
    // list);
    // System.out.println("through generate playlist in main");

    // return playlistURI;
    return null;
  }

  /**
   * Creates a new templating engine for free marker
   *
   * @return a new free marker engine
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable to use %s for template loading.\n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Runs the GUI on Spark's server
   */
  private void runSparkServer() {
    SparkBase.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/vibe", new HomepageHandler(), freeMarker);
    Spark.get("/login", new LoginHandler());
    Spark.get("/playlists", new PlaylistPageHandler(), freeMarker);
    Spark.post("/code", new CodeHandler());
    Spark.post("/newEvent", new AddEventHandler());
    Spark.post("/getPlaylist", new GetPlaylistHandler());
    Spark.post("/deleteEvent", new DeleteEventHandler());
    Spark.post("/editEvent", new EditEventHandler());

  }

  /**
   * Handles creating the GUI for the homepage
   *
   * @author cjps
   *
   */
  private class HomepageHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Vibe");
      return new ModelAndView(variables, "vibe.ftl");
    }
  }

  /**
   * Handles creating the GUI for processing login requests
   *
   * @author cjps
   *
   */
  private class LoginHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      /*
       * Set the necessary scopes that the application will need from the user
       *
       * Vibe can read the user's email and modify public and private playlists
       */
      final List<String> scopes = Arrays.asList("user-read-email",
          "playlist-modify-public", "playlist-modify-private");

      /*
       * Set a state. This is used to prevent cross site request forgeries.
       */
      final String state = "0euk0hmxnc";
      String authorizeURL = api.createAuthorizeURL(scopes, state);

      return GSON.toJson(authorizeURL);
    }
  }

  /**
   * Handles acquiring and utilizing the code for the user to create an access
   * token
   *
   * @author cjps
   *
   */
  private class CodeHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      // Retrieve the code from the front-end to get an access token
      String code = GSON.fromJson(qm.value("code"), String.class);
      /*
       * Make a token request. Asynchronous requests are made with the .getAsync
       * method and synchronous requests are made with the .get method. This
       * holds for all type of requests.
       */
      AuthorizationCodeCredentials acg = null;
      currentUser = null;
      accessToken = "";
      refreshToken = "";

      try {
        acg = api.authorizationCodeGrant(code).build().get();
        accessToken = acg.getAccessToken();
        refreshToken = acg.getRefreshToken();
        // System.out.println("Getme request: " +
        // api.getMe().accessToken(accessToken).build().toString());

        api.setAccessToken(accessToken);
        api.setRefreshToken(refreshToken);
        // System.out.println("Access token : " + acg.getAccessToken());
        currentUser = api.getMe().accessToken(accessToken).build().get();
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("ERROR: issue retrieving current user");
      }

      String display = "";

      if (currentUser.getDisplayName() == null) {
        display = currentUser.getId();
      } else {
        display = currentUser.getDisplayName();
      }

      List<String> params = new ArrayList<>();

      // generatePlaylist();
      String playlistURI = generatePlaylist();
      params.add(display);
      params.add(playlistURI);

      System.out.printf("User: %s\n", display);

      return params;
    }
  }

  /**
   * Handles creating the GUI for processing login requests
   *
   * @author cjps
   *
   */
  private class PlaylistPageHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Vibe");
      return new ModelAndView(variables, "playlists.ftl");
    }
  }

  /**
   *
   * Handles adding an event to a user's calendar.
   *
   */
  private class AddEventHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      // Retrieve event information from the front-end
      String start = qm.value("start");
      Boolean amOrPm = Boolean.parseBoolean(qm.value("startAMPM"));
      String end = qm.value("end");
      Boolean endAmOrPm = Boolean.parseBoolean(qm.value("endAMPM"));
      String eventName = qm.value("name");

      CalendarEvent newEvent = eventProcessor
    		  .addEvent(start, amOrPm, end, endAmOrPm, eventName);

      // Return an event object to the front-end
      return GSON.toJson(newEvent);
    }
  }

  /**
   *
   * Handles retrieving a playlist for a specific event.
   *
   * */
  private class GetPlaylistHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      // Retrieve the event ID and find the associated playlist
      String eventID = qm.value("eventID");
      String uri = hq.convertForSpotify(eventID, api, currentUser);

      System.out.println("Cached playlist: " + uri);

      return uri;
    }
  }

  /**
   *
   * Handles deleting a specific event.
   *
   * */
  private class DeleteEventHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String response = "SUCCESS";

      String eventID = qm.value("eventID");

      eventProcessor.deleteEvent(eventID);

      // TODO: catch an error and store the response if there's an issue

      return response;
    }
  }

  /**
   *
   * Handles editing a specific event.
   *
   * */
  private class EditEventHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String start = qm.value("start");
      Boolean amOrPm = Boolean.parseBoolean(qm.value("startAMPM"));
      String end = qm.value("end");
      Boolean endAMOrPM = Boolean.parseBoolean(qm.value("endAMPM"));
      String eventName = qm.value("name");

      CalendarEvent editedEvent = eventProcessor
    		  .editEvent(start, amOrPm, end, endAMOrPM, eventName);

      return editedEvent;
    }
  }



  /**
   * Handles printing out exceptions to the GUI
   *
   * @author cjps
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
