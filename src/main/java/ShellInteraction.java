
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.term.TelnetTermOptions;

import java.util.ArrayList;
import java.util.Formatter;
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
//        String appCommand = " cd  /home/root/arduinoEasyIDE/project/galih1994_811ed91ecbc040448720d26faafa19c1/ & make ";
        String [] command=new String[]{"/bin/sh", "-c","cd /home/root/arduinoEasyIDE/project/galih1994_811ed91ecbc040448720d26faafa19c1 ; make"};
//        String[] command = new String[]{"sudo", "cd ~", "&", " make"};
//        String appCommand ="make";
        try {
            String line;
            Process p = Runtime.getRuntime().exec(command);

            String tmp="";
            while (p.isAlive() || p.getInputStream().available() > 0) {
                p.getOutputStream().flush();
                if (p.getInputStream().available() > 0) {
                    byte buff[] = new byte[1];
                    p.getInputStream().read(buff);
//                    System.out.print(new String(buff) + " |");
                    tmp+=new String(buff);
                    System.out.print(new String(buff));
//                    response.write(new String(buff));
                }
                
//                if (tmp.length()>10) {
//                    response.write(tmp);
//                }
//                Thread.sleep(10);
            }
            response.putHeader("content-type", "text/plain").end(tmp);
//            response.end(tmp);
            System.out.println("finish compile");

            
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

    public void getDate() {
        System.out.println("Read date");
        String appCommand = "date";
        try {
            String line;
            Process p = Runtime.getRuntime().exec(appCommand);

            while (p.isAlive() || p.getInputStream().available() > 0) {
                p.getOutputStream().flush();
                if (p.getInputStream().available() > 0) {
                    byte buff[] = new byte[1];
                    p.getInputStream().read(buff);
                    System.out.print(new String(buff));
                }
                Thread.sleep(100);

            }
            System.out.println("");
            System.out.println("After");
            byte buff[] = new byte[10];
            p.getInputStream().read(buff);
            System.out.println(new String(buff));

            System.out.println(p.exitValue());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
