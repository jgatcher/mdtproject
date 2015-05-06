package com.walkwithme.clusterserver;

import java.util.ArrayList;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import java.util.*;

public class ZoneContainer {

    private Set<Zone> zones = new HashSet<>();

    public Set<Zone> getZones() {
        return zones;
    }

    public ZoneContainer() {
        super();
        markZones();
    }

    public Integer findZoneId(String latLong) {
        // TODO split the string
        String[] latlongs = latLong.split(",");
        String lat = latlongs[0];
        String lng = latlongs[1];
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lng);
        Integer zoneId = null;
        GeometryFactory gf = new GeometryFactory();
        for (Zone zone : zones) {
            Coordinate coord = new Coordinate(latitude, longitude);
            Point point = gf.createPoint(coord);
            if (point.within(zone.getPolygon())) {
                zoneId = zone.getZoneID();
            }
        }
        return zoneId;
    }

    /**
     * Method which creates polygons with coordinates as its boundary Currently,
     * the zones are marked by pre-defined coordinates for demo purpose
     */
    public Set<Zone> markZones() {

        GeometryFactory gf = new GeometryFactory();

        ArrayList<Coordinate> points_Morewood = new ArrayList<Coordinate>();
        // points.add(new Coordinate(40.4446264,-79.9430175));
        // zone1 - morewood to centre and back
        // 40.4435� N, 79.9416� W - Heinz college
        // 40.4446264,-79.9430175 - CMU + Morewood ave
        // 40.4541633 and Longitude: -79.9444427- center at morewood ave
        // 40.4599587 and Longitude: -79.9250232 - center at S.highland ave
        // 40.4519349 and Longitude: -79.9239878 - s highland ave at fifth
        // 40.4474396 and Longitude: -79.9424542- fifth at morewood ave

        /**
         * Coordinates marking Zone_Morewood having coordinates around Morewood
         * avenue - Center Avenue.
         */
        points_Morewood.add(new Coordinate(40.4435, -79.9416));
        points_Morewood.add(new Coordinate(40.4446264, -79.9430175));
        points_Morewood.add(new Coordinate(40.4541633, -79.9444427));
        points_Morewood.add(new Coordinate(40.4599587, -79.9250232));
        points_Morewood.add(new Coordinate(40.4519349, -79.9239878));
        points_Morewood.add(new Coordinate(40.4474396, -79.9424542));
        points_Morewood.add(new Coordinate(40.4435, -79.9416));

        Polygon polygon_Morewood = gf.createPolygon(
                new LinearRing(new CoordinateArraySequence(points_Morewood
                                .toArray(new Coordinate[points_Morewood.size()])), gf),
                null);

        /**
         * Zone 2 - lulu's - all India - 40.4444403, -79.9486749 - forbes at s
         * craig 40.4518900 ,-79.9520215 - cetre avenue at N Craig street
         * 40.452503, -79.949648 - n neviile st 40.444527, -79.946743 - s
         * neville st near college
         */
        ArrayList<Coordinate> points_Neville = new ArrayList<Coordinate>();
        points_Neville.add(new Coordinate(40.4444403, -79.9486749));
        points_Neville.add(new Coordinate(40.4518900, -79.9520215));
        points_Neville.add(new Coordinate(40.452503, -79.949648));
        points_Neville.add(new Coordinate(40.444527, -79.946743));
        points_Neville.add(new Coordinate(40.4444403, -79.9486749));

        Polygon polygon_Neville = gf.createPolygon(
                new LinearRing(new CoordinateArraySequence(points_Neville
                                .toArray(new Coordinate[points_Neville.size()])), gf),
                null);

        /**
         * Zone 3 - forbes at wightman 40.437853, -79.927507 forbes at murray -
         * 40.437902, -79.922872 murray at forward 40.429770, -79.923384
         * pocusset st 40.429309, -79.927362 forbes at wightman 40.437853,
         * -79.927507
         */
        ArrayList<Coordinate> points_Wightman = new ArrayList<Coordinate>();
        points_Wightman.add(new Coordinate(40.437853, -79.927507));
        points_Wightman.add(new Coordinate(40.437902, -79.922872));
        points_Wightman.add(new Coordinate(40.429770, -79.923384));
        points_Wightman.add(new Coordinate(40.429309, -79.927362));
        points_Wightman.add(new Coordinate(40.437853, -79.927507));
        points_Wightman.add(new Coordinate(40.437853, -79.927507));

        Polygon polygon_Wightman = gf.createPolygon(
                new LinearRing(new CoordinateArraySequence(points_Wightman
                                .toArray(new Coordinate[points_Wightman.size()])), gf),
                null);

        ArrayList<Coordinate> points_forbes = new ArrayList<Coordinate>();
        points_forbes.add(new Coordinate(40.4435, -79.9416));
        points_forbes.add(new Coordinate(40.4446264, -79.9430175));
        points_forbes.add(new Coordinate(40.4541633, -79.9444427));
        points_forbes.add(new Coordinate(40.4599587, -79.9250232));
        points_forbes.add(new Coordinate(40.4519349, -79.9239878));
        points_forbes.add(new Coordinate(40.4474396, -79.9424542));
        points_forbes.add(new Coordinate(40.4435, -79.9416));

        Polygon polygon_forbes = gf.createPolygon(
                new LinearRing(new CoordinateArraySequence(points_forbes
                                .toArray(new Coordinate[points_forbes.size()])), gf),
                null);

        Zone zone_Morewood = new Zone(1, polygon_Morewood);
        zone_Morewood.setPolygon(polygon_Morewood);
        zone_Morewood.setZoneID(1);

        Zone zone_Neville = new Zone(2, polygon_Neville);
        zone_Neville.setPolygon(polygon_Neville);
        zone_Neville.setZoneID(2);

        Zone zone_Wightman = new Zone(3, polygon_Wightman);
        zone_Wightman.setPolygon(polygon_Wightman);
        zone_Wightman.setZoneID(3);

        Zone zone_forbes = new Zone(4, polygon_forbes);
        zone_forbes.setPolygon(polygon_forbes);
        zone_forbes.setZoneID(4);

        zones.add(zone_Morewood);
        zones.add(zone_Neville);
        zones.add(zone_Wightman);
        zones.add(zone_forbes);

        return zones;

    }
}
