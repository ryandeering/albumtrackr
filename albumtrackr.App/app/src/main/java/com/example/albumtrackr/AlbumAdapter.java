package com.example.albumtrackr;

import android.content.Context;
import android.os.Build;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private final AlbumList AlbumArrayList;
    private final Context context;
    private final String SERVICE_URI = "https://albumtrackrapi.azurewebsites.net/api/AlbumList/";

    private final ArrayList<AlbumList> lists = new ArrayList<AlbumList>();

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
            Album modal = AlbumArrayList.getAlbums().get(holder.getAdapterPosition());
            holder.AlbumName.setText(modal.getName());
            holder.ArtistName.setText(modal.getArtist());

            // Getting the position of the item (the album) then getting the ID in the API request below
            Album albumToDelete = AlbumArrayList.getAlbums().get(holder.getAdapterPosition());
            // List ID
            Integer listID = AlbumArrayList.getId();




            Picasso.get().load(modal.getThumbnail()).into(holder.AlbumCover);


            if(AlbumArrayList.getUsername().equals(getUserId())){
                holder.albumDelete.setVisibility(View.VISIBLE);
            } else {
                holder.albumDelete.setVisibility(View.INVISIBLE);
            }

            holder.albumDelete.setOnClickListener(v -> {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, SERVICE_URI + listID.toString() + "/album/" + albumToDelete.getId().toString(),
                        response -> {

                            List<Album> albums = AlbumArrayList.getAlbums();
                            AlbumArrayList.setAlbums(albums);

                            albums.remove(albumToDelete);

                            Toast.makeText(context, "Album Deleted!", Toast.LENGTH_LONG).show();

                            notifyDataSetChanged();
                        }, error -> {
                    parseVolleyError(error);
                    Toast.makeText(context, "Unable to delete album!", Toast.LENGTH_SHORT).show();
                });

                queue.add(stringRequest);

            });
        }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
            JSONObject data = new JSONObject(responseBody);
            JSONArray errors = data.getJSONArray("errors");
            JSONObject jsonMessage = errors.getJSONObject(0);
            String message = jsonMessage.getString("message");
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        }
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return AlbumArrayList.getAlbums().size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView AlbumName;
        private final TextView ArtistName;
        private final ImageView AlbumCover;
        final Button albumDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            AlbumName = itemView.findViewById(R.id.AlbumName);
            ArtistName = itemView.findViewById(R.id.ArtistName);
            AlbumCover = itemView.findViewById(R.id.AlbumCover);
            albumDelete = itemView.findViewById(R.id.button_deleteAlbum);



        }
    }

    public String getUserId(){
        return Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10+ Build.TAGS.length() % 10 + Build.TYPE + Build.USER.length() % 10;
    }
}