package com.example.albumtrackr;

import android.content.Intent;
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

public class FragmentLatest extends Fragment {

    View v;
    private RecyclerView latest;
    private final ArrayList<AlbumList> latestAlbumLists = new ArrayList<AlbumList>();

    public FragmentLatest() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.popular_fragment,container,false);
        latest = (RecyclerView) v.findViewById(R.id.popular_recyclerview);
        buildRecyclerViewList();

        // Row click listener
        latest.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), latest, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(int position) {
                AlbumList albumList  = latestAlbumLists.get(position);

                try {
                    Intent intent = new Intent(getActivity() , SecondaryActivity.class);
                    intent.putExtra("albumListID", albumList.getId());
                    startActivity(intent);
                } catch (Exception e){
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
        } catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }


    }

    private void getLists() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://albumtrackrapi.azurewebsites.net/api/AlbumList/", null, response -> {
            latest.setVisibility(View.VISIBLE);
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
                        String stars = obj.getString("stars");
                        latestAlbumLists.add(new AlbumList(Integer.parseInt(id), username, name, description, created, new ArrayList<Album>(), Integer.valueOf(stars)));
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
        AlbumListAdapter adapter = new AlbumListAdapter(latestAlbumLists, getActivity().getApplicationContext());

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        latest.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        latest.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        latest.setAdapter(adapter);
    }
}
