package main;

import Guo_Cam.CameraController;
import Guo_Cam.Vec_Guo;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import processing.core.PApplet;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render;
import wblut.processing.WB_Render3D;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Show extends PApplet {
    public WB_Render3D render;
    public CameraController cam;
    boolean flag = false;

    public WB_Polygon ply;
    int color = 255;
    public void settings() {
        size(600, 600, P3D);
        System.out.println(displayDensity());
        pixelDensity(displayDensity());
        smooth(8);
    }

    public void setup() {
        cam = new CameraController(this, 1000);

        render = new WB_Render(this);
        cam.getCamera().setPosition(new Vec_Guo(0, 0, 1000));
        cam.getCamera().setLookAt(new Vec_Guo(0, 0, 0));
        cam.setRotateButton(10);

        WB_Point[] pts = new WB_Point[5];
        pts[0] = new WB_Point(0, 0, 0);
        pts[1] = new WB_Point(100, 0, 0);
        pts[2] = new WB_Point(100, 100, 0);
        pts[3] = new WB_Point(50, 50, 0);
        pts[4] = new WB_Point(0, 100, 0);
        ply = new WB_Polygon(pts);
    }


    public void draw() {

        flag = true;
        background(55);
        fill(color);

        render.drawPolygonEdges(ply);

        cam.begin2d();
        fill(255);
        rect(0, 0, 100, 1000);
        cam.begin3d();
    }


    public void mouseMoved() {
        WB_Point pt = new WB_Point(cam.getCoordinateFromScreenOnXYPlaneDouble(mouseX, mouseY));

        Polygon p = toJTSPolygon(ply);
        Coordinate c = new Coordinate(pt.xd(), pt.yd());
        GeometryFactory gf = new GeometryFactory();


        if(p.contains(gf.createPoint(c))) {
            color = 0;
        } else {
            color = 255;
        }

    }


    public static Polygon toJTSPolygon(WB_Polygon ply) {
        WB_Coord[] polypt = ply.getPoints().toArray();
        Coordinate[] pts = new Coordinate[polypt.length + 1];

        for (int i = 0; i < polypt.length; ++i) {
            pts[i] = new Coordinate(polypt[i].xd(), polypt[i].yd());
        }
        pts[polypt.length] = new Coordinate(polypt[0].xd(), polypt[0].yd());
        return new GeometryFactory().createPolygon(pts);
    }


}
