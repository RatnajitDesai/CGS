package com.darpg33.hackathon.cgs.ui.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

class LoginRepository {

    private static final String TAG = "LoginRepository";



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

}
