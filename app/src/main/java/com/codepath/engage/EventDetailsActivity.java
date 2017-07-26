package com.codepath.engage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.Event;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EventDetailsActivity extends AppCompatActivity{

    @Nullable
    @BindView(R.id.ivPicture) ImageView ivPicture;
    @BindView(R.id.tvHost) TextView tvHost;
    @BindView(R.id.tvEventInfo) TextView tvEventInfo;
    @BindView(R.id.tvEventDescription) TextView tvEventDescription;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.tvEventName) TextView tvEventName;
    @BindView(R.id.btnMap) Button btnMap;

    Event event;
    UserEvents currentUpdate;
    String uid;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;
    DatabaseReference savedEvents;
    List<String> events ;
    boolean savedEventsCreated;
    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);
        events = new ArrayList<String>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("users");
        savedEvents = firebaseDatabase.getReference();
        uid = Profile.getCurrentProfile().getId();

        currentUpdate = Parcels.unwrap(getIntent().getParcelableExtra("current"));

        if (currentUpdate == null) {
            event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));
            if (ivPicture != null) {
                Glide.with(this)
                        .load(event.ivEventImage)
                        .centerCrop()
                        .into(ivPicture);
            }
            tvEventName.setText(event.tvEventName);
            tvEventDescription.setText(event.tvDescription);
            tvEventInfo.setText(event.tvEventInfo);
            tvHost.setText(event.organizer.name);
            btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EventDetailsActivity.this, MapActivity.class);
                    intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
                    intent.putExtra("whichProfile", "You are ");
                    startActivity(intent);
                }
            });
        }else {
            if (ivPicture != null) {
                Glide.with(this)
                        .load(currentUpdate.eventImage)
                        .centerCrop()
                        .into(ivPicture);
            }
            tvEventName.setText(currentUpdate.eventName);
            tvEventDescription.setText(currentUpdate.eventDescription);
            tvEventInfo.setText(currentUpdate.eventInfo);
            tvHost.setText(currentUpdate.eventHost);
            btnSave.setVisibility(View.GONE);
            btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + event.venue.getSimpleAddress());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            });

        }

        Properties properties = new Properties();

        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder( new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                @Override
                public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {

                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Prompt the user to enter a query term.
            String queryTerm = event.getOrganizerName()
                    ;
            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            String apiKey = "AIzaSyBf2t2bQwGPJqMF9O0XyQZgz8q77e-Kgz8";
            search.setKey(apiKey);
            search.setQ(queryTerm);
            Log.i("INFO",queryTerm);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                final SearchResult singleVideo = searchResultList.get(0);
                final ResourceId rId = singleVideo.getId();
               Log.i("INFO",singleVideo.getSnippet().getTitle());
                YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                        getFragmentManager().findFragmentById(R.id.youtubeFragment);
                youtubeFragment.initialize("YOUR API KEY",
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                YouTubePlayer youTubePlayer, boolean b) {
                                // do any work here to cue video, play video, etc.
                                youTubePlayer.cueVideo(rId.getVideoId());
                            }
                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public void saveEvent(View view){
        saveNewEvent(uid, event.getEventId(), event.getTvEventName(), event.organizer.getName(), event.getTimeStart(),event.getVenue().getAddress()+" "+event.getVenue().getCity() +" " +event.getVenue().getCountry(), event.ivEventImage, event.tvDescription);
    }

    public void saveNewEvent(String uid, String eventId, String eventName, String eventHost,String eventTime, String eventAddress, String eventImage, String eventDescription) {
        savedEventsCreated = false;
        events.clear();

        UserEvents info = new UserEvents(eventName, eventHost, eventTime, eventAddress, eventId, eventImage, eventDescription);
        savedEvents.child("savedEvents").child(eventId).setValue(info);
        users.child(uid).child("eventsList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    events.add(postSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < events.size(); i++)
            Log.i("Info", events.get(i));
            events.add(eventId);
            users.child(uid).child("eventsList").setValue(events, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                    } else {
                        System.out.println("Data saved successfully.");
                    }
                }
            });
        }

}
