package com.example.albumtrackr;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AddListDialog extends AppCompatDialogFragment {

    private EditText editTextName;
    private EditText editTextDescription;
    private DialogListenerAddList listener2;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_list_dialog_layout, null);


        // Title - "Add List" - internationalised
        builder.setView(view).setTitle(getResources().getString(R.string.add_list))
                .setNegativeButton(getResources().getString(R.string.cancel), (dialog, i) -> {

                })
                .setPositiveButton("Ok", (dialog, i) -> {


                    String name = editTextName.getText().toString();
                    String description = editTextDescription.getText().toString();


                    listener2.applyTexts2(name, description);
                });


        editTextName = view.findViewById(R.id.edit_list_name);
        editTextDescription = view.findViewById(R.id.edit_description);

        return builder.create();
    }

    @Override
    public void onAttach(Context addListContext) {
        super.onAttach(addListContext);
        try {
            listener2 = (DialogListenerAddList) addListContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(addListContext.toString() + "must implement DialogListenerAddAlbum");
        }
    }

    public interface DialogListenerAddList {
        void applyTexts2(String name, String description);
    }

}


