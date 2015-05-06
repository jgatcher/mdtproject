package com.walkwithme.clusterserver;

public class Request1 {

	private String methodName;

	private String latLong;

	private String time;

	public String getMethod() {
		return methodName;
	}

	public void setMethod(String method) {
		this.methodName = method;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
