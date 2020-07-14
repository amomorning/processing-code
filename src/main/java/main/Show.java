package main;

import Guo_Cam.CameraController;
import org.kabeja.dxf.*;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import processing.core.PApplet;
import readDXF.DXFImport;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.hemesh.HEC_Cylinder;
import wblut.hemesh.HES_CatmullClark;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render;
import wblut.processing.WB_Render3D;

import java.util.ArrayList;
import java.util.List;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Show extends PApplet {
    private WB_Render3D render;
    private CameraController cam;
    private List<WB_Polygon> plys;

    public void settings() {
        size(1000, 1000, P3D);
    }

    public void setup() {
        cam = new CameraController(this, 1000);
        cam.top();

        render = new WB_Render(this);
        plys = readDXF("./model/aaa-eth.dxf", "brokenLine");
    }


    public void draw() {
        background(255);
        fill(0);

        render.drawPolygonEdges(plys);

    }

    private List<WB_Polygon> readDXF(String filename, String layerName) {
        Parser parser = ParserBuilder.createDefaultParser();
        DXFDocument doc = null;
        try {
            parser.parse(filename, DXFParser.DEFAULT_ENCODING);
            doc = parser.getDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DXFPolyline(doc.getDXFLayer(layerName));
    }

    private List<WB_Polygon> DXFPolyline(DXFLayer layer) {
        List<DXFPolyline> pls = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);
        List<WB_Polygon> polygons = new ArrayList<WB_Polygon>();
        for (DXFPolyline pl : pls) {
            WB_Point[] pts = new WB_Point[pl.getVertexCount()];
            for (int j = 0; j < pts.length; j++) {
                DXFVertex v = pl.getVertex(j);
                pts[j] = new WB_Point(v.getX(), v.getY());
            }
            polygons.add(new WB_Polygon(pts));
        }
        return polygons;
    }
}
