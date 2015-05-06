package com.walkwithme.mdt;
import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import java.util.*;

public class GeoTest {

  public static void main(final String[] args) {
      

    final GeometryFactory gf = new GeometryFactory();
    final GeometryFactory gf1 = new GeometryFactory();

    final ArrayList<Coordinate> points = new ArrayList<Coordinate>();
   // points.add(new Coordinate(40.4446264,-79.9430175));
    //zone1 - morewood to centre and back
       //40.4435 N, 79.9416 W - Heinz college
  	//40.4446264,-79.9430175 - CMU + Morewood ave
  	//40.4541633 and Longitude: -79.9444427- center at morewood ave
  	//40.4599587 and Longitude: -79.9250232 - center at S.highland ave
  	//40.4519349 and Longitude: -79.9239878 - s highland ave at fifth
  	//40.4474396 and Longitude: -79.9424542- fifth at morewood ave
   
    //zone 2 - lulu's - all india - 
    // 40.4444403 and Longitude: -79.9486749 - forbes at s craig
    //40.4518900 ,-79.9520215 - cetre avenue at N Craig street
    //40.452503, -79.949648 - n neviile st
    //40.444527, -79.946743 - s neville st near college
    
    //zone 3
    // forbes at wightman 40.437853, -79.927507
    //forbes at murray - 40.437902, -79.922872
    // murray at forward 40.429770, -79.923384
    //pocusset st 40.429309, -79.927362
    //forbes at wightman 40.437853, -79.927507
    
    points.add(new Coordinate(40.4435, -79.9416));
    points.add(new Coordinate(40.4446264,-79.9430175));
    points.add(new Coordinate(40.4541633, -79.9444427));
    points.add(new Coordinate(40.4599587, -79.9250232));
    points.add(new Coordinate(40.4519349, -79.9239878));
    points.add(new Coordinate(40.4474396, -79.9424542));
    points.add(new Coordinate(40.4435, -79.9416));
    
    final Polygon polygon = gf.createPolygon(new LinearRing(new CoordinateArraySequence(points
        .toArray(new Coordinate[points.size()])), gf), null);

    final Coordinate coord = new Coordinate(40.4531330, -79.9421103);
    final Point point = gf1.createPoint(coord);

    System.out.println(point.within(polygon));

  }

}
