package com.darpg33.hackathon.cgs.HelperViewModels;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ActivityHelperViewModel extends ViewModel {

    private static final String TAG = "ActivityHelperViewModel";

    public MutableLiveData<User> getCurrentUser() {

        final MutableLiveData<User> liveData = new MutableLiveData<>();

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (e!=null)
                            return;

                        if (documentSnapshot.exists()) {
                            User user = new User();
                            user.setFirst_name(documentSnapshot.getString(Fields.DB_USER_FIRSTNAME));
                            user.setLast_name(documentSnapshot.getString(Fields.DB_USER_LASTNAME));
                            user.setEmail_id(documentSnapshot.getString(Fields.DB_USER_EMAIL_ID));
                            user.setUser_department(documentSnapshot.getString(Fields.DB_USER_DEPARTMENT));

                            liveData.setValue(user);

                        }

                    }
                });

        return liveData;
    }


    public MutableLiveData<ArrayList<Grievance>> getGrievances(String queryText) {

        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();

        FirebaseFirestore.getInstance().collection(Fields.DBC_REQUESTS)
                .whereEqualTo(Fields.DB_GR_REQUESTED_BY, queryText).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e(TAG, "onEvent: ", e);
                }

                ArrayList<Grievance> grievances = new ArrayList<>();
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Log.d(TAG, "onEvent: " + queryDocumentSnapshots.toString());
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

            }
        });


        FirebaseFirestore.getInstance().collection(Fields.DBC_REQUESTS)
                .whereEqualTo(Fields.DB_GR_REQUEST_ID, queryText).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e(TAG, "onEvent: ", e);
                }

                ArrayList<Grievance> grievances = new ArrayList<>();
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Log.d(TAG, "onEvent: " + queryDocumentSnapshots.toString());
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

            }
        });


        FirebaseFirestore.getInstance().collection(Fields.DBC_REQUESTS)
                .whereEqualTo(Fields.DB_GR_REQUESTED_BY, queryText).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e(TAG, "onEvent: ", e);
                }

                ArrayList<Grievance> grievances = new ArrayList<>();
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Log.d(TAG, "onEvent: " + queryDocumentSnapshots.toString());
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

            }
        });

        return data;
    }


}