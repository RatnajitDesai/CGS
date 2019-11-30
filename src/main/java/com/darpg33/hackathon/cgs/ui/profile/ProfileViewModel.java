package com.darpg33.hackathon.cgs.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.User;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<User> userMutableLiveData;
    private ProfileRepository mRepository;


    public ProfileViewModel() {

        mRepository = new ProfileRepository();
    }

    LiveData<User> getUserInfo() {

        userMutableLiveData = mRepository.getUserInfo();

        return userMutableLiveData;

    }

}
