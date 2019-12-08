package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Action;

public class CustomActionViewModel extends ViewModel {

    private CustomActionRepository mRepository;

    public CustomActionViewModel()
    {
        mRepository = new CustomActionRepository();
    }

    LiveData<Action> saveAction(Action action)
    {
        return mRepository.saveAction(action);
    }
}
