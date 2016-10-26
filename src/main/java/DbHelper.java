
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;

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
    AsyncSQLClient postgreSQLClient;

    static public DbHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DbHelper();
        }
        return dbHelper;
    }

    public void init(Vertx vertx) {
        JsonObject mysqlConfig = new JsonObject()
                //                .put("host", "31.220.54.10")
                //                .put("port", 5432)
                .put("maxPoolSize", 1000)
                .put("username", "kukuliner")
                .put("password", "budiman1994")
                .put("database", "kulikuliner")
                .put("queryTimeout", 7000);
        this.postgreSQLClient = MySQLClient.createShared(vertx, mysqlConfig);
    }

    public DbHelper() {
    }

    public void addUser() {
//        INSERT INTO `user_2`(`user_name`, `display_name`, `detail`, `pass`, `key_2`) VALUES ('galih1994','Dienstya galih','detail','123','')

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

}
