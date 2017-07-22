package com.codepath.engage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateEventActivity extends AppCompatActivity implements CalenderFragment.CalenderFragmentListener, TimeFragment.TimeFragmentListener {
    @BindView(R.id.eDate) TextView eDate;
    @BindView(R.id.eTime) TextView eTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
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
}
