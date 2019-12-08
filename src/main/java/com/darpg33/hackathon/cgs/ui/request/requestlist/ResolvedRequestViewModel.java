package com.darpg33.hackathon.cgs.ui.request.requestlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;

import java.util.ArrayList;

public class ResolvedRequestViewModel extends ViewModel {

    private RequestRepository mRepository;

    public ResolvedRequestViewModel(){

        mRepository = new RequestRepository();

    }


    LiveData<ArrayList<Grievance>> getAllResolvedRequests() {

        return mRepository.getResolvedRequests();
    }

}
