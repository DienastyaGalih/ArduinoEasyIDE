/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class Console extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        
        
    
        
        
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        
        router.route().handler(StaticHandler.create().setWebRoot("C:/Users/User/Documents/bekUP/ArduinoOnlineIDE/resources"));
        
        Route route1 = router.route("/net/*").handler(routingContext -> {

            
            System.out.println("Netttt"+routingContext.getBody());
            HttpServerResponse response = routingContext.response();
            response.sendFile("webroot/index.html");
        });

        server.requestHandler(router::accept).listen(8080);

    }
}
