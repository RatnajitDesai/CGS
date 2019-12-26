package com.darpg33.hackathon.cgs.Model;

import com.google.firebase.Timestamp;

public class Notification {
    private String title, body, requestId;
    private Timestamp timestamp;

    public Notification(String title, String body, Timestamp timestamp, String requestId) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
        this.requestId = requestId;
    }

    public Notification() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
