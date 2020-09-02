package main;

import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import processing.core.PApplet;
import wblut.geom.*;
import wblut.processing.WB_Render2D;

import java.net.URISyntaxException;
import java.util.*;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Show extends PApplet {

    private Socket socket;
    private WB_Render2D render;

    static public Controls controls;
    static private List<WB_Quad> quads;
    static private List<WB_Triangle> tris;
    static private List<WB_Circle> circles;
    static private boolean drawFlag = false;


    public void settings() {
        size(1000, 1000);

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

            socket.on("modulateReceiveParameters", new Emitter.Listener() {

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


    public void draw() {
        if (drawFlag) {

            int[] bc = hex2Rgb(controls.background);
            background(bc[0], bc[1], bc[2]);
            translate(0, height - 1);
            scale(1, -1);


            int[] color = hex2Rgb(controls.typeColor);
            fill(color[0], color[1], color[2]);
            noStroke();

            render.drawQuad2D(quads);
            render.drawTriangle2D(tris);
            for (WB_Circle c : circles) {
                render.drawCircle2D(c);
            }
        }
    }

    public int[] hex2Rgb(String colorStr) {
        return new int[]{
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16)};
    }

    static public Geometry parseGeometry(Controls controls) {
        Geometry geom = new Geometry();
        Random rand = new Random(controls.seed);

        // 初始化
        quads = new ArrayList<WB_Quad>();
        tris = new ArrayList<WB_Triangle>();
        circles = new ArrayList<WB_Circle>();

        // 确定 cell 尺寸
        double cell_X = (double) controls.width / controls.LinNum_X;
        double cell_Y = (double) controls.height / controls.LinNum_Y;

        // 确定轴网偏移
        double[] rndL_X = new double[controls.LinNum_X];
        double[] rndL_Y = new double[controls.LinNum_Y];

        double len_X = controls.sizPoi_max < 1.0 ? cell_X * (1.0 - controls.sizPoi_max) : 0;
        double len_Y = controls.sizPoi_max < 1.0 ? cell_Y * (1.0 - controls.sizPoi_max) : 0;

        for (int i = 0; i < controls.LinNum_X; ++i) {
            rndL_X[i] = controls.shiL_X ? rand.nextDouble() * len_X - len_X / 2 : 0;
        }

        for (int i = 0; i < controls.LinNum_Y; ++i) {
            rndL_Y[i] = controls.shiL_Y ? rand.nextDouble() * len_Y - len_Y / 2 : 0;
        }

        // 筛选点数量
        rand = new Random(controls.seed);
        int total = (int) (controls.selPoi * controls.LinNum_X * controls.LinNum_Y);
        Set<Integer> st = new HashSet<Integer>();
        while (st.size() < total) {
            st.add(rand.nextInt(controls.LinNum_X * controls.LinNum_Y));
        }

        // 确定点尺寸
        rand = new Random(controls.seed);
        double[] pointSize = new double[controls.LinNum_X * controls.LinNum_Y];
        for(int i = 0; i < controls.LinNum_X * controls.LinNum_Y; ++ i) {
            pointSize[i] = rand.nextDouble() * (controls.sizPoi_max - controls.sizPoi_min) + controls.sizPoi_min;
        }

        int cnt = 0;
        rand = new Random(controls.seed);
        for (int i = 0; i < controls.LinNum_X; ++i) {
            for (int j = 0; j < controls.LinNum_Y; ++j) {

                int index = i * controls.LinNum_Y + j;
                if (!st.contains(index)) continue;

                // 确定点位置

                double rndP_X = controls.shiP_X ? rand.nextDouble() * len_X - len_X / 2 : 0;
                double rndP_Y = controls.shiP_Y ? rand.nextDouble() * len_Y - len_Y / 2 : 0;

                WB_Point center = new WB_Point((0.5f + i) * cell_X + rndL_X[i] + rndP_X, (0.5f + j) * cell_Y + rndL_Y[j] + rndP_Y);


                if (controls.shaPoi.equals("circle")) {
                    double radius = Math.min(cell_X, cell_Y) * pointSize[index] / 2.0;
                    WB_Circle c = new WB_Circle(center, radius);

                    circles.add(c);
                    geom.addVerts(center);
                    geom.addCircle(center, radius);

                } else if (controls.shaPoi.equals("square")) {
                    double szX = cell_X * pointSize[index] * 0.5;
                    double szY = cell_Y * pointSize[index] * 0.5;

                    WB_Point p0 = new WB_Point(center.xd() + szX, center.yd() + szY);
                    WB_Point p1 = new WB_Point(center.xd() - szX, center.yd() + szY);
                    WB_Point p2 = new WB_Point(center.xd() - szX, center.yd() - szY);
                    WB_Point p3 = new WB_Point(center.xd() + szX, center.yd() - szY);

                    WB_Quad q = new WB_Quad(p0, p1, p2, p3);

                    WB_Transform2D trans = new WB_Transform2D();
                    double angle = controls.angPoi / 180 * Math.PI;
                    trans.addRotateAboutPoint(angle, center);
                    q.apply2DSelf(trans);

                    quads.add(q);
                    cnt = geom.addRectangle(q.getP1(), q.getP2(), q.getP3(), q.getP4(), cnt);

                } else if (controls.shaPoi.equals("triangle")) {
                    double szX = cell_X * pointSize[index] * 0.5;
                    double szY = cell_Y * pointSize[index] * 0.5;

                    WB_Point p0 = new WB_Point(center.xd(), center.yd() + szY);
                    WB_Point p1 = new WB_Point(center.xd() - szX, center.yd() - szY);
                    WB_Point p2 = new WB_Point(center.xd() + szX, center.yd() - szY);

                    WB_Triangle t = new WB_Triangle(p0, p1, p2);

                    WB_Transform2D trans = new WB_Transform2D();
                    double angle = controls.angPoi / 180 * Math.PI;
                    trans.addRotateAboutPoint(angle, center);
                    t.apply2DSelf(trans);

                    tris.add(t);
                    cnt = geom.addTriangle(t.p1(), t.p2(), t.p3(), cnt);

                }
            }
        }
        System.out.println("Number of circles = " + circles.size());
        System.out.println("Number of squares = " + quads.size());
        System.out.println("Number of triangles = " + tris.size());
        drawFlag = true;
        return geom;
    }
}
