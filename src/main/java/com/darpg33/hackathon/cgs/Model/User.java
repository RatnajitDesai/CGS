package com.darpg33.hackathon.cgs.Model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class User implements Parcelable {

    private String user_id,
    user_type,
    first_name, last_name, gender,
    phone_number, email_id,
            address, pin_code, country, state, district, user_department;
    private Timestamp timestamp;
    private Boolean registered;


    public User() {
    }

    public User(String user_id, String user_type,
                String first_name, String last_name, String gender,
                String phone_number, String email_id, String address, String pin_code, String country, String state, String district,
                String user_department, Timestamp timestamp, Boolean registered) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.phone_number = phone_number;
        this.email_id = email_id;
        this.address = address;
        this.pin_code = pin_code;
        this.country = country;
        this.state = state;
        this.district = district;
        this.user_department = user_department;
        this.timestamp = timestamp;
        this.registered = registered;
    }

    protected User(Parcel in) {
        user_id = in.readString();
        user_type = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        gender = in.readString();
        phone_number = in.readString();
        email_id = in.readString();
        address = in.readString();
        pin_code = in.readString();
        country = in.readString();
        state = in.readString();
        district = in.readString();
        user_department = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        byte tmpRegistered = in.readByte();
        registered = tmpRegistered == 0 ? null : tmpRegistered == 1;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUser_department() {
        return user_department;
    }

    public void setUser_department(String user_department) {
        this.user_department = user_department;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getRegistered() {
        return registered;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", user_type='" + user_type + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email_id='" + email_id + '\'' +
                ", address='" + address + '\'' +
                ", pin_code='" + pin_code + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", district='" + district + '\'' +
                ", user_department='" + user_department + '\'' +
                ", timestamp=" + timestamp +
                ", registered=" + registered +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(user_id);
        parcel.writeString(user_type);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(gender);
        parcel.writeString(phone_number);
        parcel.writeString(email_id);
        parcel.writeString(address);
        parcel.writeString(pin_code);
        parcel.writeString(country);
        parcel.writeString(state);
        parcel.writeString(district);
        parcel.writeString(user_department);
        parcel.writeParcelable(timestamp, i);
        parcel.writeByte((byte) (registered == null ? 0 : registered ? 1 : 2));
    }
}



