package com.teacherimpact.teacherimpact.DataTransferObjects;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    //Fields used by everyone - Teachers / Students / StudentParents.
    private String userid;
    private String firstname;
    private String lastname;
    private String birthdate;
    private String gender;
    private String zip;
    private String county;
    private String city;
    private String state;
    private String email;
    private String password;
    private String provider;
    private String role;
    private String interests;
    private boolean favorited;
    @SuppressWarnings("unused")
    private Userid [] favoriteUsers;
    private  String imageFile;

    //Fields used by Teachers
    private String teachingphilosophy;
    private String certifications;
    private String awards;
    private String background;
    private String experience;

    //Fields used by Students
    private String grade;
    private String lstrengths;
    private String lweaknesses;

    //Fields used by StudentParent
    private String biography;

    public User(){}

    public User(String name, String password, String email) {
        this.firstname = name;
        this.password = password;
        this.email = email;
    }

    public String searchAll(){
        return firstname + lastname + birthdate + zip + county + city + state + email + role;
    }

    public String searchMin(){
        return firstname + lastname + birthdate + county + email;
    }

    public String getImageFile() {
        return imageFile;
    }

    @SuppressWarnings("unused")
    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProvider() {
        return provider;
    }

    @SuppressWarnings("unused")
    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    @SuppressWarnings("unused")
    public boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public String getTeachingphilosophy() {
        return teachingphilosophy;
    }

    public void setTeachingphilosophy(String teachingphilosophy) {
        this.teachingphilosophy = teachingphilosophy;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLstrengths() {
        return lstrengths;
    }

    public void setLstrengths(String lstrengths) {
        this.lstrengths = lstrengths;
    }

    public String getLweaknesses() {
        return lweaknesses;
    }

    public void setLweaknesses(String lweaknesses) {
        this.lweaknesses = lweaknesses;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        //MUST BE WRITTEN IN THE SAME WAY THEY'LL BE READ!!
        //Base Write
        out.writeString(getUserid());
        out.writeString(getFirstname());
        out.writeString(getLastname());
        out.writeString(getBirthdate());
        out.writeString(getGender());
        out.writeString(getZip());
        out.writeString(getCounty());
        out.writeString(getCity());
        out.writeString(getState());
        out.writeString(getEmail());
        out.writeString(getPassword());
        out.writeString(getProvider());
        out.writeString(getRole());
        out.writeString(getInterests());
        out.writeString(getImageFile());

        //Teacher Write
        out.writeString(getTeachingphilosophy());
        out.writeString(getCertifications());
        out.writeString(getAwards());
        out.writeString(getBackground());
        out.writeString(getExperience());

        //Student Write
        out.writeString(getGrade());
        out.writeString(getLstrengths());
        out.writeString(getLweaknesses());

        //Parent Write
        out.writeString(getBiography());
    }

    //This is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        //MUST BE READ IN THE SAME WAY THEY'RE WRITTEN!!
        //Base Read
        userid = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        birthdate = in.readString();
        gender = in.readString();
        zip = in.readString();
        county = in.readString();
        city = in.readString();
        state = in.readString();
        email = in.readString();
        password = in.readString();
        provider = in.readString();
        role = in.readString();
        interests = in.readString();
        imageFile= in.readString();
        teachingphilosophy = in.readString();
        certifications = in.readString();
        awards = in.readString();
        background = in.readString();
        experience = in.readString();

        //Student Read
        grade = in.readString();
        lstrengths = in.readString();
        lweaknesses = in.readString();

        //Parent Read
        biography = in.readString();
    }
}
