package main;

import Guo_Cam.CameraController;
import Guo_Cam.Vec_Guo;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;
import wblut.hemesh.HEC_Cylinder;
import wblut.hemesh.HES_CatmullClark;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render;
import wblut.processing.WB_Render3D;

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
    public ControlP5 cp5;

    Vec_Guo pos, lookat;
    double near, far, fov;
    boolean flag = false;
    public void settings() {
        size(1000, 1000, P3D);
        smooth(8);
    }

    public void setup() {
        createMesh();
        cam = new CameraController(this, 1000);
        cp5 = new ControlP5(this);
        cp5.setAutoDraw(false);

        HES_CatmullClark subdividor = new HES_CatmullClark();
        subdividor.setKeepBoundary(true);// preserve position of vertices on a surface boundary
        subdividor.setKeepEdges(true);// preserve position of vertices on edge of selection (only useful if using subdivideSelected)
        subdividor.setBlendFactor(1.0); //controls how much the vertices are moved: 0.0=planar, 1.0=true Catmull-Clark
        mesh.subdivide(subdividor, 3);

        render = new WB_Render(this);

        cp5.addSlider("slider")
                .setPosition(100,305)
                .setSize(200,20)
                .setRange(0,200)
                .setValue(128)
        ;
        cp5.getController("slider").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
        cp5.getController("slider").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
        pos =  cam.getCamera().getPosition();
        lookat = cam.getCamera().getLookAt();
        near = cam.getCamera().getNear();
        far = cam.getCamera().getFar();
        fov = cam.getCamera().getFovy();
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

        hint(DISABLE_DEPTH_TEST);
        cam.begin2d();
        cp5.update();
        cp5.draw();
        cam.begin3d();
        hint(ENABLE_DEPTH_TEST);

    }


    public void createMesh() {
        HEC_Cylinder creator = new HEC_Cylinder();
        creator.setFacets(6).setSteps(1).setRadius(250).setHeight(500).setCap(true, false);
        mesh = new HE_Mesh(creator);
    }

    public void mouseClicked() {
        System.out.println("clicked");
        pos =  cam.getCamera().getPosition();
        lookat = cam.getCamera().getLookAt();
        near = cam.getCamera().getNear();
        far = cam.getCamera().getFar();
        fov = cam.getCamera().getFovy();
    }

    public void mousePressed() {
        System.out.println("pressed");


    }

    public void mouseDragged() {
        System.out.println("dragged");
        System.out.println(pos);
        cam.getCamera().setPosition(pos);
        cam.getCamera().setLookAt(lookat);
        cam.getCamera().setDist(near, far);
        cam.getCamera().setFovy(fov);

    }

    public void slider() {
//        myColor = color(theColor);
        if(flag) {
            cam.setFovy(fov);
            cam.getCamera().setPosition(pos);
            cam.getCamera().setLookAt(lookat);
            cam.getCamera().setDist(near, far);

        }
    }

}
