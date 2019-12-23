package com.darpg33.hackathon.cgs.ui.request.newrequest;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

class NewGrievanceRepository {

    private static final String TAG = "NewGrievanceRepository";
    private FirebaseFirestore db;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private FirebaseAuth mFirebaseAuth;

    NewGrievanceRepository() {

        db = FirebaseFirestore.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }


    /**
     * Upload attachments to FireBase Storage
     *
     * @param grievance  - grievance with attachments to be uploaded to FireBase storage
     * @param request_id - new request ID provide by @getNewRequestId method
     * @return Hashmap of details of attachments uploaded to Storage
     */

    MutableLiveData<HashMap<String, HashMap<String, Object>>> uploadAttachments(final Grievance grievance, final String request_id) {
        final MutableLiveData<HashMap<String, HashMap<String, Object>>> attachmentLiveData = new MutableLiveData<>();
        grievance.setRequest_id(request_id);

        final HashMap<String, HashMap<String, Object>> attachmentMap = new HashMap<>();
        if (grievance.getAttachment().size() > 0) {
            Log.d(TAG, "uploadAttachments: total attachments :" + grievance.getAttachment().size());
            for (final Attachment attachment : grievance.getAttachment()) {
                Log.d(TAG, "uploadAttachments: attachmentpath :" + attachment.getAttachmentPath());
                if (!attachment.getAttachmentType().equals("location")) {
                    mStorageReference = mFirebaseStorage.getReference("Requests/" + request_id + "/attachments/" + attachment.getAttachment_name());

                    UploadTask uploadTask = mStorageReference.putFile(attachment.getAttachmentUri());


                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            if (taskSnapshot != null) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();

                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Log.d(TAG, "onComplete: document uploaded....." + uri.toString());
                                        HashMap<String, Object> attachmentDetails = new HashMap<>();
                                        attachmentDetails.put("name", attachment.getAttachment_name());
                                        attachmentDetails.put("type", attachment.getAttachmentType());
                                        attachmentDetails.put("path", Objects.requireNonNull(uri).toString());
                                        attachmentDetails.put("timestamp", attachment.getTimestamp());
                                        attachmentMap.put("attachment" + attachmentMap.size(), attachmentDetails);

                                        if (grievance.getAttachment().size() == attachmentMap.size()) {
                                            Log.d(TAG, "onComplete: all files uploaded.");
                                            attachmentLiveData.setValue(attachmentMap);
                                        }

                                    }
                                });
                            }
                        }
                    });

                } else {
                    HashMap<String, Object> attachmentDetails = new HashMap<>();
                    GeoPoint point = new GeoPoint(attachment.getAddress().getLatitude(), attachment.getAddress().getLongitude());
                    attachmentDetails.put("address", attachment.getAddress().getAddressLine(0));
                    attachmentDetails.put("geo_point", point);
                    attachmentDetails.put("timestamp", attachment.getTimestamp());
                    attachmentDetails.put("type", attachment.getAttachmentType());
                    attachmentMap.put("attachment" + attachmentMap.size(), attachmentDetails);

                    if (grievance.getAttachment().size() == attachmentMap.size()) {
                        Log.d(TAG, "onComplete: all files uploaded." + attachmentMap.toString());
                        attachmentLiveData.setValue(attachmentMap);
                    }

                }

            }

        } else {

            attachmentLiveData.setValue(attachmentMap);

        }

        return attachmentLiveData;
    }


    /**
     * @param grievance     - object with grievance details
     * @param attachmentMap - Hashmap of attachments added
     * @return Grievance submitted to database along with new request ID
     */


    MutableLiveData<Grievance> submitNewRequest(final Grievance grievance, final HashMap<String, HashMap<String, Object>> attachmentMap) {

        final MutableLiveData<Grievance> grievanceMutableLiveData = new MutableLiveData<>();


        grievance.setUser_id(mFirebaseAuth.getCurrentUser().getUid());

        db.collection(Fields.DBC_USERS)
                .document(grievance.getUser_id())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            String firstname = documentSnapshot.getString(Fields.DB_USER_FIRSTNAME);
                            String lastname = documentSnapshot.getString(Fields.DB_USER_LASTNAME);
                            String username = firstname + " " + lastname;
                            String user_email = documentSnapshot.getString(Fields.DB_USER_EMAIL_ID);

                            final HashMap<String, Object> grievanceMap = new HashMap<>();
                            grievanceMap.put(Fields.DB_GR_REQUEST_ID, grievance.getRequest_id());
                            grievanceMap.put(Fields.DB_GR_USER_ID, Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid());
                            grievanceMap.put(Fields.DB_GR_TITLE, grievance.getTitle());
                            grievanceMap.put(Fields.DB_GR_CATEGORY, grievance.getCategory());
                            grievanceMap.put(Fields.DB_GR_DESCRIPTION, grievance.getDescription());
                            grievanceMap.put(Fields.DB_GR_PRIVACY, grievance.getPrivacy());
                            grievanceMap.put(Fields.DB_GR_TIMESTAMP, new Timestamp(new Date()));
                            grievanceMap.put(Fields.DB_GR_STATUS, grievance.getStatus());
                            grievanceMap.put(Fields.DB_GR_ATTACHMENTS, attachmentMap);
                            grievanceMap.put(Fields.DB_GR_HANDLED_BY, Fields.USER_TYPE_MEDIATOR);  //initialize as 'mediator' department will be handling this request.
                            grievanceMap.put(Fields.DB_GR_REQUESTED_BY, username);

                            final Action action = new Action();
                            action.setAction_performed("SUBMIT");
                            action.setAction_description("Our executive will look into this as soon as possible.");
                            action.setUsername(username);
                            action.setUser_type(Fields.USER_TYPE_CITIZEN);
                            action.setEmail_id(user_email);

                            final HashMap<String, Object> actionHashmap = new HashMap<>();
                            actionHashmap.put(Fields.DB_GR_ACTION_PERFORMED, action.getAction_performed());
                            actionHashmap.put(Fields.DB_GR_ACTION_INFO, action.getAction_info());
                            actionHashmap.put(Fields.DB_GR_ACTION_DESCRIPTION, action.getAction_description());
                            actionHashmap.put(Fields.DB_GR_ACTION_USERNAME, action.getUsername());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_TYPE, action.getUser_type());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_EMAIL, action.getEmail_id());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_ID, mFirebaseAuth.getCurrentUser().getUid());
                            actionHashmap.put(Fields.DB_GR_ACTION_TIMESTAMP, new Timestamp(new Date()));

                            //adding request to requests  database
                            db.collection(Fields.DBC_REQUESTS)
                                    .document(grievance.getRequest_id())
                                    .set(grievanceMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //adding request to user's MyRequests database
                                    db.collection(Fields.DBC_USERS)
                                            .document(grievance.getUser_id())
                                            .collection(Fields.DBC_USERS_MY_REQUESTS)
                                            .document(grievance.getRequest_id())
                                            .set(grievanceMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            db.collection(Fields.DBC_MEDIATORS)
                                                    .document(Fields.DBC_REQUESTS)
                                                    .collection(Fields.DBC_MED_ALL_REQUESTS)
                                                    .document(grievance.getRequest_id())
                                                    .set(grievanceMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            db.collection(Fields.DBC_REQUESTS)
                                                                    .document(grievance.getRequest_id())
                                                                    .collection(Fields.DBC_REQ_ACTIONS)
                                                                    .document().set(actionHashmap)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {


                                                                            Log.d(TAG, "onSuccess: new grievance saved.");
                                                                            grievanceMutableLiveData.setValue(grievance);

                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d(TAG, "onFailure: Failed to load action.");
                                                                    grievanceMutableLiveData.setValue(null);
                                                                    mStorageReference.delete();
                                                                    db.collection(Fields.DBC_REQUESTS)
                                                                            .document(grievance.getRequest_id())
                                                                            .delete();
                                                                }
                                                            });
                                                        }
                                                    });

                                            //adding action to 'Requests/request_id' database
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Log.e(TAG, "onFailure: MyRequest" + e.getMessage());

                                            grievanceMutableLiveData.setValue(null);
                                            mStorageReference.delete();
                                            db.collection(Fields.DBC_REQUESTS)
                                                    .document(grievance.getRequest_id())
                                                    .delete();
                                        }
                                    });

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: Request " + e.getMessage());
                                            mStorageReference.delete();
                                            grievanceMutableLiveData.setValue(null);
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Unable to get user info" + e.getMessage());
            }
        });


        return grievanceMutableLiveData;
    }


    /**
     * Get new unique request id from database
     *
     * @return string - request Id
     */

    MutableLiveData<String> getNewRequestId() {

        Log.d(TAG, "getNewRequestId:");
        final MutableLiveData<String> request_id = new MutableLiveData<>();

        db.collection(Fields.DBC_REQUESTS)
                .orderBy(Fields.DB_GR_TIMESTAMP, Query.Direction.DESCENDING) //order by grievance timestamp descending
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.exists()) {
                            Log.d(TAG, document.getId());
                            int i = Integer.parseInt(document.getId());
                            i++;
                            request_id.setValue(String.valueOf(i));
                            Log.d(TAG, "onComplete: request id: " + i);
                            break;
                        } else {
                            request_id.setValue(String.valueOf(10000000));
                            Log.d(TAG, "onComplete: request id: " + 10000000);
                            break;
                        }
                    }
                }
            }
        });

        return request_id;
    }


}
