
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

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
                .put("username", "root")
                .put("password", "budiman1994")
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

            System.out.println(resConnection.cause().toString());
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

    public JsonArray getListProject(String username) {
        String returnList = "[\n"
                + "    {\n"
                + "        \"name\": \"nameProject\",\n"
                + "        \"id\": \"1\",\n"
                + "        \"date\": \"11-2-1994\"\n"
                + "    },\n"
                + "    {\n"
                + "        \"name\": \"nameProject\",\n"
                + "        \"id\": \"1\",\n"
                + "        \"date\": \"11-2-1994\"\n"
                + "    },\n"
                + "    {\n"
                + "        \"name\": \"nameProject\",\n"
                + "        \"id\": \"1\",\n"
                + "        \"date\": \"11-2-1994\"\n"
                + "    },\n"
                + "    {\n"
                + "        \"name\": \"nameProject\",\n"
                + "        \"id\": \"1\",\n"
                + "        \"date\": \"11-2-1994\"\n"
                + "    }    \n"
                + "]";

        return new JsonArray(returnList);
    }

    public String getFileSource(String username, String fileId) {
        return "cuma isi codingan aja... heheheheh";
    }

}
