package com.darpg33.hackathon.cgs.ui.signout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignOutViewModel extends ViewModel {
    private MutableLiveData<Boolean> isUserSignedOut;
    private SignOutRepository repository;

    public SignOutViewModel() {
        repository = new SignOutRepository();
        isUserSignedOut = new MutableLiveData<>();
    }

     LiveData<Boolean> signOutCurrentUser() {

        isUserSignedOut = repository.signOutCurrentUser();

        return isUserSignedOut;

    }

}
