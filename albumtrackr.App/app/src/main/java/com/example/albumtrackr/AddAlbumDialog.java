package com.example.albumtrackr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AddAlbumDialog extends AppCompatDialogFragment {

    private Album album;
    private EditText editTextName;
    private EditText editTextArtist;
    private DialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view).setTitle("Add Album")
                .setNegativeButton("cancel", (dialog, i) -> {

                })
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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface DialogListener{
        void applyTexts(String artist, String name);
    }

}
