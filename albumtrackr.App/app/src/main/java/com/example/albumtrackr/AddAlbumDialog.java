package com.example.albumtrackr;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AddAlbumDialog extends AppCompatDialogFragment {

    private EditText editTextName;
    private EditText editTextArtist;
    private DialogListenerAddAlbum listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_album_dialog_layout, null);

        // Title - "Add Album" - internationalised
        builder.setView(view).setTitle(getResources().getString(R.string.add_album_title))
                // Cancel
                .setNegativeButton(getResources().getString(R.string.cancel), (dialog, i) -> {

                })
                // Ok
                .setPositiveButton("Ok", (dialog, i) -> {

                    String artist = editTextArtist.getText().toString();
                    String name = editTextName.getText().toString();


                    listener.applyTexts(artist, name);
                });

        editTextArtist = view.findViewById(R.id.edit_artist);
        editTextName = view.findViewById(R.id.edit_name);

        return builder.create();
    }

    @Override
    public void onAttach(Context addAlbumContext) {
        super.onAttach(addAlbumContext);
        try {
            listener = (DialogListenerAddAlbum) addAlbumContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(addAlbumContext.toString() + "must implement DialogListenerAddAlbum");
        }
    }

    public interface DialogListenerAddAlbum {
        void applyTexts(String artist, String name);
    }

}
