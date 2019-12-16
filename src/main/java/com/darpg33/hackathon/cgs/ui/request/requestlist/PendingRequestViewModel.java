package com.darpg33.hackathon.cgs.ui.request.requestlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;

import java.util.ArrayList;

public class PendingRequestViewModel extends ViewModel {


    private RequestRepository mRepository;

    public PendingRequestViewModel(){

        mRepository = new RequestRepository();

    }

    LiveData<String> getUserType()
    {

        return mRepository.getUserType();
    }


     LiveData<ArrayList<Grievance>> getAllPendingRequests(String s) {

         return mRepository.getPendingRequests(s);
    }
}
