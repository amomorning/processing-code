package main;

import wblut.geom.WB_Coord;
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
    public List<double[]> circles;

    public Geometry() {
        verts = new ArrayList<double[]>();
        faces = new ArrayList<int[]>();
        circles = new ArrayList<double[]>();
    }

    public void addVerts(WB_Coord pt) {
        verts.add(new double[]{pt.xd(), pt.yd(), pt.zd()});
    }

    public void addFaces(int f0, int f1, int f2) {
        faces.add(new int[]{f0, f1, f2});

    }

    public void addCircle(WB_Coord center, double radius) {
        circles.add(new double[]{center.xd(), center.yd(), center.zd(), radius});
    }

    public int addCircle(WB_Coord center, double radius, int cnt, int segs) {
        WB_Point[] p = new WB_Point[segs];
        for(int k = 0; k < segs; ++ k) {
            double angle = Math.PI * 2 / segs * k;

            double x = Math.cos(angle) * radius + center.xd();
            double y = Math.sin(angle) * radius + center.yd();
            p[k] = new WB_Point(x, y);

            this.addVerts(p[k]);

        }

        for(int k = 0; k < segs-1; ++ k) {
            this.addFaces(cnt, cnt+k+1, cnt+k+2);
        }
        this.addFaces(cnt, cnt+segs, cnt+1);

        cnt += (segs+1);

        return cnt;
    }

    public int addRectangle(WB_Coord p0, WB_Coord p1, WB_Coord p2, WB_Coord p3, int cnt) {
        this.addVerts(p0);
        this.addVerts(p1);
        this.addVerts(p2);
        this.addVerts(p3);

        this.addFaces(cnt, cnt+1, cnt+3);
        this.addFaces(cnt+1, cnt+2, cnt+3);

        cnt += 4;

        return cnt;
    }

    public int addTriangle(WB_Coord p0, WB_Coord p1, WB_Coord p2, int cnt) {
        this.addVerts(p0);
        this.addVerts(p1);
        this.addVerts(p2);

        this.addFaces(cnt, cnt+1, cnt+2);
        cnt += 3;
        return cnt;
    }

}

