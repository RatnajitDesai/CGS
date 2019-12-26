package com.darpg33.hackathon.cgs.ui.mediator.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;

import java.util.ArrayList;

public class DashboardViewModel extends ViewModel {

    private static final String TAG = "DashboardViewModel";

    //vars
    private DashboardRepository repository;

    public DashboardViewModel() {

        repository = new DashboardRepository();
    }

    LiveData<ArrayList<Integer>> getGrievancesCount() {

        return repository.getGrievancesCount();
    }

    public LiveData<Integer> getGrievanceCount(String status) {
        return repository.getGrievanceCount(status);
    }

    LiveData<ArrayList<Grievance>> getImportantGrievances() {


        return repository.getImportantGrievances();

    }
}
