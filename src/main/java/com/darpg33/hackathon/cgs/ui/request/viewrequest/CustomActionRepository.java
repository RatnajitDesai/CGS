package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class CustomActionRepository {

    private static final String TAG = "CustomActionRepository";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;

    public CustomActionRepository() {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
    }

    MutableLiveData<HashMap<String, HashMap<String, Object>>> uploadAttachments(final Action action) {
        final MutableLiveData<HashMap<String, HashMap<String, Object>>> attachmentLiveData = new MutableLiveData<>();


        final HashMap<String, HashMap<String, Object>> attachmentMap = new HashMap<>();
        if (action.getAttachments().size() > 0) {
            Log.d(TAG, "uploadAttachments: total attachments :" + action.getAttachments().size());
            for (final Attachment attachment : action.getAttachments()) {

                Log.d(TAG, "uploadAttachments: attachmentpath :" + attachment.getAttachmentPath());
                if (!attachment.getAttachmentType().equals("location")) {
                    mStorageReference = mStorage.getReference("Requests/" + action.getAction_request_id() + "/action_attachments/" + attachment.getAttachment_name());

                    UploadTask uploadTask = mStorageReference.putFile(attachment.getAttachmentUri());

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            if (taskSnapshot != null) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();

                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Log.d(TAG, "onComplete: documents uploaded....." + uri.toString());
                                        HashMap<String, Object> attachmentDetails = new HashMap<>();
                                        attachmentDetails.put("name", attachment.getAttachment_name());
                                        attachmentDetails.put("type", attachment.getAttachmentType());
                                        attachmentDetails.put("path", Objects.requireNonNull(uri).toString());
                                        attachmentDetails.put("timestamp", attachment.getTimestamp());
                                        attachmentMap.put("attachment" + attachmentMap.size(), attachmentDetails);

                                        if (action.getAttachments().size() == attachmentMap.size()) {
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

                    if (action.getAttachments().size() == attachmentMap.size()) {
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


    MutableLiveData<Action> addNote(final Action action, final HashMap<String, HashMap<String, Object>> attachmentMap) {

        final MutableLiveData<Action> data = new MutableLiveData<>();


        db.collection(Fields.DBC_USERS)
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            String firstname = documentSnapshot.getString(Fields.DB_USER_FIRSTNAME);
                            String lastname = documentSnapshot.getString(Fields.DB_USER_LASTNAME);
                            String username = firstname + " " + lastname;
                            String user_email = documentSnapshot.getString(Fields.DB_USER_EMAIL_ID);
                            final String user_type = documentSnapshot.getString(Fields.DB_USER_USER_TYPE);

                            action.setUsername(username);
                            action.setUser_id(mAuth.getCurrentUser().getUid());
                            action.setEmail_id(user_email);

                            final HashMap<String, Object> actionHashmap = new HashMap<>();
                            actionHashmap.put(Fields.DB_GR_ACTION_PERFORMED, action.getAction_performed());
                            actionHashmap.put(Fields.DB_GR_ACTION_INFO, action.getAction_info());
                            actionHashmap.put(Fields.DB_GR_ACTION_DESCRIPTION, action.getAction_description());
                            actionHashmap.put(Fields.DB_GR_ACTION_USERNAME, action.getUsername());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_TYPE, action.getUser_type());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_EMAIL, action.getEmail_id());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_ID, mAuth.getCurrentUser().getUid());
                            actionHashmap.put(Fields.DB_GR_ACTION_TIMESTAMP, new Timestamp(new Date()));
                            actionHashmap.put(Fields.DB_GR_ACTION_ATTACHMENTS, attachmentMap);

                            db.collection(Fields.DBC_REQUESTS)
                                    .document(action.getAction_request_id())
                                    .collection(Fields.DBC_REQ_ACTIONS)
                                    .document().set(actionHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    if (user_type.equals(Fields.USER_TYPE_DEP_WORKER)) {
                                        final String user_dept = documentSnapshot.getString(Fields.DB_USER_DEPARTMENT);
                                        db.collection(Fields.DBC_DEPARTMENTS)
                                                .document(user_dept)
                                                .collection(Fields.DBC_USERS)
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .collection(Fields.DBC_REQUESTS)
                                                .document(action.getAction_request_id())
                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot ds) {

                                                if (ds.exists()) {
                                                    String gr_status = ds.getString(Fields.DB_GR_STATUS);
                                                    if (gr_status.equals(Fields.GR_STATUS_PENDING)) {
                                                        db.collection(Fields.DBC_DEPARTMENTS)
                                                                .document(user_dept)
                                                                .collection(Fields.DBC_USERS)
                                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .collection(Fields.DBC_REQUESTS)
                                                                .document(action.getAction_request_id())
                                                                .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_IN_PROCESS).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                data.setValue(action);
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });

                                    } else {
                                        data.setValue(action);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    data.setValue(null);

                                }
                            });
                        }
                    }

                });
        return data;
    }

    MutableLiveData<Action> assignRequest(final Action action, final String assignTo, final String priority, final HashMap<String, HashMap<String, Object>> attachmentsHashmap) {

        final MutableLiveData<Action> data = new MutableLiveData<>();

        db.collection(Fields.DBC_USERS)
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            String firstname = documentSnapshot.getString(Fields.DB_USER_FIRSTNAME);
                            String lastname = documentSnapshot.getString(Fields.DB_USER_LASTNAME);
                            final String username = firstname + " " + lastname;
                            String user_email = documentSnapshot.getString(Fields.DB_USER_EMAIL_ID);
                            final String user_dept = documentSnapshot.getString(Fields.DB_USER_DEPARTMENT);

                            action.setUsername(username);
                            action.setUser_id(mAuth.getCurrentUser().getUid());
                            action.setEmail_id(user_email);

                            final HashMap<String, Object> actionHashmap = new HashMap<>();
                            actionHashmap.put(Fields.DB_GR_ACTION_PERFORMED, action.getAction_performed());
                            actionHashmap.put(Fields.DB_GR_ACTION_INFO, action.getAction_info());
                            actionHashmap.put(Fields.DB_GR_ACTION_DESCRIPTION, assignTo + " : " + action.getAction_description());
                            actionHashmap.put(Fields.DB_GR_ACTION_USERNAME, action.getUsername());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_TYPE, action.getUser_type());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_EMAIL, action.getEmail_id());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_ID, mAuth.getCurrentUser().getUid());
                            actionHashmap.put(Fields.DB_GR_ACTION_TIMESTAMP, new Timestamp(new Date()));
                            actionHashmap.put(Fields.DB_GR_ACTION_ATTACHMENTS, attachmentsHashmap);

                            // get global request database data
                            db.collection(Fields.DBC_REQUESTS)
                                    .document(action.getAction_request_id())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot ds) {
                                    if (ds.exists()) {
                                        final HashMap<String, Object> grievanceMap = new HashMap<>();
                                        grievanceMap.put(Fields.DB_GR_REQUEST_ID, ds.getString(Fields.DB_GR_REQUEST_ID));
                                        grievanceMap.put(Fields.DB_GR_USER_ID, ds.getString(Fields.DB_GR_USER_ID));
                                        grievanceMap.put(Fields.DB_GR_TITLE, ds.getString(Fields.DB_GR_TITLE));
                                        grievanceMap.put(Fields.DB_GR_CATEGORY, ds.getString(Fields.DB_GR_CATEGORY));
                                        grievanceMap.put(Fields.DB_GR_DESCRIPTION, ds.getString(Fields.DB_GR_DESCRIPTION));
                                        grievanceMap.put(Fields.DB_GR_TIMESTAMP, ds.getTimestamp(Fields.DB_GR_TIMESTAMP));
                                        grievanceMap.put(Fields.DB_GR_REQUESTED_BY, ds.getString(Fields.DB_GR_REQUESTED_BY));
                                        grievanceMap.put(Fields.DB_GR_HANDLED_BY, assignTo);
                                        grievanceMap.put(Fields.DB_GR_STATUS, Fields.GR_STATUS_PENDING);


                                        //userID to change status of grievance in MyRequests database of user with @userID
                                        final String userID = ds.getString(Fields.DB_GR_USER_ID);

                                        //add action details to global requests database
                                        final DocumentReference reference = db.collection(Fields.DBC_REQUESTS)
                                                .document(action.getAction_request_id())
                                                .collection(Fields.DBC_REQ_ACTIONS)
                                                .document();

                                        reference.set(actionHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                if (action.getAction_priority() == null) {
                                                    Log.d(TAG, "onSuccess: " + action.getAction_priority() + " " + action.getAction_description());

                                                    //set global grievance status to "In Process"
                                                    db.collection(Fields.DBC_REQUESTS)
                                                            .document(action.getAction_request_id())
                                                            .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_IN_PROCESS).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            Log.d(TAG, "onSuccess: global grievance status to In Process");

                                                            //set mediators bucket grievance status to "In Process"
                                                            db.collection(Fields.DBC_MEDIATORS)
                                                                    .document(Fields.DBC_REQUESTS)
                                                                    .collection(Fields.DBC_MED_ALL_REQUESTS)
                                                                    .document(action.getAction_request_id())
                                                                    .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_IN_PROCESS).addOnSuccessListener(
                                                                    new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Log.d(TAG, "onSuccess: mediators bucket grievance status to \"In Process\"");

                                                                            //set myRequests grievance status to "In Process"
                                                                            db.collection(Fields.DBC_USERS)
                                                                                    .document(userID)
                                                                                    .collection(Fields.DBC_USERS_MY_REQUESTS)
                                                                                    .document(action.getAction_request_id())
                                                                                    .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_IN_PROCESS)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                            Log.d(TAG, "onSuccess: set myRequests grievance status to In Process");
                                                                                            //Add global grievance data to @assignTo departments bucket with status as "Pending"
                                                                                            db.collection(Fields.DBC_DEPARTMENTS)
                                                                                                    .document(assignTo)
                                                                                                    .collection(Fields.DBC_REQUESTS)
                                                                                                    .document(action.getAction_request_id())
                                                                                                    .set(grievanceMap)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                            Log.d(TAG, "onSuccess: grievance saved to department.");

                                                                                                            //set global request handled by field to @user_dept
                                                                                                            db.collection(Fields.DBC_REQUESTS)
                                                                                                                    .document(action.getAction_request_id())
                                                                                                                    .update(Fields.DB_GR_HANDLED_BY, assignTo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Void aVoid) {

                                                                                                                    data.setValue(action);

                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                            ).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.e(TAG, "onFailure: " + e.getMessage());
                                                                }
                                                            });
                                                        }
                                                    })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.e(TAG, "onFailure: " + e.getMessage());
                                                                }
                                                            });

                                                } else {

                                                    //To be done

                                                    db.collection(Fields.DBC_DEPARTMENTS)
                                                            .document("Department of Water")
                                                            .collection(Fields.DBC_USERS)
                                                            .document("Anup Patil")
                                                            .collection("Requests")
                                                            .document(action.getAction_request_id())
                                                            .set(grievanceMap)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    data.setValue(action);
                                                                }

                                                            });
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                data.setValue(null);
                                            }
                                        });
                                    }
                                }
                            });


                        }
                    }
                });

        return data;

    }

    MutableLiveData<Action> completeRequest(final Action action, final HashMap<String, HashMap<String, Object>> attachmentsHashmap) {

        final MutableLiveData<Action> data = new MutableLiveData<>();

        db.collection(Fields.DBC_USERS)
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            String firstname = documentSnapshot.getString(Fields.DB_USER_FIRSTNAME);
                            String lastname = documentSnapshot.getString(Fields.DB_USER_LASTNAME);
                            String username = firstname + " " + lastname;
                            String user_email = documentSnapshot.getString(Fields.DB_USER_EMAIL_ID);
                            final String user_type = documentSnapshot.getString(Fields.DB_USER_USER_TYPE);

                            action.setUsername(username);
                            action.setUser_id(mAuth.getCurrentUser().getUid());
                            action.setEmail_id(user_email);

                            final HashMap<String, Object> actionHashmap = new HashMap<>();
                            actionHashmap.put(Fields.DB_GR_ACTION_PERFORMED, action.getAction_performed());
                            actionHashmap.put(Fields.DB_GR_ACTION_INFO, action.getAction_info());
                            actionHashmap.put(Fields.DB_GR_ACTION_DESCRIPTION, action.getAction_description());
                            actionHashmap.put(Fields.DB_GR_ACTION_USERNAME, action.getUsername());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_TYPE, action.getUser_type());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_EMAIL, action.getEmail_id());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_ID, mAuth.getCurrentUser().getUid());
                            actionHashmap.put(Fields.DB_GR_ACTION_TIMESTAMP, new Timestamp(new Date()));
                            actionHashmap.put(Fields.DB_GR_ACTION_ATTACHMENTS, attachmentsHashmap);

                            // get global request database data
                            db.collection(Fields.DBC_REQUESTS)
                                    .document(action.getAction_request_id())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot ds) {
                                    if (ds.exists()) {
                                        final HashMap<String, Object> grievanceMap = new HashMap<>();

                                        grievanceMap.put(Fields.DB_GR_REQUEST_ID, ds.getString(Fields.DB_GR_REQUEST_ID));
                                        grievanceMap.put(Fields.DB_GR_USER_ID, ds.getString(Fields.DB_GR_USER_ID));
                                        grievanceMap.put(Fields.DB_GR_TITLE, ds.getString(Fields.DB_GR_TITLE));
                                        grievanceMap.put(Fields.DB_GR_CATEGORY, ds.getString(Fields.DB_GR_CATEGORY));
                                        grievanceMap.put(Fields.DB_GR_DESCRIPTION, ds.getString(Fields.DB_GR_DESCRIPTION));
                                        grievanceMap.put(Fields.DB_GR_TIMESTAMP, ds.getTimestamp(Fields.DB_GR_TIMESTAMP));
                                        grievanceMap.put(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED);

                                        //userID to change status of grievance in MyRequests database of user with @userID
                                        final String userID = ds.getString(Fields.DB_GR_USER_ID);

                                        //add action details to global requests database
                                        final DocumentReference reference = db.collection(Fields.DBC_REQUESTS)
                                                .document(action.getAction_request_id())
                                                .collection(Fields.DBC_REQ_ACTIONS)
                                                .document();

                                        reference.set(actionHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Log.d(TAG, "onSuccess: " + action.getAction_priority() + " " + action.getAction_description());

                                                //set global grievance status to "Resolved"
                                                db.collection(Fields.DBC_REQUESTS)
                                                        .document(action.getAction_request_id())
                                                        .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Log.d(TAG, "onSuccess: global grievance status to Resolved.");

                                                        //set mediators bucket grievance status to "Resolved"
                                                        db.collection(Fields.DBC_MEDIATORS)
                                                                .document(Fields.DBC_REQUESTS)
                                                                .collection(Fields.DBC_MED_ALL_REQUESTS)
                                                                .document(action.getAction_request_id())
                                                                .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED).addOnSuccessListener(
                                                                new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d(TAG, "onSuccess: mediators bucket grievance status to rejected and resolved.");


                                                                        //set myRequests grievance status to "Resolved"
                                                                        db.collection(Fields.DBC_USERS)
                                                                                .document(userID)
                                                                                .collection(Fields.DBC_USERS_MY_REQUESTS)
                                                                                .document(action.getAction_request_id())
                                                                                .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {

                                                                                        final String user_dept = documentSnapshot.getString(Fields.DB_USER_DEPARTMENT);
                                                                                        Log.d(TAG, "onSuccess: " + user_dept);

                                                                                        if (user_dept != null && (user_type.equals(Fields.USER_TYPE_DEP_INCHARGE))) {
                                                                                            db.collection(Fields.DBC_DEPARTMENTS)
                                                                                                    .document(user_dept)
                                                                                                    .collection(Fields.DBC_REQUESTS)
                                                                                                    .document(action.getAction_request_id())
                                                                                                    .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {

                                                                                                            Log.d(TAG, "onSuccess: resolved by department : " + user_dept);
                                                                                                            data.setValue(action);

                                                                                                        }
                                                                                                    });
                                                                                        } else if (user_dept != null && user_type.equals(Fields.USER_TYPE_DEP_WORKER)) {
                                                                                            db.collection(Fields.DBC_DEPARTMENTS)
                                                                                                    .document(user_dept)
                                                                                                    .collection(Fields.DBC_REQUESTS)
                                                                                                    .document(action.getAction_request_id())
                                                                                                    .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {

                                                                                                            db.collection(Fields.DBC_DEPARTMENTS)
                                                                                                                    .document(user_dept)
                                                                                                                    .collection(Fields.DBC_USERS)
                                                                                                                    .document(action.getUser_id())
                                                                                                                    .collection(Fields.DBC_REQUESTS)
                                                                                                                    .document(action.getAction_request_id())
                                                                                                                    .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                                            Log.d(TAG, "onSuccess: resolved by worker of department : " + user_dept);
                                                                                                                            data.setValue(action);
                                                                                                                        }
                                                                                                                    });

                                                                                                        }
                                                                                                    });
                                                                                        } else {
                                                                                            data.setValue(action);
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                        ).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e(TAG, "onFailure: " + e.getMessage());
                                                            }
                                                        });
                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e(TAG, "onFailure: " + e.getMessage());
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                data.setValue(null);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });


        return data;
    }

    MutableLiveData<Action> rejectRequest(final Action action, final HashMap<String, HashMap<String, Object>> attachmentsHashmap) {

        final MutableLiveData<Action> data = new MutableLiveData<>();


        db.collection(Fields.DBC_USERS)
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            String firstname = documentSnapshot.getString(Fields.DB_USER_FIRSTNAME);
                            String lastname = documentSnapshot.getString(Fields.DB_USER_LASTNAME);
                            String username = firstname + " " + lastname;
                            String user_email = documentSnapshot.getString(Fields.DB_USER_EMAIL_ID);
                            final String user_type = documentSnapshot.getString(Fields.DB_USER_USER_TYPE);

                            action.setUsername(username);
                            action.setUser_id(mAuth.getCurrentUser().getUid());
                            action.setEmail_id(user_email);

                            final HashMap<String, Object> actionHashmap = new HashMap<>();
                            actionHashmap.put(Fields.DB_GR_ACTION_PERFORMED, action.getAction_performed());
                            actionHashmap.put(Fields.DB_GR_ACTION_INFO, action.getAction_info());
                            actionHashmap.put(Fields.DB_GR_ACTION_DESCRIPTION, action.getAction_description());
                            actionHashmap.put(Fields.DB_GR_ACTION_USERNAME, action.getUsername());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_TYPE, action.getUser_type());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_EMAIL, action.getEmail_id());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_ID, mAuth.getCurrentUser().getUid());
                            actionHashmap.put(Fields.DB_GR_ACTION_TIMESTAMP, new Timestamp(new Date()));
                            actionHashmap.put(Fields.DB_GR_ACTION_ATTACHMENTS, attachmentsHashmap);

                            // get global request database data
                            db.collection(Fields.DBC_REQUESTS)
                                    .document(action.getAction_request_id())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot ds) {
                                    if (ds.exists()) {


                                        //userID to change status of grievance in MyRequests database of user with @userID
                                        final String userID = ds.getString(Fields.DB_GR_USER_ID);

                                        //add action details to global requests database
                                        final DocumentReference reference = db.collection(Fields.DBC_REQUESTS)
                                                .document(action.getAction_request_id())
                                                .collection(Fields.DBC_REQ_ACTIONS)
                                                .document();

                                        reference.set(actionHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Log.d(TAG, "onSuccess: " + action.getAction_priority() + " " + action.getAction_description());

                                                //set global grievance status to "Resolved"
                                                db.collection(Fields.DBC_REQUESTS)
                                                        .document(action.getAction_request_id())
                                                        .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Log.d(TAG, "onSuccess: global grievance status to rejected and resolved.");

                                                        //set mediators bucket grievance status to "Resolved"
                                                        db.collection(Fields.DBC_MEDIATORS)
                                                                .document(Fields.DBC_REQUESTS)
                                                                .collection(Fields.DBC_MED_ALL_REQUESTS)
                                                                .document(action.getAction_request_id())
                                                                .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED).addOnSuccessListener(
                                                                new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d(TAG, "onSuccess: mediators bucket grievance status to rejected and resolved.");


                                                                        //set myRequests grievance status to "Resolved"
                                                                        db.collection(Fields.DBC_USERS)
                                                                                .document(userID)
                                                                                .collection(Fields.DBC_USERS_MY_REQUESTS)
                                                                                .document(action.getAction_request_id())
                                                                                .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {

                                                                                        final String user_dept = documentSnapshot.getString(Fields.DB_USER_DEPARTMENT);
                                                                                        Log.d(TAG, "onSuccess: " + user_dept);

                                                                                        if (user_dept != null && (user_type.equals(Fields.USER_TYPE_DEP_INCHARGE))) {
                                                                                            db.collection(Fields.DBC_DEPARTMENTS)
                                                                                                    .document(user_dept)
                                                                                                    .collection(Fields.DBC_REQUESTS)
                                                                                                    .document(action.getAction_request_id())
                                                                                                    .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {

                                                                                                            Log.d(TAG, "onSuccess: rejected by department : " + user_dept);
                                                                                                            data.setValue(action);

                                                                                                        }
                                                                                                    });
                                                                                        } else if (user_dept != null && user_type.equals(Fields.USER_TYPE_DEP_WORKER)) {
                                                                                            db.collection(Fields.DBC_DEPARTMENTS)
                                                                                                    .document(user_dept)
                                                                                                    .collection(Fields.DBC_REQUESTS)
                                                                                                    .document(action.getAction_request_id())
                                                                                                    .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {

                                                                                                            db.collection(Fields.DBC_DEPARTMENTS)
                                                                                                                    .document(user_dept)
                                                                                                                    .collection(Fields.DBC_USERS)
                                                                                                                    .document(action.getUser_id())
                                                                                                                    .collection(Fields.DBC_REQUESTS)
                                                                                                                    .document(action.getAction_request_id())
                                                                                                                    .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(Void aVoid) {

                                                                                                                            data.setValue(action);

                                                                                                                        }
                                                                                                                    });
                                                                                                        }
                                                                                                    });
                                                                                        } else {
                                                                                            data.setValue(action);
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                        ).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e(TAG, "onFailure: " + e.getMessage());
                                                            }
                                                        });
                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e(TAG, "onFailure: " + e.getMessage());
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                data.setValue(null);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });


        return data;

    }

    /**
     * forward requests from one department to other department
     * @param action    - action data
     * @param forwardTo - department to which request is supposed to be forwarded
     * @param attachmentsHashmap
     * @return
     */
    MutableLiveData<Action> forwardRequest(final Action action, final String forwardTo, final HashMap<String, HashMap<String, Object>> attachmentsHashmap) {

        final MutableLiveData<Action> data = new MutableLiveData<>();

        db.collection(Fields.DBC_USERS)
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            final String firstname = documentSnapshot.getString(Fields.DB_USER_FIRSTNAME);
                            String lastname = documentSnapshot.getString(Fields.DB_USER_LASTNAME);
                            final String username = firstname + " " + lastname;
                            String user_email = documentSnapshot.getString(Fields.DB_USER_EMAIL_ID);
                            final String user_type = documentSnapshot.getString(Fields.DB_USER_USER_TYPE);
                            final String user_id = documentSnapshot.getString(Fields.DB_USER_USER_ID);

                            final String user_dept = documentSnapshot.getString(Fields.DB_USER_DEPARTMENT);

                            action.setUsername(username);
                            action.setUser_id(mAuth.getCurrentUser().getUid());
                            action.setEmail_id(user_email);

                            final HashMap<String, Object> actionHashmap = new HashMap<>();
                            actionHashmap.put(Fields.DB_GR_ACTION_PERFORMED, action.getAction_performed());
                            actionHashmap.put(Fields.DB_GR_ACTION_INFO, action.getAction_info());
                            actionHashmap.put(Fields.DB_GR_ACTION_DESCRIPTION, forwardTo + " :" + action.getAction_description());
                            actionHashmap.put(Fields.DB_GR_ACTION_USERNAME, action.getUsername());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_TYPE, action.getUser_type());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_EMAIL, action.getEmail_id());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_ID, mAuth.getCurrentUser().getUid());
                            actionHashmap.put(Fields.DB_GR_ACTION_TIMESTAMP, new Timestamp(new Date()));
                            actionHashmap.put(Fields.DB_GR_ACTION_ATTACHMENTS, attachmentsHashmap);

                            //add action details to global requests database
                            final DocumentReference reference = db.collection(Fields.DBC_REQUESTS)
                                    .document(action.getAction_request_id())
                                    .collection(Fields.DBC_REQ_ACTIONS)
                                    .document();

                            reference.set(actionHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    // get global request database values
                                    db.collection(Fields.DBC_REQUESTS)
                                            .document(action.getAction_request_id())
                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot ds) {
                                            if (ds.exists()) {

                                                final HashMap<String, Object> grievanceMap = new HashMap<>();
                                                grievanceMap.put(Fields.DB_GR_REQUEST_ID, ds.getString(Fields.DB_GR_REQUEST_ID));
                                                grievanceMap.put(Fields.DB_GR_USER_ID, ds.getString(Fields.DB_GR_USER_ID));
                                                grievanceMap.put(Fields.DB_GR_TITLE, ds.getString(Fields.DB_GR_TITLE));
                                                grievanceMap.put(Fields.DB_GR_CATEGORY, ds.getString(Fields.DB_GR_CATEGORY));
                                                grievanceMap.put(Fields.DB_GR_DESCRIPTION, ds.getString(Fields.DB_GR_DESCRIPTION));
                                                grievanceMap.put(Fields.DB_GR_TIMESTAMP, ds.getTimestamp(Fields.DB_GR_TIMESTAMP));
                                                grievanceMap.put(Fields.DB_GR_REQUESTED_BY, ds.getString(Fields.DB_GR_REQUESTED_BY));
                                                grievanceMap.put(Fields.DB_GR_HANDLED_BY, ds.getString(forwardTo));
                                                grievanceMap.put(Fields.DB_GR_STATUS, Fields.GR_STATUS_PENDING);

                                                //copy global grievance data to @forwardTo department's requests database
                                                db.collection(Fields.DBC_DEPARTMENTS)
                                                        .document(forwardTo)
                                                        .collection(Fields.DBC_REQUESTS)
                                                        .document(action.getAction_request_id())
                                                        .set(grievanceMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        //set global request handled by field to @forwardTo
                                                        db.collection(Fields.DBC_REQUESTS)
                                                                .document(action.getAction_request_id())
                                                                .update(Fields.DB_GR_HANDLED_BY, forwardTo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                switch (user_type) {

                                                                    case Fields.USER_TYPE_DEP_WORKER: {

                                                                        //set current department workers request status as resolved.
                                                                        db.collection(Fields.DBC_DEPARTMENTS)
                                                                                .document(user_dept)
                                                                                .collection(Fields.DBC_REQUESTS)
                                                                                .document(action.getAction_request_id())
                                                                                .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                db.collection(Fields.DBC_DEPARTMENTS)
                                                                                        .document(user_dept)
                                                                                        .collection(Fields.DBC_USERS)
                                                                                        .document(action.getUser_id())
                                                                                        .collection(Fields.DBC_REQUESTS)
                                                                                        .document(action.getAction_request_id())
                                                                                        .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                data.setValue(action);

                                                                                            }
                                                                                        });
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.e(TAG, "onFailure: " + e.getMessage());
                                                                                data.setValue(null);
                                                                            }
                                                                        });


                                                                        break;
                                                                    }
                                                                    case Fields.USER_TYPE_DEP_INCHARGE: {

                                                                        //set current departments request status as resolved.
                                                                        db.collection(Fields.DBC_DEPARTMENTS)
                                                                                .document(user_dept)
                                                                                .collection(Fields.DBC_REQUESTS)
                                                                                .document(action.getAction_request_id())
                                                                                .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                data.setValue(action);

                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.e(TAG, "onFailure: " + e.getMessage());
                                                                                data.setValue(null);
                                                                            }
                                                                        });

                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        });

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e(TAG, "onFailure: " + e.getMessage());
                                                        data.setValue(null);
                                                    }
                                                });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: " + e.getMessage());
                                            data.setValue(null);
                                        }
                                    });

                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
                data.setValue(null);
            }
        });

        return data;
    }


    MutableLiveData<Action> assignToWorkerRequest(final Action action, final User assignTo, final HashMap<String, HashMap<String, Object>> attachmentsHashmap) {

        final MutableLiveData<Action> data = new MutableLiveData<>();


        db.collection(Fields.DBC_USERS)
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            String firstname = documentSnapshot.getString(Fields.DB_USER_FIRSTNAME);
                            String lastname = documentSnapshot.getString(Fields.DB_USER_LASTNAME);
                            String username = firstname + " " + lastname;
                            String user_email = documentSnapshot.getString(Fields.DB_USER_EMAIL_ID);
                            final String user_type = documentSnapshot.getString(Fields.DB_USER_USER_TYPE);
                            final String user_id = documentSnapshot.getString(Fields.DB_USER_USER_ID);

                            final String user_dept = documentSnapshot.getString(Fields.DB_USER_DEPARTMENT);

                            action.setUsername(username);
                            action.setUser_id(mAuth.getCurrentUser().getUid());
                            action.setEmail_id(user_email);

                            final HashMap<String, Object> actionHashmap = new HashMap<>();
                            actionHashmap.put(Fields.DB_GR_ACTION_PERFORMED, action.getAction_performed());
                            actionHashmap.put(Fields.DB_GR_ACTION_INFO, action.getAction_info());
                            actionHashmap.put(Fields.DB_GR_ACTION_DESCRIPTION, action.getAction_description());
                            actionHashmap.put(Fields.DB_GR_ACTION_USERNAME, action.getUsername());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_TYPE, action.getUser_type());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_EMAIL, action.getEmail_id());
                            actionHashmap.put(Fields.DB_GR_ACTION_USER_ID, mAuth.getCurrentUser().getUid());
                            actionHashmap.put(Fields.DB_GR_ACTION_TIMESTAMP, new Timestamp(new Date()));
                            actionHashmap.put(Fields.DB_GR_ACTION_ATTACHMENTS, attachmentsHashmap);

                            // get global request database data
                            db.collection(Fields.DBC_REQUESTS)
                                    .document(action.getAction_request_id())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot ds) {
                                    if (ds.exists()) {

                                        final HashMap<String, Object> grievanceMap = new HashMap<>();

                                        grievanceMap.put(Fields.DB_GR_REQUEST_ID, ds.getString(Fields.DB_GR_REQUEST_ID));
                                        grievanceMap.put(Fields.DB_GR_USER_ID, ds.getString(Fields.DB_GR_USER_ID));
                                        grievanceMap.put(Fields.DB_GR_TITLE, ds.getString(Fields.DB_GR_TITLE));
                                        grievanceMap.put(Fields.DB_GR_CATEGORY, ds.getString(Fields.DB_GR_CATEGORY));
                                        grievanceMap.put(Fields.DB_GR_DESCRIPTION, ds.getString(Fields.DB_GR_DESCRIPTION));
                                        grievanceMap.put(Fields.DB_GR_TIMESTAMP, ds.getTimestamp(Fields.DB_GR_TIMESTAMP));
                                        grievanceMap.put(Fields.DB_GR_STATUS, Fields.GR_STATUS_PENDING);
                                        grievanceMap.put(Fields.DB_GR_PRIORITY, action.getAction_priority());

                                        //add action details to global requests database
                                        final DocumentReference reference = db.collection(Fields.DBC_REQUESTS)
                                                .document(action.getAction_request_id())
                                                .collection(Fields.DBC_REQ_ACTIONS)
                                                .document();

                                        reference.set(actionHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "onSuccess: " + action.getAction_priority() + " " + action.getAction_description());

                                                if (user_dept != null) {
                                                    db.collection(Fields.DBC_DEPARTMENTS)
                                                            .document(user_dept)
                                                            .collection(Fields.DBC_USERS)
                                                            .document(assignTo.getUser_id())
                                                            .collection(Fields.DBC_REQUESTS)
                                                            .document(action.getAction_request_id())
                                                            .set(grievanceMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            db.collection(Fields.DBC_DEPARTMENTS)
                                                                    .document(user_dept)
                                                                    .collection(Fields.DBC_REQUESTS)
                                                                    .document(action.getAction_request_id())
                                                                    .update(Fields.DB_GR_STATUS, Fields.GR_STATUS_IN_PROCESS).addOnSuccessListener(
                                                                    new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            data.setValue(action);

                                                                        }
                                                                    }
                                                            ).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                    data.setValue(null);

                                                                }
                                                            });
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            data.setValue(null);

                                                        }
                                                    });
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: " + e.getMessage());
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
        return data;
    }
}
