package com.walkwithme.activity;
/**
 * Created by User on 4/28/2015.
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

/**
 * Does Geocoding off the UI thread
 */
//public class GeocodeAsyncTask extends AsyncTask<String,Integer,List<Address>>{
public class SearchRequestAsyncTask extends AsyncTask <SearchRequest,Integer,SearchResponse> {
    private SearchResponse result;
    private Activity activity;


    String baseURL="http://10.0.2.2:8080/walkwithme/clusteringserver";
    String baseURL2="http://128.237.168.36:8080/walkwithme/clusteringserver";
    //AsyncTask needs a reference to an activity so we pass it thorough the constructor TODO remember to deal with possible memory Leaks etc
    SearchRequestAsyncTask(Activity activity){
        this.activity=activity;
    }
    @Override
    //protected List<Address> doInBackground(String... params) {
    protected SearchResponse  doInBackground(SearchRequest... params) {
        SearchResponse response=new SearchResponse();
        Gson gson;
        Log.d("wvm", "Search Group async");
        String searchUrl=baseURL+params[0].toUrl();
        //String searchUrl=baseURL + "?method=request1&latLng=" + params[0].destination + "&user=" + params[0].user + "&time=" + URLEncoder.encode(params[0].timeToLeave);
        Log.d("wvm", "url is" + searchUrl);
        String jsonString = Util.getURL(searchUrl);
        if(jsonString==null|| jsonString.isEmpty()){
            searchUrl=baseURL+params[0].toUrl();
            jsonString=Util.getURL(searchUrl);
        }
        if(jsonString!=null && !jsonString.isEmpty()){
            if(!jsonString.equals("success")&& !jsonString.equals("no_zone")){
                gson= new GsonBuilder().create();
                response = gson.fromJson(jsonString, SearchResponse.class);
            }else{
                response.message = jsonString;
            }
        }

        return response;
        //return addresses;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
    @Override
    //protected void onPostExecute(List<Address> addresses) {
    protected void onPostExecute(SearchResponse response) {
        super.onPostExecute(response);
        Log.d("wvm", "Results available");
        if(activity instanceof  GroupsResult)
            ((GroupsResult)activity).getAsyncUpdate(response);
        else
            ((VisualizeGroup)activity).getAsyncUpdate(response);
    }



}
