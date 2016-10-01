package com.teacherimpact.teacherimpact.DataTransferObjects;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentContain implements Parcelable {
    private String [] comment;
    private String [] sender;
    private String [] name;
    private String [] viewFlag;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public CommentContain() {}

    public CommentContain(String[] comment, String[] sender, String[] name , String [] viewFlag) {
        this.comment = comment;
        this.sender = sender;
        this.name = name;
        this.viewFlag =viewFlag;
    }

    public String[] getViewFlag() {
        return viewFlag;
    }

    public void setViewFlag(String[] viewFlag) {
        this.viewFlag = viewFlag;
    }

    public String [] getComment() {
        return comment;
    }

    public void setComment(String [] comment) {
        this.comment = comment;
    }

    public String[] getSender() {
        return sender;
    }

    @SuppressWarnings("unused")
    public void setSender(String [] sender) {
        this.sender = sender;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        //MUST BE WRITTEN IN THE SAME WAY THEY'LL BE READ!!
        //Base Write
        out.writeStringArray(getComment());
        out.writeStringArray(getSender());
        out.writeStringArray(getName());
        out.writeStringArray(getViewFlag());
    }

    public static final Parcelable.Creator<CommentContain> CREATOR = new Parcelable.Creator<CommentContain>() {
        public CommentContain createFromParcel(Parcel in) {
            return new CommentContain(in);
        }

        public CommentContain[] newArray(int size) {
            return new CommentContain[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private CommentContain(Parcel in) {
        //MUST BE READ IN THE SAME WAY THEY'RE WRITTEN!!
        //Base Read
        comment= in.createStringArray();
        sender= in.createStringArray();
        name= in.createStringArray();
        viewFlag = in.createStringArray();
    }
}
