package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class CustomActionRepository {

    private static final String TAG = "CustomActionRepository";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    MutableLiveData<Action> addNote(final Action action) {

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
                            String username = firstname + " " + lastname;
                            String user_email = documentSnapshot.getString(Fields.DB_USER_EMAIL_ID);
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

                            db.collection(Fields.DBC_REQUESTS)
                                    .document(action.getAction_request_id())
                                    .collection(Fields.DBC_REQ_ACTIONS)
                                    .document().set(actionHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    data.setValue(action);

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


    MutableLiveData<Action> assignRequest(final Action action, final String assignTo, final String priority) {

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
                            String username = firstname + " " + lastname;
                            String user_email = documentSnapshot.getString(Fields.DB_USER_EMAIL_ID);

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


                            // get global request database data
                            db.collection("Requests")
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
                                                                                            //Add global grievance data to @assignTo departments bucket with status as pending
                                                                                            db.collection(Fields.DBC_DEPARTMENTS)
                                                                                                    .document(assignTo)
                                                                                                    .collection(Fields.DBC_REQUESTS)
                                                                                                    .document(action.getAction_request_id())
                                                                                                    .set(grievanceMap)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                            Log.d(TAG, "onSuccess: grievance saved to department.");
                                                                                                            data.setValue(action);
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                            ).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.e(TAG, "onFailure: "+e.getMessage() );
                                                                }
                                                            });
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.e(TAG, "onFailure: "+e.getMessage());
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


    MutableLiveData<Action> completeRequest(Action action) {

        MutableLiveData<Action> data = new MutableLiveData<>();


        return data;
    }

    MutableLiveData<Action> rejectRequest(Action action) {

        MutableLiveData<Action> data = new MutableLiveData<>();


        return data;

    }

    MutableLiveData<Action> forwardRequest(Action action, String forwardTo) {

        MutableLiveData<Action> data = new MutableLiveData<>();


        return data;
    }
}
