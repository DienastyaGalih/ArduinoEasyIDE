

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

/**
 * This is an example application to showcase the usage of Vert.x Web.
 *
 * In this application you will see the usage of:
 *
 *  * Freemarker templates
 *  * Vert.x Web
 *
 * @author <a href="mailto:pmlopes@gmail.com>Paulo Lopes</a>
 */
public class FreeMarker extends AbstractVerticle {


  @Override
  public void start() throws Exception {

    // To simplify the development of the web components we use a Router to route all HTTP requests
    // to organize our code in a reusable way.
    final Router router = Router.router(vertx);

    // In order to use a template we first need to create an engine
    final FreeMarkerTemplateEngine engine = FreeMarkerTemplateEngine.create();

    // Entry point to the application, this will render a custom template.
    router.get().handler(ctx -> {
      // we define a hardcoded title for our application
      ctx.put("name", "Vert.x Web");

      // and now delegate to the engine to render it.
      engine.render(ctx, "C:/Users/User/Documents/bekUP/ArduinoOnlineIDE/src/main/java/templetes/index.ftl", res -> {
        if (res.succeeded()) {
          ctx.response().end(res.result());
        } else {
          ctx.fail(res.cause());
        }
      });
    });

    // start a HTTP web server on port 8080
    vertx.createHttpServer().requestHandler(router::accept).listen(8082);
  }
}