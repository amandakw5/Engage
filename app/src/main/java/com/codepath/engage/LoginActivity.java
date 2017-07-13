package com.codepath.engage;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    public static CallbackManager mCallbackManager;
    private String TAG = "TOKEN_ACCESS";
    private AccessToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.codepath.engage", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (NoSuchAlgorithmException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }

        if (AccessToken.getCurrentAccessToken()!=null){
            Intent i = new Intent(LoginActivity.this, EventDetailsActivity.class);
            startActivity(i);
        }

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //TODO make sure to change this before pushing to master
                Intent intent = new Intent(LoginActivity.this, ViewEvents.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() { }

            @Override
            public void onError(FacebookException error) { }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            Toast.makeText(this, token.toString(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Token: " + token.getToken());
            Log.e(TAG, "UserID: " + token.getUserId());
        }
    }
}