package com.darpg33.hackathon.cgs.ui.department.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DepartmentHomeViewModel extends ViewModel {

    private DepartmentHomeRepository departmentHomeRepository;

    public DepartmentHomeViewModel(){

        departmentHomeRepository = new DepartmentHomeRepository();

    }


    LiveData<Integer> getGrievanceCount(String status) {

        return departmentHomeRepository.getGrievanceCount(status);

    }
}
