package com.darpg33.hackathon.cgs.ui.worker.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class WorkerHomeViewModel extends ViewModel {

    private WorkerHomeRepository workerHomeRepository;

    public WorkerHomeViewModel() {

        workerHomeRepository = new WorkerHomeRepository();

    }


    LiveData<Integer> getGrievanceCount(String status, String user_type) {

        return workerHomeRepository.getGrievanceCount(status, user_type);

    }
}
