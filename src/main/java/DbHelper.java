
import Data.Project;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class DbHelper {

    static DbHelper dbHelper;
    AsyncSQLClient mySQLClient;

    static public DbHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DbHelper();
        }
        return dbHelper;
    }

    public void init(Vertx vertx) {
        JsonObject mysqlConfig = new JsonObject()
                .put("host", "127.0.0.1")
                //                .put("port", 5432)
                .put("maxPoolSize", 1000)
                .put("username", "admin")
                .put("password", "212")
                .put("database", "arduinoeasydb")
                .put("queryTimeout", 7000);
        this.mySQLClient = MySQLClient.createShared(vertx, mysqlConfig);
        mySQLClient.getConnection(conHandler -> {
            System.out.println(conHandler.cause());
        });
    }

    public void selectUser() {
        String query = "SELECT * FROM `user_2` WHERE 1";

        mySQLClient.getConnection(resConnection -> {

            if (resConnection.succeeded()) {
                SQLConnection connection;
                connection = resConnection.result();

                connection.setAutoCommit(false, autoCommit -> {
                    if (autoCommit.succeeded()) {

                        connection.query(query, handlerQuery -> {
                            if (handlerQuery.succeeded()) {

                                ResultSet resultSet = handlerQuery.result();
                                String resultJSON = resultSet.getRows().get(0).toString();
                                System.out.println(resultJSON + " resilasdfaslkd");

                            } else {

                                System.out.println("failed");
                            }
                            connection.close();
                        });
                    } else {
                        System.out.println("auto commit failed");
                    }

                });

                // Got a connection
            } else {
                // Failed to get connection - deal with it
                System.out.println("true failes");
            }
        });
    }

    public DbHelper() {
    }

    public void addUser() {
//        INSERT INTO `user_2`(`user_name`, `display_name`, `detail`, `pass`, `key_2`) VALUES ('galih1994','Dienstya galih','detail','123','')

    }

    public JsonObject openProject(String userName, String projectName) {
        String project = "{\n"
                + "    \"id\": 234,\n"
                + "    \"name\": \"nama project\",\n"
                + "    \"detail\": \" detail of project\",\n"
                + "    \"config\": {\n"
                + "        \"arduinoType\": \"arduino uno\",\n"
                + "        \"icType\": \"atmega 328p\",\n"
                + "        \"acessbility\": \"private|public|friend\",\n"
                + "        \"port\": \"com3\"\n"
                + "    },\n"
                + "    \"sourceCode\": {\n"
                + "        \"folders\": [\n"
                + "            {\n"
                + "                \"name\": \"name folder\",\n"
                + "                \"id\": 345,\n"
                + "                \"dateModified\": \"11-feb-2014\",\n"
                + "                \"dateCreated\": \"11-feb-2014\",\n"
                + "                \"modifiedBy\": \"userId\",\n"
                + "                \"files\": [\n"
                + "                    {\n"
                + "                        \"name\": \"file4.c\",\n"
                + "                        \"id\": 4,\n"
                + "                        \"dateModified\": \"11-feb-2014\",\n"
                + "                        \"dateCreated\": \"11-feb-2014\",\n"
                + "                        \"modifiedBy\": \"userId\"\n"
                + "                    },\n"
                + "                    {\n"
                + "                        \"name\": \"file3.c\",\n"
                + "                        \"id\": 3,\n"
                + "                        \"dateModified\": \"11-feb-2014\",\n"
                + "                        \"dateCreated\": \"11-feb-2014\",\n"
                + "                        \"modifiedBy\": \"userId\"\n"
                + "                    }\n"
                + "                ]\n"
                + "            }\n"
                + "        ],\n"
                + "        \"files\": [\n"
                + "            {\n"
                + "                \"name\": \"file1.c\",\n"
                + "                \"id\": 1,\n"
                + "                \"dateModified\": \"11-feb-2014\",\n"
                + "                \"dateCreated\": \"11-feb-2014\",\n"
                + "                \"modifiedBy\": \"userId\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"name\": \"file2.c\",\n"
                + "                \"id\": 2,\n"
                + "                \"dateModified\": \"11-feb-2014\",\n"
                + "                \"dateCreated\": \"11-feb-2014\",\n"
                + "                \"modifiedBy\": \"userId\"\n"
                + "            }\n"
                + "        ]\n"
                + "    }\n"
                + "}\n"
                + "";

        return new JsonObject(project);
    }

    public JsonObject getProject(String userName, String projectName) {
        String returnDummy = "{\n"
                + "    \"project\":\n"
                + "            {\n"
                + "                \"name\": \"blinking\",\n"
                + "                \"create_date\": \"11-feb-2014\",\n"
                + "                \"modify_date\": \"11-feb-2014\",\n"
                + "                \"detail\": \"some detail\",\n"
                + "                \"visibility\": \"public\",\n"
                + "                \"visibility\": \"public\",\n"
                + "                \"arduino_type\": \"arduino uno\",\n"
                + "                \"arduino_IC\": \"atmega 328\"\n"
                + "            },\n"
                + "    \"project_structure\":\n"
                + "            {\n"
                + "                \"folders\": [\n"
                + "                    {\n"
                + "                        \"name\": \"lib\",\n"
                + "                        \"create_date\": \"11-feb-2014\",\n"
                + "                        \"modify_date\": \"11-feb-2014\",\n"
                + "                        \"files\": [\n"
                + "                            {\n"
                + "                                \"name\": \"lcd.c\",\n"
                + "                                \"source\": \"text of source\",\n"
                + "                                \"create_date\": \"11-feb-2014\",\n"
                + "                                \"modify_date\": \"11-feb-2014\"\n"
                + "                            },\n"
                + "                            {\n"
                + "                                \"name\": \"lcd.h\",\n"
                + "                                \"source\": \"text of source\",\n"
                + "                                \"create_date\": \"11-feb-2014\",\n"
                + "                                \"modify_date\": \"11-feb-2014\"\n"
                + "                            },\n"
                + "                            {\n"
                + "                                \"name\": \"dht.c\",\n"
                + "                                \"source\": \"text of source\",\n"
                + "                                \"create_date\": \"11-feb-2014\",\n"
                + "                                \"modify_date\": \"11-feb-2014\"\n"
                + "                            },\n"
                + "                            {\n"
                + "                                \"name\": \"dht.h\",\n"
                + "                                \"source\": \"text of source\",\n"
                + "                                \"create_date\": \"11-feb-2014\",\n"
                + "                                \"modify_date\": \"11-feb-2014\"\n"
                + "                            }\n"
                + "                        ]\n"
                + "\n"
                + "                    }\n"
                + "                ],\n"
                + "                \"files\": [\n"
                + "                    {\n"
                + "                        \"name\": \"lcd.c\",\n"
                + "                        \"source\": \"text of source\",\n"
                + "                        \"create_date\": \"11-feb-2014\",\n"
                + "                        \"modify_date\": \"11-feb-2014\"\n"
                + "                    },\n"
                + "                    {\n"
                + "                        \"name\": \"lcd.h\",\n"
                + "                        \"source\": \"text of source\",\n"
                + "                        \"create_date\": \"11-feb-2014\",\n"
                + "                        \"modify_date\": \"11-feb-2014\"\n"
                + "                    },\n"
                + "                    {\n"
                + "                        \"name\": \"dht.c\",\n"
                + "                        \"source\": \"text of source\",\n"
                + "                        \"create_date\": \"11-feb-2014\",\n"
                + "                        \"modify_date\": \"11-feb-2014\"\n"
                + "                    },\n"
                + "                    {\n"
                + "                        \"name\": \"dht.h\",\n"
                + "                        \"source\": \"text of source\",\n"
                + "                        \"create_date\": \"11-feb-2014\",\n"
                + "                        \"modify_date\": \"11-feb-2014\"\n"
                + "                    }\n"
                + "                ]\n"
                + "            }\n"
                + "}\n"
                + "\n"
                + "";
        return new JsonObject(returnDummy);
    }

    public void getListProject(String username, Handler<List<JsonObject>> handler) {

        String query = "SELECT pk_id_project as id , `name`, modify_date as date FROM project WHERE `USER_2_user_name`='" + username + "';";

        mySQLClient.getConnection(resConnection -> {

            if (resConnection.succeeded()) {
                SQLConnection connection;
                connection = resConnection.result();

                connection.setAutoCommit(false, autoCommit -> {
                    if (autoCommit.succeeded()) {

                        connection.query(query, handlerQuery -> {
                            if (handlerQuery.succeeded()) {

                                ResultSet resultSet = handlerQuery.result();

//                                System.out.println(resultSet.getRows().toString());
                                handler.handle(resultSet.getRows());

                            } else {

                                System.out.println("failed");
                            }
                            connection.close();
                        });
                    } else {
                        System.out.println("auto commit failed");
                    }

                });

                // Got a connection
            } else {
                // Failed to get connection - deal with it
                System.out.println("true failes");
            }
        });

    }

    public void createProject(String username, String projectName, String detail, String visibility, String createdDate, String modifyDate, String boardType, String icType, Handler<JsonObject> handler) {
        String query = "INSERT INTO arduinoeasydb.project (`USER_2_user_name`, `name`, detail, visibility, cretate_date, modify_date, board_type, ic_type) "
                + "VALUES ('" + username + "', '" + projectName + "', '" + detail + "', '" + visibility + "', '" + createdDate + "', '" + modifyDate + "', '" + boardType + "', '" + icType + "')";

        mySQLClient.getConnection(resConnection -> {
            if (resConnection.succeeded()) {
                SQLConnection connection;
                connection = resConnection.result();
                connection.setAutoCommit(false, autoCommit -> {
                    if (autoCommit.succeeded()) {

                        connection.query(query, handlerQuery -> {
                            if (handlerQuery.succeeded()) {

                                ResultSet resultSet = handlerQuery.result();
                                handler.handle(resultSet.toJson());

                            } else {

                                System.out.println("failed");
                            }
                            connection.close();
                        });
                    } else {
                        System.out.println("auto commit failed");
                    }

                });

                // Got a connection
            } else {
                // Failed to get connection - deal with it
                System.out.println("true failes");
            }
        });

    }

    public void createFolder(String projectId) {
//        INSERT INTO arduinoeasydb.folders (`PROJECT_pk_id_project`, `name`, create_date, modify_date) 
//	VALUES (14, 'dana budi', '2016-10-31 21:26:13.489', '2016-10-31 21:26:14.602')

    }

    public void createFile(String folerId, String fileName) {
//        INSERT INTO arduinoeasydb.files (`FOLDERS_pk_id_folder`, `name`, `source`, crete_date, modify_date) 
//	VALUES (1, 'test.ino', 'asdlkfasdf', '2016-10-31 21:28:23.043', '2016-10-31 21:28:23.995')

    }

    public void updateSourceFile() {

    }

    private void getFileFromFrolder(String projectId, List<JsonObject> listFolders, Handler<JsonArray> handler) {
        String queryFile = "SELECT files.`name` as name, files.crete_date as dateCreated, files.modify_date as dateModified , files.pk_id_file as id, files.FOLDERS_pk_id_folder as folderId FROM  folders RIGHT JOIN files   ON folders.pk_id_folder=files.pk_id_file and folders.pk_id_folder IN (SELECT pk_id_folder FROM folders WHERE `PROJECT_pk_id_project`=" + projectId + ")";
        mySQLClient.getConnection(resConnection -> {
            if (resConnection.succeeded()) {
                SQLConnection connection;
                connection = resConnection.result();
                connection.setAutoCommit(false, autoCommit -> {
                    if (autoCommit.succeeded()) {
                        connection.query(queryFile, handlerQuery -> {
                            if (handlerQuery.succeeded()) {

                                ResultSet resultSet = handlerQuery.result();

                                List<JsonObject> listFiles = resultSet.getRows();
//                                System.out.println(new JsonArray(list).toString());
                                for (JsonObject listFile : listFiles) {
                                    int folderId = listFile.getInteger("folderId");
                                    
//                                    System.out.println("id folder " + folderId);

                                    

                                    for (JsonObject listFolder : listFolders) {
                                        
                                        
                                        if (listFolder.getInteger("id") == folderId) {
                                            try {
                                                listFolder.getJsonArray("files").add(listFile);
                                            } catch (Exception e) {
                                                List<JsonObject> listTmpFile = new ArrayList<>();
                                                listTmpFile.add(listFile);
                                                listFolder.put("files", listTmpFile);
                                            }

                                        }
                                        
                                    }
                                }

                                
//                                System.out.println(new JsonArray(listFolders).toString());
                                handler.handle(new JsonArray(listFolders));
                            } else {

                                System.out.println("failed");
                            }
                            connection.close();
                        });
                    } else {
                        System.out.println("auto commit failed");
                    }

                });

                // Got a connection
            } else {
                // Failed to get connection - deal with it
                System.out.println("true failes");
            }
        });
    }

    public void getProject(String projectId, Handler<JsonObject> handlerRequest) {
        String queryFolder = "SELECT pk_id_project as id, name , detail, board_type as arduinoType, ic_type as icType, visibility as acessbility FROM project WHERE project.pk_id_project =" + projectId + ";";
        mySQLClient.getConnection(resConnection -> {
            if (resConnection.succeeded()) {
                SQLConnection connection;
                connection = resConnection.result();
                connection.setAutoCommit(false, autoCommit -> {
                    if (autoCommit.succeeded()) {

                        connection.query(queryFolder, handlerQuery -> {
                            if (handlerQuery.succeeded()) {

                                ResultSet resultSet = handlerQuery.result();
//                                System.out.println(resultSet.toJson());
                                JsonObject resultJSON = resultSet.getRows().get(0);
//                                System.out.println("----------------------");
//                                System.out.println(resultJSON.toString());
                                JsonObject project = new JsonObject();
                                project.put("id", resultJSON.getInteger("id"));
                                project.put("name", resultJSON.getString("name"));
                                project.put("detail", resultJSON.getString("detail"));

                                JsonObject configProject = new JsonObject();
                                configProject.put("arduinoType", resultJSON.getString("arduinoType"));
                                configProject.put("icType", resultJSON.getString("icType"));
                                configProject.put("arduinoType", resultJSON.getString("arduinoType"));
                                configProject.put("acessbility", resultJSON.getString("acessbility"));
                                configProject.put("port", "com3");
                                project.put("config", configProject);

                                getProjectStructure(projectId, handler -> {
                                    System.out.println("finisssss--------------------");
                                    JsonObject folders=new JsonObject();
                                    folders.put("folders", handler);
                                    project.put("sourceCode",folders );
//                                    project.put("files", new JsonArray().add(handler.getJsonObject(0).getJsonArray("files").getJsonObject(0)));
//                                    System.out.println(project.toString());
                                    handlerRequest.handle(project);
                                });

                            } else {

                                System.out.println("failed");
                            }
                            connection.close();
                        });
                    } else {
                        System.out.println("auto commit failed");
                    }

                });

                // Got a connection
            } else {
                // Failed to get connection - deal with it
                System.out.println("true failes");
            }
        });

    }

    public void getProjectStructure(String projectId, Handler<JsonArray> handler) {
//        String queryFolder = "SELECT pk_id_folder as id, name FROM folders WHERE folders.`PROJECT_pk_id_project`=" + projectId + ";";
//        String queryFolder ="SELECT * FROM folders , files WHERE files.pk_id_file=folders.pk_id_folder";

//        String queryFolder = "SELECT files.`name` as name, files.crete_date as dateCreated, files.modify_date as dateModified , files.pk_id_file as id, files.FOLDERS_pk_id_folder as folderId FROM  folders RIGHT JOIN files   ON folders.pk_id_folder=files.pk_id_file and folders.pk_id_folder IN (SELECT pk_id_folder FROM folders WHERE `PROJECT_pk_id_project`="+projectId+")";
        String queryFolder = "SELECT pk_id_folder as id, name, create_date as \"dateCreated\", modify_date as \"dateModified\" FROM folders WHERE folders.`PROJECT_pk_id_project`=" + projectId + ";";
        mySQLClient.getConnection(resConnection -> {
            if (resConnection.succeeded()) {
                SQLConnection connection;
                connection = resConnection.result();
                connection.setAutoCommit(false, autoCommit -> {
                    if (autoCommit.succeeded()) {

                        connection.query(queryFolder, handlerQuery -> {
                            if (handlerQuery.succeeded()) {

                                ResultSet resultSet = handlerQuery.result();

                                System.out.println(resultSet.toJson());

                                getFileFromFrolder(projectId, resultSet.getRows(), handler);
//                                List<JsonObject> list = resultSet.getRows();
//                                for (JsonObject folder : list) {
//                                    System.out.println(folder.getInteger("id") + " id of folders user");;
//                                    getFileFromFrolder(folder.getInteger("id")+"", handler->{
//                                        folder.put("files", handler);
//                                        System.out.println("Finish 1 "+new JsonArray(list).toString());
//                                    });
//                                }
//                                System.out.println("Finish "+new JsonArray(list).toString());
                            } else {

                                System.out.println("failed");
                            }
                            connection.close();
                        });
                    } else {
                        System.out.println("auto commit failed");
                    }

                });

                // Got a connection
            } else {
                // Failed to get connection - deal with it
                System.out.println("true failes");
            }
        });

    }

    public String getFileSource(String username, String fileId) {
        return "cuma isi codingan aja... heheheheh";
    }

}
