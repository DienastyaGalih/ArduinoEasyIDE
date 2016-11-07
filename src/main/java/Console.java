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
import java.util.UUID;
import org.apache.commons.codec.binary.Base32;

public class Console extends AbstractVerticle {

    String username = "galih1994";

    @Override
    public void start(Future<Void> fut) {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
//        router.route("/static/*").handler(StaticHandler.
//                create()
//                .setWebRoot("C:/Users/User/Documents/bekUP/ArduinoOnlineIDE/resources")
//                .setCachingEnabled(false)
//                .setAllowRootFileSystemAccess(true)
//                .setDirectoryListing(true)
//                .setMaxAgeSeconds(1000)
//        );
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("X-PINGARUNER")
                .allowedHeader("Content-Type"));

        DbHelper.getInstance().init(vertx);
        FileAccess.getInstance().init(vertx);

        router.get("/project/openProject/:projectId").handler(this::handleOpenProject);
        router.get("/project/loadFile/:projectId/:fileId").handler(this::handleLoadFile);

        router.post("/project/create").handler(this::handleCreateProject);
        router.post("/project/saveFile/:projectId/:fileId").handler(this::handleSaveFile);
        router.get("/project/getListProject").handler(this::handleGetListProject);
        router.post("/project/updateProjectConfig/:fileId").handler(this::testing);
        router.get("/project/downloadHex/:projectId").handler(this::testing);

        router.get("/project/createFile/:projectId/:directory/:fileName").handler(this::handleCreateFile);
        router.get("/project/createFolder/:projectId/:directory/:folderName").handler(this::handleCreateFolder);

        server.requestHandler(router::accept).listen(8080);

    }

    private void handleCreateFolder(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
//        String directoryName = routingContext.request().getParam("directory");
        String folderName = routingContext.request().getParam("folderName");

        System.out.println("Create file " + projectId + " " + folderName);
        FileAccess.getInstance().createFolder(projectId, folderName, hand -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json").
                    end(new JsonObject().
                            put("result", hand).
                            toString());

        });
    }

    private void handleCreateFile(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        String directory = routingContext.request().getParam("directory");
        String fileName = routingContext.request().getParam("fileName");

        System.out.println("Create file " + projectId + " " + directory + " " + fileName);
        FileAccess.getInstance().createFile(projectId, directory, fileName, hand -> {

            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json").
                    end(new JsonObject().
                            put("result", hand).
                            toString());

        });
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

    private void handleOpenProject(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        DbHelper.getInstance().getProject(projectId, handles -> {

            handles.put("sourceCode", FileAccess.getInstance().getProjectStructure(projectId));
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json").end(handles.toString());
        });
    }

    private void handleLoadFile(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        System.out.println("project name " + projectId);
        String fileId = routingContext.request().getParam("fileId");
        System.out.println("Before " + fileId);
        fileId = new String(new Base32().decode(fileId.replace("0", "=")));
        System.out.println("file name " + fileId);
        HttpServerResponse response = routingContext.response();
        if (fileId == null) {
            sendError(400, response);
        } else {

            String fileSource = FileAccess.getInstance().getFileSource("", projectId, fileId);
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

            String projectId = username + "_" + UUID.randomUUID().toString().replace("-", "");
            DbHelper.getInstance().createProject(this.username,
                    projectId,
                    project.getString("name"),
                    project.getString("detail"),
                    project.getString("visibility"),
                    dateFormat.format(date),
                    dateFormat.format(date),
                    project.getString("board"),
                    project.getString("ic"),
                    handler -> {
                        FileAccess.getInstance().createFolder(projectId, "", resultWrite -> {
                            if (resultWrite) {
                                FileAccess.getInstance().writeFile(projectId, "main.ino", "example", resultWriteText -> {
                                    routingContext.response().end(new JsonObject().put("result", resultWriteText).put("projectId", projectId).toString());
                                });
                            } else {
                                routingContext.response().end(new JsonObject().put("result", resultWrite).toString());
                            }
                        });
                    });

        });

    }

    private void handleSaveFile(RoutingContext routingContext) {

//        routingContext.request().handler(hndlr->{
//            String projectId = routingContext.request().getParam("projectId");
//            System.out.println("project name " + projectId);
//            String fileId = routingContext.request().getParam("fileId");
//            fileId = new String(new Base32().decode(fileId.replace("0", "=")));
//            System.out.println("file name " + fileId);
//
//            System.out.println("source " + new String(hndlr.getBytes()));
//
//            HttpServerResponse response = routingContext.response();
//
//            FileAccess.getInstance().
//                    saveFile(hndlr.toString(), projectId, fileId, handlerr -> {
//                        routingContext.response().
//                                end(new JsonObject().
//                                        put("result", handlerr).toString());
//                    });
//
//        });
        routingContext.response().putHeader("content-type", "application/json");
        routingContext.request().bodyHandler(hndlr -> {
            String projectId = routingContext.request().getParam("projectId");
            System.out.println("project name " + projectId);
            String fileId = routingContext.request().getParam("fileId");
            fileId = new String(new Base32().decode(fileId.replace("0", "=")));
            System.out.println("file name " + fileId);

            System.out.println("source " + new String(hndlr.getBytes()));

            HttpServerResponse response = routingContext.response();

            FileAccess.getInstance().
                    saveFile(hndlr.toString(), projectId, fileId, handlerr -> {
                        routingContext.response().
                                end(new JsonObject().
                                        put("result", handlerr).toString());
                    });

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
