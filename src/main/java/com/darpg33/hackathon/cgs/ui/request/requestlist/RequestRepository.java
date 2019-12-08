package com.darpg33.hackathon.cgs.ui.request.requestlist;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Grievance;
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



    MutableLiveData<ArrayList<Grievance>> getPendingRequests() {


        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();

        db.collection("Requests")
                .whereEqualTo("grievance_status", "Pending")
                .orderBy("grievance_timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed." +e.getMessage());
                            return;
                        }
                        ArrayList<Grievance> grievances = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : values) {
                            Log.d(TAG, "onEvent: "+values.toString());
                            if (doc != null) {
                                Log.d(TAG, "onEvent: "+doc.toString());
                                Grievance grievance = new Grievance();
                                grievance.setRequest_id(doc.getString("grievance_request_id"));
                                grievance.setTitle(doc.getString("grievance_title"));
                                grievance.setStatus(doc.getString("grievance_status"));
                                grievance.setTimestamp(doc.getTimestamp("grievance_timestamp"));
                                grievances.add(grievance);
                            }
                        }
                        data.setValue(grievances);
                    }
                });

        return data;
    }


    MutableLiveData<ArrayList<Grievance>> getInProcessRequests() {


        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();

        db.collection("Requests")
                .whereEqualTo("grievance_status", "In Process")
                .orderBy("grievance_timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed." +e.getMessage());
                            return;
                        }
                        ArrayList<Grievance> grievances = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : values) {
                            Log.d(TAG, "onEvent: "+values.toString());
                            if (doc != null) {
                                Log.d(TAG, "onEvent: "+doc.toString());
                                Grievance grievance = new Grievance();
                                grievance.setRequest_id(doc.getString("grievance_request_id"));
                                grievance.setTitle(doc.getString("grievance_title"));
                                grievance.setStatus(doc.getString("grievance_status"));
                                grievance.setTimestamp(doc.getTimestamp("grievance_timestamp"));
                                grievances.add(grievance);
                            }
                        }
                        data.setValue(grievances);
                    }
                });

        return data;
    }


    MutableLiveData<ArrayList<Grievance>> getResolvedRequests() {


        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();

        db.collection("Requests")
                .whereEqualTo("grievance_status", "Resolved")
                .orderBy("grievance_timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed." +e.getMessage());
                            return;
                        }
                        ArrayList<Grievance> grievances = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : values) {
                            Log.d(TAG, "onEvent: "+values.toString());
                            if (doc != null) {
                                Log.d(TAG, "onEvent: "+doc.toString());
                                Grievance grievance = new Grievance();
                                grievance.setRequest_id(doc.getString("grievance_request_id"));
                                grievance.setTitle(doc.getString("grievance_title"));
                                grievance.setStatus(doc.getString("grievance_status"));
                                grievance.setTimestamp(doc.getTimestamp("grievance_timestamp"));
                                grievances.add(grievance);
                            }
                        }
                        data.setValue(grievances);
                    }
                });

        return data;
    }





}
