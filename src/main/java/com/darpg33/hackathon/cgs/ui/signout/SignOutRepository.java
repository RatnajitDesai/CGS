package com.darpg33.hackathon.cgs.ui.signout;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

 class SignOutRepository {

    private FirebaseAuth mAuth;

    SignOutRepository(){

        mAuth = FirebaseAuth.getInstance();
    }

     MutableLiveData<Boolean> signOutCurrentUser() {

        MutableLiveData<Boolean> isUserSignedOut = new MutableLiveData<>();
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null)
        {
            isUserSignedOut.setValue(true);
        }
        else {
            isUserSignedOut.setValue(false);
        }

        return isUserSignedOut;
    }
}
