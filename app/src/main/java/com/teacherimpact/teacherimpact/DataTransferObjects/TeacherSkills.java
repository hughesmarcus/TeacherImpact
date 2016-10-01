package com.teacherimpact.teacherimpact.DataTransferObjects;

import android.os.Parcel;
import android.os.Parcelable;

public class TeacherSkills implements Parcelable{
    private String[] skills;
    private int[] values;

    public TeacherSkills(){}

    public String[] getSkills() {
        return skills;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        //MUST BE WRITTEN IN THE SAME WAY THEY'LL BE READ!!
        //Base Write
        out.writeStringArray(getSkills());
        out.writeIntArray(getValues());
    }

    //This is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TeacherSkills> CREATOR = new Parcelable.Creator<TeacherSkills>() {
        public TeacherSkills createFromParcel(Parcel in) {
            return new TeacherSkills(in);
        }

        public TeacherSkills[] newArray(int size) {
            return new TeacherSkills[size];
        }
    };

    private TeacherSkills(Parcel in) {
        //MUST BE READ IN THE SAME WAY THEY'RE WRITTEN!!
        //Base Read
        skills= in.createStringArray();
        values = in.createIntArray();
    }
}
