package com.teacherimpact.teacherimpact.DataTransferObjects;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"name "})
public class Comment implements Parcelable {
    private String comment;
    private String sender;
    private String name;
    private String viewflag;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public  Comment() {}



    @SuppressWarnings("unused")
    public Comment(String comment, String sender,String name , String viewflag) {
        this.comment = comment;
        this.sender = sender;
        this.name = name;
        this.viewflag = viewflag;

    }
    public String getViewflag() {
        return viewflag;
    }

    public void setViewflag(String viewflag) {
        this.viewflag = viewflag;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSender() {
        return sender;
    }

    @SuppressWarnings("unused")
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        //MUST BE WRITTEN IN THE SAME WAY THEY'LL BE READ!!
        //Base Write
        out.writeString(getComment());
        out.writeString(getSender());
        out.writeString(getName());
        out.writeString(getViewflag());
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private Comment(Parcel in) {
        //MUST BE READ IN THE SAME WAY THEY'RE WRITTEN!!
        //Base Read
        comment= in.readString();
        sender = in.readString();
        name = in.readString();
        viewflag = in.readString();
    }
}
