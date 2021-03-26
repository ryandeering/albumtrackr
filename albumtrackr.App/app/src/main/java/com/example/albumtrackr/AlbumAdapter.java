package com.example.albumtrackr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<Album> AlbumArrayList;
    private Context context;

    // creating a constructor for our variables.
    public AlbumAdapter(ArrayList<Album> albumArrayList, Context context) {
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
        Album modal = AlbumArrayList.get(position);
        holder.AlbumName.setText(modal.getName());
        holder.ArtistName.setText(modal.getArtist());
        Picasso.get().load(modal.getThumbnail()).into(holder.AlbumCover);
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return AlbumArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private TextView AlbumName, ArtistName;
        private ImageView AlbumCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            AlbumName = itemView.findViewById(R.id.AlbumName);
            ArtistName = itemView.findViewById(R.id.ArtistName);
            AlbumCover = itemView.findViewById(R.id.AlbumCover);
        }
    }
}