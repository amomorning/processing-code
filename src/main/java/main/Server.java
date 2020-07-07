package main;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.FileHandler;

/**
 * @classname: processing-code
 * @description:
 * @author: amomorning
 * @date: 2020/07/07
 */
public class Server {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(48201), 0);
        System.out.println("Server bind to 35553..");
        server.createContext("", new FileHandler());
    }

    static class FileHandler implements HttpHandler {

        /**
         *
         */
        public void handle(HttpExchange exchange) {

        }
}
