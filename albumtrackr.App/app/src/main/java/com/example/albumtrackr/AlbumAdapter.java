package com.example.albumtrackr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private AlbumList AlbumArrayList;
    private Context context;
    private String SERVICE_URI = "https://albumtrackrapi.azurewebsites.net/api/AlbumList/";

    private ArrayList<AlbumList> lists = new ArrayList<AlbumList>();

    // creating a constructor for our variables.
    public AlbumAdapter(AlbumList albumArrayList, Context context) {
        this.AlbumArrayList = albumArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);

    }

    @Override
        public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
            // setting data to our views of recycler view.
            Album modal = AlbumArrayList.getAlbums().get(position);
            holder.AlbumName.setText(modal.getName());
            holder.ArtistName.setText(modal.getArtist());

            // Displaying the ID for troubleshooting purposes
            holder.AlbumID.setText(modal.getId().toString());


            Album albumtoDelete = AlbumArrayList.getAlbums().get(position);

            // Tried this too, same thing
            //Integer albumID = modal.getId();


            Integer listID = AlbumArrayList.getId();



            Picasso.get().load(modal.getThumbnail()).into(holder.AlbumCover);

            holder.albumDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue queue = Volley.newRequestQueue(context);
                    StringRequest stringRequest = new StringRequest(Request.Method.DELETE, SERVICE_URI + listID.toString() + "/album/" + albumtoDelete.getId().toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    List<Album> albums = AlbumArrayList.getAlbums();
                                    AlbumArrayList.setAlbums(albums);

                                    albums.remove(albumtoDelete);




                                    Toast.makeText(context, "Album Deleted!", Toast.LENGTH_LONG).show();


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            parseVolleyError(error);
                            Toast.makeText(context, "Unable to delete album!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    queue.add(stringRequest);

                }
            });
        }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            JSONArray errors = data.getJSONArray("errors");
            JSONObject jsonMessage = errors.getJSONObject(0);
            String message = jsonMessage.getString("message");
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return AlbumArrayList.getAlbums().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private TextView AlbumName, ArtistName, AlbumID;
        private ImageView AlbumCover;
        Button albumDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            AlbumName = itemView.findViewById(R.id.AlbumName);
            ArtistName = itemView.findViewById(R.id.ArtistName);
            AlbumCover = itemView.findViewById(R.id.AlbumCover);
            AlbumID = itemView.findViewById(R.id.AlbumID);
            albumDelete = itemView.findViewById(R.id.button_deleteAlbum);

        }
    }

    // public void getStars(AlbumList list){
    //        list.getStars();
    //}

    //public void deleteAlbumList(AlbumList list){
    //
    //}
}