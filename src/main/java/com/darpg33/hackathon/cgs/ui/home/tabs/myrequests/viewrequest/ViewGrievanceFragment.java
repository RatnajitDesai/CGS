package com.darpg33.hackathon.cgs.ui.home.tabs.myrequests.viewrequest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.darpg33.hackathon.cgs.MainActivity;
import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.TimeDateUtilities;

public class ViewGrievanceFragment extends Fragment {

    private static final String TAG = "ViewGrievanceFragment";


    //vars
    private ViewGrievanceViewModel viewGrievanceViewModel;

    //widgets
    private TextView mTitle, mDescription, mTimeStamp, mStatus;
    private ProgressBar mProgressBar;
    private ScrollView mScrollView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_grievance, container, false);
        mTitle = view.findViewById(R.id.grievanceTitle);
        mScrollView = view.findViewById(R.id.scrollView);
        mDescription = view.findViewById(R.id.grievanceDescription);
        mTimeStamp = view.findViewById(R.id.grievance_timestamp);
        mStatus = view.findViewById(R.id.grievance_status);
        mProgressBar = view.findViewById(R.id.progressBar);
        viewGrievanceViewModel = ViewModelProviders.of(this).get(ViewGrievanceViewModel.class);
        init();

        return view;
    }

    private void init() {
        isProcessing(true);
        Log.d(TAG, "init.");

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            String request_id = bundle.getString("grievance_request_id");

            ((MainActivity)getActivity()).getSupportActionBar().setTitle(request_id);

            viewGrievanceViewModel.getGrievanceData(request_id).observe(this, new Observer<Grievance>() {
                @Override
                public void onChanged(Grievance grievance) {

                    if (grievance != null)
                    {
                        mTitle.setText(grievance.getTitle());
                        mDescription.setText(grievance.getDescription());
                        mTimeStamp.setText(TimeDateUtilities.getDateAndTime(grievance.getTimestamp()));
                        mStatus.setText(grievance.getStatus());
                        isProcessing(false);
                    }
                    else {
                        isProcessing(false);
                        Toast.makeText(getContext(), "Unable to retrieve data at the moment.Please try again.", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(),R.id.nav_view_grievance).navigate(R.id.nav_home);
                    }

                }
            });


        }

    }


    private void isProcessing(boolean b)
    {
        if (b)
        {
            mScrollView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else {

            mScrollView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

}


