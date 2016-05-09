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
   * 
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

  private final String TIMEREGEX = "(\\d\\d|\\d):\\d\\d";

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

    // Run should take in a database
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

    int port = 5555;
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
    };
  }

  /**
   * Handles acquiring and utilizing the code for the user to create an access
   * token ;
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

      // Look at this later
      try {
        events = eventProcessor.getEventsFromUserID(currentUser.getId());
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

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

      // (1): Retrieve event information from the front-end
      String start = qm.value("start");
      Boolean amOrPm = Boolean.parseBoolean(qm.value("startAMPM"));
      String end = qm.value("end");
      Boolean endAmOrPm = Boolean.parseBoolean(qm.value("endAMPM"));
      String eventName = qm.value("name");
      CalendarEvent newEvent = null;

      Map<String, Object> frontEndInfo;

      // (2): Check if the input times have the correct format
      if (start.matches(TIMEREGEX) && end.matches(TIMEREGEX)
          && timeErrorHelper(start, end)
          && startBeforeEnd(start, amOrPm, end, endAmOrPm)) {
        // Need to check that nothing is > 12 or 59 and start is before end

        System.out.println("The input maches the regex");

        // (3): Add the event to the database
        try {
          newEvent = eventProcessor.addEvent(start, amOrPm, end, endAmOrPm,
              eventName, currentUser.getId());
        } catch (SQLException e) {
          System.out.println("Error in adding event");
          e.printStackTrace();
        }

        // Add event to the cache
        VibeCache.getEventCache().put(newEvent.getId(), newEvent);

        // Generate the playlist associated with this event
        hq.generateFromTag(newEvent, api, currentUser, accessToken);

        frontEndInfo = ImmutableMap.of("event", newEvent, "success", true);
      } else {
        System.out.println("The input does not match the regex");
        frontEndInfo = ImmutableMap.of("event", "null", "success", false);
        System.out.println("where is the error");
      }
      System.out.println("about to return");
      return GSON.toJson(frontEndInfo);

    }
  }

  // Error check the entered time
  public boolean timeErrorHelper(String start, String end) {
    String[] startAr = start.split(":");
    int startHour = Integer.parseInt(startAr[0]);
    int startMin = Integer.parseInt(startAr[1]);
    String[] endAr = end.split(":");
    int endHour = Integer.parseInt(endAr[0]);
    int endMin = Integer.parseInt(endAr[1]);
    if (startHour < 1 || startHour > 12 || endHour < 1 || endHour > 12) {
      return false;
    }
    if (startMin < 0 || startMin > 59 || endMin < 0 || endMin > 59) {
      return false;
    }
    return true;
  }

  // Start time before end time checker
  public boolean startBeforeEnd(String start, boolean isAm, String end,
      boolean isAm2) {
    String[] startAr = start.split(":");
    int startHour = Integer.parseInt(startAr[0]);
    int startMin = Integer.parseInt(startAr[1]);
    String[] endAr = end.split(":");
    int endHour = Integer.parseInt(endAr[0]);
    int endMin = Integer.parseInt(endAr[1]);
    int startinMin = this.getTimeInMins(startHour, startMin, isAm);
    int endinMin = this.getTimeInMins(endHour, endMin, isAm2);
    return (endinMin - startinMin >= 0);
  }

  // returns the time in military minutes from midnight
  public int getTimeInMins(int startH, int startM, boolean isAm) {
    // If PM and not 12pm, add 12 hours
    if ((!isAm && (startH != 12))) {
      startH = startH + 12; // accounting for 24 hour time
    }
    // If 12AM, convert to 0
    if (isAm && startH == 12) {
      startH = 0;
    }
    int startinMins = startH * 60 + startM;
    return startinMins;
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
      CalendarEvent thisEvent = VibeCache.getEventCache().get(eventID);

      if (thisEvent.getPlayListURI().equals("")) {
        System.out.println("no uri set");
        VibePlaylist playlist = VibeCache.getPlaylistCache().get(eventID);

        String eventName = thisEvent.getName();
        returnURI = hq.convertForSpotify(playlist, eventName, api, currentUser);
        thisEvent.setPlayListURI(returnURI);
        System.out.println("confirming uri set " + thisEvent.getPlayListURI());
        // This is the case where an existing URI is set
      } else {
        System.out.println("uri was set");
        returnURI = thisEvent.getPlayListURI();
        System.out.println("return uri is " + returnURI);
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

      // remove this event from the cache
      VibeCache.getEventCache().remove(UUID.fromString(eventID));
      VibeCache.getPlaylistCache().remove(UUID.fromString(eventID));

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

      // If the edits are valid an event will be returned to the front-end
      // (1): Grab the event from the cache
      CalendarEvent oldEvent = VibeCache.getEventCache().get(
          UUID.fromString(eventID));

      Map<String, Object> frontEndInfo;
      // (2): Make modifications to the event i;f the times match the correct
      // format
      if (start.matches(TIMEREGEX) && end.matches(TIMEREGEX)
          && startBeforeEnd(start, amOrPm, end, endAMOrPM)
          && timeErrorHelper(start, end)) {
        System.out.println("The input matches");
        CalendarEvent editedEvent = eventProcessor.editEvent(start, amOrPm,
            end, endAMOrPM, eventName, oldEvent);

        // (3): Generate the playlist associated with this event

        // Replace the event in the cache, keeping the playlist that was
        // associated
        if (!oldEvent.getPlayListURI().equals("")) {
          editedEvent.setPlayListURI(oldEvent.getPlayListURI());
        } else {
          // This case means a spotify playlist doesn't yet exist, in which case
          // create a new VibePlaylist for later conversion to spotify
          hq.generateFromTag(editedEvent, api, currentUser, accessToken);
        }

        // Recache this edited event
        VibeCache.getEventCache()
            .replace(UUID.fromString(eventID), editedEvent);

        // (4): Return the event along to true so front end can recognize that
        // edit was succesful
        frontEndInfo = ImmutableMap.of("event", editedEvent, "success", true);

      } else {
        frontEndInfo = ImmutableMap.of("event", oldEvent, "success", false);
      }

      return GSON.toJson(frontEndInfo);
    }
  }

  /**
   * Class to handle adding custom playlists to events. I'm assuming that the
   * event and the settings will be passed back.
   *
   *
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
}
