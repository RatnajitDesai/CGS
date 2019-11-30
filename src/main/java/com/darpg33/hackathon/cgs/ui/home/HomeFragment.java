package com.darpg33.hackathon.cgs.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.darpg33.hackathon.cgs.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;

    private FloatingActionButton mFab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: called.");
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mFab = root.findViewById(R.id.fabNewRequest);

        mFab.setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.fabNewRequest:
            {
                Navigation.findNavController(getView()).navigate(R.id.nav_grievance);
                break;
            }


        }
    }
}