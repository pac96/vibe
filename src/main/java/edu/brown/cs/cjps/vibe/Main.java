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
import java.util.UUID;

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
import edu.brown.cs.cjps.music.Settings;
import edu.brown.cs.cjps.music.VibePlaylist;
import edu.brown.cs.cjps.vibe.MusicEventTag.Tag;
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

  private boolean receivedCode;
  
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
  
  private String code;

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
    VibeCache vc = new VibeCache();
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

    // String playlistName = "Party";
    // hq.generateFromTag(playlistName, api, currentUser, accessToken);
    // VibePlaylist p2 = VibeCache.getPlaylistCache().get(playlistName);
    // System.out.println("~~~THE TRACKS~~~");
    // System.out.println(p2.getTracks());
    // String playlistURI = hq.convertForSpotify(p2, playlistName, api,
    // currentUser);

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
      receivedCode = false;	
	  Map<String, Object> variables = ImmutableMap.of("title", "Vibe");
	  return new ModelAndView(variables, "vibe.ftl");
    }
  }

  /**
   * Handles creating the GUI for procString playlistName = "Party"; //
   * hq.generateFromTag(playlistName, api, currentUser, accessToken); //
   * VibePlaylist p2 = VibeCache.getPlaylistCache().get(playlistName); //
   * System.out.println("~~~THE TRACKS~~~"); //
   * System.out.println(p2.getTracks()); // String playlistURI =
   * hq.convertForSpotify(p2, playlistName, api, // currentUser);essing login
   * requests
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

      if (!receivedCode) {
    	 
    	// Retrieve the code from the front-end to get an access token
          code = qm.value("code").trim();
          System.out.println("Code: " + code);
          receivedCode = true;
          
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
            System.out.println("API authorization code grant is good");
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
            System.out.println(e.toString());
          }
      }
      
      String display = currentUser.getDisplayName();

      if (display == null) {
    	  display = currentUser.getId();
      }
      System.out.printf("Current User: %s\n", display);        

      return display;
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
      // CalendarEvent newEvent = eventProcessor.addEvent(qm);

      // Retrieve event information from the front-end
      String start = qm.value("start");
      Boolean amOrPm = Boolean.parseBoolean(qm.value("startAMPM"));
      String end = qm.value("end");
      Boolean endAmOrPm = Boolean.parseBoolean(qm.value("endAMPM"));
      String eventName = qm.value("name");

      CalendarEvent newEvent = eventProcessor.addEvent(start, amOrPm, end,
          endAmOrPm, eventName);

//       Generate the playlist associated with this event
//      VibePlaylist p = hq.generateFromTag(newEvent, api, currentUser, accessToken);
//      VibeCache.getPlaylistCache().put(newEvent.getId(), p);
//      hq.generateFromTag(newEvent, api, currentUser, accessToken);
      
//      Settings testSettings = new Settings(Tag.PARTY, Arrays.asList("rock"),
//          0.1f, 10, 0.1f);
//      hq.generateCustom(testSettings, newEvent, api, currentUser, accessToken);

      // These lines are only for testing
//      VibePlaylist p2 = VibeCache.getPlaylistCache().get(newEvent.getId());
//      String tempURI = hq.convertForSpotify(p2, newEvent.getName(), api,
//          currentUser);
//      System.out.println("~~~THE TRACKS~~~");
//      System.out.println(p2.getTracks());
//      System.out.println(p2.getTracks().size());

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
      UUID eventID = UUID.fromString(qm.value("eventID"));
      VibePlaylist playlist = VibeCache.getPlaylistCache().get(eventID);

      // TODO: Need to get the name from the eventID
      String eventName = "event";

      String uri = hq.convertForSpotify(playlist, eventName, api, currentUser);

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

      CalendarEvent editedEvent = eventProcessor.editEvent(start, amOrPm, end,
          endAMOrPM, eventName);

      // Generate the playlist associated with this event
      hq.generateFromTag(editedEvent, api, currentUser, accessToken);

      // These lines are only for testing
      VibePlaylist p2 = VibeCache.getPlaylistCache().get(editedEvent.getId());
      System.out.println("~~~THE TRACKS~~~");
      System.out.println(p2.getTracks());

      return editedEvent;
    }
  }

  /**
   * Class to handle adding custom playlists to events. I'm assuming that the
   * event and the settings will be passed back.
   * 
   * @author smayfiel
   *
   */
  private class AddCustomEventHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      // Event stuff
      String start = qm.value("start");
      Boolean amOrPm = Boolean.parseBoolean(qm.value("startAMPM"));
      String end = qm.value("end");
      Boolean endAMOrPM = Boolean.parseBoolean(qm.value("endAMPM"));
      String eventName = qm.value("name");

      CalendarEvent newEvent = eventProcessor.editEvent(start, amOrPm, end,
          endAMOrPM, eventName);

      // Playlist stuff
      String tag = qm.value("tag");
      String genres = qm.value("genres");
      String energy = qm.value("energy");
      String hotness = qm.value("hotness");
      String mood = qm.value("mood");

      // Add these things to a list
      List<String> settingsList = Arrays.asList(tag, genres, energy, hotness,
          mood);

      // Generate the playlist
      hq.generateCustom(settingsList, newEvent, api, currentUser, accessToken);

      // These lines are only for testing
      VibePlaylist p2 = VibeCache.getPlaylistCache().get(newEvent.getId());
      System.out.println("~~~THE TRACKS~~~");
      System.out.println(p2.getTracks());

      // TODO: I have no idea what this should return
      return newEvent;
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
