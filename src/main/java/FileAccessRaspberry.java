
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileProps;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.apache.commons.codec.binary.Base32;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class FileAccessRaspberry {

    Vertx vertx;
    String splitter;

    public void init(Vertx vertx) {
        this.vertx = vertx;
    }
    private static FileAccessRaspberry fileAccess;

    private FileAccessRaspberry() {

    }

    public static FileAccessRaspberry getInstance() {
        if (fileAccess == null) {
            fileAccess = new FileAccessRaspberry();
        }
        return fileAccess;
    }

    public void createFolder(String projectId, String folderName, Handler<Boolean> resultWrite) {
        // Write a file
        vertx.fileSystem().mkdir("project/" + projectId + "/" + folderName, result -> {
            if (result.succeeded()) {
                resultWrite.handle(true);
            } else {
                resultWrite.handle(false);
                System.err.println("Oh oh ..." + result.cause());
            }
        });
    }

    public void createFolder(String projectId, String folderName) {
        // Write a file
        vertx.fileSystem().mkdirBlocking("project/" + projectId + "/" + folderName);
    }

    public void createFile(String projectId, String directory, String fileName, Handler<Boolean> resultWrite) {
        // Write a file
        vertx.fileSystem().createFile("project/" + projectId + directory + "/" + fileName, result -> {
            if (result.succeeded()) {
                resultWrite.handle(true);
            } else {
                resultWrite.handle(false);
                System.err.println("Oh oh ..." + result.cause());
            }
        });
    }

    public void writeFile(String projectId, String fileName, String text, Handler<Boolean> resultWrite) {
        // Write a file
        vertx.fileSystem().writeFile("project/" + projectId + "/" + fileName, Buffer.buffer(text), result -> {
            if (result.succeeded()) {
                resultWrite.handle(true);
            } else {
                resultWrite.handle(false);
                System.err.println("Oh oh ..." + result.cause());
            }
        });
    }

    public JsonObject getProjectStructure(String projectId) {
        JsonObject projectStructure = new JsonObject();

        JsonArray rootFolder = new JsonArray();

        JsonArray rootFile = new JsonArray();

        List<String> rootDirectorys = vertx.fileSystem().readDirBlocking("project/" + projectId);

        System.out.println(new JsonArray(rootDirectorys).toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (String directory : rootDirectorys) {
            System.out.println(directory + "directory");
            FileProps something = vertx.fileSystem().lpropsBlocking(directory);
            if (something.isDirectory()) {
                JsonObject directoryJSON = new JsonObject();
                String splitDirectory[] = directory.split("/");
//                String splitDirectory[] = directory.split("\\\\");
                directoryJSON.put("name", splitDirectory[splitDirectory.length - 1]);
                String id = splitDirectory[splitDirectory.length - 2];
                String tmpId = new Base32().encodeAsString(splitDirectory[splitDirectory.length - 1].getBytes()).replace("=", "0");
                directoryJSON.put("id", tmpId);
                directoryJSON.put("create_date", dateFormat.format(new Date(something.creationTime())));
                directoryJSON.put("modify_date", dateFormat.format(new Date(something.lastModifiedTime())));
                rootFolder.add(directoryJSON);

                List<String> subDirectorysFiles = vertx.fileSystem().readDirBlocking(directory);
                JsonArray subFiles = new JsonArray();
                for (String subDirectoryFile : subDirectorysFiles) {
                    JsonObject fileJSON = new JsonObject();
                    String splitFile[] = subDirectoryFile.split("/");
//                    String splitFile[] = subDirectoryFile.split("\\\\");
                    fileJSON.put("name", splitFile[splitFile.length - 1]);
                    fileJSON.put("id", new Base32().encodeAsString(
                            (splitFile[splitFile.length - 2]
                            + "/"
                            + splitFile[splitFile.length - 1]).
                            getBytes()
                    ).replace("=", "0")
                    );
                    fileJSON.put("create_date", dateFormat.format(new Date(something.creationTime())));
                    fileJSON.put("modify_date", dateFormat.format(new Date(something.lastModifiedTime())));

                    subFiles.add(fileJSON);
                }
                directoryJSON.put("files", subFiles);

            } else {
                JsonObject fileJSON = new JsonObject();
                String splitFile[] = directory.split("/");
//                String splitFile[] = directory.split("\\\\");
                fileJSON.put("name", splitFile[splitFile.length - 1]);
                fileJSON.put("id", new Base32().encodeAsString(
                        splitFile[splitFile.length - 1].
                        getBytes()
                ).replace("=", "0")
                );
                fileJSON.put("create_date", dateFormat.format(new Date(something.creationTime())));
                fileJSON.put("modify_date", dateFormat.format(new Date(something.lastModifiedTime())));
                rootFile.add(fileJSON);
            }
        }

        projectStructure.put("folders", rootFolder);
        projectStructure.put("files", rootFile);

        System.out.println(projectStructure.toString());
        return projectStructure;
    }

    public String getFileSource(String string, String projectId, String fileId) {
        String tmp = vertx.fileSystem().readFileBlocking("project/" + projectId + "/" + fileId).toString();
        return tmp;
    }

    public void saveFile(String source, String projectId, String fileId,
            Handler<Boolean> handlerIsScuucess) {
        vertx.fileSystem().writeFileBlocking("project/" + projectId + "/" + fileId, Buffer.buffer(source));
        vertx.fileSystem().writeFile("project/" + projectId + "/" + fileId, Buffer.buffer(source), nahle -> {
            handlerIsScuucess.handle(nahle.succeeded());
        });

    }

    public void createNewProject(String projectId, String projectName, String detail,
            String createdDate, String boardType, String processorType) {
        JsonObject configProject = new JsonObject();
        configProject.put("projectName", projectName);
        configProject.put("boardType", boardType);
        configProject.put("processorType", processorType);
        configProject.put("port", "");
        configProject.put("createDate", createdDate);
        configProject.put("detail", detail);

        vertx.fileSystem().writeFileBlocking("project/" + projectId + "/projectConfig.json", Buffer.buffer(configProject.toString()));
//        updateProjectConfig(projectId, configProject);

    }

    public void updateProjectConfig(String projectId, JsonObject configProject) {
        vertx.fileSystem().writeFileBlocking("project/" + projectId + "/projectConfig.json", Buffer.buffer(configProject.toString()));
    }

    public JsonArray getListProject() {
        System.out.println("Masuk list project");
        JsonArray listProject = new JsonArray();
        List<String> resultProjectDir = vertx.fileSystem().readDirBlocking("project/");
        for (String projectFolder : resultProjectDir) {
            String splitProjectFolder[] = projectFolder.split("/");
            JsonObject configProject = readProjectConfig(splitProjectFolder[splitProjectFolder.length - 1]);
            System.out.println(configProject.getString("projectName") + " " + configProject.getString("createDate") + splitProjectFolder[splitProjectFolder.length - 1]);
            listProject.add(new JsonObject().
                    put("name", configProject.getString("projectName")).
                    put("date", configProject.getString("createDate")).
                    put("id", splitProjectFolder[splitProjectFolder.length - 1])
            );

        }
        return listProject;
    }

    public JsonObject readProjectConfig(String projectId) {
        Buffer buffer = vertx.fileSystem().readFileBlocking("project/" + projectId + "/projectConfig.json");
        return buffer.toJsonObject();
    }

    public void updateSerialPortConfig(String projectId, String portName) {
        System.out.println("\r\n" + projectId + "  " + portName);
        Buffer buffer = vertx.fileSystem().readFileBlocking("project/" + projectId + "/projectConfig.json");
        JsonObject config = buffer.toJsonObject();
        config.put("port", portName);
        updateProjectConfig(projectId, config);
    }

    public void updateBoardTypeConfig(String projectId, String boardType, String processorType) {
        System.out.println("\r\n" + projectId + boardType + "  " + processorType);
        Buffer buffer = vertx.fileSystem().readFileBlocking("project/" + projectId + "/projectConfig.json");
        JsonObject config = buffer.toJsonObject();
        config.put("boardType", boardType);
        config.put("processorType", processorType);
        updateProjectConfig(projectId, config);
    }

    public void updateMakeFile(String projectId, String boardType, String processorType, String serialPortName) {
        String makeFile = null;
        if (processorType.equals("default")) {
            makeFile
                    = "BOARD_TAG    = " + boardType + "\n"
                    + "MONITOR_PORT = " + serialPortName + "\n"
                    + "include /usr/share/arduino/Arduino.mk";
        } else {
            makeFile
                    = "BOARD_TAG    = " + boardType + "\n"
                    + "MONITOR_PORT = " + serialPortName + "\n"
                    + "include /usr/share/arduino/Arduino.mk";
        }

        vertx.fileSystem().writeFileBlocking(
                "project/" + projectId + "/Makefile",
                Buffer.buffer(makeFile)
        );

    }
}
