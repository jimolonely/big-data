package com.jimo;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;

public class Main {

    public static void main(String[] args) throws ParseException {

        // 从WKT里创建几何体
        WKTReader wktReader = new WKTReader();
        Geometry a = wktReader.read("POLYGON ((50 150, 150 150, 150 50, 50 50, 50 150))");
        // Geometry b = wktReader.read("POLYGON ((100 100, 200 100, 200 0, 100 0, 100 100))");

        // 用工厂方法创建
        GeometryFactory factory = new GeometryFactory();
        Polygon b = factory.createPolygon(new Coordinate[]{
                new Coordinate(100, 100),
                new Coordinate(200, 100),
                new Coordinate(200, 0),
                new Coordinate(100, 0),
                new Coordinate(100, 100),
        });

        // 关系运算
        Geometry res = a.symDifference(b);
        System.out.println(res.toText());

        // 输出不同的格式
        // WKTWriter wktWriter = new WKTWriter();
        GeoJsonWriter geoJsonWriter = new GeoJsonWriter();
        String json = geoJsonWriter.write(res);
        System.out.println(json);
    }
}
