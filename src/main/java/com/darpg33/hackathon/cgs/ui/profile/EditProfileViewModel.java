package com.darpg33.hackathon.cgs.ui.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.User;

public class EditProfileViewModel extends ViewModel {

    private static final String TAG = "EditProfileViewModel";

    //Live Data
    private MutableLiveData<User> mUser;
    private ProfileRepository mRepository;

    public EditProfileViewModel()
    {
        mRepository = new ProfileRepository();
    }

     MutableLiveData<User> updateUserProfile(User user)
    {

        mUser = mRepository.updateUserProfile(user);

        return mUser;

    }




}
