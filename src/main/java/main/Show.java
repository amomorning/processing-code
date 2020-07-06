package main;

import processing.core.PApplet;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Show extends PApplet {

    float bx, by;
    int boxSize = 75;
    boolean overBox = false;
    boolean locked = false;
    float xOffset = 0.0f;
    float yOffset = 0.0f;

    public void settings() {
        size(1000, 1000, P2D);

    }

    public void setup() {
        bx = width/2.0f;
        by = height/2.0f;
        rectMode(RADIUS);
    }



    public void draw(){
        background(0);

        // Test if the cursor is over the box
        if (mouseX > bx-boxSize && mouseX < bx+boxSize &&
                mouseY > by-boxSize && mouseY < by+boxSize) {
            overBox = true;
            if(!locked) {
                stroke(255);
                fill(153);
            }
        } else {
            stroke(153);
            fill(153);
            overBox = false;
        }

        // Draw the box
        rect(bx, by, boxSize, boxSize);
    }


    public void keyPressed() {

    }

    public void mousePressed() {
        if(overBox) {
            locked = true;
            fill(255, 255, 255);
        } else {
            locked = false;
        }
        xOffset = mouseX-bx;
        yOffset = mouseY-by;

    }

    public void mouseDragged() {
        if(locked) {
            bx = mouseX - xOffset;
            by = mouseY - yOffset;
        }
    }

    public void mouseReleased() {
        locked = false;
    }
}
