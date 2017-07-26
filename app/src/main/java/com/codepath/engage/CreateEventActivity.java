package com.codepath.engage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateEventActivity extends AppCompatActivity implements CalenderFragment.CalenderFragmentListener, TimeFragment.TimeFragmentListener {
    @BindView(R.id.eDate) TextView eDate;
    @BindView(R.id.eTime) TextView eTime;
    @BindView(R.id.eName) EditText eName;
    @BindView(R.id.submitEvent) Button submitEvent;
    @BindView(R.id.eLocation) EditText eLocation;
    @BindView(R.id.eDescription) EditText eDescription;
    private static final String REQUIRED_MSG = "required";
    private boolean selectedTime = false;
    private boolean selectedDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
        submitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySubmitEvent();
            }
        });
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCal();
            }
        });
        eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
            }
        });
    }
    private void showCal() {
        android.app.FragmentManager fm = getFragmentManager();
        CalenderFragment editNameDialogFragment = CalenderFragment.newInstance("Some Title");
        editNameDialogFragment.setListener(this);
        editNameDialogFragment.show(fm, "date_picker");
    }
    private void showTime() {
        android.app.FragmentManager fm = getFragmentManager();
        TimeFragment editNameDialogFragment = TimeFragment.newInstance("Some Title");
        editNameDialogFragment.setListener(this);
        editNameDialogFragment.show(fm, "time_picker");
    }
    @Override
    public void onFinishEditDialog() {

    }
    public void verifySubmitEvent(){
        if(hasText(eName) && hasText(eLocation) && hasText(eDescription)&&selectedTime&&selectedDate) {
            String eventName = eName.getText().toString();
            String eventDescription = eName.getText().toString();
            String eventsLocation = eName.getText().toString();
        }
        else{
            return;
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

}
