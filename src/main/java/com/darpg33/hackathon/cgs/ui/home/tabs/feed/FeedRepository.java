package com.darpg33.hackathon.cgs.ui.home.tabs.feed;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

class FeedRepository {

    private static final String TAG = "FeedRepository";
    private FirebaseFirestore db;


    FeedRepository() {

        db = FirebaseFirestore.getInstance();
    }

    MutableLiveData<String> getUserDistrict() {

        final MutableLiveData<String> data = new MutableLiveData<>();

        db.collection(Fields.DBC_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    data.setValue(documentSnapshot.getString(Fields.DB_USER_DISTRICT));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Log.e(TAG, "onFailure: ", e);

            }
        });

        return data;

    }


    MutableLiveData<ArrayList<Grievance>> getAllRequests(final String user_district) {

        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();

        db.collection(Fields.DBC_REQUESTS)
                .whereEqualTo(Fields.DB_GR_PRIVACY, "public")
                .orderBy(Fields.DB_GR_TIMESTAMP, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        if (queryDocumentSnapshots != null) {
                            final ArrayList<Grievance> grievances = new ArrayList<>();

                            Log.d(TAG, "onEvent: found :" + queryDocumentSnapshots.getDocuments().size());
                            for (final DocumentSnapshot snapshot :
                                    queryDocumentSnapshots.getDocuments()) {


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

                                ArrayList<String> upvotes = (ArrayList<String>) snapshot.get(Fields.DB_GR_UPVOTES);
                                if (upvotes != null) {
                                    Log.d(TAG, "onEvent: getting upvotes.");
                                    grievance.setUpvotes(upvotes);
                                }
                                grievances.add(grievance);
                                data.setValue(grievances);

                            }

                            data.setValue(grievances);

                        }
                    }
                });


        return data;
    }

    MutableLiveData<Boolean> setUpvote(final String userId, String requestId) {

        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        db.collection(Fields.DBC_REQUESTS)
                .document(requestId)
                .update(Fields.DB_GR_UPVOTES, FieldValue.arrayUnion(userId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: upvoted : " + userId);
                data.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
                data.setValue(false);
            }
        });

        return data;

    }

    MutableLiveData<Boolean> resetUpvote(final String userId, String requestId) {

        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        db.collection(Fields.DBC_REQUESTS)
                .document(requestId)
                .update(Fields.DB_GR_UPVOTES, FieldValue.arrayRemove(userId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: upvote resat : " + userId);

                data.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
                data.setValue(false);
            }
        });

        return data;

    }

    MutableLiveData<User> getUser(final String userId) {

        final MutableLiveData<User> data = new MutableLiveData<>();

        db.collection(Fields.DBC_USERS)
                .document(userId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    User user = new User();
                    user.setUser_id(userId);
                    user.setAddress(documentSnapshot.getString(Fields.DB_USER_ADDRESS));
                    user.setCountry(documentSnapshot.getString(Fields.DB_USER_COUNTRY));
                    user.setState(documentSnapshot.getString(Fields.DB_USER_STATE));
                    user.setDistrict(documentSnapshot.getString(Fields.DB_USER_DISTRICT));
                    user.setPin_code(documentSnapshot.getString(Fields.DB_USER_PINCODE));
                    user.setEmail_id(documentSnapshot.getString(Fields.DB_USER_EMAIL_ID));
                    user.setGender(documentSnapshot.getString(Fields.DB_USER_GENDER));
                    user.setPhone_number(documentSnapshot.getString(Fields.DB_USER_PHONE_NUMBER));
                    user.setFirst_name(documentSnapshot.getString(Fields.DB_USER_FIRSTNAME));
                    user.setLast_name(documentSnapshot.getString(Fields.DB_USER_LASTNAME));

                    data.setValue(user);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
                data.setValue(null);
            }
        });

        return data;

    }
}
