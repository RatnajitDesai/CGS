package com.darpg33.hackathon.cgs.Model;

import android.location.Address;
import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class Attachment {

    private String attachmentPath,
            attachmentType, attachment_name, location_address;
    private Uri attachmentUri;
    private Timestamp timestamp;
    private Address address;
    private GeoPoint geoPoint;

    public Attachment(Address address,String attachmentType, Timestamp timestamp)
    {
        this.address = address;
        this.attachmentType = attachmentType;
        this.timestamp = timestamp;
    }

    public Attachment(String attachmentType, Timestamp timestamp, GeoPoint geoPoint, String location_address) {
        this.attachmentType = attachmentType;
        this.timestamp = timestamp;
        this.geoPoint = geoPoint;
        this.location_address = location_address;
    }

    public Attachment(String attachmentPath, String attachmentType, String attachment_name, Uri attachmentUri, Timestamp timestamp) {
        this.attachmentPath = attachmentPath;
        this.attachmentType = attachmentType;
        this.attachment_name = attachment_name;
        this.attachmentUri = attachmentUri;
        this.timestamp = timestamp;
    }

    public Attachment( String attachment_name,String attachmentType, Uri attachmentUri, Timestamp timestamp) {
        this.attachment_name = attachment_name;
        this.attachmentType = attachmentType;
        this.attachmentUri = attachmentUri;
        this.timestamp = timestamp;
    }

    public Attachment() {
    }

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Uri getAttachmentUri() {
        return attachmentUri;
    }

    public void setAttachmentUri(Uri attachmentUri) {
        this.attachmentUri = attachmentUri;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "attachmentPath='" + attachmentPath + '\'' +
                ", attachmentType='" + attachmentType + '\'' +
                ", attachment_name='" + attachment_name + '\'' +
                ", location_address='" + location_address + '\'' +
                ", attachmentUri=" + attachmentUri +
                ", timestamp=" + timestamp +
                ", address=" + address +
                ", geoPoint=" + geoPoint +
                '}';
    }
}
