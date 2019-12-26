package com.darpg33.hackathon.cgs.ui.worker.search;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WorkerSearchViewModel extends ViewModel {


    private static final String TAG = "WorkerSearchViewModel";

    public MutableLiveData<String> getUserDepartment() {

        final MutableLiveData<String> data = new MutableLiveData<>();

        FirebaseFirestore.getInstance().collection(Fields.DBC_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    data.setValue(documentSnapshot.getString(Fields.DB_USER_DEPARTMENT));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                data.setValue(null);
            }
        });


        return data;

    }


    public MutableLiveData<ArrayList<Grievance>> getGrievances(final String queryText, String department) {

        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();


        FirebaseFirestore.getInstance().collection(Fields.DBC_DEPARTMENTS)
                .document(department)
                .collection(Fields.DBC_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection(Fields.DBC_REQUESTS)
                .orderBy(Fields.DB_GR_TIMESTAMP, Query.Direction.DESCENDING)
                .whereEqualTo(Fields.DB_GR_REQUEST_ID, queryText)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                                data.setValue(grievances);
                            }


                        }

                    }
                });


        return data;
    }


}
