package main;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

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
            o.addProperty("key", "9d8b6db7-1a95-4747-ae96-b5641047794c");
            o.addProperty("identity", "engine");
            socket.emit("register", gson.toJson(o), (Ack) objects -> {
                JSONObject response = (JSONObject) objects[0];
                try {
                    System.out.println(response.getString("status"));

                    // message must send after registered.
                    sendMessage(gson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        socket.on("receive", args -> {
            // ...
            System.out.println(args[0]);
        });


    }

    public void sendMessage(Gson gson) {
        JsonObject o = new JsonObject();
        o.addProperty("to", "client");
        o.addProperty("body", "hello");

        socket.emit("exchange", gson.toJson(o), (Ack) objects -> {
            JSONObject response = (JSONObject) objects[0];
            try {
                System.out.println(response.getString("status"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        Server server = new Server();

    }
}
