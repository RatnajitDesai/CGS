package com.darpg33.hackathon.cgs.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    private LoginRepository mLoginRepository = new LoginRepository();
    public MutableLiveData<FirebaseUser> user;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    MutableLiveData<FirebaseUser> signInUser(String email,String password){
        user = mLoginRepository.signInUser(email,password);
        return user;
    }



}
