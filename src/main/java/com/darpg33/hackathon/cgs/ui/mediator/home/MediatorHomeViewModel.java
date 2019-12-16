package com.darpg33.hackathon.cgs.ui.mediator.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MediatorHomeViewModel extends ViewModel {

    private MediatorHomeRepository mediatorHomeRepository;

    public MediatorHomeViewModel(){

        mediatorHomeRepository = new MediatorHomeRepository();

    }


    LiveData<Integer> getGrievanceCount(String status, String user_type) {

        return mediatorHomeRepository.getGrievanceCount(status, user_type);

    }
}
