package com.example.albumtrackr;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AddDescDialog extends AppCompatDialogFragment {

    private EditText editListDescription;
    private EditText editListName;
    private DialogListenerDescription listenerDescription;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_desc_dialog_layout, null);

        // Title - "Edit List Description" - internationalised
        builder.setView(view).setTitle(getResources().getString(R.string.edit_desc))
                .setNegativeButton(getResources().getString(R.string.cancel), (dialog, i) -> {

                })
                .setPositiveButton("Ok", (dialog, i) -> {

                    String name = editListName.getText().toString();
                    String description = editListDescription.getText().toString();


                    listenerDescription.applyListDescription(name, description);
                });

        editListDescription = view.findViewById(R.id.edit_list_description);
        editListName = view.findViewById(R.id.edit_list_name);

        return builder.create();
    }

    @Override
    public void onAttach(Context descriptionContext) {
        super.onAttach(descriptionContext);
        try {
            listenerDescription = (DialogListenerDescription) descriptionContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(descriptionContext.toString() + "must implement DialogListenerAddAlbum");
        }
    }

    public interface DialogListenerDescription {
        void applyListDescription(String name, String description);
    }

}


