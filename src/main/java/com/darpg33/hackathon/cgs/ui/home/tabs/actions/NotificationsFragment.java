package com.darpg33.hackathon.cgs.ui.home.tabs.actions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Notification;
import com.darpg33.hackathon.cgs.R;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements NotificationAdapter.NotificationOnClickListener {

    private static final String TAG = "NotificationsFragment";

    //widgets
    private RecyclerView mNotificatiosnRecyclerView;
    private NotificationAdapter mAdapter;
    private ArrayList<Notification> mNotifications = new ArrayList<>();
    private NotificationsViewModel notificationsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        mNotificatiosnRecyclerView = view.findViewById(R.id.notificationsRecyclerView);
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        setupRecyclerView();

        getUserNotifications();

        return view;
    }

    private void getUserNotifications() {

        notificationsViewModel.getUserNotifications().observe(this, new Observer<ArrayList<Notification>>() {
            @Override
            public void onChanged(ArrayList<Notification> notifications) {

                Log.d(TAG, "onChanged: " + notifications.toString());

                mNotifications.clear();
                mNotifications.addAll(notifications);
                mAdapter.notifyDataSetChanged();

            }
        });


    }

    private void setupRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mNotificatiosnRecyclerView.setLayoutManager(manager);
        mAdapter = new NotificationAdapter(mNotifications, this);
        mNotificatiosnRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void viewGrievance(String requestID) {

        Bundle bundle = new Bundle();
        bundle.putString("grievance_request_id", requestID);
        Navigation.findNavController(getActivity(), R.id.requestsRecyclerView).navigate(R.id.nav_view_grievance, bundle);


    }
    
}
