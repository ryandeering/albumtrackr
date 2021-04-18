package com.example.albumtrackr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

public class SecondaryActivity extends AppCompatActivity implements AddAlbumDialog.DialogListenerAddAlbum, AddDescDialog.DialogListenerDescription {
    Integer id;
    Integer albumId;
    Integer stars;

    private AlbumAdapter adapter;
    private RecyclerView album;
    private AlbumList albumList;
    private final ArrayList<AlbumList> lists = new ArrayList<AlbumList>();
    private final String SERVICE_URI = "https://albumtrackrapi.azurewebsites.net/api/AlbumList/";
    private ProgressBar progressBar;
    private String UserId = "";

    private TextView albumListName;
    private TextView albumListDesc;
    private TextView textView_stars;


    Button albumDelete;
    Button secondaryDelete;
    Button editDescription;
    Button star;
    Button starHollow;
    FloatingActionButton addAlbum;


    ConstraintLayout layout1;
    LinearLayout layout2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        Intent intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        id = intent.getIntExtra("albumListID", 0);
        albumId = intent.getIntExtra("albumID", 0);
        stars = intent.getIntExtra("stars", 0);

        layout1 = findViewById(R.id.secondary_layout);
        layout2 = findViewById(R.id.linearLayout3);

        album = findViewById(R.id.idAlbums);
        progressBar = findViewById(R.id.progressBar);

        secondaryDelete = (Button) findViewById(R.id.button_delete2);
        albumDelete = (Button) findViewById(R.id.button_deleteAlbum);
        addAlbum = (FloatingActionButton) findViewById(R.id.fab_add);
        editDescription = (Button) findViewById(R.id.button_edit);
        star = (Button) findViewById(R.id.button_star);
        starHollow = (Button) findViewById(R.id.button_unstarred);


        getData();
        delete();
        addAlbum();
        editDescription();
        starHollow();
        star();


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(SecondaryActivity.this);
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.

        UserId = getUserId();
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, SERVICE_URI + id.toString(), null, response -> {
            progressBar.setVisibility(View.GONE);
            album.setVisibility(View.VISIBLE);
            // creating a new json object and
            // getting each object from our json array.
            try {
                // we are getting each json object.
                JSONArray jsonArray = new JSONArray();
                ArrayList<Album> albumArrayList = new ArrayList<Album>();

                Log.e("toString() error: ", response.toString());
                String listid = response.getString("id");
                String username = response.getString("username");
                String created = response.getString("created");
                String name = response.getString("name");
                String description = response.getString("description");
                String stars = response.getString("stars");


                if (response.optJSONArray("albums") != null) {
                    jsonArray = response.optJSONArray("albums");
                }


                if (jsonArray != null) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject obj = jsonArray.getJSONObject(j);
                        String id = obj.getString("id");
                        String albumName = obj.getString("name");
                        String artistName = obj.getString("artist");
                        String thumbnail = obj.getString("thumbnail");
                        albumArrayList.add(new Album(Integer.parseInt(id), artistName, albumName, thumbnail));
                    }
                }


                albumList = new AlbumList(Integer.parseInt(listid), username, name, description, created, albumArrayList, Integer.valueOf(stars));

                buildRecyclerView();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Internationalised - Failure to retrieve data
        }, error -> Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.fail_get_data), Toast.LENGTH_SHORT).show());
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
        albumListName = (TextView) findViewById(R.id.textView_albumListName);


        //in your OnCreate() method
        albumListName.setText(albumList.getName());

        albumListName.setVisibility(View.VISIBLE);

        // globally
        albumListDesc = (TextView) findViewById(R.id.textView_albumListDesc);

        //in your OnCreate() method
        albumListDesc.setText(albumList.getDescription());

        albumListDesc.setVisibility(View.VISIBLE);

        textView_stars = (TextView) findViewById(R.id.textView_stars);

        stars = albumList.getStars();

        // Internationalised - "Stars: "
        textView_stars.setText(getResources().getString(R.string.stars) + stars.toString());

        if (albumList.getStars() >= 1) {
            starHollow.setVisibility(View.INVISIBLE);
            star.setVisibility(View.VISIBLE);
        } else {
            starHollow.setVisibility(View.VISIBLE);
            star.setVisibility(View.INVISIBLE);
        }


        // setting adapter to
        // our recycler view.
        album.setAdapter(adapter);

        if (albumList.getUsername().equals(UserId)) { //checking if user can modify list
            secondaryDelete.setVisibility(View.VISIBLE);
            addAlbum.setVisibility(View.VISIBLE);
            editDescription.setVisibility(View.VISIBLE);
            star.setVisibility(View.INVISIBLE);
            starHollow.setVisibility(View.INVISIBLE);
        } else {
            secondaryDelete.setVisibility(View.INVISIBLE);
            addAlbum.setVisibility(View.INVISIBLE);
            editDescription.setVisibility(View.INVISIBLE);
            star.setVisibility(View.VISIBLE);
            starHollow.setVisibility(View.VISIBLE);
        }
    }


    public void delete() {

        secondaryDelete.setOnClickListener(v -> {

            RequestQueue queue = Volley.newRequestQueue(SecondaryActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, SERVICE_URI + id.toString(),
                    response -> {


                        lists.removeIf(albumList -> albumList.getId().equals(id));
                        // Internationalised - Deleting list
                        Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.list_delete), Toast.LENGTH_LONG).show();

                        finish();


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                        // Internationalised - Failure to delete list
                    }, error -> Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.list_delete_fail), Toast.LENGTH_SHORT).show());

            queue.add(stringRequest);

        });


    }


    public void addAlbum() {
        addAlbum.setOnClickListener(v -> openDialog());
    }

    public void openDialog() {
        AddAlbumDialog Dialog = new AddAlbumDialog();
        Dialog.show(getSupportFragmentManager(), "example dialog");
        secondaryDelete.setVisibility(View.INVISIBLE);
        addAlbum.setVisibility(View.INVISIBLE);
        editDescription.setVisibility(View.INVISIBLE);
    }

    @Override
    public void applyTexts(String artist, String name) {   // taking in strings from dialogue class
        // Hashmap storing the variables as two strings
        secondaryDelete.setVisibility(View.VISIBLE);
        addAlbum.setVisibility(View.VISIBLE);
        editDescription.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<String, String>();
        // applying the variables
        params.put("artist", artist);
        params.put("name", name);

        // taking in artist and name to the JSON object parameters
        RequestQueue queue = Volley.newRequestQueue(SecondaryActivity.this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, SERVICE_URI + id.toString() + "/album", new JSONObject(params),
                response -> {
                    try {
                        VolleyLog.v("Response:%n %s", response.toString(4));
                        // Internationalised - Adding album
                        Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.album_added), Toast.LENGTH_LONG).show();

                        JSONArray jsonArray = new JSONArray();
                        ArrayList<Album> albumArrayList = new ArrayList<Album>();


                        Log.e("toString() error: ", response.toString());


                        if (response.optJSONArray("albums") != null) {
                            jsonArray = response.optJSONArray("albums");
                        }


                        if (jsonArray != null) {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject obj = jsonArray.getJSONObject(j);
                                String id = obj.getString("id");
                                String albumName = obj.getString("name");
                                String artistName = obj.getString("artist");
                                String thumbnail = obj.getString("thumbnail");
                                albumArrayList.add(new Album(Integer.parseInt(id), artistName, albumName, thumbnail));
                            }
                        }

                        albumList.setAlbums(albumArrayList);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();

                        // Internationalised - Failure to add album
                        Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.album_add_fail), Toast.LENGTH_LONG).show();
                    }
                }, error -> VolleyLog.e("Error: ", error.getMessage()));

        queue.add(req);

    }


    public void editDescription() {
        editDescription.setOnClickListener(v -> openDialog3());
    }

    public void openDialog3() {
        AddDescDialog Dialog = new AddDescDialog();
        Dialog.show(getSupportFragmentManager(), "example dialog");
        secondaryDelete.setVisibility(View.INVISIBLE);
        addAlbum.setVisibility(View.INVISIBLE);
        editDescription.setVisibility(View.INVISIBLE);
    }

    @Override
    public void applyListDescription(String name, String description) {   // taking in strings from dialogue class
        // Hashmap storing the variables as two strings
        secondaryDelete.setVisibility(View.VISIBLE);
        addAlbum.setVisibility(View.VISIBLE);
        editDescription.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<String, String>();
        // applying the variables
        params.put("name", name);
        params.put("description", description);

        // taking in artist and name to the JSON object parameters
        RequestQueue queue = Volley.newRequestQueue(SecondaryActivity.this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, SERVICE_URI + id.toString() + "/" + name + "/" + description, new JSONObject(params),
                response -> {
                    try {
                        VolleyLog.v("Response:%n %s", response.toString(4));

                        // Internationalised - Description update
                        Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.desc_update), Toast.LENGTH_LONG).show();

                        JSONArray jsonArray = new JSONArray();
                        ArrayList<Album> albumArrayList = new ArrayList<Album>();

                        albumList.setDescription(description);
                        albumList.setName(name);
                        Log.e("toString() error: ", response.toString());
                        albumListName.setText(albumList.getName());
                        albumListDesc.setText(albumList.getDescription());

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();


                        // Internationalised - Failure to update description
                        Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.desc_update_fail), Toast.LENGTH_SHORT).show();
                    }
                }, error -> VolleyLog.e("Error: ", error.getMessage()));

        queue.add(req);


    }

    public String getUserId() {

        return Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE + Build.USER.length() % 10;
    }


    public void star() {

        star.setOnClickListener(v -> {

            RequestQueue queue = Volley.newRequestQueue(SecondaryActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVICE_URI + id.toString(),
                    response -> {

                        // Internationalised - "Stars: "
                        stars++;
                        textView_stars.setText(getResources().getString(R.string.stars) + stars.toString());


                        // Internationalised - Starring list
                        Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.list_starred), Toast.LENGTH_LONG).show();



                        adapter.notifyDataSetChanged();


                        // Internationalised - Failure to star list
                    }, error -> Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.list_star_fail), Toast.LENGTH_SHORT).show());

            queue.add(stringRequest);

        });


    }

    public void starHollow() {

        starHollow.setOnClickListener(v -> {
            RequestQueue queue = Volley.newRequestQueue(SecondaryActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVICE_URI + id.toString(),
                    response -> {

                        // Internationalised - "Stars: "
                        stars++;
                        textView_stars.setText(getResources().getString(R.string.stars) + stars.toString());

                        // Internationalised - Starring list
                        Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.list_starred), Toast.LENGTH_LONG).show();



                        adapter.notifyDataSetChanged();


                        // Internationalised - Failure to star list
                    }, error -> Toast.makeText(SecondaryActivity.this, getResources().getString(R.string.list_star_fail), Toast.LENGTH_SHORT).show());

            queue.add(stringRequest);


        });


    }


}
