package com.walkwithme.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.maps.model.GeocodingResult;

import java.util.ArrayList;
import java.util.List;


public class GroupsResult extends Activity {
    private static final int JOIN_GROUP =123 ;
    ListView listView;
    GroupResultsListAdapter listAdapter;
    SearchResponse searchResponse;
    List <Group> groups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_result);
        Intent intent=getIntent();
        String lat=String.valueOf(intent.getExtras().getDouble(Constants.LATITUDE));
        String lng=String.valueOf(intent.getExtras().getDouble(Constants.LONGITUDE));
        String timeToLeave=String.valueOf(intent.getExtras().getString(Constants.TIME_TO_LEAVE));
        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        listAdapter=new GroupResultsListAdapter(this,new ArrayList<Group>());
        new SearchRequestAsyncTask(this).execute(new SearchRequest("request1",lat+","+lng,timeToLeave,"user"));
        listView=(ListView)findViewById(R.id.groups_list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(GroupsResult.this,VisualizeGroup.class);
                Group result = (Group)listView.getItemAtPosition(position);
                intent.putExtra("groupId", result.groupId);
                intent.putExtra("timeToLeave",result.time);
                intent.putStringArrayListExtra("latlngs", (ArrayList) result.latLongs);

                Log.d("wwm", "moving to visual activity");
                //setResult(RESULT_OK, intent);
                startActivityForResult(intent,JOIN_GROUP);
                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("wvm","return to activity from ..");
        if (requestCode == JOIN_GROUP) {
            if (resultCode == RESULT_OK) {
               finish();
                Log.d("wvm", "join selection returned ok");
            } else {
                Log.d("wvm", "join selection returned bad");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_groups_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void getAsyncUpdate(SearchResponse _searchResponse){

        searchResponse= _searchResponse;
        if(searchResponse.message.equals("no_zone")){
            Toast.makeText(this,"Sorry,:-( cant help ",Toast.LENGTH_SHORT);
        }
        groups =searchResponse.grouplist;
        for (Group r: groups){
            System.out.println(r.groupId);
        }
        listAdapter.listItems= groups;
        listAdapter.notifyDataSetChanged();
        listView.invalidateViews();
        Log.d("wvm", "results received");


    }
}
