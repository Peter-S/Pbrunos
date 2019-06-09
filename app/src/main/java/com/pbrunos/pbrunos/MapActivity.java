package com.pbrunos.pbrunos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

        }

        loadCameraData(dataurl);
    }

    String dataurl = "http://brisksoft.us/ad340/traffic_cameras_merged.json";
    ArrayList<TrafficCamera> cameraData = new ArrayList<>();

    private FusedLocationProviderClient mFused;

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9;
    private final String FINE_LOC = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String COURSE_LOC = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted = false;
    private static final int LOC_PERM_REQ_CODE = 1234;


    private GoogleMap mMap;
    private Location mLastLocation;
    private static final String TAG = "MapActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Maps");

        mFused = LocationServices.getFusedLocationProviderClient(this);

        getLocationPerm();
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPerm(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOC) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOC) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOC_PERM_REQ_CODE);
            }
        }else {
                ActivityCompat.requestPermissions(this, permissions, LOC_PERM_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch(requestCode){
            case LOC_PERM_REQ_CODE: {
                if(grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;

                }
            }
        }

    }


    public void loadCameraData(String dataURL) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, dataURL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 1; i < response.length(); i++) {

                                JSONObject camera = response.getJSONObject(i);

                                double[] coords = {camera.getDouble("ypos"), camera.getDouble("xpos")};
                                TrafficCamera c = new TrafficCamera(
                                        camera.getString("cameralabel"),
                                        camera.getJSONObject("imageurl").getString("url"),
                                        camera.getString("ownershipcd"),

                                        coords
                                );
                                Log.d(TAG, "Camera Lable :  " + camera.getDouble("ypos"));
                                cameraData.add(c);

                            }
                            showMarkers();
                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: exception " + e.getMessage());
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: No ");
                    }
                });
        queue.add(jsonArrayRequest);
//        initRecyclerView();
    }

    public void showMarkers(){
        ArrayList<TrafficCamera> tc = cameraData;
        for (int i = 0; i < tc.size(); i++) {

            double lat = tc.get(i).corrdinats[1];
            double lng  = tc.get(i).corrdinats[0];

            mMap.addMarker(new MarkerOptions().position(new LatLng(lng, lat))
                    .title(tc.get(i).label));

        }

    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: ");
        mFused = LocationServices.getFusedLocationProviderClient(this);
        try {
            if(mLocationPermissionGranted) {

                Task location = mFused.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            Log.d(TAG, "onComplete: Found location");
                            Location currentLocation = (Location) task.getResult();

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 14));
                            mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("You Are Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                        } else {
                            Log.d(TAG, "onComplete: current is null");
                            Toast.makeText(MapActivity.this, "Unable to find current location", Toast.LENGTH_LONG).show();
                            LatLng home = new LatLng(47.614765, -122.345626);
                            mMap.addMarker(new MarkerOptions().position(home).title("Marker at my Pad").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            mMap.setMinZoomPreference(13);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
                        }
                    }
                });


            }
        }catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: " + latLng.latitude + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }




}
