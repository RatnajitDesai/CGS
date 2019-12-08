package com.darpg33.hackathon.cgs.ui.ui_officer.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MediatorHomeViewModel extends ViewModel {

    private MediatorHomeRepository mediatorHomeRepository;

    public MediatorHomeViewModel(){

        mediatorHomeRepository = new MediatorHomeRepository();

    }


    LiveData<Integer> getGrievanceCount(String status) {

        return mediatorHomeRepository.getGrievanceCount(status);

    }
}
