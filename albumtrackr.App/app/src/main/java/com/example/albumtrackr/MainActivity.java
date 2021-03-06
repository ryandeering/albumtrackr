// add Internet permission
package com.example.albumtrackr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


// in build.gradle for Module:app
// implementation 'com.android.volley:volley:1.1.1'
// implementation 'com.google.code.gson:gson:2.8.5'

public class MainActivity extends AppCompatActivity implements AddListDialog.DialogListenerAddList {

    //https://www.geeksforgeeks.org/how-to-extract-data-from-json-array-in-android-using-volley-library/

    FloatingActionButton addList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // My Album Lists
        pagerAdapter.AddFragment(new FragmentMine(), getResources().getString(R.string.my_album_lists));
        // Popular Album Lists
        pagerAdapter.AddFragment(new FragmentPopular(), getResources().getString(R.string.pop_lists));
        // Newest Album Lists
        pagerAdapter.AddFragment(new FragmentLatest(), getResources().getString(R.string.new_lists));

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        addList = (FloatingActionButton) findViewById(R.id.fab_add_list);

        addAlbumList();


    }


    public void addAlbumList() {
        addList.setOnClickListener(v -> openDialog2());
    }

    public void openDialog2() {
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
        params.put("Username", getUserId());

        // taking in artist and name to the JSON object parameters
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String SERVICE_URI_addlist = "https://albumtrackrapi.azurewebsites.net/api/AlbumList";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, SERVICE_URI_addlist, new JSONObject(params),
                response -> {
                    try {

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                        VolleyLog.v("Response:%n %s", response.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> VolleyLog.e("Error: ", error.getMessage()));

        queue.add(req);

    }

    public String getUserId() {

        return Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE + Build.USER.length() % 10;
    }


}
