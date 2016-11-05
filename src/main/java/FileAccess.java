
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
public class FileAccess {

    Vertx vertx;

    public void init(Vertx vertx) {
        this.vertx = vertx;
    }
    private static FileAccess fileAccess;

    private FileAccess() {

    }

    public static FileAccess getInstance() {
        if (fileAccess == null) {
            fileAccess = new FileAccess();
        }
        return fileAccess;
    }

    public void createFolder(String projectId, String fileName, Handler<Boolean> resultWrite) {
        // Write a file
        vertx.fileSystem().mkdir("project/" + projectId + "/" + fileName, result -> {
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
                String splitDirectory[] = directory.split("\\\\");
                directoryJSON.put("name", splitDirectory[splitDirectory.length - 1]);
//                tinggal tambahkan encode menggunakan base 64 agar tidak terdeteksi titik.
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
                    String splitFile[] = subDirectoryFile.split("\\\\");
                    fileJSON.put("name", splitFile[splitFile.length - 1]);
                    fileJSON.put("id", new Base32().encodeAsString(
                            (splitFile[splitFile.length - 2]
                            + "/"
                            + splitFile[splitFile.length-1 ]).
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
                String splitFile[] = directory.split("\\\\");
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

    public void saveFile(String source, String projectId, String fileId,Handler<Boolean> handlerIsScuucess) {
        vertx.fileSystem().writeFileBlocking("project/" + projectId + "/" + fileId, Buffer.buffer(source));
        vertx.fileSystem().writeFile("project/" + projectId + "/" + fileId, Buffer.buffer(source), nahle -> {
            handlerIsScuucess.handle(nahle.succeeded());
        });
        
    }
}
