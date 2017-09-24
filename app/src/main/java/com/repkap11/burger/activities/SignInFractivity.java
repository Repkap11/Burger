package com.repkap11.burger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.repkap11.burger.BurgerApplication;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.base.Fractivity;
import com.repkap11.burger.fragments.LunchGroupsFractivityFragment;
import com.repkap11.burger.fragments.SignInFractivityFragment;
import com.repkap11.burger.fragments.TabFractivityFragment;


public class SignInFractivity extends Fractivity {

    private static final String TAG = SignInFractivity.class.getSimpleName();
    public static final int REQUEST_CODE_SIGN_IN = 43;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        BurgerApplication.showUpdateDialogIfNecessary(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected FractivityFragment createFragment(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String instanceToken = FirebaseInstanceId.getInstance().getToken();
        boolean result = BurgerApplication.getUserPerferedNotoficationsEnabled(this);
        BurgerApplication.updateDeviceToken(this, result);
        //Log.e(TAG, "instanceToken:" + instanceToken);

        String groupKey = BurgerApplication.getUserPerferedLunchGroup(this);
        if (groupKey != null) {
            String rootGroupsName = FirebaseDatabase.getInstance().getReference(groupKey).getParent().getKey();
            if (rootGroupsName != null) {
                String expectedRootGroup = getResources().getString(R.string.root_key_lunch_groups);
                if (!expectedRootGroup.equals(rootGroupsName)) {
                    Log.e(TAG, "Wrong root group expected:" + expectedRootGroup + " got:" + rootGroupsName);
                    Toast.makeText(this, "", Toast.LENGTH_SHORT);
                    BurgerApplication.setUserPerferedLunchGroup(this, null);
                }
            }
        }
        if (currentUser != null) {
            //Log.e(TAG, "Sign in done in create for:" + currentUser);
            //continueAfterSignIn();
            String perferedGroup = BurgerApplication.getUserPerferedLunchGroup(this);
            //Log.e(TAG, "Starting signed in user with preferedGroup:" + perferedGroup);
            if (perferedGroup == null) {
                return new LunchGroupsFractivityFragment();
            } else {
                return new TabFractivityFragment();
            }
        } else {
            return new SignInFractivityFragment();
        }
    }

    public void continueAfterSignIn() {
        Intent intent = new Intent(this, LunchGroupsFractivity.class);
        //Clear the back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e(TAG, "Activity result");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                //Log.e(TAG, "Sign in ok");
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                ((SignInFractivityFragment) mFragment).firebaseAuthWithGoogle(account);
            } else {
                //Log.e(TAG, "Sign in failed");

                // Google Sign In failed, update UI appropriately
                // ...
            }
        } else {
            //Log.e(TAG, "Activity Wrong code:" + requestCode);

        }
    }
}
