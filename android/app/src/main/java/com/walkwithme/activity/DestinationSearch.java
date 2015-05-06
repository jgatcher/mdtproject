package com.walkwithme.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.maps.model.GeocodingResult;

import java.util.ArrayList;

/**
 * Activity to confirm destination address entered
 */
public class DestinationSearch extends Activity{

    ListView listView;
    TextView destination;
    Button searchBtn;
   // ArrayList<Address> addresses =new ArrayList<Address>();
    GeocodingResult[] results={};
    //ListAdapter listAdapter;
    GeocodingResultsListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_search);
        destination=(TextView)findViewById(R.id.destinationTextView);
        searchBtn=(Button)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("wwm", "do Search");
                new GeocodeAsyncTask(DestinationSearch.this).execute(new GeocodeQuery(destination.getText().toString()));
            }
        });


        listAdapter=new GeocodingResultsListAdapter(this, results);/*vnmc*/
        listView=(ListView)findViewById(R.id.addressList);
        listView.setAdapter(listAdapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent =new Intent(DestinationSearch.this,MainActivity.class);
               GeocodingResult result=(GeocodingResult)listView.getItemAtPosition(position);
               intent.putExtra("address",result.formattedAddress);
               intent.putExtra(Constants.LATITUDE,result.geometry.location.lat);
               intent.putExtra(Constants.LONGITUDE,result.geometry.location.lng);
               Log.d("wwm", "Returning to main form destination activity");
               setResult(RESULT_OK, intent);
               finish();

           }
       });

    }


//    void getAsyncUpdate(ArrayList<Address> _addresses){
void getAsyncUpdate(GeocodingResult [] _results){
        //addresses=_addresses;
        results=_results;
        for (GeocodingResult r:results){
            System.out.println(r.formattedAddress);
        }
        listAdapter.listItems=results;
        listAdapter.notifyDataSetChanged();
        //listView.invalidateViews();
        Log.d("wvm", "results received");


    }
}
