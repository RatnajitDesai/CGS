package com.darpg33.hackathon.cgs.ui.department.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.ui.request.requestlist.RequestsAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;

public class DashboardFragment extends Fragment implements RequestsAdapter.GrievanceOnClickListener {

    private static final String TAG = "DashboardFragment";

    //vars
    private String[] xdata = {
            Fields.GR_STATUS_RESOLVED,
            Fields.GR_STATUS_PENDING,
            Fields.GR_STATUS_IN_PROCESS};

    private String[] xPriorityData = {"High",
            "Medium",
            "Low"};
    private DashboardViewModel dashboardViewModel;
    private User mUser = new User();
    private RequestsAdapter adapter;
    private ArrayList<Grievance> mGrievances = new ArrayList<>();

    //widgets
    private PieChart mPieChart, mPriorityPieChart;
    private RecyclerView mRecyclerView;
    private TextView mNoGrievancesText;
    private ProgressBar mProgressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_department, container, false);

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        mNoGrievancesText = view.findViewById(R.id.textNoGrievances);
        mProgressBar = view.findViewById(R.id.progressBar);

        mPieChart = view.findViewById(R.id.piechart);
        mPriorityPieChart = view.findViewById(R.id.priorityPieChart);
        mRecyclerView = view.findViewById(R.id.requestsRecyclerView);

        mPieChart.setCenterText("Grievances");
        mPieChart.setHoleRadius(40f);
        mPieChart.setCenterTextSize(10);
        mPieChart.setTransparentCircleAlpha(0);
        mPieChart.getDescription().setText("Grievances by status");

        mPriorityPieChart.setCenterText("Grievances");
        mPriorityPieChart.setHoleRadius(40f);
        mPriorityPieChart.setCenterTextSize(10);
        mPriorityPieChart.setTransparentCircleAlpha(0);
        mPriorityPieChart.getDescription().setText("In Process Grievances by priority");
        setupRecyclerView();
        init();

        return view;
    }

    private void setupRecyclerView() {

        Log.d(TAG, "setupRecyclerView: ");
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        adapter = new RequestsAdapter(mGrievances, this);
        mRecyclerView.setAdapter(adapter);
    }


    private void init() {
        dashboardViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if (user != null) {
                    mUser = user;

                    dashboardViewModel.getGrievancesCount(mUser.getUser_department()).observe(DashboardFragment.this, new Observer<ArrayList<Integer>>() {
                        @Override
                        public void onChanged(ArrayList<Integer> integers) {
                            refreshGrievanceData(integers);
                        }
                    });

                    dashboardViewModel.getPriorityCount(mUser.getUser_department()).observe(DashboardFragment.this, new Observer<ArrayList<Integer>>() {
                        @Override
                        public void onChanged(ArrayList<Integer> integers) {
                            refreshPriorityData(integers);
                        }
                    });

                    isProcessing(true);
                    dashboardViewModel.getImportantGrievances(mUser.getUser_department()).observe(DashboardFragment.this, new Observer<ArrayList<Grievance>>() {
                        @Override
                        public void onChanged(ArrayList<Grievance> grievances) {

                            if (grievances.size() > 0) {
                                mGrievances.clear();
                                mGrievances.addAll(grievances);
                                adapter.notifyDataSetChanged();
                                mNoGrievancesText.setVisibility(View.GONE);
                                isProcessing(false);

                            } else {
                                mNoGrievancesText.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }
            }
        });
    }


    private void isProcessing(boolean b) {

        if (b) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }


    }


    private void refreshGrievanceData(ArrayList<Integer> values) {

        mPieChart.invalidate();
        ArrayList<PieEntry> y = new ArrayList<>();
        ArrayList<String> x = new ArrayList<>(Arrays.asList(xdata).subList(0, xdata.length));

        for (int i = 0; i < values.size(); i++) {
            if (!(values.get(i) <= 0)) {
                y.add(new PieEntry(values.get(i), x.get(i)));
            }
        }

        int total = 0;

        for (Integer i : values) {
            total += i;
        }

        PieDataSet pieDataSet = new PieDataSet(y, "Total : " + total);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(10);
        pieDataSet.setValueTextColor(Color.WHITE);
        mPieChart.setDrawEntryLabels(false);


        ArrayList<Integer> color = new ArrayList<>();

        color.add(Color.rgb(102, 187, 106));
        color.add(Color.rgb(92, 107, 192));
        color.add(Color.rgb(239, 83, 80));

        pieDataSet.setColors(color);

        Legend legend = mPieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);

        PieData pieData = new PieData(pieDataSet);
        mPieChart.setData(pieData);
        mPieChart.invalidate();

    }


    //get priority requests

    private void refreshPriorityData(ArrayList<Integer> values) {

        mPriorityPieChart.invalidate();
        ArrayList<PieEntry> y = new ArrayList<>();
        ArrayList<String> x = new ArrayList<>(Arrays.asList(xPriorityData).subList(0, xPriorityData.length));

        for (int i = 0; i < values.size(); i++) {
            if (!(values.get(i) <= 0)) {
                y.add(new PieEntry(values.get(i), x.get(i)));
            }

        }

        int total = 0;

        for (Integer i : values) {
            total += i;
        }

        PieDataSet pieDataSet = new PieDataSet(y, "Total : " + total);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(10);
        pieDataSet.setValueTextColor(Color.WHITE);
        mPriorityPieChart.setDrawEntryLabels(false);

        ArrayList<Integer> color = new ArrayList<>();
        color.add(Color.rgb(239, 83, 80)); // red
        color.add(Color.rgb(92, 107, 192));// blue
        color.add(Color.rgb(102, 187, 106));//green

        pieDataSet.setColors(color);


        Legend legend = mPriorityPieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);

        PieData pieData = new PieData(pieDataSet);
        mPriorityPieChart.setData(pieData);
        mPriorityPieChart.invalidate();

    }

    @Override
    public void viewGrievance(String requestID) {
        Bundle bundle = new Bundle();
        bundle.putString(Fields.DB_GR_REQUEST_ID, requestID);
        Navigation.findNavController(getView()).navigate(R.id.nav_view_grievance, bundle);

    }
}
