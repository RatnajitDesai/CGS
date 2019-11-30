package com.darpg33.hackathon.cgs.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.User;

public class RegisterViewModel extends ViewModel {


    private MutableLiveData<User> userMutableLiveData;
    MutableLiveData<Boolean> phoneNumberLiveData;

    private RegisterRepository repository = new RegisterRepository();


    public MutableLiveData<User> registerUser(User user, String password)
    {

        userMutableLiveData = repository.registerUser(user, password);
        return userMutableLiveData;

    }

    MutableLiveData<User> saveUserToDatabase(User user)
    {

        userMutableLiveData = repository.saveUserToDatabase(user);
        return userMutableLiveData;

    }

}
