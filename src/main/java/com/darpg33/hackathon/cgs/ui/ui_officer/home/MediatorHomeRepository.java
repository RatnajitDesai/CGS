package com.darpg33.hackathon.cgs.ui.ui_officer.home;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

class MediatorHomeRepository {

    private static final String TAG = "MediatorHomeRepository";

     MutableLiveData<Integer> getGrievanceCount(String status) {

         final MutableLiveData<Integer> data = new MutableLiveData<>();

         FirebaseFirestore.getInstance()
                 .collection("Requests")
                 .orderBy("grievance_timestamp", Query.Direction.DESCENDING)
                 .whereEqualTo("grievance_status",status)
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
