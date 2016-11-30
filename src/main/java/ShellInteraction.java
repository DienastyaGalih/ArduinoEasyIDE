
import com.fazecast.jSerialComm.SerialPort;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import netscape.javascript.JSObject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.WorkerExecutor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class ShellInteraction {

    private Vertx vertx;
    private static ShellInteraction shellInteraction;

    private ShellInteraction() {
    }

    public static ShellInteraction getInstance() {
        if (shellInteraction == null) {
            shellInteraction = new ShellInteraction();
        }
        return shellInteraction;
    }

    public void init(Vertx vertx) {
        this.vertx = vertx;
    }

    public void compile(String projectId, HttpServerResponse response) {
        System.out.println("Read date");
//        String[] command = new String[]{"/bin/sh", "-c", "cd /home/root/arduinoEasyIDE/project/galih1994_811ed91ecbc040448720d26faafa19c1 ; make"};
        String[] command = new String[]{"/bin/sh", "-c", "cd ~/ArduinoEasyIDE/project/galih1994_9fe4d6779b7c467b8d48e7962d8b91b8 ; make"};

        try {
            String line;
            Process p = Runtime.getRuntime().exec(command);

            String tmp = "";
            response.write("<div class=\"fg-dark\">");
            while (p.isAlive() || p.getInputStream().available() > 0) {
                p.getOutputStream().flush();
                if (p.getInputStream().available() > 0) {
                    byte buff[] = new byte[p.getInputStream().available()];
                    p.getInputStream().read(buff);
                    tmp += new String(buff);
                    System.out.print(new String(buff));

                }

                if (tmp.length() > 10 || !p.isAlive()) {
                    response.write(tmp);
                    tmp = "";
                }
                Thread.sleep(10);
            }
//            response.putHeader("content-type", "text/plain").end(tmp);
            response.write("</div><br>");
            System.out.println("finish compile");

            p.waitFor();

            int errorData = p.getErrorStream().available();
            if (errorData > 0) {
                response.write("<div class=\"fg-red\">-----Eror----<br>");
                byte[] readData = new byte[errorData];
                p.getErrorStream().read(readData);
                response.end(new String(readData) + "</div>");
            } else {

                response.end("<div class=\"fg-green\">-----No Error----<br></div>");
            }

//            while (p.isAlive() || p.getErrorStream().available() > 0) {
//
//                if (p.getErrorStream().available() > 0) {
//                    byte buff[] = new byte[1];
//                    p.getErrorStream().read(buff);
//                    System.out.print(new String(buff) + " |");
//                    tmp+=new String(buff);
//                    System.out.println(tmp);
////                    response.write(new String(buff));
//
//                }
//                if (tmp.length()>10) {
//                    response.write(tmp+ "adasdasdfs");
//                }
//                
//                Thread.sleep(10);
//
//            }
//            p.waitFor();
//            
            System.out.println("");
//            System.out.println("After opo");
//            byte buff[] = new byte[100];
//            p.getInputStream().read(buff);
//            System.out.println(new String(buff));

            System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void clean(String projectId, HttpServerResponse response) {
        System.out.println("Read date");

        String[] command = new String[]{"/bin/sh", "-c", "cd ~/ArduinoEasyIDE/project/galih1994_9fe4d6779b7c467b8d48e7962d8b91b8 ; make clean"};

        try {
            String line;
            Process p = Runtime.getRuntime().exec(command);

            String tmp = "";
            response.write("<div class=\"fg-dark\">");
            while (p.isAlive() || p.getInputStream().available() > 0) {
                p.getOutputStream().flush();
                if (p.getInputStream().available() > 0) {
                    byte buff[] = new byte[p.getInputStream().available()];
                    p.getInputStream().read(buff);
                    tmp += new String(buff);
                    System.out.print(new String(buff));

                }

                if (tmp.length() > 10 || !p.isAlive()) {
                    response.write(tmp);
                    tmp = "";
                }
                Thread.sleep(10);
            }
//            response.putHeader("content-type", "text/plain").end(tmp);
            response.write("</div><br>");
            System.out.println("finish compile");

            p.waitFor();

            int errorData = p.getErrorStream().available();
            if (errorData > 0) {
                response.write("<div class=\"fg-red\">-----Eror----<br>");
                byte[] readData = new byte[errorData];
                p.getErrorStream().read(readData);
                response.end(new String(readData) + "</div>");
            } else {

                response.end("<div class=\"fg-green\">-----No Error----<br></div>");
            }

            System.out.println("");

            System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void compileAndUpload(String projectId, HttpServerResponse response) {
        System.out.println("Read date");

        String[] command = new String[]{"/bin/sh", "-c", "cd ~/ArduinoEasyIDE/project/" + projectId + " ; make upload"};

        try {
            String line;
            Process p = Runtime.getRuntime().exec(command);

            String tmp = "";
            response.write("<div class=\"fg-dark\">");
            while (p.isAlive() || p.getInputStream().available() > 0) {
                p.getOutputStream().flush();
                if (p.getInputStream().available() > 0) {
                    byte buff[] = new byte[p.getInputStream().available()];
                    p.getInputStream().read(buff);
                    tmp += new String(buff);
                    System.out.print(new String(buff));

                }

                if (tmp.length() > 10 || !p.isAlive()) {
                    response.write(tmp);

                    tmp = "";
                }
                Thread.sleep(100);
            }
//            response.putHeader("content-type", "text/plain").end(tmp);
            response.write("</div><br>");
            System.out.println("finish compile");

            p.waitFor();

            int errorData = p.getErrorStream().available();
            if (errorData > 0) {
                response.write("<div class=\"fg-red\">-----Eror----<br>");
                byte[] readData = new byte[errorData];
                p.getErrorStream().read(readData);
                response.end(new String(readData) + "</div>");
            } else {

                response.end("<div class=\"fg-green\">-----No Error----<br></div>");
            }

            System.out.println("");

            System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public List<String> getListComPort() {

        List<String> serialPorts = new ArrayList<>();
        SerialPort[] datas = SerialPort.getCommPorts();
        for (SerialPort data : datas) {
            serialPorts.add(data.getSystemPortName());
            System.out.println(data.getDescriptivePortName());
            System.out.println(data.getSystemPortName());

        }
        return serialPorts;
    }

    public void compileAndUploadRealtime(String projectId, ServerWebSocket response) {
        System.out.println("Read date");

        String[] command = new String[]{"/bin/sh", "-c", "cd ~/ArduinoEasyIDE/project/" + projectId + " ; make upload"};

        try {
            String line;
            Process p = Runtime.getRuntime().exec(command);

            String tmp = "";
            response.writeFinalTextFrame("<div class=\"fg-dark\">");
            while (p.isAlive() || p.getInputStream().available() > 0) {
                p.getOutputStream().flush();
                if (p.getInputStream().available() > 0) {
                    byte buff[] = new byte[p.getInputStream().available()];
                    p.getInputStream().read(buff);
                    tmp += new String(buff);
                    System.out.print(new String(buff));

                }

                if (tmp.length() > 10 || !p.isAlive()) {
                    response.writeFinalTextFrame(tmp);

                    tmp = "";
                }
                Thread.sleep(100);
            }
//            response.putHeader("content-type", "text/plain").end(tmp);
            response.writeFinalTextFrame("</div><br>");
            System.out.println("finish compile");

            p.waitFor();

            int errorData = p.getErrorStream().available();
            if (errorData > 0) {
                response.writeFinalTextFrame("<div class=\"fg-red\">-----Eror----<br>");
                byte[] readData = new byte[errorData];
                p.getErrorStream().read(readData);
                response.writeFinalTextFrame("<div class=\"fg-red\">" + new String(readData) + "</div>");
            } else {

                response.writeFinalTextFrame("<div class=\"fg-green\">-----No Error----<br></div>");
            }

            response.close();
            System.out.println("");

            System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void compileRealtime(String projectId, ServerWebSocket response) {
        System.out.println("Read date");
        String[] command = new String[]{"/bin/sh", "-c", "cd ~/ArduinoEasyIDE/project/"+projectId+" ; make"};

        try {
            String line;
            Process p = Runtime.getRuntime().exec(command);

            String tmp = "";
            response.writeFinalTextFrame("<div class=\"fg-dark\">");
            while (p.isAlive() || p.getInputStream().available() > 0) {
                p.getOutputStream().flush();
                if (p.getInputStream().available() > 0) {
                    byte buff[] = new byte[p.getInputStream().available()];
                    p.getInputStream().read(buff);
                    tmp += new String(buff);
                    System.out.print(new String(buff));

                }

                if (tmp.length() > 10 || !p.isAlive()) {
                    response.writeFinalTextFrame(tmp);
                    tmp = "";
                }

            }

            response.writeFinalTextFrame("</div><br>");
            System.out.println("finish compile");

            while (p.getErrorStream().available() > 0) {

                int errorData = p.getErrorStream().available();
                if (errorData > 0) {
                    response.writeFinalTextFrame("<div class=\"fg-red\">-----Eror----<br>");
                    byte[] readData = new byte[errorData];
                    p.getErrorStream().read(readData);
                    response.writeFinalTextFrame("<div class=\"fg-red\">" + new String(readData) + "</div>");
                } else {

                    response.writeFinalTextFrame("<div class=\"fg-green\">-----No Error----<br></div>");
                }

            }
            response.close();

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void cleanRealtime(String projectId, ServerWebSocket response) {
        System.out.println("Read date");

        String[] command = new String[]{"/bin/sh", "-c", "cd ~/ArduinoEasyIDE/project/"+projectId+" ; make clean"};

        try {
            String line;
            Process p = Runtime.getRuntime().exec(command);

            String tmp = "";
            response.writeFinalTextFrame("<div class=\"fg-dark\">");
            while (p.isAlive() || p.getInputStream().available() > 0) {
                p.getOutputStream().flush();
                if (p.getInputStream().available() > 0) {
                    byte buff[] = new byte[p.getInputStream().available()];
                    p.getInputStream().read(buff);
                    tmp += new String(buff);
                    System.out.print(new String(buff));

                }

                if (tmp.length() > 10 || !p.isAlive()) {
                    response.writeFinalTextFrame(tmp);
                    tmp = "";
                }
                Thread.sleep(100);
            }

            response.writeFinalTextFrame("</div><br>");
            System.out.println("finish compile");

            p.waitFor();

            int errorData = p.getErrorStream().available();
            if (errorData > 0) {
                response.writeFinalTextFrame("<div class=\"fg-red\">-----Eror----<br>");
                byte[] readData = new byte[errorData];
                p.getErrorStream().read(readData);
                response.writeFinalTextFrame("<div class=\"fg-red\">" + new String(readData) + "</div>");
            } else {

                response.writeFinalTextFrame("<div class=\"fg-green\">-----No Error----<br></div>");
            }

            response.close();

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void readSerialRealtime(String projectId, ServerWebSocket response) {

        JsonObject config = FileAccessRaspberry.getInstance().readProjectConfig(projectId);
        SerialPort comPort = SerialPort.getCommPort(config.getString("port"));
        comPort.openPort();

        
        WorkerExecutor executor = vertx.createSharedWorkerExecutor("my-worker-pool");
        executor.executeBlocking(future -> {
            // Call some blocking API that takes a significant amount of time to return
            try {

                while (comPort.isOpen()) {
                    try {
                        int counting=0;
                        while (comPort.bytesAvailable()==0) {
                            Thread.sleep(100);
                        }
                        byte[] readBuffer = new byte[comPort.bytesAvailable()];
                        int numRead = comPort.readBytes(readBuffer, comPort.bytesAvailable());
                        System.out.println("From file "+new String(readBuffer));
                        response.writeFinalTextFrame(new String(readBuffer));
                    } catch (Exception e) {
                        System.out.println("Errorrrrrrr************************" + e.toString());
                        break;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            future.complete();
        }, res -> {
            response.writeFinalTextFrame("\r\n\r\nSerial Error "+res.toString());
            response.close();
            System.out.println("The result is: " + res.result());
        });

        response.frameHandler(hndlr -> {
            try {
                System.out.println("frame " + hndlr.textData());
                comPort.writeBytes(hndlr.textData().getBytes(), hndlr.textData().length());
            } catch (Exception e) {
                System.out.println("Errorrrrrrr************************ "+e.toString());
            }

        });
        response.closeHandler(hndlr -> {
            try {
                comPort.closePort();
                executor.close();
            } catch (Exception e) {
                System.out.println("Error close "+hndlr.toString());
            }

        });

    }

    void serialMonitor(String string, ServerWebSocket ws) {

    }
}
