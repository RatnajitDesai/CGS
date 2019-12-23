package com.darpg33.hackathon.cgs.ui.CustomDialogs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.User;

import java.util.ArrayList;

public class CustomActionDialogViewModel extends ViewModel {


    private CustomActionDialogRepository repository;

    public CustomActionDialogViewModel() {

        repository = new CustomActionDialogRepository();

    }


    LiveData<ArrayList<User>> getUsersInDepartment() {

        return repository.getUsersInDepartment();
    }

}
