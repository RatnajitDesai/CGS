package com.darpg33.hackathon.cgs.ui.request.newrequest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;

import java.util.HashMap;

public class NewGrievanceViewModel extends ViewModel {

    private NewGrievanceRepository repository;

    public NewGrievanceViewModel() {

        repository = new NewGrievanceRepository();

    }

     LiveData<String> getNewRequestId()
    {
        return repository.getNewRequestId();
    }

     LiveData<HashMap<String, HashMap<String,Object>>> uploadAttachments(Grievance grievance,String request_id) {

         return repository.uploadAttachments(grievance, request_id);

    }

     LiveData<Grievance> submitNewRequest(Grievance grievance,HashMap<String, HashMap<String,Object>> attachmentMap){
         return repository.submitNewRequest(grievance, attachmentMap);
    }



}
