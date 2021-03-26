// add Internet permission

package com.example.albumtrackr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.os.StrictMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.*;
import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


// in build.gradle for Module:app
// implementation 'com.android.volley:volley:1.1.1'
// implementation 'com.google.code.gson:gson:2.8.5'

public class MainActivity extends AppCompatActivity {

    // uri of RESTful service on Azure, note: https, cleartext support disabled by default
    private String SERVICE_URI = "https://192.168.0.234:5001/api/AlbumList/id?id=5";          // or https
    private String TAG = "";
    //https://www.geeksforgeeks.org/how-to-extract-data-from-json-array-in-android-using-volley-library/

    private RecyclerView album;
    private AlbumAdapter adapter;
    private AlbumList albumArrayList;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        setContentView(R.layout.activity_main);
        album = findViewById(R.id.idAlbums);
        progressBar = findViewById(R.id.idPB);
        AlbumList albumArrayList = new AlbumList();

       try {
        getData();
       } catch (Exception e){
           Log.e("aaaaaaa fuck my life", e.getMessage());
       }
        buildRecyclerView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//
//        viewofAlbums = (ListView) findViewById(R.id.AlbumViews);
//        viewofAlbums.setAdapter(itemsAdapter);
//
//        // floating action button, call the service
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                callService(view);
//            }
//        });




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

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, SERVICE_URI, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("eee", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);



        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, SERVICE_URI, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                album.setVisibility(View.VISIBLE);
                // creating a new json object and
                // getting each object from our json array.
                try {
                    // we are getting each json object.
                    String username = response.getString("username");
                    String name = response.getString("name");
                    String description = response.getString("description");
                    albumArrayList.setUsername(username);
                    albumArrayList.setDescription(description);
                    albumArrayList.setName(name);


                    JSONArray jsonArray = response.getJSONArray("albums");


                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject obj = jsonArray.getJSONObject(j);
                        String id = obj.getString("id");
                        String albumName = obj.getString("name");
                        String artistName = obj.getString("artist");
                        String thumbnail = obj.getString("thumbnail");
                        albumArrayList.getAlbums().add(new Album(Integer.parseInt(id), artistName, albumName, thumbnail));
                    }


                    buildRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new AlbumAdapter(albumArrayList, MainActivity.this);

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        album.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        album.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        album.setAdapter(adapter);
    }







    // call RESTful service using volley and display results
    public void callService(View v)
    {
        // get TextView for displaying result
        final TextView outputTextView = (TextView) findViewById(R.id.outputTextView);

        try
        {
            // make a string request (JSON request an alternative)
            RequestQueue queue = Volley.newRequestQueue(this);
            Log.d(TAG, "Making request");
            try
            {
                StringRequest strObjRequest = new StringRequest(Request.Method.GET, SERVICE_URI,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                // parse resulting string containing JSON to Greeting object
                                Album album = new Gson().fromJson(response, Album.class);
                                outputTextView.setText(album.toString());
                                Log.d(TAG, "Displaying data" + album.toString());
                                //how on earth do we display an image here? lol
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                outputTextView.setText(error.toString());
                                Log.d(TAG, "Error" + error.toString());
                            }
                        });
                queue.add(strObjRequest);           // can have multiple in a queue, and can cancel
            }
            catch (Exception e1)
            {
                Log.d(TAG, e1.toString());
                outputTextView.setText(e1.toString());
            }
        }
        catch (Exception e2)
        {
            Log.d(TAG, e2.toString());
            outputTextView.setText(e2.toString());
        }
    }
}
