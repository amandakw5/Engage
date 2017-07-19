package com.codepath.engage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static CallbackManager mCallbackManager;
    private String TAG = "TOKEN_ACCESS";
//    private AccessToken token;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("users");
//        final DatabaseReference uid = database.getReference("users/uid");
//        final DatabaseReference userName = database.getReference("users/name");
//        final DatabaseReference userFirstName = database.getReference("users/first_name");
//        final DatabaseReference userLastName = database.getReference("users/last_name");
//        final DatabaseReference userEmail = database.getReference("users/email");
//        final DatabaseReference userPicture = database.getReference("users/profileUrl");

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());
                String uid;

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    uid = user.getUid();
                    final DatabaseReference userInfo = users.child(uid);
                    final Map<String, Object> userInfoUpdates = new HashMap<String, Object>();

                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.i("LoginActivity", response.toString());
                            // Get facebook data from login
                            Bundle bFacebookData = getFacebookData(object);
                            try {
                                String first_name = object.getString("first_name");
                                userInfoUpdates.put("first_name", first_name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                String last_name = object.getString("last_name");
                                userInfoUpdates.put("last_name", last_name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                String email = object.getString("email");
                                userInfoUpdates.put("email", email);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                String id = object.getString("id");
                                try {
                                    URL profile_picture = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=200");
                                    String profilePicture = profile_picture.toString();
                                    userInfoUpdates.put("profile_pic", profilePicture);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            userInfo.updateChildren(userInfoUpdates);

                            Intent intent = new Intent(LoginActivity.this, HomePage.class);
                            intent.putExtras(bFacebookData);
                            startActivity(intent);
                        }
                    });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override
            public void onCancel() { }

            @Override
            public void onError(FacebookException error) { }

            private Bundle getFacebookData(JSONObject object) {

                try {
                    Bundle bundle = new Bundle();
                    String id = object.getString("id");

                    try {
                        URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=200");
                        Log.i("profile_pic", profile_pic + "");
                        bundle.putString("profile_pic", profile_pic.toString());

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return null;
                    }
                    bundle.putString("idFacebook", id);
                    if (object.has("first_name"))
                        bundle.putString("first_name", object.getString("first_name"));
                    if (object.has("last_name"))
                        bundle.putString("last_name", object.getString("last_name"));
                    if (object.has("email"))
                        bundle.putString("email", object.getString("email"));
                    if (object.has("gender"))
                        bundle.putString("gender", object.getString("gender"));
                    if (object.has("birthday"))
                        bundle.putString("birthday", object.getString("birthday"));
                    if (object.has("location"))
                        bundle.putString("location", object.getJSONObject("location").getString("name"));
                    return bundle;
                }
                catch(JSONException e) {
                    Log.d(TAG,"Error parsing JSON");
                }
                return null;
            }
        });
//        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Intent i = new Intent(LoginActivity.this, ViewEvents.class);
            startActivity(i);
        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}