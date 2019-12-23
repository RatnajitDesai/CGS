package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.Model.User;

public class CustomActionViewModel extends ViewModel {

    private CustomActionRepository mRepository;

    public CustomActionViewModel()
    {
        mRepository = new CustomActionRepository();
    }

    LiveData<Action> addNote(Action action)
    {
        return mRepository.addNote(action);
    }

     LiveData<Action> assignRequest(Action action, String assignTo, String priority) {
        return mRepository.assignRequest(action,assignTo,priority);
    }

    LiveData<Action> assignToWorkerRequest(Action action, User assignTo) {
        return mRepository.assignToWorkerRequest(action, assignTo);
    }

    LiveData<Action> completeRequest(Action action) {

        return mRepository.completeRequest(action);
    }

     LiveData<Action> rejectRequest(Action action) {

        return mRepository.rejectRequest(action);
    }

    LiveData<Action> forwardRequest(Action action, String forwardTo) {

        return mRepository.forwardRequest(action,forwardTo);
    }
}
