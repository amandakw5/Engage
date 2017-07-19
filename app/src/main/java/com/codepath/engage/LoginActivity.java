package com.codepath.engage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.codepath.engage.models.User;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "TOKEN_ACCESS";

    public static CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null){
            Intent i = new Intent(this, HomePage.class);
            startActivity(i);
            finish();
        }

        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        //LoginManager.getInstance()
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        final User user = new User();
                        Bundle bFacebookData = getFacebookData(object);
                        Log.d(TAG, "facebook:onCompleted");
                        try {
                            String id = object.getString("id");
                            user.setUid(id);
                            Log.d(TAG, "facebook id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String first_name = object.getString("first_name");
                            user.setFirstName(first_name);
                            Log.d(TAG, "facebook first_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String last_name = object.getString("last_name");
                            user.setLastName(last_name);
                            Log.d(TAG, "facebook last_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String email = object.getString("email");
                            user.setEmail(email);
                            Log.d(TAG, "facebook email");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            URL profile_picture = new URL("https://graph.facebook.com/" + user.getUid() + "/picture?width=200&height=200");
                            String profilePicture = profile_picture.toString();
                            user.setProfilePicture(profilePicture);
                            Log.d(TAG, "facebook profilePicture");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        writeNewUser(user.getUid(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfilePicture(), bFacebookData);

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

            }

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
                    return bundle;
                }
                catch(JSONException e) {
                    Log.d(TAG,"Error parsing JSON");
                }
                return null;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent in = new Intent(LoginActivity.this, HomePage.class);
            String uid = mAuth.getCurrentUser().getUid();
            in.putExtra("uid", uid);
            startActivity(in);
        } else {
            // No user is signed in
            Log.d(TAG, "User is not signed in or is null");
        }
        super.onStart();
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
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void writeNewUser(final String uid, String firstName, String lastName, String email, String profilePicture, final Bundle facebookData) {
        final User user = new User(firstName, lastName, email, profilePicture);
        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)) {
                } else {
                    mDatabase.child(uid).setValue(user);
                    Intent intent = new Intent(LoginActivity.this, HomePage.class);
                    intent.putExtras(facebookData);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
