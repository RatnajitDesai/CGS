package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

class ViewGrievanceRepository {

    private static final String TAG = "ViewGrievanceRepository";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    MutableLiveData<Grievance> getGrievanceData(final String requestID) {

        final MutableLiveData<Grievance> liveData = new MutableLiveData<>();

        db.collection(Fields.DBC_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot ds) {

                if (ds.exists()) {

                    String user_type = ds.getString(Fields.DB_USER_USER_TYPE);

                    if (user_type != null) {
                        switch (user_type) {

                            case Fields.USER_TYPE_CITIZEN:
                            case Fields.USER_TYPE_MEDIATOR: {
                                db.collection(Fields.DBC_REQUESTS)
                                        .document(requestID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                        if (e != null) {
                                            return;
                                        }

                                        Grievance grievance = new Grievance();
                                        grievance.setRequest_id(requestID);
                                        grievance.setTitle(documentSnapshot.getString(Fields.DB_GR_TITLE));
                                        grievance.setStatus(documentSnapshot.getString(Fields.DB_GR_STATUS));
                                        grievance.setUser_id(documentSnapshot.getString(Fields.DB_GR_USER_ID));
                                        grievance.setDescription(documentSnapshot.getString(Fields.DB_GR_DESCRIPTION));
                                        grievance.setTimestamp(documentSnapshot.getTimestamp(Fields.DB_GR_TIMESTAMP));
                                        grievance.setCategory(documentSnapshot.getString(Fields.DB_GR_CATEGORY));
                                        grievance.setRequestedBy(documentSnapshot.getString(Fields.DB_GR_REQUESTED_BY));
                                        grievance.setHandledBy(documentSnapshot.getString(Fields.DB_GR_HANDLED_BY));
                                        grievance.setPrivacy(documentSnapshot.getString(Fields.DB_GR_PRIVACY));
                                        ArrayList<String> upvotes = (ArrayList<String>) documentSnapshot.get(Fields.DB_GR_UPVOTES);
                                        grievance.setUpvotes(upvotes);

                                        liveData.setValue(grievance);

                                    }
                                });
                                break;
                            }

                            case Fields.USER_TYPE_DEP_INCHARGE: {

                                final String user_dept = ds.getString(Fields.DB_USER_DEPARTMENT);
                                db.collection(Fields.DBC_DEPARTMENTS)
                                        .document(user_dept)
                                        .collection(Fields.DBC_REQUESTS)
                                        .document(requestID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                        if (e != null) {
                                            return;
                                        }

                                        final Grievance grievance = new Grievance();
                                        grievance.setRequest_id(requestID);
                                        grievance.setTitle(documentSnapshot.getString(Fields.DB_GR_TITLE));
                                        grievance.setStatus(documentSnapshot.getString(Fields.DB_GR_STATUS));
                                        grievance.setUser_id(documentSnapshot.getString(Fields.DB_GR_USER_ID));
                                        grievance.setDescription(documentSnapshot.getString(Fields.DB_GR_DESCRIPTION));
                                        grievance.setTimestamp(documentSnapshot.getTimestamp(Fields.DB_GR_TIMESTAMP));
                                        grievance.setCategory(documentSnapshot.getString(Fields.DB_GR_CATEGORY));

                                        db.collection(Fields.DBC_REQUESTS)
                                                .document(requestID)
                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                String user_id = documentSnapshot.getString(Fields.DB_GR_USER_ID);
                                                grievance.setHandledBy(documentSnapshot.getString(Fields.DB_GR_HANDLED_BY));
                                                grievance.setPrivacy(documentSnapshot.getString(Fields.DB_GR_PRIVACY));

                                                ArrayList<String> upvotes = (ArrayList<String>) documentSnapshot.get(Fields.DB_GR_UPVOTES);
                                                grievance.setUpvotes(upvotes);
                                                db.collection(Fields.DBC_USERS)
                                                        .document(user_id)
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot ds) {

                                                        String firstname = ds.getString(Fields.DB_USER_FIRSTNAME);
                                                        String lastname = ds.getString(Fields.DB_USER_LASTNAME);
                                                        String username = firstname + " " + lastname;

                                                        grievance.setRequestedBy(username);
                                                        liveData.setValue(grievance);

                                                    }
                                                });

                                            }
                                        });


                                    }
                                });
                                break;
                            }

                            case Fields.USER_TYPE_DEP_WORKER: {

                                String user_dept = ds.getString(Fields.DB_USER_DEPARTMENT);
                                db.collection(Fields.DBC_DEPARTMENTS)
                                        .document(user_dept)
                                        .collection(Fields.DBC_USERS)
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .collection(Fields.DBC_REQUESTS)
                                        .document(requestID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                        if (e != null) {
                                            return;
                                        }

                                        final Grievance grievance = new Grievance();
                                        grievance.setRequest_id(requestID);
                                        grievance.setTitle(documentSnapshot.getString(Fields.DB_GR_TITLE));
                                        grievance.setStatus(documentSnapshot.getString(Fields.DB_GR_STATUS));
                                        grievance.setUser_id(documentSnapshot.getString(Fields.DB_GR_USER_ID));
                                        grievance.setDescription(documentSnapshot.getString(Fields.DB_GR_DESCRIPTION));
                                        grievance.setTimestamp(documentSnapshot.getTimestamp(Fields.DB_GR_TIMESTAMP));
                                        grievance.setCategory(documentSnapshot.getString(Fields.DB_GR_CATEGORY));


                                        db.collection(Fields.DBC_REQUESTS)
                                                .document(requestID)
                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                String user_id = documentSnapshot.getString(Fields.DB_GR_USER_ID);

                                                grievance.setHandledBy(documentSnapshot.getString(Fields.DB_GR_HANDLED_BY));
                                                grievance.setPrivacy(documentSnapshot.getString(Fields.DB_GR_PRIVACY));


                                                ArrayList<String> upvotes = (ArrayList<String>) documentSnapshot.get(Fields.DB_GR_UPVOTES);
                                                grievance.setUpvotes(upvotes);

                                                db.collection(Fields.DBC_USERS)
                                                        .document(user_id)
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot ds) {

                                                        String firstname = ds.getString(Fields.DB_USER_FIRSTNAME);
                                                        String lastname = ds.getString(Fields.DB_USER_LASTNAME);
                                                        String username = firstname + " " + lastname;

                                                        grievance.setRequestedBy(username);
                                                        liveData.setValue(grievance);

                                                    }
                                                });

                                            }
                                        });


                                    }
                                });
                                break;
                            }

                        }
                    }

                }

            }
        });

        return liveData;
    }


    /**
     * Get attachments of request
     * @param requestID - request id for which attachments are to be retrieved
     * @return
     */
    LiveData<ArrayList<Attachment>> getGrievanceAttachments(final String requestID) {
        final MutableLiveData<ArrayList<Attachment>> liveData = new MutableLiveData<>();

        db.collection(Fields.DBC_REQUESTS)
                .document(requestID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                ArrayList<Attachment> attachmentArrayList = new ArrayList<>();
                if (documentSnapshot.exists())
                {
                    HashMap<String,HashMap<String,Object>> grievanceAttachments = (HashMap<String, HashMap<String, Object>>) documentSnapshot.get("grievance_attachments");

                    if (grievanceAttachments!= null)
                    {
                        for(HashMap.Entry<String,HashMap<String,Object>> attachments:grievanceAttachments.entrySet())
                        {
                            HashMap<String, Object> attachment= attachments.getValue();

                            Attachment attachment1= new Attachment();
                            for (HashMap.Entry<String,Object> attachmentDetails:attachment.entrySet())
                            {

                                if (attachment.keySet().contains("geo_point"))
                                {
                                    switch (attachmentDetails.getKey())
                                    {
                                        case "address":
                                        {
                                            attachment1.setLocation_address((String)attachmentDetails.getValue());
                                            break;
                                        }
                                        case "geo_point":
                                        {
                                            attachment1.setGeoPoint((GeoPoint)attachmentDetails.getValue());
                                            break;
                                        }
                                        case "timestamp":
                                        {
                                            attachment1.setTimestamp((Timestamp) attachmentDetails.getValue());
                                            break;
                                        }
                                        case "type": {
                                            attachment1.setAttachmentType((String) attachmentDetails.getValue());
                                            break;
                                        }
                                    }

                                }
                                else {
                                    switch (attachmentDetails.getKey())
                                    {
                                        case "name":
                                        {
                                            attachment1.setAttachment_name((String)attachmentDetails.getValue());
                                            break;
                                        }
                                        case "path":
                                        {
                                            attachment1.setAttachmentPath((String)attachmentDetails.getValue());
                                            break;
                                        }
                                        case "timestamp":
                                        {
                                            attachment1.setTimestamp((Timestamp) attachmentDetails.getValue());
                                            break;
                                        }
                                        case "type":
                                        {
                                            attachment1.setAttachmentType((String)attachmentDetails.getValue());
                                            break;
                                        }
                                    }
                                }
                            }
                            attachmentArrayList.add(attachment1);
                        }
                        if (attachmentArrayList.size()==grievanceAttachments.size())
                        {
                            liveData.setValue(attachmentArrayList);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e(TAG, "onFailure: Exception :"+e.getMessage() );
                liveData.setValue(null);

            }
        });

        return liveData;


    }

    MutableLiveData<ArrayList<Action>> getGrievanceActions(String requestID) {

        final MutableLiveData<ArrayList<Action>> data = new MutableLiveData<>();

        db.collection("Requests")
                .document(requestID)
                .collection("Actions")
                .orderBy("action_timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        Log.d(TAG, "onSuccess: ");

                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        ArrayList<Action> actions = new ArrayList<>();

                        if (queryDocumentSnapshots != null) {

                            if (!queryDocumentSnapshots.isEmpty()) {

                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {

                                    Action action = new Action();

                                    action.setAction_performed(snapshot.getString("action_performed"));
                                    action.setAction_description(snapshot.getString("action_description"));
                                    action.setEmail_id(snapshot.getString("action_user_email"));
                                    action.setUser_id(snapshot.getString("action_user_id"));
                                    action.setTimestamp(snapshot.getTimestamp("action_timestamp"));
                                    action.setUser_type(snapshot.getString("action_user_type"));
                                    action.setAction_info(snapshot.getString("action_info"));
                                    action.setUsername(snapshot.getString("action_username"));

                                    actions.add(action);
                                }

                                data.setValue(actions);
                            }
                            else {
                                data.setValue(new ArrayList<Action>());
                            }
                        }

                    }
                });

        return data;

    }

    MutableLiveData<String> getUserType() {

        final MutableLiveData<String> data = new MutableLiveData<>();

        db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot.exists())
                        {
                            String userType = documentSnapshot.getString("user_type");

                            data.setValue(userType);
                        }
                        else {
                            data.setValue(null);
                        }
                    }
                });

        return data;
    }
}
