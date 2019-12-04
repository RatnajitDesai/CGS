package com.darpg33.hackathon.cgs.ui.home.tabs.myrequests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;

import java.util.ArrayList;

public class MyRequestsViewModel extends ViewModel {

    private MyRequestsRepository repository;

    public MyRequestsViewModel()
    {
        repository = new MyRequestsRepository();
    }

    LiveData<Integer> getGrievanceStatusLiveData(String requestStatus){

        return repository.getGrievanceStatusLiveData(requestStatus);

    }

    LiveData<ArrayList<Grievance>> getMyRequests()
    {

        return repository.getMyRequests();
    }


    LiveData<Integer> getTotalCount() {

        return repository.getTotalCount();
    }


}
