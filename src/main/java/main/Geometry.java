package main;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.hemesh.HEC_FromPolygons;
import wblut.hemesh.HE_Mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @classname: processing-code
 * @description:
 * @author: amomorning
 * @date: 2020/08/12
 */
public class Geometry {
    public List<double[]> verts;
    public List<int[]> faces;
    public List<int[]> wires;
    public List<double[]> circles;
    public List<String> colors_faces;
    public List<String> colors_circles;
    public List<String> colors_wires;
    private int count = 0;

    public Geometry() {
        verts = new ArrayList<>();
        faces = new ArrayList<>();
        circles = new ArrayList<>();
        colors_faces = new ArrayList<>();
        colors_circles = new ArrayList<>();
        wires = new ArrayList<>();
        colors_wires = new ArrayList<>();
    }

    public void addMesh(HE_Mesh mesh, String color, String wireColor) {
        WB_CoordCollection pts = mesh.getPoints();


        for (int i = 0; i < pts.size(); i++) {
            WB_Coord pt = pts.get(i);
            verts.add(new double[]{pt.xd(), pt.yd(), pt.zd()});
        }

        int[] tris = mesh.getTriangles();
        for (int i = 0; i < tris.length; i += 3) {
            int[] temp = new int[]{count + tris[i], count + tris[i + 1], count + tris[i + 2]};
            int[] wire_temp = new int[]{count + tris[i], count + tris[i + 1], count + tris[i + 2]};
            faces.add(temp);
            wires.add(wire_temp);
            colors_wires.add(wireColor);
            colors_faces.add(color);

        }
        count += pts.size();

    }

    public void addPolygon(WB_Polygon polygon, String color, String wireColor) {
        WB_Point p1 = new WB_Point(1, 0);
        WB_Point p2 = new WB_Point(1, 1);
        WB_Point p3 = new WB_Point(0, 1);
        WB_Point p4 = new WB_Point(0, 0);
        WB_Polygon p=new WB_Polygon(p1,p2,p3,p4);
        HEC_FromPolygons creator = new HEC_FromPolygons(Arrays.asList(polygon));
        HE_Mesh mesh = creator.create();
        addMesh(mesh, color, wireColor);
    }

    public void addCircle(WB_Coord center, double radius, String color) {
        circles.add(new double[]{center.xd(), center.yd(), center.zd(), radius});
        colors_circles.add(color);
    }
}

