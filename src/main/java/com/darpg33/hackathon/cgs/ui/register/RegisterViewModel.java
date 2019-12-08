package com.darpg33.hackathon.cgs.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.User;

public class RegisterViewModel extends ViewModel {


    private MutableLiveData<User> userMutableLiveData;
    MutableLiveData<Boolean> phoneNumberLiveData;

    private RegisterRepository repository = new RegisterRepository();



     MutableLiveData<User> saveUserToDatabase(User user)
    {

        return repository.saveUserToDatabase(user);

    }

}