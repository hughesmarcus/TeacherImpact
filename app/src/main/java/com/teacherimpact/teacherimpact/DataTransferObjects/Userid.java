package com.teacherimpact.teacherimpact.DataTransferObjects;

import android.os.Parcel;
import android.os.Parcelable;

public class Userid implements Parcelable {
    private String id;

    public Userid(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        //MUST BE WRITTEN IN THE SAME WAY THEY'LL BE READ!!
        //Base Write
        out.writeString(getId());
    }

    //This is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Userid> CREATOR = new Parcelable.Creator<Userid>() {
        public Userid createFromParcel(Parcel in) {
            return new Userid(in);
        }

        public Userid[] newArray(int size) {
            return new Userid[size];
        }
    };

    private Userid(Parcel in) {
        //MUST BE READ IN THE SAME WAY THEY'RE WRITTEN!!
        //Base Read
        id = in.readString();
    }
}
