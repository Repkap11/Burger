package com.repkap11.burger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by paul on 8/8/17.
 */

public class LongClickDeleteDialogFragment extends DialogFragment {

    public static final String ARG_TITLE = "com.repkap11.burger.ARG_TITLE";
    public static final String ARG_MESSAGE = "com.repkap11.burger.ARG_MESSAGE";
    public static final String ARG_KEY = "com.repkap11.burger.ARG_KEY";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString(ARG_TITLE);
        String message = args.getString(ARG_MESSAGE);
        final String key = args.getString(ARG_KEY);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference(key).removeValue();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return alert.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
