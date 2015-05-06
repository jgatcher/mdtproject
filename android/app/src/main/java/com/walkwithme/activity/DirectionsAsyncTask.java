//package com.walkwithme.activity;
//
//import android.app.Activity;
//import android.location.Geocoder;
//import android.os.AsyncTask;
//import android.util.Log;
//import com.google.maps.DirectionsApi;
//import com.google.maps.DirectionsApiRequest;
//import com.google.maps.GeoApiContext;
//import com.google.maps.model.DirectionsRoute;
//
///**
// * Does Directions off the UI thread
// */
////public class GeocodeAsyncTask extends AsyncTask<String,Integer,List<Address>>{
//    public class DirectionsAsyncTask extends AsyncTask<GeocodeQuery,Integer,Routes[]>{
//    private static final int MAX_RESULTS = 5;
//
//    private aGeoApiContext geoContext;
//    private GeocodingResult[] results;
//    private Activity activity;
//
//    //AsyncTask needs a reference to an activity so we pass it thorough the constructor TODO remember to deal with possible memory Leaks etc
//    DirectionsAsyncTask(Activity activity){
//        this.activity=activity;
//        // Replace the API key below with a valid API key.
//        geoContext = new GeoApiContext().setApiKey("AIzaSyBkfXVfQdq58v_zx4rTg8OLYeQnycPmFU8");//switch to using metadata
//
//    }
//
//
//    @Override
//    protected GeocodingResult [] doInBackground(GeocodeQuery... params) {
//        Log.d("wvm", "Geocode async");
//        Geocoder geocoder=new Geocoder(activity);
//        //List<Address> addresses=new ArrayList<Address>();
//        GeocodingResult [] results={};
//        try {
//            //addresses= geocoder.getFromLocationName(params[0], MAX_RESULTS);
//            System.out.println(params[0]);
//            if(params[0].hasAddress())
//
//                results =  GeocodingApi.newRequest(geoContext).address(params[0].address)
//                        .components(GeocodingApi.ComponentFilter.administrativeArea("PA"))
//                        .components(GeocodingApi.ComponentFilter.locality("pittsburgh")).await();
//            else {
//                results=GeocodingApi.newRequest(geoContext).latlng(params[0].position).await();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return results;
//        //return addresses;
//    }
//     @Override
//    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
//    }
//    @Override
//    //protected void onPostExecute(List<Address> addresses) {
//        protected void onPostExecute(GeocodingResult[] results) {
//        super.onPostExecute(results);
//        Log.d("wvm", "Results available");
//        if(activity instanceof  DestinationSearch)
//            ((DestinationSearch)activity).getAsyncUpdate(results);
//        else
//            ((MainActivity)activity).getAsyncUpdate(results);
//    }
//
//
//
//}
