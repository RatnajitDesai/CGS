package com.darpg33.hackathon.cgs.ui.request.requestlist;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

class RequestRepository {

    private static final String TAG = "RequestRepository";

    private FirebaseFirestore db;

    RequestRepository(){

        db  = FirebaseFirestore.getInstance();

    }



    MutableLiveData<ArrayList<Grievance>> getPendingRequests(String s) {


        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();

        switch (s)
        {
            case "mediator": {
                db.collection(Fields.DBC_MEDIATORS)
                        .document(Fields.DBC_REQUESTS)
                        .collection(Fields.DBC_MED_ALL_REQUESTS)
                        .whereEqualTo(Fields.DB_GR_STATUS, Fields.GR_STATUS_PENDING)
                        .orderBy(Fields.DB_GR_TIMESTAMP, Query.Direction.ASCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.e(TAG, "Listen failed." + e.getMessage());
                                    return;
                                }
                                ArrayList<Grievance> grievances = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : values) {
                                    Log.d(TAG, "onEvent: " + values.toString());
                                    if (doc != null) {
                                        Log.d(TAG, "onEvent: " + doc.toString());
                                        Grievance grievance = new Grievance();
                                        grievance.setRequest_id(doc.getString(Fields.DB_GR_REQUEST_ID));
                                        grievance.setTitle(doc.getString(Fields.DB_GR_TITLE));
                                        grievance.setStatus(doc.getString(Fields.DB_GR_STATUS));
                                        grievance.setTimestamp(doc.getTimestamp(Fields.DB_GR_TIMESTAMP));
                                        grievances.add(grievance);
                                    }
                                }
                                data.setValue(grievances);
                            }
                        });
                break;
            }

            case "Dep_incharge":
            {
                // To be done
//                db.collection("Departments")
//                        .document("")
//                        .whereEqualTo("grievance_status", "Pending")
//                        .orderBy("grievance_timestamp", Query.Direction.ASCENDING)
//                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
//                                if (e != null) {
//                                    Log.e(TAG, "Listen failed." +e.getMessage());
//                                    return;
//                                }
//                                ArrayList<Grievance> grievances = new ArrayList<>();
//                                for (QueryDocumentSnapshot doc : values) {
//                                    Log.d(TAG, "onEvent: "+values.toString());
//                                    if (doc != null) {
//                                        Log.d(TAG, "onEvent: "+doc.toString());
//                                        Grievance grievance = new Grievance();
//                                        grievance.setRequest_id(doc.getString("grievance_request_id"));
//                                        grievance.setTitle(doc.getString("grievance_title"));
//                                        grievance.setStatus(doc.getString("grievance_status"));
//                                        grievance.setTimestamp(doc.getTimestamp("grievance_timestamp"));
//                                        grievances.add(grievance);
//                                    }
//                                }
//                                data.setValue(grievances);
//                            }
//                        });
                break;
            }


        }



        return data;
    }


    MutableLiveData<ArrayList<Grievance>> getInProcessRequests(String s) {


        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();

        switch (s) {
            case "mediator": {
                db.collection(Fields.DBC_MEDIATORS)
                        .document(Fields.DBC_REQUESTS)
                        .collection(Fields.DBC_MED_ALL_REQUESTS)
                        .whereEqualTo(Fields.DB_GR_STATUS, Fields.GR_STATUS_IN_PROCESS)
                        .orderBy(Fields.DB_GR_TIMESTAMP, Query.Direction.ASCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.e(TAG, "Listen failed." + e.getMessage());
                                    return;
                                }
                                ArrayList<Grievance> grievances = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : values) {
                                    Log.d(TAG, "onEvent: " + values.toString());
                                    if (doc != null) {
                                        Log.d(TAG, "onEvent: " + doc.toString());
                                        Grievance grievance = new Grievance();
                                        grievance.setRequest_id(doc.getString(Fields.DB_GR_REQUEST_ID));
                                        grievance.setTitle(doc.getString(Fields.DB_GR_TITLE));
                                        grievance.setStatus(doc.getString(Fields.DB_GR_STATUS));
                                        grievance.setTimestamp(doc.getTimestamp(Fields.DB_GR_TIMESTAMP));
                                        grievances.add(grievance);
                                    }
                                }
                                data.setValue(grievances);
                            }
                        });
                break;
            }
        }

            return data;
    }


    MutableLiveData<ArrayList<Grievance>> getResolvedRequests(String s) {


        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();

        switch (s) {
            case "mediator": {
                db.collection(Fields.DBC_MEDIATORS)
                        .document(Fields.DBC_REQUESTS)
                        .collection(Fields.DBC_MED_ALL_REQUESTS)
                        .whereEqualTo(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                        .orderBy(Fields.DB_GR_TIMESTAMP, Query.Direction.ASCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.e(TAG, "Listen failed." + e.getMessage());
                                    return;
                                }
                                ArrayList<Grievance> grievances = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : values) {
                                    Log.d(TAG, "onEvent: " + values.toString());
                                    if (doc != null) {
                                        Log.d(TAG, "onEvent: " + doc.toString());
                                        Grievance grievance = new Grievance();
                                        grievance.setRequest_id(doc.getString(Fields.DB_GR_REQUEST_ID));
                                        grievance.setTitle(doc.getString(Fields.DB_GR_TITLE));
                                        grievance.setStatus(doc.getString(Fields.DB_GR_STATUS));
                                        grievance.setTimestamp(doc.getTimestamp(Fields.DB_GR_TIMESTAMP));
                                        grievances.add(grievance);
                                    }
                                }
                                data.setValue(grievances);
                            }
                        });
                break;
            }
        }

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
