package com.walkwithme.activity;

import com.google.maps.model.LatLng;

/**
 * Created by User on 4/21/2015.
 */
public class GeocodeQuery {
    GeocodeQuery(String address){
        this.address=address;
    }
    GeocodeQuery(LatLng postion){
        this.position=position;
    }
    GeocodeQuery(double lat,double lng){
        this.position=new LatLng(lat, lng);
    }
    String address;
    LatLng position;
    boolean hasAddress(){
        if(address!=null)
            return !address.isEmpty();
        else return false;
    }
    boolean hasPosition(){
        if(position!=null)
            return true;
        else
            return false;
    }

}
