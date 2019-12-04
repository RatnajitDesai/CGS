package com.darpg33.hackathon.cgs.ui.home.tabs.myrequests.viewrequest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

class ViewGrievanceRepository {

    private static final String TAG = "ViewGrievanceRepository";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    MutableLiveData<Grievance> getGrievanceData(final String requestID) {

        final MutableLiveData<Grievance> liveData = new MutableLiveData<>();

        db.collection("Requests")
                .document(requestID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Grievance grievance = new Grievance();

                grievance.setRequest_id(requestID);
                grievance.setTitle(documentSnapshot.getString("grievance_title"));
                grievance.setStatus(documentSnapshot.getString("grievance_status"));
                grievance.setUser_id(documentSnapshot.getString("grievance_user_id"));
                grievance.setDescription(documentSnapshot.getString("grievance_description"));
                grievance.setTimestamp(documentSnapshot.getTimestamp("grievance_timestamp"));
                grievance.setCategory(documentSnapshot.getString("grievance_category"));
                liveData.setValue(grievance);
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

}
