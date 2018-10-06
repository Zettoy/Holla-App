package com.holla.group1.holla.signin;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

// Can't pass the sign in client through intents or convert it to json and then pass it
// so this method is the best way apparently
public class GoogleAccountSingleton {

    //private static GoogleAccountSingleton mInstance = null;
    public static GoogleSignInClient mGoogleSignInClient = null; // Don't really need much but the sign in client atm

    /*private GoogleAccountSingleton() {

    }

    public static synchronized GoogleAccountSingleton getInstance(GoogleSignInClient googleApiClient) {
        if (mInstance == null) {
            mInstance = new GoogleAccountSingleton();
            if (googleApiClient != null) {
                mGoogleSignInClient = googleApiClient;
            }
        }
        return mInstance;
    }

    public static GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }*/
}
