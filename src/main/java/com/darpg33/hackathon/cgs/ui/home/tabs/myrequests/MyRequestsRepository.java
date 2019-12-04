package com.darpg33.hackathon.cgs.ui.home.tabs.myrequests;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


class MyRequestsRepository {
    private static final String TAG = "MyRequestsRepository";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    MutableLiveData<Integer> getGrievanceStatusLiveData(String requestStatus)
    {

        final MutableLiveData<Integer> data = new MutableLiveData<>();

        db.collection("Users")
            .document(mAuth.getCurrentUser().getUid())
                .collection("MyRequests")
                .orderBy("grievance_timestamp",Query.Direction.DESCENDING)
                .whereEqualTo("grievance_status",requestStatus)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "Listen failed."+e.getMessage());
                            return;
                        }

                        if (!queryDocumentSnapshots.isEmpty())
                        {
                            data.setValue(queryDocumentSnapshots.size());
                        }
                        else {
                            data.setValue(0);
                        }
                    }
                });

        return data;
    }

     MutableLiveData<ArrayList<Grievance>> getMyRequests() {


        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();

        db.collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("MyRequests")
                .orderBy("grievance_timestamp", Query.Direction.DESCENDING)
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


     LiveData<Integer> getTotalCount() {


         final MutableLiveData<Integer> data = new MutableLiveData<>();

         db.collection("Users")
                 .document(mAuth.getCurrentUser().getUid())
                 .collection("MyRequests")
                 .addSnapshotListener(new EventListener<QuerySnapshot>() {
                     @Override
                     public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                         if (e != null) {
                             Log.w(TAG, "Listen failed."+e.getMessage());
                             return;
                         }

                         if (!queryDocumentSnapshots.isEmpty())
                         {
                             data.setValue(queryDocumentSnapshots.size());
                         }
                         else {
                             data.setValue(0);
                         }
                     }
                 });

         return data;


    }

}
