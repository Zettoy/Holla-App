package com.holla.group1.holla.signin;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.Task;
import com.holla.group1.holla.MapsActivity;
import com.holla.group1.holla.R;

public class StartupActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "StartupActivity";

    private SignInButton signInButton;

    private GoogleSignInOptions get_gso() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        return gso;
    }

    private void do_silent_sign_in(GoogleSignInOptions gso) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, connectionResult.toString());

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //From here: https://stackoverflow.com/a/38750275

        // https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInApi.html#silentSignIn(com.google.android.gms.common.api.GoogleApiClient)

        // silentSignIn will refresh the user's id_token if an account is available
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                GoogleAccountSingleton.mGoogleSignInAccount = account;
                moveToMap();
            }
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    GoogleSignInAccount account = googleSignInResult.getSignInAccount();
                    if (account != null) {
                        GoogleAccountSingleton.mGoogleSignInAccount = account;
                        StartupActivity.this.moveToMap();

                    } else {
                        if (signInButton != null) {
                            //re-enable sign-in button if we can't silently log in user.
                            signInButton.setEnabled(true);
                        }
                    }
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        GoogleSignInOptions gso = get_gso();
        GoogleAccountSingleton.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        do_silent_sign_in(gso);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        //disable sign in button whilst we try to auto login user
        signInButton.setEnabled(false);

        initAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        tryLastSignedInAccount();
    }

    private void tryLastSignedInAccount() {
        // Check if we've already signed in and quickly skip to the map activity
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        GoogleAccountSingleton.mGoogleSignInAccount = account;
        if (account != null) {
            moveToMap();
        }
    }

    private void initAnimation() {
        // There's no nice way to grab heights of elements in a RelativeLayout
        // unless we want to do callbacks for literally getting the height
        float whiteSpaceLogoButtons = 60f;
        float logoHeight = 218f;
        float buttonsHeight = 60f;

        TextView logoText = (TextView) findViewById(R.id.logo_text);
        ImageView logoImage = (ImageView) findViewById(R.id.logo_image);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(logoImage, "alpha", 0f, 1f);
        fadeOut.setDuration(2000);
        fadeOut.start();

        ObjectAnimator fadeOut2 = ObjectAnimator.ofFloat(logoText, "alpha", 0f, 1f);
        fadeOut2.setDuration(2000);
        fadeOut2.start();

        float logoTranslateAmount = (buttonsHeight + whiteSpaceLogoButtons);
        RelativeLayout logoLayout = (RelativeLayout) findViewById(R.id.logo_layout);
        ObjectAnimator translateUp = ObjectAnimator.ofFloat(logoLayout, "translationY", 0f, -logoTranslateAmount);
        translateUp.setDuration(1000);
        translateUp.setStartDelay(3000);
        translateUp.start();

//        float buttonPosY = (logoHeight + whiteSpaceLogoButtons + buttonsHeight);
        float buttonPosY = 0;
        signInButton.setY(buttonPosY);

        ObjectAnimator fadeInButton = ObjectAnimator.ofFloat(signInButton, "alpha", 0f, 1f);
        fadeInButton.setDuration(2000);
        fadeInButton.setStartDelay(3500);
        fadeInButton.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                handleSignIn();
                break;
        }
    }

    private void handleSignIn() {
        Intent signInIntent = GoogleAccountSingleton.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Called when the google sign in activity has closed
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleAccountSingleton.mGoogleSignInAccount = account;

            // Move onto the map activity
            // Note, we don't validate this with the backend as we will be providing the
            // id token with all our user specific api calls
            moveToMap();
        } catch (ApiException e) {
            Toast.makeText(this, "Failed to sign in with the Google Account.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void moveToMap() {
        Intent mapIntent = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(mapIntent); //Our singleton, reluctantly takes passing parameters
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect to Google.", Toast.LENGTH_SHORT).show();
    }
}

