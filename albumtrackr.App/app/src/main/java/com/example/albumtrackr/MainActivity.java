// add Internet permission

package com.example.albumtrackr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Debug;
import android.os.StrictMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
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
import java.util.HashMap;


// in build.gradle for Module:app
// implementation 'com.android.volley:volley:1.1.1'
// implementation 'com.google.code.gson:gson:2.8.5'

public class MainActivity extends AppCompatActivity implements AddListDialog.DialogListener2 {

    // uri of RESTful service on Azure, note: https, cleartext support disabled by default
    private String SERVICE_URI = "https://albumtrackrapi.azurewebsites.net/api/AlbumList/1";          // or https
    private String SERVICE_URI_addlist = "https://albumtrackrapi.azurewebsites.net/api/AlbumList";
    private String TAG = "";
    //https://www.geeksforgeeks.org/how-to-extract-data-from-json-array-in-android-using-volley-library/

    private RecyclerView album;
    private RecyclerView AlbumList;
    private AlbumAdapter adapter;
    private AlbumListAdapter adapter2;
    private AlbumList albumList;
    private ArrayList<AlbumList> lists = new ArrayList<AlbumList>();
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;

    FloatingActionButton addList;

    AlbumAdapter albumAdapt;

    Button btnAddAlbum;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());



        // RecyclerView for Albumlist
        // AlbumList = (RecyclerView) findViewById(R.id.recycler_alist);
        // RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(getApplicationContext());
        // AlbumList.setLayoutManager(aLayoutManager);
        // AlbumList.setItemAnimator(new DefaultItemAnimator());
        // AlbumList.setAdapter(adapter2);

        pagerAdapter.AddFragment(new FragmentMine(), "My Album Lists");
        pagerAdapter.AddFragment(new FragmentPopular(), "Popular Album Lists");
        pagerAdapter.AddFragment(new FragmentLatest(), "Newest Album Lists");;

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        addList = (FloatingActionButton)findViewById(R.id.fab_add_list);

        addAlbumList();


    }



    private void getData() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.



        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, SERVICE_URI, null, new Response.Listener<JSONObject>() {
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
                    String listid = response.getString("id");
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
                            String id = obj.getString("id");
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

                    albumList = new AlbumList(Integer.parseInt(listid), username, name, description, created, albumArrayList, starArrayList);



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

    private void getLists() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://albumtrackrapi.azurewebsites.net/api/AlbumList/", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                AlbumList.setVisibility(View.VISIBLE);
                // creating a new json object and
                // getting each object from our json array.
                try {
                    // we are getting each json object.


                    if(response != null) {
                        for (int j = 0; j < response.length(); j++) {
                            JSONObject obj = response.getJSONObject(j);
                            String id = obj.getString("id");
                            String username = obj.getString("username");
                            String created = obj.getString("created");
                            String name = obj.getString("name");
                            String description = obj.getString("description");
                            lists.add(new AlbumList(Integer.parseInt(id), username, name, description, created, new ArrayList<Album>(), new ArrayList<Star>() ));

                        }
                    }


                    buildRecyclerViewList();
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
        adapter = new AlbumAdapter(albumList, MainActivity.this);

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

    private void buildRecyclerViewList() {

        // initializing our adapter class.
        adapter2 = new AlbumListAdapter(lists, MainActivity.this);

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        AlbumList.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        AlbumList.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        AlbumList.setAdapter(adapter2);
    }



    public void addAlbumList(){
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog2();
            }
        });
    }

    public void openDialog2(){
        AddListDialog Dialog = new AddListDialog();
        Dialog.show(getSupportFragmentManager(), "example dialog");

    }

    @Override
    public void applyTexts2(String name, String description) {   // taking in strings from dialogue class
        // Hashmap storing the variables as two strings
        HashMap<String, String> params = new HashMap<String, String>();
        // applying the variables
        params.put("Name", name);
        params.put("Description", description);

        // taking in artist and name to the JSON object parameters
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, SERVICE_URI_addlist, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                                JSONObject obj = response;
                                String id = obj.getString("id");
                                String username = obj.getString("username");
                                String created = obj.getString("created");
                                String name = obj.getString("name");
                                String description = obj.getString("description");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

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

        queue.add(req);

    }



}
