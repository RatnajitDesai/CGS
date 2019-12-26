package com.darpg33.hackathon.cgs.ui.worker.dashboard;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

    MutableLiveData<ArrayList<Integer>> getGrievancesCount(final String user_dept, final String user_id) {
        final MutableLiveData<ArrayList<Integer>> data = new MutableLiveData<>();

        db
                .collection(Fields.DBC_DEPARTMENTS)
                .document(user_dept)
                .collection(Fields.DBC_USERS)
                .document(user_id)
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

                        db.collection(Fields.DBC_DEPARTMENTS)
                                .document(user_dept)
                                .collection(Fields.DBC_USERS)
                                .document(user_id)
                                .collection(Fields.DBC_REQUESTS)
                                .whereEqualTo(Fields.DB_GR_STATUS, Fields.GR_STATUS_PENDING)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable final QuerySnapshot pendingGrievances, @Nullable FirebaseFirestoreException e) {


                                        if (e != null) {
                                            Log.w(TAG, "Listen failed." + e.getMessage());
                                            return;
                                        }


                                        db.collection(Fields.DBC_DEPARTMENTS)
                                                .document(user_dept)
                                                .collection(Fields.DBC_USERS)
                                                .document(user_id)
                                                .collection(Fields.DBC_REQUESTS)
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


    MutableLiveData<User> getUser() {
        final MutableLiveData<User> data = new MutableLiveData<>();

        db.collection(Fields.DBC_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    User user = new User();
                    user.setUser_type(documentSnapshot.getString(Fields.DB_USER_USER_TYPE));
                    user.setUser_department(documentSnapshot.getString(Fields.DB_USER_DEPARTMENT));
                    user.setFirst_name(documentSnapshot.getString(Fields.DB_USER_FIRSTNAME));
                    user.setLast_name(documentSnapshot.getString(Fields.DB_USER_LASTNAME));
                    user.setUser_id(documentSnapshot.getString(Fields.DB_USER_USER_ID));

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

    MutableLiveData<ArrayList<Integer>> getPriorityCount(final String user_dept, final String user_id) {

        final MutableLiveData<ArrayList<Integer>> data = new MutableLiveData<>();

        final ArrayList<String> FILTER_BY_GRIEVANCES = new ArrayList<>();

        FILTER_BY_GRIEVANCES.add(Fields.GR_STATUS_PENDING);
        FILTER_BY_GRIEVANCES.add(Fields.GR_STATUS_IN_PROCESS);

        db.collection(Fields.DBC_DEPARTMENTS)
                .document(user_dept)
                .collection(Fields.DBC_USERS)
                .document(user_id)
                .collection(Fields.DBC_REQUESTS)
                .whereEqualTo(Fields.DB_GR_PRIORITY, "High")
                .whereIn(Fields.DB_GR_STATUS, FILTER_BY_GRIEVANCES)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable final QuerySnapshot highPriorityGrivances, @Nullable FirebaseFirestoreException e) {

                        final ArrayList<Integer> list = new ArrayList<>();

                        if (e != null) {
                            Log.w(TAG, "Listen failed." + e.getMessage());
                            return;
                        }

                        db.collection(Fields.DBC_DEPARTMENTS)
                                .document(user_dept)
                                .collection(Fields.DBC_USERS)
                                .document(user_id)
                                .collection(Fields.DBC_REQUESTS)
                                .whereEqualTo(Fields.DB_GR_PRIORITY, "Medium")
                                .whereIn(Fields.DB_GR_STATUS, FILTER_BY_GRIEVANCES)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable final QuerySnapshot mediumPriorityGrievances, @Nullable FirebaseFirestoreException e) {


                                        if (e != null) {
                                            Log.w(TAG, "Listen failed." + e.getMessage());
                                            return;
                                        }


                                        db.collection(Fields.DBC_DEPARTMENTS)
                                                .document(user_dept)
                                                .collection(Fields.DBC_USERS)
                                                .document(user_id)
                                                .collection(Fields.DBC_REQUESTS)
                                                .whereEqualTo(Fields.DB_GR_PRIORITY, "Low")
                                                .whereIn(Fields.DB_GR_STATUS, FILTER_BY_GRIEVANCES)
                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot lowPriorityGrievances, @Nullable FirebaseFirestoreException e) {

                                                        if (e != null) {
                                                            Log.w(TAG, "Listen failed." + e.getMessage());
                                                            return;
                                                        }
                                                        list.add(0, highPriorityGrivances.size());
                                                        list.add(1, mediumPriorityGrievances.size());
                                                        list.add(2, lowPriorityGrievances.size());
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


    MutableLiveData<ArrayList<Grievance>> getImportantGrievances(String userDept) {

        final MutableLiveData<ArrayList<Grievance>> data = new MutableLiveData<>();


        db.collection(Fields.DBC_REQUESTS)
                .orderBy(Fields.DB_GR_TIMESTAMP)
                .whereEqualTo(Fields.DB_GR_HANDLED_BY, userDept)
                .whereIn(Fields.DB_GR_STATUS, Arrays.asList(Fields.GR_STATUS_IN_PROCESS, Fields.GR_STATUS_PENDING))
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
