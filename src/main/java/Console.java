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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
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

        router.get("/project/get/file/:fileId").handler(routingContext -> {
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
        });

//        //file save handler 
//        router.post("/project/saveFile/:fileId").handler(ctx -> {
//            ctx.response().putHeader("Content-Type", "text/plain");
//
//            ctx.response().setChunked(true);
//
//            ctx.request().bodyHandler(hndlr -> {
//                System.out.println(new String(hndlr.getBytes()));
//
//                ctx.response().end("terimakaish");
//            });
//
//        });
//        router.post("/project/create").handler(ctx -> {
//            ctx.response().putHeader("Content-Type", "text/plain");
//
//            ctx.response().setChunked(true);
//
//            ctx.request().bodyHandler(hndlr -> {
//                System.out.println(new String(hndlr.getBytes()));
//
//                ctx.response().end("terimakaish");
//            });
//
//        });
        router.get("/project/openProject/:projectId").handler(this::handleOpenProject);
        router.get("/project/loadFile/:fileId").handler(this::handleLoadFile);

        router.post("/project/create").handler(this::handleCreateProject);

        router.post("/project/saveFile/:fileId").handler(this::handleSaveFile);

        router.get("/project/getListProject").handler(this::handleGetListProject);

        router.post("/project/updateProjectConfig/:fileId").handler(this::testing);
        router.get("/project/downloadHex/:projectId").handler(this::testing);

        server.requestHandler(router::accept).listen(8080);
        vertx.deployVerticle(new RestAPI());
        
        DbHelper.getInstance().init(vertx);
        DbHelper.getInstance().selectUser();

    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

    private void handleOpenProject(RoutingContext routingContext) {
        String projectName = routingContext.request().getParam("projectId");
        System.out.println("projectId " + projectName);
        HttpServerResponse response = routingContext.response();
        if (projectName == null) {
            sendError(400, response);
        } else {
            JsonObject project = DbHelper.getInstance().openProject("", projectName);
            System.out.println(project.toString());
            if (project == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(project.toString());
            }
        }
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
            JsonObject project = hndlr.toJsonObject();
            System.out.println(project.toString());

            project.getString("name");
            project.getString("board");
            project.getString("ic");
            project.getString("detail");
            project.getString("visibility");

            routingContext.response().end(new JsonObject().put("makan", "finish").toString());
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
        
        JsonArray lissat=DbHelper.getInstance().getListProject("galih");

        response.putHeader("content-type", "application/json").end(lissat.toString());

    
}

private void testing(RoutingContext routingContext) {

    }

}
