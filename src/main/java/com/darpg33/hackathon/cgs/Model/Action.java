package com.darpg33.hackathon.cgs.Model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Action implements Parcelable{

    private String user_id,user_type,username, email_id, action_performed,
            action_info, action_description, action_request_id,
            action_priority;
    private Timestamp timestamp;


    public Action(String user_id, String user_type,String username, String email_id, String action_performed,
                  String action_info, String action_description, String action_request_id, Timestamp timestamp, String action_priority) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.username = username;
        this.email_id = email_id;
        this.action_performed = action_performed;
        this.action_info = action_info;
        this.action_description = action_description;
        this.action_request_id = action_request_id;
        this.timestamp = timestamp;
        this.action_priority = action_priority;
    }




    public Action() {

    }

    protected Action(Parcel in) {
        user_id = in.readString();
        user_type = in.readString();
        username = in.readString();
        email_id = in.readString();
        action_performed = in.readString();
        action_info = in.readString();
        action_description = in.readString();
        action_request_id = in.readString();
        action_priority = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Action> CREATOR = new Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };

    public String getAction_priority() {
        return action_priority;
    }

    public void setAction_priority(String action_priority) {

        if (!this.user_type.equals("dep_incharge"))
        {
            this.action_priority = null;
        }
        else{
            this.action_priority = action_priority;
        }

    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getAction_performed() {
        return action_performed;
    }

    public void setAction_performed(String action_performed) {
        this.action_performed = action_performed;
    }

    public String getAction_description() {
        return action_description;
    }

    public void setAction_description(String action_description) {
        this.action_description = action_description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction_info() {
        switch (action_performed)
        {
            case "SUBMIT":
            {
                action_info = "Request submitted.";
                break;
            }
            case "ASSIGN":
            {
                action_info = "Request assigned to:";
                break;
            }
            case "FORWARD":
            {
                action_info = "Request forwarded to:";
                break;
            }
            case "REJECT":
            {
                action_info = "Request rejected.";
                break;
            }
            case "SAVE":
            {
                action_info = "Update:";
                break;
            }
            case "COMPLETE":
            {
                action_info = "Request resolved.";
                break;
            }
            case "FEEDBACK":
            {
                action_info = "Please provide your feedback.";
                break;
            }
        }
        return action_info;
    }

    public String getAction_request_id() {
        return action_request_id;
    }

    public void setAction_request_id(String action_request_id) {
        this.action_request_id = action_request_id;
    }

    public void setAction_info(String action_info) {
        this.action_info = action_info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(user_type);
        dest.writeString(username);
        dest.writeString(email_id);
        dest.writeString(action_performed);
        dest.writeString(action_info);
        dest.writeString(action_description);
        dest.writeParcelable(timestamp, flags);
    }

    @Override
    public String toString() {
        return "Action{" +
                "user_id='" + user_id + '\'' +
                ", user_type='" + user_type + '\'' +
                ", username='" + username + '\'' +
                ", email_id='" + email_id + '\'' +
                ", action_performed='" + action_performed + '\'' +
                ", action_info='" + action_info + '\'' +
                ", action_description='" + action_description + '\'' +
                ", action_request_id='" + action_request_id + '\'' +
                ", action_priority='" + action_priority + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}



