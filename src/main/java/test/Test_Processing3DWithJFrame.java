package test;

import Guo_Cam.CameraController;
import com.jogamp.newt.awt.NewtCanvasAWT;
import processing.core.PApplet;
import processing.opengl.PSurfaceJOGL;
import wblut.geom.WB_Point;
import wblut.processing.WB_Render;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: Sample3D
 * @description:
 * @author: Naturalpowder
 * @create: 2021-03-14 10:00
 **/
public class Test_Processing3DWithJFrame extends PApplet {
    private List<WB_Point> points;

    private CameraController camera;
    private WB_Render render;

    public Test_Processing3DWithJFrame() {
        runSketch();
    }

    @Override
    public void settings() {
        size(800, 600, P3D);
    }

    @Override
    public void setup() {
        camera = new CameraController(this);
        render = new WB_Render(this);
        points = new ArrayList<>();
        points.add(new WB_Point(10, 10, 10));
    }

    public void update() {
        points.add(new WB_Point(Math.random() * 100, Math.random() * 100, Math.random() * 100));
    }

    public String getInfo() {
        List<String> strings = points.stream().map(WB_Point::toString).collect(Collectors.toList());
        return String.join("\n", strings);
    }

    @Override
    public void draw() {
        background(255);
        camera.drawSystem(100);
        render.drawPoint(points, 10);
    }

    public static void main(String[] args) {
        // prepare JFrame
        JFrame frame = new JFrame("Processing in JFrame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // run Processing
        Test_Processing3DWithJFrame node = new Test_Processing3DWithJFrame();
        PSurfaceJOGL ps = (PSurfaceJOGL) node.getSurface();
        ps.initOffscreen(node);
        ps.setLocation(0, 0);
        // add canvas to JFrame (used as a Component)
        NewtCanvasAWT canvas = (NewtCanvasAWT) ps.getComponent();
        frame.add(canvas);
        // display
        frame.setSize(800, 600);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame.setVisible(true);
        System.out.println("ok");
    }
}