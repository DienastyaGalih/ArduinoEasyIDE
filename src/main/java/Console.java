/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class Console extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route("/static/*").handler(StaticHandler.
                create()
                .setWebRoot("C:/Users/User/Documents/bekUP/ArduinoOnlineIDE/resources")
                .setCachingEnabled(false)
                .setAllowRootFileSystemAccess(true)
                .setDirectoryListing(true)
                .setMaxAgeSeconds(1000)
        );
//        router.route().handler(CorsHandler.create("vertx\\.io").allowedMethod(HttpMethod.GET));
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("X-PINGARUNER")
                .allowedHeader("Content-Type"));
        router.get("/project/get/:projectName").handler(routingContext -> {
            String projectName = routingContext.request().getParam("projectName");
            System.out.println("project name " + projectName);
            HttpServerResponse response = routingContext.response();
            if (projectName == null) {
                sendError(400, response);
            } else {
                JsonObject project = DbHelper.getInstance().getProject("", projectName);
                System.out.println(project.toString());
                if (project == null) {
                    sendError(404, response);
                } else {
                    response.putHeader("content-type", "application/json").end(project.toString());
                }
            }
        });

        server.requestHandler(router::accept).listen(8080);

        vertx.deployVerticle(new RestAPI());

    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }
}
