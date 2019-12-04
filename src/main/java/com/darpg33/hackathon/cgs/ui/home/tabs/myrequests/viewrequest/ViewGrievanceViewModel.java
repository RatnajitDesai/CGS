package com.darpg33.hackathon.cgs.ui.home.tabs.myrequests.viewrequest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;

public class ViewGrievanceViewModel extends ViewModel {

    private ViewGrievanceRepository repository;

    public ViewGrievanceViewModel(){

        repository = new ViewGrievanceRepository();

    }


    public LiveData<Grievance> getGrievanceData(String requestID) {

        return repository.getGrievanceData(requestID);
    }


}
