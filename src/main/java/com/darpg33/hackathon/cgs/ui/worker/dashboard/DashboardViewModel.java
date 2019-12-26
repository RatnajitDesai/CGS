package com.darpg33.hackathon.cgs.ui.worker.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.Model.User;

import java.util.ArrayList;

public class DashboardViewModel extends ViewModel {

    private static final String TAG = "DashboardViewModel";

    //vars
    private DashboardRepository repository;

    public DashboardViewModel() {

        repository = new DashboardRepository();
    }

    LiveData<ArrayList<Integer>> getGrievancesCount(String user_dept, String user_id) {

        return repository.getGrievancesCount(user_dept, user_id);
    }

    LiveData<ArrayList<Integer>> getPriorityCount(String user_dept, String user_id) {

        return repository.getPriorityCount(user_dept, user_id);
    }


    LiveData<ArrayList<Grievance>> getImportantGrievances(String user_dept) {

        return repository.getImportantGrievances(user_dept);

    }


    LiveData<User> getUser() {

        return repository.getUser();
    }

}
