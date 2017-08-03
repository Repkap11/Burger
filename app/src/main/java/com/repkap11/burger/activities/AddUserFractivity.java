package com.repkap11.burger.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.models.User;

public class AddUserFractivity extends Fractivity<AddUserFractivity.AddUserFragment> {

    @Override
    protected AddUserFragment createFragment(Bundle savedInstanceState) {
        return new AddUserFragment();
    }

    public static class AddUserFragment extends Fractivity.FractivityFragment {
        private EditText mEditTextCarSize;
        private Button mSaveLocationButtion;
        private EditText mEditTextFirstName;
        private EditText mEditTextLastName;

        @Override
        protected void create(Bundle savedInstanceState) {

        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fractivity_add_user, container, false);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.fractivity_add_user_title);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear_black, null);
            toolbar.setNavigationIcon(drawable);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            mEditTextCarSize = (EditText) rootView.findViewById(R.id.fractivity_add_user_edit_text_car_size);
            mEditTextFirstName = (EditText) rootView.findViewById(R.id.fractivity_add_user_edit_text_first_name);
            mEditTextLastName = (EditText) rootView.findViewById(R.id.fractivity_add_user_edit_text_last_name);

            mSaveLocationButtion = (Button) rootView.findViewById(R.id.fractivity_add_user_button_save);
            mSaveLocationButtion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference locationsRef = database.getReference("users");
                    DatabaseReference newLocation = locationsRef.push();
                    newLocation.setValue(new User(mEditTextFirstName.getText().toString(), mEditTextLastName.getText().toString(), mEditTextCarSize.getText().toString()));
                    getActivity().finish();
                }
            });
            return rootView;
        }

        @Override
        protected void destroyView() {
            mEditTextCarSize = null;
            mEditTextFirstName = null;
            mEditTextLastName = null;
            mSaveLocationButtion = null;
        }
    }
}
