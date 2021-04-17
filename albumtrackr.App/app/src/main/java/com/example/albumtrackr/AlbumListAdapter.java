package com.example.albumtrackr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private final ArrayList<AlbumList> AlbumArrayList;

    // creating a constructor for our variables.
    public AlbumListAdapter(ArrayList<AlbumList> albumArrayList, Context context) {
        this.AlbumArrayList = albumArrayList;
    }

    @NonNull
    @Override
    public AlbumListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albumlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumListAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        AlbumList modal = AlbumArrayList.get(position);
        holder.AListName.setText(modal.getName());
        holder.AListDesc.setText(modal.getDescription());
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return AlbumArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView AListName;
        private final TextView AListDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            AListName = itemView.findViewById(R.id.aListName);
            AListDesc = itemView.findViewById(R.id.aListDescription);

            TextView AListName2 = itemView.findViewById(R.id.textView_albumListName);
            TextView AListDesc2 = itemView.findViewById(R.id.textView_albumListDesc);
        }
    }


}