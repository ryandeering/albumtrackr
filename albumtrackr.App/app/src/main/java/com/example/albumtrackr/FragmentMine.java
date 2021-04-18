package com.example.albumtrackr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentMine extends Fragment {

    View v;
    private RecyclerView mine;
    private final ArrayList<AlbumList> myAlbumLists = new ArrayList<AlbumList>();

    public FragmentMine() {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.popular_fragment, container, false);
        mine = (RecyclerView) v.findViewById(R.id.popular_recyclerview);
        buildRecyclerViewList();

        // Row click listener
        mine.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), mine, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(int position) {
                AlbumList albumList = myAlbumLists.get(position);

                try {
                    Intent intent = new Intent(getActivity(), SecondaryActivity.class);
                    intent.putExtra("albumListID", albumList.getId());
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }


            }

            @Override
            public void onLongClick() {

            }
        }));

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            getLists();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    private void getLists() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://albumtrackrapi.azurewebsites.net/api/albumlist/username/" + getUserId(), null, response -> {
            mine.setVisibility(View.VISIBLE);
            // creating a new json object and
            // getting each object from our json array.
            try {
                // we are getting each json object.

                if (response != null) {

                    if (response.length() == 0) {
                        // Internationalised - No lists found for user
                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.no_lists_found), Toast.LENGTH_LONG).show();
                    }

                    for (int j = 0; j < response.length(); j++) {
                        JSONObject obj = response.getJSONObject(j);
                        String id = obj.getString("id");
                        String username = obj.getString("username");
                        String created = obj.getString("created");
                        String name = obj.getString("name");
                        String description = obj.getString("description");
                        String stars = obj.getString("stars");
                        myAlbumLists.add(new AlbumList(Integer.parseInt(id), username, name, description, created, new ArrayList<Album>(), Integer.valueOf(stars)));
                    }
                }


                buildRecyclerViewList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Internationalised - Failure to retrieve data
        }, error -> Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.fail_get_data), Toast.LENGTH_SHORT).show());
        queue.add(jsonArrayRequest);
    }


    private void buildRecyclerViewList() {

        // initializing our adapter class.
        AlbumListAdapter adapter = new AlbumListAdapter(myAlbumLists, getActivity().getApplicationContext());

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        mine.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        mine.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        mine.setAdapter(adapter);
    }

    public String getUserId() {
        return Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE + Build.USER.length() % 10;
    }


}



