package main;

import com.google.gson.Gson;
import io.socket.emitter.Emitter;
import org.locationtech.jts.geom.Point;
import processing.core.PApplet;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Show extends PApplet {

    private Socket socket;
    private Controls controls;

    public void settings() {
        size(1000, 1000, P2D);

    }

    public void setup() {

        background(192);
        try {
            socket = IO.socket("http://127.0.0.1:23810");
            socket.connect();

            socket.on("queryCanvasSize", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Sent canvas size: ");
                    String canvasSize = "{\"width\":" + width + ",\"height\":" + height + "}";
                    System.out.println(canvasSize);
                    socket.emit("changeCanvas", canvasSize);
                }
            });

            socket.on("receiveParameters", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println(args.length);
                    System.out.println(args[0]);
                    Gson gson = new Gson();
                    controls = gson.fromJson(args[0].toString(), Controls.class);
                    System.out.println(controls.shaPoi);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }


    public void draw(){
        background(192);

        if(controls != null) {
            float cell_X = width / controls.LinNum_X;
            float cell_Y = height / controls.LinNum_Y;

            int[] color = hex2Rgb(controls.typeColor);
            fill(color[0], color[1], color[2]);
            noStroke();
            for (int i = 0; i < controls.LinNum_X; ++i) {
                for (int j = 0; j < controls.LinNum_Y; ++j) {
                    float scale = (float) controls.sizPoi;
//                    WB_Point center = new Point((0.5f + i) * cell_X - 0.5f * scale * cell_X, (0.5f + j) * cell_Y - 0.5f * scale * cell_Y);
//                    WB_Circle c = new WB_Circle()

                    rect((0.5f + i) * cell_X - 0.5f * scale * cell_X, (0.5f + j) * cell_Y - 0.5f * scale * cell_Y, scale * cell_X, scale * cell_Y);
                }
            }
        }
    }

    public void keyPressed() {
        if(key == 's' || key == 'S') {
            if(socket != null) {
                socket.emit("create", "hello from java");
            }
        }

        if(key == 'i' || key == 'I') {

        }
    }

    public static int[] hex2Rgb(String colorStr) {
        return new int[] {
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) };
    }

}
