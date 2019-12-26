package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.Model.User;

import java.util.HashMap;

public class CustomActionViewModel extends ViewModel {

    private CustomActionRepository mRepository;

    public CustomActionViewModel()
    {
        mRepository = new CustomActionRepository();
    }

    MutableLiveData<HashMap<String, HashMap<String, Object>>> uploadAttachments(Action action) {
        return mRepository.uploadAttachments(action);
    }


    LiveData<Action> addNote(Action action, HashMap<String, HashMap<String, Object>> attachmentMap)
    {
        return mRepository.addNote(action, attachmentMap);
    }

    LiveData<Action> assignRequest(Action action, String assignTo, String priority, HashMap<String, HashMap<String, Object>> attachmentsHashmap) {
        return mRepository.assignRequest(action, assignTo, priority, attachmentsHashmap);
    }

    LiveData<Action> assignToWorkerRequest(Action action, User assignTo, HashMap<String, HashMap<String, Object>> attachmentsHashmap) {
        return mRepository.assignToWorkerRequest(action, assignTo, attachmentsHashmap);
    }

    LiveData<Action> completeRequest(Action action, HashMap<String, HashMap<String, Object>> attachmentsHashmap) {

        return mRepository.completeRequest(action, attachmentsHashmap);
    }

    LiveData<Action> rejectRequest(Action action, HashMap<String, HashMap<String, Object>> attachmentsHashmap) {

        return mRepository.rejectRequest(action, attachmentsHashmap);
    }

    LiveData<Action> forwardRequest(Action action, String forwardTo, HashMap<String, HashMap<String, Object>> attachmentsHashmap) {

        return mRepository.forwardRequest(action, forwardTo, attachmentsHashmap);
    }
}
