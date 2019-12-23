package com.darpg33.hackathon.cgs.ui.mediator.home;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

class MediatorHomeRepository {

     private static final String TAG = "MediatorHomeRepository";

     MutableLiveData<Integer> getGrievanceCount(String status, String user_type) {

         final MutableLiveData<Integer> data = new MutableLiveData<>();


         switch (user_type){

             case Fields.USER_TYPE_MEDIATOR:
             {
                 FirebaseFirestore.getInstance()
                         .collection(Fields.DBC_MEDIATORS)
                         .document(Fields.DBC_REQUESTS)
                         .collection(Fields.DBC_MED_ALL_REQUESTS)
                         .orderBy(Fields.DB_GR_TIMESTAMP, Query.Direction.DESCENDING)
                         .whereEqualTo(Fields.DB_GR_STATUS,status)
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
                             }
                         });
                }

             }

         return data;


     }
}
