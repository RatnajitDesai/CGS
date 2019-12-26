package com.darpg33.hackathon.cgs.ui.mediator.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.ui.request.requestlist.RequestsAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MediatorSearchFragment extends Fragment implements RequestsAdapter.GrievanceOnClickListener, TextWatcher {

    private static final String TAG = "SearchActivity";

    //vars
    private ArrayList<Grievance> mGrievances = new ArrayList<>();
    private RequestsAdapter mRequestsAdapter;
    private MediatorSearchViewModel mediatorSearchViewModel;

    //widgets
    private TextInputEditText mSearchText;
    private RecyclerView mRequestsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchText = view.findViewById(R.id.etSearch);
        mRequestsRecyclerView = view.findViewById(R.id.requestsRecyclerView);
        mediatorSearchViewModel = ViewModelProviders.of(this).get(MediatorSearchViewModel.class);
        setupRecyclerView();
        mSearchText.addTextChangedListener(this);

        return view;
    }

    private void setupRecyclerView() {

        // set up recycler view
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRequestsRecyclerView.setLayoutManager(manager);
        mRequestsAdapter = new RequestsAdapter(mGrievances, this);
        mRequestsRecyclerView.setAdapter(mRequestsAdapter);

    }


    @Override
    public void viewGrievance(String requestID) {

        Log.d(TAG, "viewGrievance: " + requestID);

        Log.d(TAG, "viewGrievance: " + requestID);
        Bundle bundle = new Bundle();
        bundle.putString(Fields.DB_GR_REQUEST_ID, requestID);
        Navigation.findNavController(getView()).navigate(R.id.nav_view_grievance, bundle);


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(final CharSequence searchText, int start, int before, int count) {


        mediatorSearchViewModel.getGrievances(String.valueOf(searchText)).observe(this, new Observer<ArrayList<Grievance>>() {
            @Override
            public void onChanged(ArrayList<Grievance> grievances) {

                mGrievances.clear();
                mGrievances.addAll(grievances);
                mRequestsAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onDetach() {
        super.onDetach();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

    }
}
