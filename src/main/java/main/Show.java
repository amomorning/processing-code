package main;

import Guo_Cam.CameraController;
import com.google.gson.Gson;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import processing.core.PApplet;
import wblut.hemesh.HEC_Cylinder;
import wblut.hemesh.HES_CatmullClark;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render;
import wblut.processing.WB_Render3D;

import java.net.URISyntaxException;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Show extends PApplet {
    public HE_Mesh mesh;
    public WB_Render3D render;
    public CameraController cam;
    boolean flag = false;

    static public Socket socket;
    static public Controls controls;

    public void settings() {
        size(1000, 1000, P3D);
        smooth(8);
    }

    public void setup() {
        IOUtils();
        cam = new CameraController(this, 1000);

        render = new WB_Render(this);
    }


    public void draw() {

        flag = true;
        background(55);
        cam.drawSystem(1000);
        directionalLight(255, 255, 255, 1, 1, -1);
        directionalLight(127, 127, 127, -1, -1, 1);
        fill(255);
        noStroke();
        render.drawFaces(mesh);
        stroke(0);
        render.drawEdges(mesh);

    }


    public void createMesh() {
        HEC_Cylinder creator = new HEC_Cylinder();
        creator.setFacets(6).setSteps(1).setRadius(250).setHeight(500).setCap(true, false);
        mesh = new HE_Mesh(creator);
    }

    public void IOUtils() {

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
                // receive
                controls = gson.fromJson(args[0].toString(), Controls.class);
                // processing
                Geometry geom = parseGeometry(controls);

                // send
                String sg = gson.toJson(geom);
                System.out.println(sg);
                socket.emit("geometryExchange", sg);
            }
        });
    }

    public Geometry parseGeometry(Controls controls) {
        Geometry geom = new Geometry();
        createMesh();
        HES_CatmullClark subdivide = new HES_CatmullClark();
        subdivide.setKeepBoundary(true);// preserve position of vertices on a surface boundary
        subdivide.setKeepEdges(true);// preserve position of vertices on edge of selection (only useful if using subdivideSelected)
        subdivide.setBlendFactor(1.0); //controls how much the vertices are moved: 0.0=planar, 1.0=true Catmull-Clark
        mesh.subdivide(subdivide, controls.step);

        geom.addMesh(mesh, "#EEEEEE", "#000000");
        return geom;
    }
}
