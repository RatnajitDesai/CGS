package com.darpg33.hackathon.cgs.ui.home.tabs.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.Model.User;

import java.util.ArrayList;

public class FeedsViewModel extends ViewModel {


    private FeedRepository mRepository;

    public FeedsViewModel() {
        mRepository = new FeedRepository();
    }

    LiveData<String> getUserDistrict() {

        return mRepository.getUserDistrict();

    }

    LiveData<ArrayList<Grievance>> getAllRequests(String user_district) {
        return mRepository.getAllRequests(user_district);
    }

    LiveData<Boolean> setUpvote(String userId, String requestId) {

        return mRepository.setUpvote(userId, requestId);
    }

    LiveData<Boolean> resetUpvote(String userId, String requestId) {


        return mRepository.resetUpvote(userId, requestId);
    }

    LiveData<User> getUser(String userId) {

        return mRepository.getUser(userId);
    }

    LiveData<Boolean> currentUserUpvoted(ArrayList<String> list) {

        return mRepository.currentUserUpvoted(list);
    }
}
