package main;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Server {
    private static final int PORT = 27781;
    private Socket socket;

    public Server (String ...args) {
        try {
            if (args.length > 0) {
                socket = IO.socket(args[0]);
                this.setup();
                System.out.println("Socket connected to " + args[0]);
            } else {
                String uri = "http://localhost:" + PORT;
                socket = IO.socket(uri);
                this.setup();
                System.out.println("Socket connected to " + uri);
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        Gson gson = new Gson();
        socket.connect();

        socket.on(Socket.EVENT_CONNECT, args -> {
            JsonObject o = new JsonObject();
            o.addProperty("key", "cb792abe-c615-45f9-9b64-e9d95ce2dd94");
            socket.emit("register", gson.toJson(o));
        });
    }

    public static void main(String[] args) {
        Server server = new Server();

    }
}
