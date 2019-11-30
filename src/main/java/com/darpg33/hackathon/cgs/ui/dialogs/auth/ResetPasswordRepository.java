package com.darpg33.hackathon.cgs.ui.dialogs.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

class ResetPasswordRepository {


    MutableLiveData<Boolean> resetAccountPasswordFor(String email) {

        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                data.setValue(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                data.setValue(false);
            }
        });

        return data;
    }
}
