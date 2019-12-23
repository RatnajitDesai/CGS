package com.darpg33.hackathon.cgs.Model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Grievance {

    private String request_id, user_id, title, category, description, priority;
    private ArrayList<Attachment> attachment;
    private String status;
    private Timestamp timestamp;

    public Grievance() {

    }

    public Grievance(String request_id, String user_id, String title, String category, String description, String priority, ArrayList<Attachment> attachment, String status, Timestamp timestamp) {
        this.request_id = request_id;
        this.user_id = user_id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.attachment = attachment;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ArrayList<Attachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(ArrayList<Attachment> attachment) {
        this.attachment = attachment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Grievance{" +
                "request_id='" + request_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", attachment=" + attachment +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
