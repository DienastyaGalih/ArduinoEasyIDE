/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Data.Project;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Console extends AbstractVerticle {

    String username = "galih1994";

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
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("X-PINGARUNER")
                .allowedHeader("Content-Type"));

        router.get("/project/openProject/:projectId").handler(this::handleOpenProject);
        router.get("/project/loadFile/:fileId").handler(this::handleLoadFile);

        router.post("/project/create").handler(this::handleCreateProject);
        router.post("/project/saveFile/:fileId").handler(this::handleSaveFile);
        router.get("/project/getListProject").handler(this::handleGetListProject);
        router.post("/project/updateProjectConfig/:fileId").handler(this::testing);
        router.get("/project/downloadHex/:projectId").handler(this::testing);

        router.get("/project/createFile/:folderId/:nameFile").handler(this::testing);
        router.get("/project/createFolder/:folderId/:nameFile").handler(this::testing);

        server.requestHandler(router::accept).listen(8080);
        vertx.deployVerticle(new RestAPI());

        DbHelper.getInstance().init(vertx);

    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

    private void handleOpenProject(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        DbHelper.getInstance().getProject(projectId, handles -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json").end(handles.toString());
        });
    }

    private void handleLoadFile(RoutingContext routingContext) {
        String fileId = routingContext.request().getParam("fileId");
        System.out.println("project name " + fileId);
        HttpServerResponse response = routingContext.response();
        if (fileId == null) {
            sendError(400, response);
        } else {
            String fileSource = DbHelper.getInstance().getFileSource("", fileId);
            System.out.println(fileSource.toString());
            if (fileSource == null) {
                sendError(404, response);
            } else {
                response.end(fileSource);
            }
        }
    }

    private void handleCreateProject(RoutingContext routingContext) {

        routingContext.response().putHeader("content-type", "application/json");

        routingContext.request().bodyHandler(hndlr -> {

            System.out.println(hndlr.toString());

            JsonObject project = hndlr.toJsonObject();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();

            DbHelper.getInstance().createProject(this.username,
                    project.getString("name"),
                    project.getString("detail"),
                    project.getString("visibility"),
                    dateFormat.format(date),
                    dateFormat.format(date),
                    project.getString("board"),
                    project.getString("ic"),
                    handler -> {
                        System.out.println(handler.toString());
                        routingContext.response().end(new JsonObject().put("makan", "finish").toString());

                    });

        });

    }

    private void handleSaveFile(RoutingContext routingContext) {

        String idFile = routingContext.request().getParam("fileId");
        routingContext.response().putHeader("content-type", "application/json");

        routingContext.response().setChunked(true);

        routingContext.request().bodyHandler(hndlr -> {
            System.out.println("File id : " + idFile);
            System.out.println("request : " + new String(hndlr.getBytes()));

            routingContext.response().end(new JsonObject().put("makan", "finish").put("id", idFile).toString());
        });

    }

    private void handleGetListProject(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();

        DbHelper.getInstance().getListProject(username, handler -> {
            response.putHeader("content-type", "application/json").end(handler.toString());
        });

    }

    private void testing(RoutingContext routingContext) {

    }

}
