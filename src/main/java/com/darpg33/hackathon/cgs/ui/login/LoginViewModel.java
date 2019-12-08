package com.darpg33.hackathon.cgs.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.User;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    private LoginRepository mLoginRepository;
    public MutableLiveData<FirebaseUser> user;

    public LoginViewModel(){
        mLoginRepository = new LoginRepository();

    }


    MutableLiveData<FirebaseUser> signInUser(String email,String password){
        user = mLoginRepository.signInUser(email,password);
        return user;
    }


    MutableLiveData<User> getUser(String userId){

        return mLoginRepository.getUser(userId);

    }






}
