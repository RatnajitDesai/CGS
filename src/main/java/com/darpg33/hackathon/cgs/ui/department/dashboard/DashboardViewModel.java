package com.darpg33.hackathon.cgs.ui.department.dashboard;

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

    LiveData<ArrayList<Integer>> getGrievancesCount(String user_dept) {

        return repository.getGrievancesCount(user_dept);

    }

    LiveData<ArrayList<Integer>> getPriorityCount(String user_dept) {

        return repository.getPriorityCount(user_dept);

    }


    LiveData<User> getUser() {

        return repository.getUser();

    }

    LiveData<ArrayList<Grievance>> getImportantGrievances(String userDept) {


        return repository.getImportantGrievances(userDept);

    }



}
