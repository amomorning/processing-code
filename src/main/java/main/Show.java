package main;

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

    public Socket socket;

    public void settings() {
        size(1000, 1000, P2D);

    }

    public void setup() {
        background(255, 122, 0);
        mouseX = -0x3f;
        mouseY = -0x3f;

        try {
            socket = IO.socket("http://127.0.0.1:23810");
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public void draw(){
        // Draw the box
        if(mouseX != -0x3f && mouseY != -0x3f) {
            rect(mouseX, mouseY, 200, 100);
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

}
