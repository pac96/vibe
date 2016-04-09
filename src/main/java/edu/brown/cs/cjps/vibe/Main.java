/**
 * Package of things specific to this project
 */
package edu.brown.cs.cjps.vibe;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.SparkBase;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;


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

  /**
   * Runs the program.
   */
  private void run() {

    System.out.println("Hello World");
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
   * @return a new free marker engine
   */
  private static FreeMarkerEngine createEngine() {
      Configuration config = new Configuration();
      File templates = new File(
              "src/main/resources/spark/template/freemarker");
      try {
          config.setDirectoryForTemplateLoading(templates);
      } catch (IOException ioe) {
          System.out.printf(
                  "ERROR: Unable to use %s for template loading.\n",
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
      Spark.get("/login", new LoginHandler(), freeMarker);
  }

  /**
   * Handles creating the GUI for the homepage
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
   * @author cjps
   *
   */
  private class LoginHandler implements TemplateViewRoute {
      @Override
      public ModelAndView handle(Request req, Response res) {
          Map<String, Object> variables = ImmutableMap.of("title", "Vibe");
          return new ModelAndView(variables, "login.ftl");
      }
  }

  /**
   * Handles printing out exceptions to the GUI
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
