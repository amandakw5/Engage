package com.codepath.engage;

import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * Created by awestort on 7/20/17.
 */

public class CalenderFragment extends DialogFragment implements View.OnClickListener {
    private CalenderFragmentListener listener;
   Button submit;
    DatePicker chooseDate;
    @Override
    public void onClick(View v) {

    }
    public interface CalenderFragmentListener {
        void onFinishEditDialog();
    }
    public static CalenderFragment newInstance(String title){
        CalenderFragment frag = new CalenderFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.date_picker, container);
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
        submit = (Button) view.findViewById(R.id.submitDate);
        chooseDate = (DatePicker) view.findViewById(R.id.datePicker);
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }
    public void setListener(CalenderFragmentListener listener) {
        this.listener = listener;
    }
}
