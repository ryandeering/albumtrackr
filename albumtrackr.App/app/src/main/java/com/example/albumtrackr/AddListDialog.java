package com.example.albumtrackr;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

public class AddListDialog  extends AppCompatDialogFragment{

    private AlbumList albumList;
    private EditText editTextName;
    private EditText editTextDescription;
    private DialogListener2 listener2;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_two, null);

        builder.setView(view).setTitle("Add Album List")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {


                        String name = editTextName.getText().toString();
                        String description = editTextDescription.getText().toString();


                        listener2.applyTexts2(name, description);
                    }
                });


        editTextName = view.findViewById(R.id.edit_list_name);
        editTextDescription = view.findViewById(R.id.edit_description);

        return builder.create();
    }

    @Override
    public void onAttach(Context context2) {
        super.onAttach(context2);
        try {
            listener2 = (DialogListener2) context2;
        } catch (ClassCastException e) {
            throw new ClassCastException(context2.toString() + "must implement DialogListener");
        }
    }

    public interface DialogListener2{
        void applyTexts2(String name, String description);
    }

}


