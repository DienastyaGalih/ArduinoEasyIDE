/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Data.Project;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocket;
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

public class ConsoleRaspberry extends AbstractVerticle {

    String username = "galih1994";

    public void start(Future<Void> fut) {

        ShellInteraction.getInstance().init(vertx);

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

//        Handler<ServerWebSocket> webSocketServer=server.websocketHandler();
        ServerWebSocket serverWebSocket;

        server.websocketHandler(ws -> {
            System.out.println(ws.path() + " path");
            String pathSPlitted[] = ws.path().split("/");

            for (String string : pathSPlitted) {
                System.out.println(string + " ccc");
            }

            if (pathSPlitted[1].equals("project")) {
                if (pathSPlitted[2].equals("upload")) {
                    System.out.println("\r\n" + pathSPlitted[3]);
//                    ShellInteraction.getInstance().compileAndUploadRealtime("galih1994_9fe4d6779b7c467b8d48e7962d8b91b8", ws);
                    ShellInteraction.getInstance().
                            compileAndUploadRealtime(pathSPlitted[3], ws);
                } else if (pathSPlitted[2].equals("clean")) {
                    ShellInteraction.getInstance().cleanRealtime(pathSPlitted[3], ws);
                } else if (pathSPlitted[2].equals("compile")) {
                    ShellInteraction.getInstance().compileRealtime(pathSPlitted[3], ws);
                } else if (pathSPlitted[2].equals("monitor")) {
                    ShellInteraction.getInstance().readSerialRealtime(pathSPlitted[3], ws);
                }
            }

        });

        FileAccessRaspberry.getInstance().init(vertx);

        router.get("/project/openProject/:projectId").handler(this::handleOpenProject);
        router.get("/project/loadFile/:projectId/:fileId").handler(this::handleLoadFile);

        router.post("/project/create").handler(this::handleCreateProject);
        router.post("/project/saveFile/:projectId/:fileId").handler(this::handleSaveFile);
        router.get("/project/getListProject").handler(this::handleGetListProject);
        router.post("/project/updateProjectConfig/:fileId").handler(this::testing);
        router.get("/project/downloadHex/:projectId").handler(this::testing);

        router.get("/project/compile/:projectId").handler(this::handleCompile);
        router.get("/project/getListPort").handler(this::handleGetListPort);
        router.get("/project/upload/:projectId").handler(this::handleUpload);
        router.get("/project/clean/:projectId").handler(this::handleClean);
        router.get("/project/updateSerialPort/:projectId/:portName").handler(this::handleUpdateSerialPort);
        router.get("/project/updateBoardType/:projectId/:boardType/:processorType").handler(this::handleUpdateBoardType);

        router.get("/project/createFile/:projectId/:directory/:fileName").handler(this::handleCreateFile);
        router.get("/project/createFolder/:projectId/:directory/:folderName").handler(this::handleCreateFolder);

        server.requestHandler(router::accept).listen(8080);

    }

    private void handleCompile(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/plain");
        response.setChunked(true);

//        JsonObject config=FileAccessRaspberry.getInstance().readProjectConfig(projectId);
//        FileAccessRaspberry.getInstance().updateMakeFile(
//                projectId, 
//                config.getString("boardType"), 
//                config.getString("processorType"), 
//                config.getString("port")
//        );
        ShellInteraction.getInstance().compile(projectId, response);

    }

    private void handleCreateFolder(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        String folderName = routingContext.request().getParam("folderName");
        System.out.println("Create file " + projectId + " " + folderName);
        FileAccessRaspberry.getInstance().createFolder(projectId, folderName, hand -> {
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
        FileAccessRaspberry.getInstance().createFile(projectId, directory, fileName, hand -> {

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

        JsonObject config = FileAccessRaspberry.getInstance().readProjectConfig(projectId);

        JsonObject project = new JsonObject();
        project.put("id", projectId);
        project.put("name", config.getString("projectName"));
        project.put("detail", config.getString("detail"));

        JsonObject configProject = new JsonObject();
        configProject.put("arduinoType", config.getString("boardType"));
        configProject.put("icType", config.getString("processorType"));
        configProject.put("acessbility", "0");
        configProject.put("port", "com3");
        project.put("config", configProject);
        project.put("sourceCode", FileAccessRaspberry.getInstance().getProjectStructure(projectId));
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json").end(project.toString());

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

            String fileSource = FileAccessRaspberry.getInstance().getFileSource("", projectId, fileId);
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

            FileAccessRaspberry.getInstance().createFolder(projectId, "");
            FileAccessRaspberry.getInstance().createNewProject(
                    projectId,
                    project.getString("name"),
                    project.getString("detail"),
                    dateFormat.format(date),
                    project.getString("board"),
                    project.getString("ic"));

            FileAccessRaspberry.getInstance().writeFile(
                    projectId,
                    "main.ino",
                    "example",
                    resultWriteText -> {
                        routingContext.response().end(
                                new JsonObject().
                                put("result", resultWriteText).
                                put("projectId", projectId).
                                toString());
                    });

        });

    }

    private void handleSaveFile(RoutingContext routingContext) {
        routingContext.response().putHeader("content-type", "application/json");
        routingContext.request().bodyHandler(hndlr -> {
            String projectId = routingContext.request().getParam("projectId");
            System.out.println("project name " + projectId);
            String fileId = routingContext.request().getParam("fileId");
            fileId = new String(new Base32().decode(fileId.replace("0", "=")));
            System.out.println("file name " + fileId);

            System.out.println("source " + new String(hndlr.getBytes()));

            HttpServerResponse response = routingContext.response();

            FileAccessRaspberry.getInstance().
                    saveFile(hndlr.toString(), projectId, fileId, handlerr -> {
                        routingContext.response().
                                end(new JsonObject().
                                        put("result", handlerr).toString());
                    });

        });

    }

    private void handleGetListProject(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json").end(
                FileAccessRaspberry.getInstance().getListProject().toString()
        );

    }

    private void handleGetListPort(RoutingContext routingContext) {
        System.out.println("Get list port");
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json").end(
                new JsonArray(ShellInteraction.getInstance().getListComPort()).toString()
        );
    }

    private void handleUpload(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/plain");
        response.setChunked(true);

        ShellInteraction.getInstance().compileAndUpload(projectId, response);
    }

    private void handleClean(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/plain");
        response.setChunked(true);

        ShellInteraction.getInstance().clean(projectId, response);
    }

    private void handleUpdateSerialPort(RoutingContext routingContext) {
        String portName = routingContext.request().getParam("portName");
        String projectId = routingContext.request().getParam("projectId");
        HttpServerResponse response = routingContext.response();
        FileAccessRaspberry.getInstance().updateSerialPortConfig(projectId, portName);

        JsonObject config = FileAccessRaspberry.getInstance().readProjectConfig(projectId);
        FileAccessRaspberry.getInstance().updateMakeFile(
                projectId,
                config.getString("boardType"),
                config.getString("processorType"),
                config.getString("port")
        );

        response.putHeader("content-type", "application/json").end(
                new JsonObject().put("result", true).toString()
        );

    }

    private void handleUpdateBoardType(RoutingContext routingContext) {
        String boardType = routingContext.request().getParam("boardType");
        String processorType = routingContext.request().getParam("processorType");
        String projectId = routingContext.request().getParam("projectId");

        HttpServerResponse response = routingContext.response();
        FileAccessRaspberry.getInstance().updateBoardTypeConfig(projectId, boardType, processorType);

        JsonObject config = FileAccessRaspberry.getInstance().readProjectConfig(projectId);
        FileAccessRaspberry.getInstance().updateMakeFile(
                projectId,
                config.getString("boardType"),
                config.getString("processorType"),
                config.getString("port")
        );

        response.putHeader("content-type", "application/json").end(
                new JsonObject().put("result", true).toString()
        );

    }

    private void testing(RoutingContext routingContext) {

    }

}
