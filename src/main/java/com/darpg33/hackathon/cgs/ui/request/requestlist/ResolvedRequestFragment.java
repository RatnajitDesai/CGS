package com.darpg33.hackathon.cgs.ui.request.requestlist;

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

import com.darpg33.hackathon.cgs.HelperAdapters.RequestsAdapter;
import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.R;

import java.util.ArrayList;

public class ResolvedRequestFragment extends Fragment implements RequestsAdapter.GrievanceOnClickListener {

    private static final String TAG = "ResolvedRequestFragment";

    //vars
    private RequestsAdapter mRequestsAdapter;
    private ArrayList<Grievance> mGrievances;
    private ResolvedRequestViewModel mResolvedRequestViewModel;

    //widgets
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_resolved_requests, container, false);
        mRecyclerView = view.findViewById(R.id.requestsRecyclerView);
        mGrievances = new ArrayList<>();
        mResolvedRequestViewModel = ViewModelProviders.of(this).get(ResolvedRequestViewModel.class);
        init();
        setupRecyclerView();

        return view;
    }

    private void init()
    {

        getPendingRequests();
    }

    private void getPendingRequests() {

        mResolvedRequestViewModel.getAllResolvedRequests().observe(this, new Observer<ArrayList<Grievance>>() {
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


    @Override
    public void viewGrievance(String requestID) {

        Bundle bundle = new Bundle();
        bundle.putString("grievance_request_id",requestID);
        Navigation.findNavController(getActivity(),R.id.requestsRecyclerView).navigate(R.id.nav_view_grievance, bundle);


    }


}
