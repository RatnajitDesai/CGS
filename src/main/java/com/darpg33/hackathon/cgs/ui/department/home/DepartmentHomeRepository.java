package com.darpg33.hackathon.cgs.ui.department.home;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

class DepartmentHomeRepository {

    private static final String TAG = "DeptHomeRepository";
    private FirebaseFirestore db;

    DepartmentHomeRepository() {

        db = FirebaseFirestore.getInstance();

    }

    MutableLiveData<Integer> getGrievanceCount(final String status) {

        final MutableLiveData<Integer> data = new MutableLiveData<>();

        db.collection(Fields.DBC_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "onSuccess: getting count.");
                            String user_dept = documentSnapshot.getString(Fields.DB_USER_DEPARTMENT);
                            Log.d(TAG, "onSuccess: getting count..." + user_dept + "  " + status);


                            try {
                                db.collection(Fields.DBC_DEPARTMENTS)
                                        .document(user_dept)
                                        .collection(Fields.DBC_REQUESTS)
                                        .whereEqualTo(Fields.DB_GR_STATUS, status)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                                if (e != null) {
                                                    Log.w(TAG, "Listen failed." + e.getMessage());
                                                    return;
                                                }

                                                Log.d(TAG, "onEvent: " + queryDocumentSnapshots.size());
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    data.setValue(queryDocumentSnapshots.size());
                                                    Log.d(TAG, "onSuccess: getting count....." + queryDocumentSnapshots.size());

                                                }

                                            }
                                        });
                            } catch (Exception er) {
                                Log.e(TAG, "onEvent: " + er.getMessage());
                            }
                        }
                    }
                });


        return data;


    }
}
