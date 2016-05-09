/**
 * Package of things specific to this project
 */
package edu.brown.cs.cjps.vibe;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.User;

import edu.brown.cs.cjps.calendar.CalendarEvent;
import edu.brown.cs.cjps.db.UserDBCreator;
import edu.brown.cs.cjps.music.PlaylistHQ;
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

    // Grab the database from the command line args.
    String db = args[0];

    UserDBCreator dbCreator = null;

    try {
      dbCreator = new UserDBCreator(db);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }

    // Run should take in a database
    eventProcessor = new EventProcessor(db);

    System.out.println("Starting Vibe...");
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    OptionSet options = parser.parse(args);

    int port = 4567;
    Spark.setPort(port);
    clientSecret = "9b79b8ae6c2a453588a6be84ca9de659";
    clientID = "bfd53bc39d2c46f081fa7951a5a88ea8";
    redirectURI = String.format("http://localhost:%s/playlists", port);
    api = Api.builder().clientId(clientID).clientSecret(clientSecret)
        .redirectURI(redirectURI).build();

    if (options.has("gui")) {
      // Runs the GUI
      runSparkServer();
    }
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
    Spark.post("/customizePlaylist", new AddCustomPlaylistHandler());
    Spark.post("/getAllPlaylists", new GetAllPlaylistsHandler());
    Spark.post("/selectExistingPlaylist", new SelectExistingPlaylistHandler());
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

      if (!receivedCode) {

        // Retrieve the code from the front-end to get an access token
        code = qm.value("code").trim();
        System.out.println("Code: " + code);
        receivedCode = true;

        /*
         * Make a token request. Asynchronous requests are made with the
         * .getAsync method and synchronous requests are made with the .get
         * method. This holds for all type of requests.
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

          api.setAccessToken(accessToken);
          api.setRefreshToken(refreshToken);

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
      List<CalendarEvent> events = null;

      try {
        events = eventProcessor.getEventsFromUserID(currentUser.getId());
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("ERROR: Database issues");
      }

      Map<String, Object> frontEndInfo = ImmutableMap.of("username", display,
          "cachedEvents", events);

      return GSON.toJson(frontEndInfo);
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
      CalendarEvent newEvent = null;

      try {
        newEvent = eventProcessor.addEvent(start, amOrPm, end, endAmOrPm,
            eventName, currentUser.getId());
      } catch (SQLException e) {
        System.out.println("Error in adding event");
        e.printStackTrace();
      }

      // Generate the playlist associated with this event
      hq.generateFromTag(newEvent, api, currentUser, accessToken);

      hq.getAllPlaylists(api, currentUser);

      // ~~~~~~~~~These lines are only for testing
      // VibePlaylist p = VibeCache.getPlaylistCache().get(newEvent.getId());
      // String tempURI = hq.convertForSpotify(p2, newEvent.getName(), api,
      // currentUser);
      // System.out.println("~~~THE TRACKS~~~");
      // System.out.println(p.getTracks());
      // System.out.println(p.getTracks().size());
      // ~~~~~end of testing

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
      String returnURI = null;
      // Retrieve the event ID and find the associated playlist
      String idString = qm.value("eventID");
      UUID eventID = UUID.fromString(idString);

      // Check to see if a prexisting playlist was associated with this event
      CalendarEvent thisEvent = null;
      try {
        thisEvent = eventProcessor.getEventFromEventID(idString);
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      // Case for new playlist
      if (thisEvent.getPlayListURI().equals("")) {
        VibePlaylist playlist = VibeCache.getPlaylistCache().get(eventID);
        // TODO: Need to get the name from the eventID
        String eventName = thisEvent.getName();
        returnURI = hq.convertForSpotify(playlist, eventName, api, currentUser);
        // This is the case where an existing URI is set
      } else {
        returnURI = thisEvent.getPlayListURI();
      }

      return returnURI;
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

      try {
        eventProcessor.deleteEvent(eventID, currentUser.getId());
      } catch (SQLException e) {
        System.out.println("Error: delteEvent failed");
        response = "FAILURE";
        e.printStackTrace();
      }

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
      String eventID = qm.value("id");
      System.out.println("Event id of event you want to edit is " + eventID);

      CalendarEvent event = null;
      try {
        event = eventProcessor.getEventFromEventID(eventID);
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      CalendarEvent editedEvent = eventProcessor.editEvent(start, amOrPm, end,
          endAMOrPM, eventName, event);
      System.out.println("Edited event: " + editedEvent.toString());

      // Generate the playlist associated with this event
      hq.generateFromTag(editedEvent, api, currentUser, accessToken);
      // These lines are only for testing
      // VibePlaylist p2 =
      // VibeCache.getPlaylistCache().get(editedEvent.getId());
      // System.out.println("~~~THE TRACKS~~~");
      // System.out.println(p2.getTracks());

      return GSON.toJson(editedEvent);
    }
  }

  /**
   * Class to handle adding custom playlists to events. I'm assuming that the
   * event and the settings will be passed back.
   *
   * @author smayfiel
   *
   */
  private class AddCustomPlaylistHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      // Create a new event and add it to the database
      // CalendarEvent newEvent = eventProcessor.editEvent(start, amOrPm, end,
      // endAMOrPM, eventName);

      // Playlist stuff
      String eventID = qm.value("eventID");
      String tag = qm.value("tag");

      System.out.println("the tag is + " + tag);
      @SuppressWarnings("unchecked")
      List<String> genres = GSON.fromJson(qm.value("genres"), List.class);
      String energy = qm.value("energy");
      System.out.println("main e is " + energy);
      String hotness = qm.value("popularity");
      String mood = qm.value("mood");
      System.out.println("main mood " + mood);

      System.out.println("the event ID + " + eventID);
      CalendarEvent thisEvent = null;
      try {
        thisEvent = eventProcessor.getEventFromEventID(eventID);
      } catch (SQLException e) {
        System.out.println("SQL EXCEPTION");
        e.printStackTrace();
      }

      // Add these things to a list
      List<String> settingsList = Arrays.asList(tag, energy, hotness, mood);

      // Generate the playlist
      hq.generateCustom(genres, settingsList, thisEvent, api, currentUser,
          accessToken);
      System.out.println("returned from generatecustom");

      // These lines are only for testing
      VibePlaylist p2 = VibeCache.getPlaylistCache().get(thisEvent.getId());
      System.out.println("~~~THE TRACKS~~~");
      System.out.println(p2.getTracks());

      // TODO: I have no idea what this should return
      return GSON.toJson(thisEvent);
    }
  }

  private class GetAllPlaylistsHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      List<String[]> playlistNames = hq.getAllPlaylists(api, currentUser);
      JsonArray jarray = new JsonArray();
      JsonParser jp = new JsonParser();
      for (String[] playlist : playlistNames) {
        JsonObject jobj = new JsonObject();
        jobj.addProperty("name", playlist[0]);
        jobj.addProperty("uri", playlist[1]);
        jarray.add(jobj);
      }

      return GSON.toJson(jarray);
    }
  }

  private class SelectExistingPlaylistHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String playlistURI = qm.value("playlistURI");
      String eventID = qm.value("eventID");

      // Associate this eventID with this URI
      CalendarEvent thisEvent = null;
      try {
        thisEvent = eventProcessor.getEventFromEventID(eventID);
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      thisEvent.setPlayListURI(playlistURI);

      return playlistURI;

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
