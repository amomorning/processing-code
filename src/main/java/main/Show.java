package main;

import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import processing.core.PApplet;
import wblut.geom.WB_Circle;
import wblut.geom.WB_Point;
import wblut.geom.WB_Quad;
import wblut.geom.WB_Triangle;
import wblut.processing.WB_Render2D;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Show extends PApplet {

    private Socket socket;
    private Controls controls;
    private WB_Render2D render;

    private List<WB_Quad> quads;
    private List<WB_Triangle> tris;
    private List<WB_Circle> circles;



    public void settings() {
        size(1000, 1000, P2D);

    }

    public void setup() {

        background(192);
        render = new WB_Render2D(this);
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

                    Geometry geom = parseGeometry(controls);
                    String sg = gson.toJson(geom);
                    System.out.println(sg);

                    socket.emit("geometryExchange", sg);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }


    public void draw(){
        background(192);

        if(controls != null) {

            int[] color = hex2Rgb(controls.typeColor);
            fill(color[0], color[1], color[2]);
            noStroke();

            if (quads != null) render.drawQuad2D(quads);
            if (tris != null) render.drawTriangle2D(tris);
            if (circles != null) {
                for (WB_Circle c : circles) {
                    render.drawCircle2D(c);
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

    public int[] hex2Rgb(String colorStr) {
        return new int[] {
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) };
    }

    public Geometry parseGeometry(Controls controls) {
        Geometry geom = new Geometry();

        quads = new ArrayList<WB_Quad>();
        tris = new ArrayList<WB_Triangle>();
        circles = new ArrayList<WB_Circle>();


        double cell_X = (double) width / controls.LinNum_X;
        double cell_Y = (double) height / controls.LinNum_Y;

        int cnt = 0;
        for (int i = 0; i < controls.LinNum_X; ++i) {
            for (int j = 0; j < controls.LinNum_Y; ++j) {

                WB_Point center = new WB_Point((0.5f + i) * cell_X, (0.5f + j) * cell_Y);

                if (controls.shaPoi.equals("circle")) {
                    double radius = Math.min(cell_X, cell_Y) * controls.sizPoi;

                    WB_Circle c = new WB_Circle(center, radius);
                    circles.add(c);
                    geom.addVerts(center);
                    cnt = geom.addCircle(center, radius, cnt, 31);



                } else if (controls.shaPoi.equals("square")) {
                    double szX = cell_X * controls.sizPoi * 0.5;
                    double szY = cell_Y * controls.sizPoi * 0.5;

                    WB_Point p0 = new WB_Point(center.xd() + szX, center.yd() + szY);
                    WB_Point p1 = new WB_Point(center.xd() - szX, center.yd() + szY);
                    WB_Point p2 = new WB_Point(center.xd() - szX, center.yd() - szY);
                    WB_Point p3 = new WB_Point(center.xd() + szX, center.yd() - szY);

                    WB_Quad q = new WB_Quad(p0, p1, p2, p3);
                    quads.add(q);
                    cnt = geom.addRectangle(p0, p1, p2, p3, cnt);


                } else if (controls.shaPoi.equals("triangle")) {
                    double szX = cell_X * controls.sizPoi * 0.5;
                    double szY = cell_Y * controls.sizPoi * 0.5;

                    WB_Point p0 = new WB_Point(center.xd(), center.yd() + szY);
                    WB_Point p1 = new WB_Point(center.xd() - szX, center.yd() - szY);
                    WB_Point p2 = new WB_Point(center.xd() + szX, center.yd() - szY);

                    WB_Triangle t = new WB_Triangle(p0, p1, p2);
                    tris.add(t);
                    cnt = geom.addTriangle(p0, p1, p2, cnt);

                }
            }
        }
        return geom;
    }
}
