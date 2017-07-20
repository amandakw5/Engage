package com.codepath.engage;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Created by awestort on 7/20/17.
 */

public class TimeFragment extends DialogFragment implements View.OnClickListener {
    private TimeFragmentListener listener;
    Button submitTime;
    TimePicker chooseTime;

    @Override
    public void onClick(View v) {

    }
    public interface TimeFragmentListener {
        void onFinishEditDialog();
    }
    public static TimeFragment newInstance(String title){
        TimeFragment frag = new TimeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.time_picker, container);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view

        // Fetch arguments from bundle and set title
        // Show soft keyboard automatically and request focus to field
        // mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        submitTime = (Button) view.findViewById(R.id.submitTime);
        chooseTime = (TimePicker) view.findViewById(R.id.timePicker);
        submitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void setListener(TimeFragmentListener listener) {
        this.listener = listener;
    }
}

