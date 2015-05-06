package com.walkwithme.clusterserver;

import com.vividsolutions.jts.geom.Polygon;

public class Zone {

	private int zoneID;

	private Polygon polygon;

	public int getZoneID() {
		return zoneID;
	}

	public void setZoneID(int zoneID) {
		this.zoneID = zoneID;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

	Zone(int id, Polygon poly) {
		zoneID = id;
		polygon = poly;
	}

	/**
	 * Default constructor
	 */

	Zone() {

	}

}
