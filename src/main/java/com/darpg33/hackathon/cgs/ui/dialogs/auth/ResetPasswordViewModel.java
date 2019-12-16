package com.darpg33.hackathon.cgs.ui.dialogs.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResetPasswordViewModel extends ViewModel {

    MutableLiveData<Boolean> resetPasswordLinkSent;
    ResetPasswordRepository repository;

    public ResetPasswordViewModel(){

        repository = new ResetPasswordRepository();

    }
    MutableLiveData<Boolean> resetAccountPasswordFor(String email) {

        resetPasswordLinkSent = repository.resetAccountPasswordFor(email);

        return resetPasswordLinkSent;
    }

}
