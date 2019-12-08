package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Action;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class CustomActionRepository {


    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();


    MutableLiveData<Action> saveAction(final Action action) {

        final MutableLiveData<Action> data = new MutableLiveData<>();


        mFirestore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            String firstname = documentSnapshot.getString("first_name");
                            String lastname = documentSnapshot.getString("last_name");
                            String username = firstname + " " + lastname;
                            String user_email = documentSnapshot.getString("email_id");

                            action.setUsername(username);
                            action.setUser_id(auth.getCurrentUser().getUid());
                            action.setEmail_id(user_email);

                            final HashMap<String, Object> actionHashmap = new HashMap<>();
                            actionHashmap.put("action_performed", action.getAction_performed());
                            actionHashmap.put("action_info", action.getAction_info());
                            actionHashmap.put("action_description", action.getAction_description());
                            actionHashmap.put("action_username", action.getUsername());
                            actionHashmap.put("action_user_type", action.getUser_type());
                            actionHashmap.put("action_user_email", action.getEmail_id());
                            actionHashmap.put("action_user_id", auth.getCurrentUser().getUid());
                            actionHashmap.put("action_timestamp", new Timestamp(new Date()));

                            mFirestore.collection("Requests")
                                    .document(action.getAction_request_id())
                                    .collection("Actions")
                                    .document().set(actionHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    data.setValue(action);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            data.setValue(null);
                                        }
                                    });
                        }


                    }

                });

        return data;
    }
}
