package com.example.albumtrackr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SecondaryActivity extends AppCompatActivity {
    Integer id;

    private AlbumAdapter adapter;
    private RecyclerView album;
    private AlbumList albumList;
    private String SERVICE_URI = "https://albumtrackrapi.azurewebsites.net/api/AlbumList/";
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        Intent intent = getIntent();
        id = intent.getIntExtra("albumListID", 0);

        album = findViewById(R.id.idAlbums);
        progressBar = findViewById(R.id.progressBar);

        getData();
    }

    private void getData() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(SecondaryActivity.this);
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.



        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, SERVICE_URI + id.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                album.setVisibility(View.VISIBLE);
                // creating a new json object and
                // getting each object from our json array.
                try {
                    // we are getting each json object.
                    JSONArray starsActual = new JSONArray();
                    JSONArray jsonArray = new JSONArray();
                    ArrayList<Album> albumArrayList = new ArrayList<Album>();
                    ArrayList<Star> starArrayList = new ArrayList<Star>();


                    Log.e("eee", response.toString());
                    String id = response.getString("id");
                    String username = response.getString("username");
                    String created = response.getString("created");
                    String name = response.getString("name");
                    String description = response.getString("description");


                    if(response.optJSONArray("albums") != null) {
                        jsonArray = response.optJSONArray("albums");
                    }

                    if(response.optJSONArray("stars") != null) {
                        starsActual = response.optJSONArray("stars");
                    }

                    if(jsonArray != null) {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject obj = jsonArray.getJSONObject(j);
                            String listid = obj.getString("id");
                            String albumName = obj.getString("name");
                            String artistName = obj.getString("artist");
                            String thumbnail = obj.getString("thumbnail");
                            albumArrayList.add(new Album(Integer.parseInt(id), artistName, albumName, thumbnail));
                        }
                    }

                    if(starsActual != null) {
                        for (int j = 0; j < starsActual.length(); j++) {
                            JSONObject obj2 = starsActual.getJSONObject(j);
                            String starid = obj2.getString("id");
                            String usernameStar = obj2.getString("username");
                            String albumListId = obj2.getString("albumListId");
                            starArrayList.add(new Star(Integer.parseInt(starid), usernameStar, Integer.parseInt(albumListId)));
                        }
                    }

                    albumList = new AlbumList(Integer.parseInt(id), username, name, description, created, albumArrayList, starArrayList);



                    buildRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SecondaryActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new AlbumAdapter(albumList, SecondaryActivity.this);

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        album.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        album.setLayoutManager(manager);

        // globally
        TextView albumListName = (TextView)findViewById(R.id.textView_albumListName);

//in your OnCreate() method
        albumListName.setText(albumList.getName());

        albumListName.setVisibility(View.VISIBLE);

        // globally
        TextView albumListDesc = (TextView)findViewById(R.id.textView_albumListDesc);

//in your OnCreate() method
        albumListDesc.setText(albumList.getDescription());

        albumListDesc.setVisibility(View.VISIBLE);

        // setting adapter to
        // our recycler view.
        album.setAdapter(adapter);
    }


}