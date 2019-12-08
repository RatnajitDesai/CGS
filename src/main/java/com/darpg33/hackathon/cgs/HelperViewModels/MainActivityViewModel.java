package com.darpg33.hackathon.cgs.HelperViewModels;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivityViewModel extends ViewModel {


    public MutableLiveData<User> getCurrentUser()
    {

        final MutableLiveData<User> liveData = new MutableLiveData<>();

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (e!=null)
                            return;

                        if (documentSnapshot.exists())
                        {
                            User user = new User();
                            user.setFirst_name(documentSnapshot.getString("first_name"));
                            user.setLast_name(documentSnapshot.getString("last_name"));
                            user.setEmail_id(documentSnapshot.getString("email_id"));

                            liveData.setValue(user);

                        }

                    }
                });

        return liveData;
    }


}
