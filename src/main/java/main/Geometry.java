package main;

import wblut.geom.WB_Point;

import java.util.ArrayList;
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

    public Geometry() {
        verts = new ArrayList<double[]>();
        faces = new ArrayList<int[]>();
    }

    public void addVerts(WB_Point pt) {
        verts.add(new double[] {pt.xd(), pt.yd(), pt.zd()});
    }

    public void addFaces(int f0, int f1, int f2) {
        faces.add(new int[] {f0, f1, f2});
    }

}

