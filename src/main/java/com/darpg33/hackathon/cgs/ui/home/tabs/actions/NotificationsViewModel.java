package com.darpg33.hackathon.cgs.ui.home.tabs.actions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Notification;

import java.util.ArrayList;

public class NotificationsViewModel extends ViewModel {

    private NotificationsRepository mRepository;


    public NotificationsViewModel() {

        mRepository = new NotificationsRepository();

    }


    LiveData<ArrayList<Notification>> getUserNotifications() {

        return mRepository.getUserNotifications();
    }
}
