package com.pbrunos.pbrunos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Queue;

public class TrafficCamActivity extends AppCompatActivity {

    private static final String TAG = "TrafficCamActivity";
    String dataurl = "http://brisksoft.us/ad340/traffic_cameras_merged.json";
    ArrayList<TrafficCamera> cameraData = new ArrayList<>();
    private TrafficCamRecylerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_cams);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Traffic Cams");

        RecyclerView rv = findViewById(R.id.traffic_cam_recyler_view);
        adapter = new TrafficCamRecylerViewAdapter(cameraData, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

//        initImBit();

        loadCameraData(dataurl);
        Log.d(TAG, "onCreate: ");
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
                            adapter.notifyDataSetChanged();
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


//    private void initImBit() {
//        mImages.add("https://www.seattle.gov/trafficcams/images/Airport_S_Norfolk_NS.jpg");
//        mLable.add( "Airport Way S & S Norfolk St");
//        mOwner.add("SDOT");
//
//        mImages.add("https://www.seattle.gov/trafficcams/images/Rainier_S_Henderson_NS.jpg");
//        mLable.add( "Rainier Ave S & S Henderson St");
//        mOwner.add("SDOT");
//
//        mImages.add("https://www.seattle.gov/trafficcams/images/14_S_Cloverdale_NS.jpg");
//        mLable.add(  "14th Ave S & S Cloverdale St");
//        mOwner.add("SDOT");
//
//        mImages.add("https://www.seattle.gov/trafficcams/images/EMarg_S_16_NS.jpg");
//        mLable.add( "E Marginal Way S & 16th Ave S");
//        mOwner.add("SDOT");
//
//        mImages.add("http://images.wsdot.wa.gov/nw/099vc02671.jpg");
//        mLable.add( "SR-99 @ West Marginal Way");
//        mOwner.add("WDOT");
//
//
//        initRecyclerView();
//
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_about:
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void initRecyclerView() {
//        Log.d(TAG, "initRecyclerView: ");
//        RecyclerView rv = findViewById(R.id.traffic_cam_recyler_view);
//        TrafficCamRecylerViewAdapter adapter = new TrafficCamRecylerViewAdapter(mImages, mLable, mOwner, this);
//        rv.setAdapter(adapter);
//        rv.setLayoutManager(new LinearLayoutManager(this));
//    }
}
