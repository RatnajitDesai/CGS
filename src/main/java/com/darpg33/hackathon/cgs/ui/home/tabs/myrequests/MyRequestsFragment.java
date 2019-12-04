package com.darpg33.hackathon.cgs.ui.home.tabs.myrequests;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.R;

import java.util.ArrayList;


public class MyRequestsFragment extends Fragment implements RequestsAdapter.GrievanceOnClickListener {

    private static final String TAG = "FeedsFragment";

    //vars
    private RequestsAdapter mRequestsAdapter;
    private ArrayList<Grievance> mGrievances;
    private MyRequestsViewModel myRequestsViewModel;

    //widgets
    private TextView mCountTotal, mCountPending, mCountResolved, mCountInProcess;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_requests, container, false);
        mCountTotal = view.findViewById(R.id.count_total_requests);
        mCountInProcess = view.findViewById(R.id.count_request_in_process);
        mCountPending = view.findViewById(R.id.count_request_pending);
        mCountResolved = view.findViewById(R.id.count_request_resolved);
        mRecyclerView = view.findViewById(R.id.requestsRecyclerView);
        mGrievances = new ArrayList<>();
        myRequestsViewModel = ViewModelProviders.of(this).get(MyRequestsViewModel.class);
        init();
        setupRecyclerView();

        return view;
    }

    private void init()
    {
        getTotalCount();
        getCountPending();
        getCountInProcess();
        getCountResolved();
        getMyRequests();
    }

    private void getTotalCount() {
        myRequestsViewModel.getTotalCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mCountTotal.setText(String.valueOf(integer));
            }
        });



    }

    private void getMyRequests() {
       myRequestsViewModel.getMyRequests().observe(this, new Observer<ArrayList<Grievance>>() {
           @Override
           public void onChanged(ArrayList<Grievance> grievances) {
               mGrievances.addAll(grievances);
               mRequestsAdapter.notifyDataSetChanged();
           }
       });


    }

    private void setupRecyclerView() {

        Log.d(TAG, "setupRecyclerView: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRequestsAdapter = new RequestsAdapter(mGrievances,this);
        mRecyclerView.setAdapter(mRequestsAdapter);

    }

    private void getCountPending(){

        myRequestsViewModel.getGrievanceStatusLiveData("Pending").observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mCountPending.setText(String.valueOf(integer));
            }
        });
    }



    private void getCountInProcess(){


        myRequestsViewModel.getGrievanceStatusLiveData("In Process").observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mCountInProcess.setText(String.valueOf(integer));
            }
        });



    }


    private void getCountResolved(){

        myRequestsViewModel.getGrievanceStatusLiveData("Resolved").observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mCountResolved.setText(String.valueOf(integer));
            }
        });


    }

    @Override
    public void viewGrievance(final String requestID) {

                    Bundle bundle = new Bundle();
                    bundle.putString("grievance_request_id",requestID);
                    Navigation.findNavController(getActivity(),R.id.requestsRecyclerView).navigate(R.id.nav_view_grievance, bundle);

    }
}
