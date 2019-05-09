package com.repkap11.burger.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.SignInFractivity;
import com.repkap11.burger.activities.base.Fractivity;

/**
 * Created by paul on 8/8/17.
 */
public class SignInFractivityFragment extends Fractivity.FractivityFragment implements GoogleApiClient.OnConnectionFailedListener {
    public static final String STARTING_INTENT_WHICH_LUNCH_GROUP = "com.repkap11.burger.STARTING_INTENT_WHICH_LUNCH_GROUP";
    private static final String TAG = SignInFractivityFragment.class.getSimpleName();

    @Override
    protected void create(Bundle savedInstanceState) {
    }

    @Override
    protected void saveState(Bundle outState) {

    }

    @Override
    protected void restoreState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fractivity_sign_in, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.fractivity_bar_menu_app_bar_layout);
        toolbar.setTitle(R.string.fractivity_sign_in_title);
        rootView.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        return rootView;
    }

    @Override
    protected void destroyView() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(
                getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        getActivity().startActivityForResult(signInIntent, SignInFractivity.REQUEST_CODE_SIGN_IN);
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.e(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            ((SignInFractivity) getActivity()).continueAfterSignIn();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
