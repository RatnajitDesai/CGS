package com.darpg33.hackathon.cgs.ui.home.tabs.actions;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Notification;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationsRepository {

    private static final String TAG = "NotificationsRepository";


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    NotificationsRepository() {

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }


    MutableLiveData<ArrayList<Notification>> getUserNotifications() {


        final MutableLiveData<ArrayList<Notification>> data = new MutableLiveData<>();


        db.collection(Fields.DBC_USERS)
                .document(mAuth.getCurrentUser().getUid())
                .collection(Fields.DBC_USERS_NOTIFICATIONS)
                .orderBy(Fields.NOTIFICATION_TIMESTAMP, Query.Direction.DESCENDING)
                .limit(10)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                        if (e != null) {
                            Log.e(TAG, "onEvent: listen failed.", e);
                            return;
                        }


                        if (queryDocumentSnapshots != null) {
                            ArrayList<Notification> notifications = new ArrayList<>();

                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                Log.d(TAG, "onEvent: found notification");

                                Notification notification = new Notification();

                                notification.setTitle(snapshot.getString(Fields.NOTIFICATION_TITLE));
                                notification.setBody(snapshot.getString(Fields.NOTIFICATION_BODY));
                                notification.setTimestamp(snapshot.getTimestamp(Fields.NOTIFICATION_TIMESTAMP));
                                notification.setRequestId(snapshot.getString(Fields.NOTIFICATION_REQUEST_ID));

                                notifications.add(notification);
                            }

                            data.setValue(notifications);
                        }

                    }

                });


        return data;

    }
}
