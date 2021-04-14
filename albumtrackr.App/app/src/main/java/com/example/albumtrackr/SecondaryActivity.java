package com.example.albumtrackr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SecondaryActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    Integer id;
    Integer albumId;

    private AlbumAdapter adapter;
    private RecyclerView album;
    private AlbumList albumList;
    private ArrayList<AlbumList> lists = new ArrayList<AlbumList>();
    private String SERVICE_URI = "https://albumtrackrapi.azurewebsites.net/api/AlbumList/";
    private ProgressBar progressBar;

    private TextView textView_album_artist;
    private TextView textView_album_name;

    Button albumDelete;
    Button secondaryDelete;
    FloatingActionButton addAlbum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        Intent intent = getIntent();
        id = intent.getIntExtra("albumListID", 0);
        albumId = intent.getIntExtra("albumID", 0);


        textView_album_artist = (TextView) findViewById(R.id.textView_artist);
        textView_album_name = (TextView) findViewById(R.id.textView_name);




        album = findViewById(R.id.idAlbums);
        progressBar = findViewById(R.id.progressBar);
        secondaryDelete = (Button)findViewById(R.id.button_delete2);
        albumDelete = (Button)findViewById(R.id.button_deleteAlbum);
        addAlbum = (FloatingActionButton)findViewById(R.id.fab_add);

        getData();



        delete();
        deleteAlbum();;
        addAlbum();



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


                    Log.e("toString() error: ", response.toString());
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


    public void delete() {

        secondaryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(SecondaryActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, SERVICE_URI + id.toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                lists.removeIf(albumList -> albumList.getId().equals(id));
                                Toast.makeText(SecondaryActivity.this, "List Deleted!", Toast.LENGTH_LONG).show();

                                finish();
                                getData();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SecondaryActivity.this, "Unable to delete list!", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(stringRequest);
                adapter.notifyDataSetChanged();
                buildRecyclerView();

            }
        });


    }



    public void deleteAlbum() {
        secondaryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Tag", "Clicked.");
                RequestQueue queue = Volley.newRequestQueue(SecondaryActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, SERVICE_URI + id.toString() + "/" + album.toString(), new Response.Listener<String>()
                {
                            @Override
                            public void onResponse(String response) {

                                lists.removeIf(album -> album.getId().equals(id));
                                Toast.makeText(SecondaryActivity.this, "Album Deleted!", Toast.LENGTH_LONG).show();

                                finish();
                                getData();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SecondaryActivity.this, "Unable to delete album!", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(stringRequest);
                adapter.notifyDataSetChanged();
                buildRecyclerView();

            }
        });


    }


    public void addAlbum(){
        addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog(){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");

    }

    @Override
    public void applyTexts(String artist, String album) {   // taking in strings from dialogue class
        // Hashmap storing the variables as two strings
        HashMap<String, String> params = new HashMap<String, String>();
        // applying the variables
        params.put(artist, album);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, SERVICE_URI + id.toString() + "/album", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        }
    }
