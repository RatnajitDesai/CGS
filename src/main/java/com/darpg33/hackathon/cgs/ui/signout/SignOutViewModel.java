package com.darpg33.hackathon.cgs.ui.signout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignOutViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public SignOutViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is sign out fragment");

    }

    public LiveData<String> getText() {

        return mText;

    }

}
