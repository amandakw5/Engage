package com.codepath.engage;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.Event;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by emilyz on 7/31/17.
 */

public class EventDetailsFragment extends Fragment {

    @BindView(R.id.ivPicture) ImageView ivPicture;
    @BindView(R.id.tvHost) TextView tvHost;
    @BindView(R.id.tvEventInfo) TextView tvEventInfo;
    @BindView(R.id.tvEventDescription) TextView tvEventDescription;
    @BindView(R.id.tvEventName) TextView tvEventName;
    @BindView(R.id.fabSave)
    Button fabSave;
    YouTubePlayerSupportFragment youtubeFragment;

    UserEvents currentUpdate;
    Event event;
    Boolean isUserCreated;

    String uid;
    List<String> events;
    String queryTerm;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;
    DatabaseReference savedEvents;

    boolean savedEventsCreated;

    //Define a global variable that identifies the name of a file thatcontains the developer's API key.
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    public static EventDetailsFragment newInstance(UserEvents currentUpdate, Event event, boolean isUserCreated){
        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
        Bundle args = new Bundle();

        if (event != null) {
            args.putParcelable("event", event);
        }
        if (currentUpdate != null){
            args.putParcelable("currentUpdate", currentUpdate);
        }

        args.putBoolean("isUserCreated", isUserCreated);

        eventDetailsFragment.setArguments(args);

        return eventDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = new ArrayList<String>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("users");
        savedEvents = firebaseDatabase.getReference();
        uid = Profile.getCurrentProfile().getId();

        Bundle bundle = getArguments();
        try {
            event = bundle.getParcelable("event");
        } catch (Exception e){
            e.printStackTrace();
            event = null;
        }

        try{
            currentUpdate = bundle.getParcelable("currentUpdate");
        } catch (Exception e){
            e.printStackTrace();
            currentUpdate = null;
        }

        try {
            isUserCreated = bundle.getBoolean("isUserCreated");
        } catch (Exception e){
            e.printStackTrace();
            isUserCreated = false;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this,view);

        if (event != null){
            if (ivPicture != null) {
                if (isUserCreated){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photos").child(String.valueOf(event.getEventId()));
                    Glide.with(this)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .error(R.drawable.image_not_found)
                            .into(ivPicture);
                } else if (!event.ivEventImage.equals("null")) {
                    Glide.with(this)
                            .load(event.ivEventImage)
                            .centerCrop()
                            .into(ivPicture);
                } else if (event.ivEventImage.equals("null")) {
                    Glide.with(this)
                            .load(R.drawable.image_not_found)
                            .centerCrop()
                            .into(ivPicture);
                }
            }
            tvEventName.setText(event.tvEventName);
            tvEventDescription.setText(event.tvDescription);
            tvEventInfo.setText(event.tvEventInfo);
            tvHost.setText(event.organizerName);

            queryTerm = event.getOrganizerName();

            fabSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (event.ivEventImage == null) {
                        saveNewEvent(uid, event.getEventId(), event.getTvEventName(), event.organizer.getName(), event.getTimeStart(), event.getVenue().getAddress() + " " + event.getVenue().getCity() + " " + event.getVenue().getCountry(), "null", event.tvDescription);

                    } else {
                        saveNewEvent(uid, event.getEventId(), event.getTvEventName(), event.organizer.getName(), event.getTimeStart(), event.getVenue().getAddress() + " " + event.getVenue().getCity() + " " + event.getVenue().getCountry(), event.ivEventImage, event.tvDescription);
                    }
                    Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (currentUpdate != null) {
            if (ivPicture != null) {
                if (!currentUpdate.eventImage.equals("null")) {
                    Glide.with(this)
                            .load(currentUpdate.eventImage)
                            .centerCrop()
                            .into(ivPicture);
                } else {
                    Glide.with(this)
                            .load(R.drawable.image_not_found)
                            .centerCrop()
                            .into(ivPicture);
                }
            }
            tvEventName.setText(currentUpdate.eventName);
            tvEventDescription.setText(currentUpdate.eventDescription);
            tvEventInfo.setText(currentUpdate.eventTime);
            tvHost.setText(currentUpdate.eventHost);
            fabSave.setVisibility(View.GONE);

        }

        try {
            // This object is used to make YouTube Data API requests. The last argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override the interface and provide a no-op function.

            //Define a global instance of a Youtube object, which will be used to make YouTube Data API requests.
            YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                @Override
                public void initialize(com.google.api.client.http.HttpRequest request) throws IOException { } }).setApplicationName("youtube-cmdline-search-sample").build();

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            String apiKey = "AIzaSyBf2t2bQwGPJqMF9O0XyQZgz8q77e-Kgz8";
            search.setKey(apiKey);
            search.setQ(queryTerm);
            Log.i("INFO", queryTerm);

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
            if (searchResultList != null && searchResultList.size() != 0) {
                final SearchResult singleVideo = searchResultList.get(0);
                final ResourceId rId = singleVideo.getId();
                Log.i("INFO",singleVideo.getSnippet().getTitle());
                youtubeFragment = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtubeFragment);
                youtubeFragment.initialize(apiKey,
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
            } else {
                youtubeFragment = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtubeFragment);
                youtubeFragment.getView().setVisibility(View.INVISIBLE);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return view;
    }

    public void saveNewEvent(final String uid, final String eventId, String eventName, String eventHost, String eventTime, String eventAddress, String eventImage, String eventDescription) {
        savedEventsCreated = false;
        events.clear();
        Date date = new Date();
        Log.i("indo", date.toString());
        UserEvents info = new UserEvents(eventName, eventHost, eventTime, eventAddress, eventId, eventImage, eventDescription, null, null);
        savedEvents.child("savedEvents").child(eventId).setValue(info);
        savedEvents.child("savedEvents").child(eventId).child("date").child(uid).setValue(date);
        users.child(uid).child("eventsList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    events.add(postSnapshot.getValue().toString());
                }
                if(!events.contains(eventId))
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
