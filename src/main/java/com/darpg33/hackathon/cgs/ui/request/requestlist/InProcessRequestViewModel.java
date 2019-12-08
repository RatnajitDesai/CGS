package com.darpg33.hackathon.cgs.ui.request.requestlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;

import java.util.ArrayList;

public class InProcessRequestViewModel extends ViewModel {


    private RequestRepository mRepository;

    public InProcessRequestViewModel(){

        mRepository = new RequestRepository();

    }


    LiveData<ArrayList<Grievance>> getAllInProcessRequests() {

        return mRepository.getInProcessRequests();
    }

}
