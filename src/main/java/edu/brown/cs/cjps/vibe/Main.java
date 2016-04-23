/**
 * Package of things specific to this project
 */
package edu.brown.cs.cjps.vibe;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.SparkBase;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.Gson;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;

import edu.brown.cs.cjps.music.PlaylistHQ;
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

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private final static Gson GSON = new Gson();

  /**
   * Runs the program.
   */
  private void run() {

    System.out.println("Hello World");
    // new PlaylistGenerator();
    PlaylistHQ hq = new PlaylistHQ();
    hq.generateFromTag("Party");
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    OptionSet options = parser.parse(args);

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
    Spark.get("/vibe", new FrontHandler(), freeMarker);
    Spark.get("/login", new LoginHandler());
    Spark.get("/playlists", new PlaylistPageHandler(), freeMarker);
  }

  /**
   * Handles creating the GUI for the homepage
   * 
   * @author cjps
   *
   */
  private class FrontHandler implements TemplateViewRoute {
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
      Map<String, Object> variables = ImmutableMap.of("title", "Vibe");
      final String clientId = "bfd53bc39d2c46f081fa7951a5a88ea8";
      final String clientSecret = "9b79b8ae6c2a453588a6be84ca9de659";
      final String redirectURI = "http://localhost:4567/playlists";

      final Api api = Api.builder().clientId(clientId)
          .clientSecret(clientSecret).redirectURI(redirectURI).build();

      /*
       * Set the necessary scopes that the application will need from the user
       * 
       * Can read the user's email and modify public and private playlists
       */
      final List<String> scopes = Arrays.asList("user-read-email",
          "playlist-modify-public", "playlist-modify-private");

      /*
       * Set a state. This is used to prevent cross site request forgeries.
       */
      final String state = "readytogo";

      String authorizeURL = api.createAuthorizeURL(scopes, state);

      /*
       * Continue by sending the user to the authorizeURL, which will look
       * something like https://accounts.spotify.com:443/authorize?client_id
       * =5fe01282e44241328a84e7c5cc169165 &response_type=code&redirect_uri=
       * https://example.com/callback&scope =user-read-private%20user-read-email
       * &state=some-state-of-my-choice
       */

      /* Application details necessary to get an access token */
      final String code = "<insert code>";

      /*
       * Make a token request. Asynchronous requests are made with the .getAsync
       * method and synchronous requests are made with the .get method. This
       * holds for all type of requests.
       */
      final SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = api
          .authorizationCodeGrant(code).build().getAsync();

      /* Add callbacks to handle success and failure */
      Futures.addCallback(authorizationCodeCredentialsFuture,
          new FutureCallback<AuthorizationCodeCredentials>() {
            @Override
            public void onSuccess(
                AuthorizationCodeCredentials authorizationCodeCredentials) {
              /* The tokens were retrieved successfully! */
              System.out.println("Successfully retrieved an access token! "
                  + authorizationCodeCredentials.getAccessToken());
              System.out.println("The access token expires in "
                  + authorizationCodeCredentials.getExpiresIn() + " seconds");
              System.out
                  .println("Luckily, I can refresh it using this refresh token! "
                      + authorizationCodeCredentials.getRefreshToken());

              /*
               * Set the access token and refresh token so that they are used
               * whenever needed
               */
              api.setAccessToken(authorizationCodeCredentials.getAccessToken());
              api.setRefreshToken(authorizationCodeCredentials
                  .getRefreshToken());
            }

            @Override
            public void onFailure(Throwable throwable) {
              /*
               * Let's say that the client id is invalid, or the code has been
               * used more than once, the request will fail. Why it fails is
               * written in the throwable's message.
               */

            }
          });

      return GSON.toJson(authorizeURL);
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
