package com.walkwithme.activity;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URLEncoder;

/**
 * Created by User on 4/28/2015.
 */
public class SearchRequest {
    String timeToLeave;
    String destination;
    String user;
    String method;
    int groupId;


    public SearchRequest(String method,String destination,String timeToLeave,String user,int groupId){
       this.groupId=groupId;
        this.method=method;
        this.destination=destination;
        this.timeToLeave=timeToLeave;
        this.user=user;
    }
    public SearchRequest(String method,String destination, String timeToLeave, String user) {
        this.method=method;
        this.destination = destination;
        this.timeToLeave = timeToLeave;
        this.user=user;

    }
    public String toUrl(){
        StringBuilder builder=new StringBuilder();
        builder.append("?method="+method);
        builder.append("&latLng="+destination);
        if(groupId!=0)
            builder.append("&groupId="+groupId);
        if(user!=null)
            builder.append("&user="+user);
        if(timeToLeave!=null){
            builder.append("&time="+timeToLeave);
        }

        return builder.toString();
    }
}

//    /*Constructor will accept null but save as empty string
//          * we may have to figure out an alt method to initing empty strings */
//    public SearchRequest(String timeToLeave, double lat, double long){
//        this.timeToLeave = timeToLeave==null?"":timeToLeave.trim();
//        this.destination=destina==null?"":product.trim();
//        this.location=location==null?"":location.trim();
//    }
//    public SearchRequest(Parcel parcel){
//        businessName=parcel.readString();
//        product=parcel.readString();
//        location=parcel.readString();
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(businessName);
//        parcel.writeString(product);
//        parcel.writeString(location);
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//public static final Parcelable.Creator<SearchRequest> CREATOR = new Parcelable.Creator<SearchRequest>() {
//    public SearchRequest createFromParcel(Parcel in) {
//        return new SearchRequest(in);
//    }
//
//    public SearchRequest[] newArray(int size) {
//        return new SearchRequest[size];
//    }
//};
//}

