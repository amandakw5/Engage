package com.codepath.engage.models;

import android.os.Parcelable;

import org.parceler.Parcel;

/**
 * Created by emilyz on 8/1/17.
 */

public class Null implements Parcelable {
    public String nothing;

    protected Null(android.os.Parcel in) {
        nothing = in.readString();
    }

    public static final Creator<Null> CREATOR = new Creator<Null>() {
        @Override
        public Null createFromParcel(android.os.Parcel in) {
            return new Null(in);
        }

        @Override
        public Null[] newArray(int size) {
            return new Null[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(nothing);

    }
}
