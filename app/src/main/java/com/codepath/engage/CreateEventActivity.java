package com.codepath.engage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codepath.engage.models.CreatedEvents;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener  {
    @BindView(R.id.eDate) TextView eDate;
    @BindView(R.id.eTime) TextView eTime;
    @BindView(R.id.eName) EditText eName;
    @BindView(R.id.submitEvent) Button submitEvent;
    @BindView(R.id.eLocation) EditText eLocation;
    @BindView(R.id.eDescription) EditText eDescription;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference createdEvents;
    DatabaseReference rootRef;
    final int REQUEST_CODE = 1;
    StorageReference storage;
    ProgressDialog progress;
    private static final String REQUIRED_MSG = "required";
    private boolean selectedTime = false;
    private boolean selectedDate = false;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private boolean finishedAddingEvent = false;
    String uid;
    long createdEventID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
        //Setting up Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        uid = Profile.getCurrentProfile().getId();

        submitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySubmitEvent();
            }
        });
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        storage = FirebaseStorage.getInstance().getReference();
        progress = new ProgressDialog(this);
        if ((mHour != 0) && (mMinute) != 0) {
            eTime.setText(mHour + ":" + mMinute);
        }
        if ((mYear != 0) && (mMonth != 0) && (mDay != 0)) {
            eDate.setText(mYear + "/" + mMonth + "/" + mDay);
        }
    }
    public void showTimePickerDialog(View v){
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"TimePicker");
    }
    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        eDate.setText(mMonth + "/" + mDay + "/" + mYear);
        selectedDate =true;
    }

    public void verifySubmitEvent(){
        if(hasText(eName) && hasText(eLocation) && hasText(eDescription)&&selectedTime&&selectedDate) {
            final Intent i = new Intent(CreateEventActivity.this,ViewEvents.class);
            ArrayList<String> createdEventInfo = new ArrayList<>();
            String eventName = eName.getText().toString();
            String eventDescription = eDescription.getText().toString();
            String eventLocation = eLocation.getText().toString();
            createdEventInfo.add(eventName);
            createdEventInfo.add(eventLocation);
            createdEventInfo.add(eventDescription);
            createdEventInfo.add(Profile.getCurrentProfile().getId());
            finishedAddingEvent = false;
            i.putExtra("createdEventInfo", Parcels.wrap(createdEventInfo));
            final CreatedEvents createdEvent = new CreatedEvents(eventName,eventLocation,eventDescription,String.valueOf(mHour),String.valueOf(mMinute),String.valueOf(mDay),String.valueOf(mMonth),String.valueOf(mYear), Profile.getCurrentProfile().getId());
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild("CreatedEvents")) {
                        // run some code
                        rootRef.child("CreatedEvents").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                createdEventID = dataSnapshot.getChildrenCount() + 1;
                                Log.i("Info", String.valueOf(createdEventID));
                                rootRef.child("CreatedEvents").child(String.valueOf(createdEventID)).setValue(createdEvent);
                                finishedAddingEvent =true;
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(CreateEventActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(CreateEventActivity.this);
                                }
                                builder.setTitle("Delete entry")
                                        .setMessage("Want to upload an image?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                pick();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                                finish();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        rootRef.child("CreatedEvents").child("1").setValue(createdEvent);
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(CreateEventActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(CreateEventActivity.this);
                        }
                        builder.setTitle("Delete entry")
                                .setMessage("Want to upload an image?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        pick();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                        finish();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    
    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {
        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        String half;
        if (mHour > 12){
            half = "PM";
            mHour = mHour % 12;
        }
        else{
            half = "AM";
        }
        if (mMinute < 10){
            eTime.setText(mHour + ":0" + mMinute);
        }
        else{
            eTime.setText(mHour + ":" + mMinute + " " + half);
        }
        selectedTime = true;

    }
    //User TO upload image
    public void pick(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            progress.setMessage("uploading");
            progress.show();
            Uri uri = data.getData();
            if(createdEventID == 0)
                createdEventID =1;
            StorageReference path = storage.child("photos").child(String.valueOf(createdEventID));
            path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Successfully upladed image",Toast.LENGTH_LONG).show();
                    finish();

                }
            });
        }
    }
}
