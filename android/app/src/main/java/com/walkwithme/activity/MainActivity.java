package com.walkwithme.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.model.GeocodingResult;

import java.util.*;


/**
 * This Activity Represent the primary entry point into to the application
 */
public class MainActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {

    private static final int GET_DESTINATION = 1;
    //Map opbject
    private GoogleMap mGoogleMap;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    //connection with GooglePlayServices
    GoogleApiClient mGoogleApiClient;
    Marker destinationMarker;
    Marker currentLocationMarker;
    //location object representing users last known location
    private Location mLastLocation;
    Button findWalkers;
    TextView destination;
    Spinner timeToLeaveChosser;
    ArrayAdapter <String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setup google api client but dont connect yet
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (mGoogleMap == null) {
            ((MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment1))//attempt ot get a map asychronously if success the map obj in  callback will be set to the field map
                    .getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap _googleMap) {
                            //the map has been initialized
                            mGoogleMap = _googleMap;

                            UiSettings uiSettings = mGoogleMap.getUiSettings();
                            uiSettings.setMapToolbarEnabled(false);//turn of toolbar on map
                            ////mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(7));
                            currentLocationMarker=mGoogleMap.addMarker(new MarkerOptions().snippet("You are here").alpha(0.5f).position(new LatLng(40.444238, -79.945556)));
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationMarker.getPosition(), 15));//center on default location TODO change to phone's currentlocation
                            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    Log.d(Constants.TAG, "clicked" + latLng.toString());
                                    if (destinationMarker != null) {
                                        destinationMarker.remove();
                                    }
                                    destinationMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
                                    new GeocodeAsyncTask(MainActivity.this).execute(new GeocodeQuery(latLng.latitude, latLng.longitude));
                                    LatLngBounds bounds=generateCenteredBounds(currentLocationMarker.getPosition(), destinationMarker.getPosition());
                                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 2));

                                }
                            });

                            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                @Override
                                public void onMarkerDragStart(Marker marker) {
                                    Log.d(Constants.TAG, "dragging started");
                                    marker.setPosition(marker.getPosition());
                                }

                                @Override
                                public void onMarkerDrag(Marker marker) {
                                    Log.d(Constants.TAG, "dragging marker");
                                }

                                @Override
                                public void onMarkerDragEnd(Marker marker) {
                                    Log.d(Constants.TAG, "dragging ended");
                                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                                }
                            });

                        }
                    });
        }
        destination = (TextView) findViewById(R.id.destination);
        destination.setFocusable(false);
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DestinationSearch.class);
                //startActivity(intent);
                startActivityForResult(intent, GET_DESTINATION);
                Log.d(Constants.TAG, "starting intent");
            }
        });

        findWalkers = (Button) findViewById(R.id.findWalkers);
        Log.d("wvm","b4 walker btn");
        findWalkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Constants.TAG, "find walkers");
                String timeToLeave= (String) timeToLeaveChosser.getSelectedItem();
                if(timeToLeave!=null && destination!=null){
                   LatLng latLng=destinationMarker.getPosition();
                    double lat=latLng.latitude;
                    double lng=latLng.longitude;
                    Intent intent = new Intent(MainActivity.this, GroupsResult.class);
                    intent.putExtra(Constants.LATITUDE,lat);
                    intent.putExtra(Constants.LONGITUDE,lng);
                    intent.putExtra(Constants.TIME_TO_LEAVE, timeToLeave);
                    Log.d("wvm", timeToLeave + "-" + "to" + lat + "--" + lng);
                    startActivity(intent);
                }



                Log.d("wvm","in on click of find");
               // startActivity(intent);
                //new SearchRequestAsyncTask().execute(new SearchRequest(timeToLeave,destinationMarker.getPosition()));


            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            updateMap(bundle);
        }
        timeToLeaveChosser =(Spinner)findViewById(R.id.spinner);
        timeToLeaveChosser.requestFocus();

      adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,generateSlots());
        NothingSelectedSpinnerAdapter wrapper=new NothingSelectedSpinnerAdapter(adapter,R.layout.nothing_selected,this);
        timeToLeaveChosser.setAdapter(wrapper);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_DESTINATION) {
            if (resultCode == RESULT_OK) {
                updateMap(data.getExtras());
                Log.d("wvm", "Address selection returned ok");
            } else {
                Log.d("wvm", "Address selection returned bad");
            }
        }
    }
    List <String> generateSlots(){
        Calendar currentTime=GregorianCalendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int min =(currentTime.get(Calendar.MINUTE));
        String fifteen="",thirty="",fortyFive="",nextHour="";
        List <String>slots=new ArrayList<String>();
        //if(min>15)
            slots.add(fifteen = hour + ":" + "15");

        //if(min>30)
            slots.add(thirty=hour+":30");
        //if(min>45)
            slots.add(fortyFive=hour+":45");
        slots.add(nextHour=hour+1+""+":00");
        slots.add(fifteen = hour +1+ ":" + "15");
        slots.add(thirty=hour+1+":30");
        slots.add(fortyFive=hour+1+":45");
        slots.add(nextHour=hour+2+""+":00");
        return slots;
               //Arrays.asList(new String []{fifteen,thirty,fortyFive,nextHour});

    }
    void updateMap(Bundle bundle) {
        Log.d("wwm", "Updating map");
        if (bundle != null) {
            double lat = bundle.getDouble(Constants.LATITUDE);
            double lng = bundle.getDouble(Constants.LONGITUDE);
            String address="";
            if(address!=null){
             address    = bundle.getString("address");//.replace(",USA", "");
            }

            destination.setText(address);
            Toast.makeText(this, address, Toast.LENGTH_LONG).show();
            LatLng selectedAdresslatLng = new LatLng(lat, lng);

//            LatLng origin = new LatLng(40.444238, -79.945556);
        if (mGoogleMap != null) {
            if(destinationMarker==null){
                destinationMarker=mGoogleMap.addMarker(new MarkerOptions().position(selectedAdresslatLng));
            }else{
                destinationMarker.setVisible(false);
                destinationMarker.setPosition(selectedAdresslatLng);
                destinationMarker.setVisible(true);
            }

            LatLngBounds bounds=generateCenteredBounds(selectedAdresslatLng,destinationMarker.getPosition());

            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
                //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

        }
    }

    /**
     * Generates a bound for two points at an appropriate zoom value
     * @param point1
     * @param point2
     * @return
     */
    LatLngBounds generateCenteredBounds(LatLng point1,LatLng point2){
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder().include(point1).include(point2);//ceate initial bounds
        final double HEADING_NORTH_EAST = 45;//
        final double HEADING_SOUTH_WEST = 215;
        LatLng center = boundsBuilder.build().getCenter();//gent center fo bounds then creat points about a km each aprt from center to decrease final zoom level
        LatLng northEast1 = SphericalUtil.computeOffset(point1, 709, HEADING_NORTH_EAST);//709m is about a kilometer?
        LatLng southWest1 = SphericalUtil.computeOffset(point1, 709,HEADING_SOUTH_WEST);//709m is about a kilometer?
        //mGoogleMap.addMarker(new MarkerOptions().position(northEast1));
        LatLng northEast2 = SphericalUtil.computeOffset(point1, 709, HEADING_NORTH_EAST);
        LatLng southWest2 = SphericalUtil.computeOffset(point2, 709, HEADING_SOUTH_WEST);
        //mGoogleMap.addMarker(new MarkerOptions().position(southWest));
        LatLngBounds bounds=boundsBuilder.include(northEast1).include(southWest1).include(northEast2).include(southWest2).build();
    return bounds;
    }
    void getAsyncUpdate(GeocodingResult[] _results) {
        //addresses=_addresses;
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", _results[0].geometry.location.lat);
        bundle.putDouble("lng", _results[0].geometry.location.lng);
        bundle.putString("address", _results[0].formattedAddress);
        updateMap(bundle);
        Log.d(Constants.TAG, "position results received from geo async");


    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Constants.TAG, "Started....");
        mGoogleApiClient.connect();//confirm presence of Play ApK this method relies on the callbacks
        Log.d(Constants.TAG, "Connecting....");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Constants.TAG, "Stopped....");
        mGoogleApiClient.disconnect();//confirm presence of Play ApK this method relies on the callbacks
        Log.d(Constants.TAG, "disconnecting....");
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("Connected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.d(Constants.TAG, "on cennection lastloc " + mLastLocation);
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    protected void onResume() {
        super.onResume();
//        adapter.clear();
//        adapter.addAll(generateSlots());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(Constants.TAG, "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(Constants.TAG, "Connection Failed");
        if (mResolvingError == true) {
            return;
        }
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                Log.d(Constants.TAG, "Error while starting intent..retry");
                mGoogleApiClient.connect();
            }

        } else {
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }

    }
    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity) getActivity()).onDialogDismissed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
