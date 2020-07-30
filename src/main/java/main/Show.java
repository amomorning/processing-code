package main;

import processing.core.PApplet;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Show extends PApplet {


    public void settings() {
        size(1600, 900, P2D);

    }

    public void setup() {
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                float distanceFromTopLeft = dist(x, y, 0, 0);
                float distanceFromTopRight = dist(x, y, width, 0);
                float distanceFromBottomLeft = dist(x, y, 0, 0);

                stroke(distanceFromTopLeft/width*210, distanceFromTopRight/width*190, distanceFromBottomLeft/height*200);
                point(x, y);
            }
        }
    }

    public void draw() {

    }


}
