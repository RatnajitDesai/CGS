package com.darpg33.hackathon.cgs.ui.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

class LoginRepository {

    private static final String TAG = "LoginRepository";
    private FirebaseFirestore firestore;

    LoginRepository(){
        firestore = FirebaseFirestore.getInstance();

    }

    MutableLiveData<FirebaseUser> signInUser(String email, String password) {

        final MutableLiveData<FirebaseUser> userMutableLiveData = new MutableLiveData<>();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = Objects.requireNonNull(task.getResult()).getUser();
                            //to be done
                            if (firebaseUser!= null)
                            {

                                userMutableLiveData.setValue(firebaseUser);

                            }
                        }
                    }
                })
            .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e(TAG, "onFailure: "+e.getMessage() );
                        userMutableLiveData.setValue(null);
                    }
                });

        return userMutableLiveData;
    }

     MutableLiveData<User> getUser(final String userId) {

        final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

        firestore.collection("Users")
                .document(userId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {

                    User user = new User();
                    user.setUser_id(userId);
                    user.setUser_type(documentSnapshot.getString("user_type"));
                    userMutableLiveData.setValue(user);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                userMutableLiveData.setValue(null);

            }
        });


        return userMutableLiveData;
    }
}
