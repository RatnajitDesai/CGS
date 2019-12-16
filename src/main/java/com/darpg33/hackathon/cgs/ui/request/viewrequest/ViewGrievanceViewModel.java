package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.Model.Grievance;

import java.util.ArrayList;

public class ViewGrievanceViewModel extends ViewModel {

    private ViewGrievanceRepository repository;

    public ViewGrievanceViewModel(){

        repository = new ViewGrievanceRepository();

    }

    LiveData<Grievance> getGrievanceData(String requestID) {

        return repository.getGrievanceData(requestID);
    }



    LiveData<ArrayList<Attachment>> getGrievanceAttachments(String requestID) {

        return repository.getGrievanceAttachments(requestID);
    }

    LiveData<ArrayList<Action>> getGrievanceActions(String requestID) {

        return repository.getGrievanceActions(requestID);
    }


    LiveData<String> getUserType() {

        return repository.getUserType();
    }
}
