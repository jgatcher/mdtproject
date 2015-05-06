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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.SphericalUtil;
import com.google.maps.model.GeocodingResult;

import java.util.*;

/**
 * This Activity Shows the various memebers of a selected group on a map
 */

public class VisualizeGroup extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
        Button joinGroup;
        TextView destination;
        Spinner timeToLeaveChosser;

       List<LatLng>toLatLngs(List<String> latLngStrings){
           List<LatLng> result=new ArrayList<LatLng>();
           try {
               for (String s : latLngStrings) {
                   String parts[] = s.split(",");
                   result.add(new LatLng(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])));
               }
           }catch (NumberFormatException nfe){
               Log.d("wvm","nfe");
           }
           return result;
       }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.visualize_group);
            Bundle groupInfoBundle=getIntent().getExtras();
            final String groupId=groupInfoBundle.getString("groupId");
            final List<String> latLngStrings=groupInfoBundle.getStringArrayList("latlngs");
            final String timeToLeave=groupInfoBundle.getString("time");
            final List <LatLng> latLngs= toLatLngs(latLngStrings);
            //setup google api client but dont connect yet
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            if (mGoogleMap == null) {
                ((MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment2))//attempt to get a map asychronously if success the map obj in  callback will be set to the field map
                        .getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap _googleMap) {
                                //the map has been initialized
                                mGoogleMap = _googleMap;

                                UiSettings uiSettings = mGoogleMap.getUiSettings();
                                uiSettings.setMapToolbarEnabled(false);//turn of toolbar on map
                                ////mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(7));

                                float hue[]={BitmapDescriptorFactory.HUE_AZURE,BitmapDescriptorFactory.HUE_VIOLET,
                                        BitmapDescriptorFactory.HUE_CYAN,BitmapDescriptorFactory.HUE_ORANGE,BitmapDescriptorFactory.HUE_MAGENTA};
                                for(int i=0;i<latLngs.size();i++){
                                    Log.d("wvm",latLngs.toString());
                                  // mGoogleMap.addMarker(

                                          //  new MarkerOptions().position(latLngs.get(i)).
                                            //        icon(BitmapDescriptorFactory.defaultMarker(hue[i % hue.length])));
                                    Log.d("wvm","addin marker"+i);
                                }

                                LatLng [] temp=new LatLng[latLngs.size()];
                                latLngs.toArray(temp);
                                LatLngBounds.Builder builder=new LatLngBounds.Builder() ;

                                for(LatLng l:latLngs){
                                    builder=LatLngBounds.builder().include(l);
                                }
//                                LatLngBounds bounds=
                                      // generateCenteredBounds(temp);

                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 15));


                            }
                        });
            }

            joinGroup = (Button) findViewById(R.id.joinGroup);
            joinGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(Constants.TAG, "join Group");

                    new SearchRequestAsyncTask(VisualizeGroup.this).execute(
                            new SearchRequest("request2",latLngStrings.get(latLngStrings.size()-1),timeToLeave,"user",Integer.parseInt(groupId)));
                    Log.d("wvm", "in on click of join");

                }
            });

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        void updateMap(Bundle bundle) {
            Log.d("wwm", "Updating map");
        }

        /**
         * Generates a bound for an array of points at an appropriate zoom value
         *  @param points
         * @return
         */
        LatLngBounds generateCenteredBounds(LatLng ...points){
            LatLngBounds.Builder boundsBuilder=new LatLngBounds.Builder();
            for(LatLng point:points){
                boundsBuilder.include(point);//ceate initial bounds
            }
            final double HEADING_NORTH_EAST = 45;//
            final double HEADING_SOUTH_WEST = 215;
            LatLng center = boundsBuilder.build().getCenter();//gent center fo bounds then creat points about a km each aprt from center to decrease final zoom level
            LatLng northEast1 = SphericalUtil.computeOffset(center, 709, HEADING_NORTH_EAST);//709m is about a kilometer?
            LatLng southWest1 = SphericalUtil.computeOffset(center, 709,HEADING_SOUTH_WEST);//709m is about a kilometer?
            LatLngBounds bounds=boundsBuilder.include(northEast1).include(southWest1).build();
            return bounds;
        }
        void getAsyncUpdate(SearchResponse response) {
            if (response.message.equals("success")){
                Toast.makeText(this, "Great,you're set to go", Toast.LENGTH_LONG).show();
                finish();
            }else{
              Toast.makeText(this,"Oops ,something went wrong",Toast.LENGTH_LONG).show();
            }
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

