package main;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Show extends PApplet {

    Tools tools;

    LineString[] segs;
    Polygon[] polys;
    int[] idx;
    List<LineString> outers;

    public void settings() {
        size(1000, 1000, P3D);
        smooth(8);

        int divideNum = 100;
        segs = new LineString[9];
        segs[0] = createLineString(6, 19, 17, 24, divideNum);
        segs[1] = createLineString(22, 10, 38, 14, divideNum);
        segs[2] = createLineString(22, 19, 38, 16, divideNum);
        segs[3] = createLineString(15, 20, 16, 10, divideNum);
        segs[4] = createLineString(3, 10, 19, 5, divideNum);

        segs[5] = createLineString(1, 0, 44, 0, divideNum);
        segs[6] = createLineString(45, 1, 45, 29, divideNum);
        segs[7] = createLineString(1, 30, 44, 30, divideNum);
        segs[8] = createLineString(0, 1, 0, 29, divideNum);


        buildVoronoi();
    }


    public void setup() {
        tools = new Tools(this, 100);

        colorMode(HSB, 100);

        tools.cam.top();
    }


    public void draw() {

        background(10, 1, 100);


        for (LineString ls : segs) {
            fill(0);
            stroke(0);
            tools.render.drawSegment(Tools.toWB_Segment(ls));

            tools.drawPoint(ls.getCoordinateN(0), 0.2);
            tools.drawPoint(ls.getCoordinateN(ls.getNumPoints() - 1), 0.2);

        }

        for (LineString ls : outers) {

            noFill();
            stroke(70, 100, 100);
            strokeWeight(2);
            tools.render.drawPolylineEdges(Tools.toWB_PolyLine(ls));

        }
        strokeWeight(1);


        for (int i = 0; i < polys.length; ++i) {

            fill(idx[i] * 10 + 10, 10, 100);
            noStroke();
//            stroke(0);
            tools.render.drawPolygonEdges(Tools.toWB_Polygon(polys[i]));
        }
    }

    public LineString createLineString(double x1, double y1, double x2, double y2, int n) {

        GeometryFactory gf = new GeometryFactory();
        Coordinate[] p = new Coordinate[n + 1];


        for (int i = 0; i < n + 1; i++) {
            double t = i * 1.0 / n;
            p[i] = new Coordinate(x1 + t * (x2 - x1), y1 + t * (y2 - y1));
        }
        return gf.createLineString(p);
    }

    public void buildVoronoi() {

        GeometryFactory gf = new GeometryFactory();
        Envelope env = new Envelope(-10, 55, -10, 40);
        VoronoiDiagramBuilder builder = new VoronoiDiagramBuilder();
        builder.setClipEnvelope(env);

        GeometryCollection collection = gf.createGeometryCollection(segs);
        builder.setSites(collection);

        Geometry gm = builder.getDiagram(gf);

        polys = new Polygon[gm.getNumGeometries()];
        for (int i = 0; i < gm.getNumGeometries(); ++i) {
            polys[i] = (Polygon) gm.getGeometryN(i);
        }

        idx = new int[polys.length];
        for (int i = 0; i < polys.length; ++i) {
            Polygon ply = polys[i];
            for (int j = 0; j < segs.length; ++j) {
                if (ply.intersects(segs[j])) idx[i] = j;
            }
        }
        System.out.println(Arrays.toString(idx));

//        outers = new LineString[segs.length];
        outers = new ArrayList<>();
        for (int j = 0; j < segs.length; ++j) {
            List<Polygon> tmp = new ArrayList<>();

            for (int i = 0; i < polys.length; ++i) {
                if (idx[i] == j) tmp.add(polys[i]);
            }
            GeometryCollection gc = gf.createGeometryCollection(tmp.toArray(new Polygon[0]));

            Geometry boundary = gc.union().getBoundary();
            if(Objects.equals(boundary.getGeometryType(), "LinearRing")) {
                outers.add((LineString) boundary);

            } else if(Objects.equals(boundary.getGeometryType(), "MultiLineString")) {
                for(int k = 0; k < boundary.getNumGeometries(); ++ k) {
                    outers.add((LineString) boundary.getGeometryN(k));
                }
            }
        }


    }

    public static void main(String[] args) {
        PApplet.main("main.Show");
    }


}
