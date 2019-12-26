package com.darpg33.hackathon.cgs.ui.mediator.dashboard;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

class DashboardRepository {

    private static final String TAG = "DashboardRepository";

    //vars
    private FirebaseFirestore db;

    DashboardRepository() {

        db = FirebaseFirestore.getInstance();

    }

    MutableLiveData<ArrayList<Integer>> getGrievancesCount() {

        Log.d(TAG, "getGrievancesCount: ");
        final MutableLiveData<ArrayList<Integer>> data = new MutableLiveData<>();

        db
                .collection(Fields.DBC_REQUESTS)
                .whereEqualTo(Fields.DB_GR_STATUS, Fields.GR_STATUS_RESOLVED)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable final QuerySnapshot resolvedGrievances, @Nullable FirebaseFirestoreException e) {

                        final ArrayList<Integer> list = new ArrayList<>();

                        if (e != null) {
                            Log.w(TAG, "Listen failed." + e.getMessage());
                            return;
                        }


                        db.collection(Fields.DBC_REQUESTS)
                                .whereEqualTo(Fields.DB_GR_STATUS, Fields.GR_STATUS_PENDING)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable final QuerySnapshot pendingGrievances, @Nullable FirebaseFirestoreException e) {


                                        if (e != null) {
                                            Log.w(TAG, "Listen failed." + e.getMessage());
                                            return;
                                        }


                                        db.collection(Fields.DBC_REQUESTS)
                                                .whereEqualTo(Fields.DB_GR_STATUS, Fields.GR_STATUS_IN_PROCESS)
                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot inProcessGrievances, @Nullable FirebaseFirestoreException e) {

                                                        if (e != null) {
                                                            Log.w(TAG, "Listen failed." + e.getMessage());
                                                            return;
                                                        }
                                                        list.add(0, resolvedGrievances.size());
                                                        list.add(1, pendingGrievances.size());
                                                        list.add(2, inProcessGrievances.size());
                                                        data.setValue(list);
                                                        list.clear();
                                                    }
                                                });

                                    }
                                });
                    }
                });


        return data;
    }


    MutableLiveData<Integer> getGrievanceCount(String status) {
        Log.d(TAG, "getGrievanceCount: ");

        final MutableLiveData<Integer> data = new MutableLiveData<>();

        FirebaseFirestore.getInstance()
                .collection(Fields.DBC_REQUESTS)
                .whereEqualTo(Fields.DB_GR_STATUS, status)
                .orderBy(Fields.DB_GR_STATUS, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "Listen failed." + e.getMessage());
                            return;
                        }

                        if (!queryDocumentSnapshots.isEmpty()) {
                            data.setValue(queryDocumentSnapshots.size());
                        }
                    }
                });

        return data;


    }


    MutableLiveData<ArrayList<Grievance>> getImportantGrievances() {

        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();

        Log.d(TAG, "getImportantGrievances: ");

        db.collection(Fields.DBC_REQUESTS)
                .whereIn(Fields.DB_GR_STATUS, Arrays.asList(Fields.GR_STATUS_IN_PROCESS, Fields.GR_STATUS_PENDING))
                .orderBy(Fields.DB_GR_TIMESTAMP)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.e(TAG, "onEvent: ", e);
                        }

                        ArrayList<Grievance> grievances = new ArrayList<>();

                        if (queryDocumentSnapshots != null) {
                            for (DocumentSnapshot snapshot :
                                    queryDocumentSnapshots.getDocuments()) {

                                Log.d(TAG, "onEvent: documents found!");

                                ArrayList<String> upvotes = (ArrayList<String>) snapshot.get(Fields.DB_GR_UPVOTES);
                                if (upvotes != null && !upvotes.isEmpty() && upvotes.size() > 1) {

                                    Log.d(TAG, "onEvent: getting upvotes.");

                                    Grievance grievance = new Grievance();

                                    grievance.setUser_id(snapshot.getString(Fields.DB_GR_USER_ID)); // grievance user id
                                    grievance.setRequestedBy(snapshot.getString(Fields.DB_GR_REQUESTED_BY));
                                    grievance.setPrivacy(snapshot.getString(Fields.DB_GR_PRIVACY)); // grievance privacy
                                    grievance.setRequestedBy(snapshot.getString(Fields.DB_GR_REQUESTED_BY)); //grievance requested by
                                    grievance.setDescription(snapshot.getString(Fields.DB_GR_DESCRIPTION)); // grievance description
                                    grievance.setTitle(snapshot.getString(Fields.DB_GR_TITLE)); //grievance title
                                    grievance.setTimestamp(snapshot.getTimestamp(Fields.DB_GR_TIMESTAMP)); // grievance timestamp
                                    grievance.setRequest_id(snapshot.getString(Fields.DB_GR_REQUEST_ID)); //request id
                                    grievance.setStatus(snapshot.getString(Fields.DB_GR_STATUS)); //status
                                    grievance.setUpvotes(upvotes);
                                    grievances.add(grievance);

                                    data.setValue(grievances);

                                }

                            }
                        }


                    }
                });


        return data;

    }


}
