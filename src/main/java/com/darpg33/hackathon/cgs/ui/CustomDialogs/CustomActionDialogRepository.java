package com.darpg33.hackathon.cgs.ui.CustomDialogs;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

class CustomActionDialogRepository {

    private static final String TAG = "CustomActionDialogRepos";

    MutableLiveData<ArrayList<User>> getUsersInDepartment() {

        final MutableLiveData<ArrayList<User>> data = new MutableLiveData<>();

        FirebaseFirestore.getInstance().collection(Fields.DBC_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    String department = documentSnapshot.getString(Fields.DB_USER_DEPARTMENT);

                    if (department != null) {

                        Log.d(TAG, "onSuccess: ");

                        FirebaseFirestore.getInstance().collection(Fields.DBC_USERS)
                                .whereEqualTo(Fields.DB_USER_USER_TYPE, Fields.USER_TYPE_DEP_WORKER)
                                .whereEqualTo(Fields.DB_USER_DEPARTMENT, department)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                if (queryDocumentSnapshots != null) {
                                    ArrayList<User> users = new ArrayList<>();
                                    for (DocumentSnapshot snapshot :
                                            queryDocumentSnapshots) {

                                        if (snapshot != null) {

                                            User user = new User();
                                            user.setUser_id(snapshot.getString(Fields.DB_USER_USER_ID));
                                            user.setFirst_name(snapshot.getString(Fields.DB_USER_FIRSTNAME));
                                            user.setLast_name(snapshot.getString(Fields.DB_USER_LASTNAME));
                                            users.add(user);
                                        }
                                    }
                                    data.setValue(users);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: " + e.getMessage());
                                data.setValue(null);
                            }
                        });
                    }
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
}
