package com.darpg33.hackathon.cgs.ui.mediator.dashboard;

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
    private String[] xdata = {Fields.GR_STATUS_RESOLVED,
            Fields.GR_STATUS_PENDING,
            Fields.GR_STATUS_IN_PROCESS};
    private DashboardViewModel dashboardViewModel;

    private RequestsAdapter adapter;
    private ArrayList<Grievance> mGrievances = new ArrayList<>();

    //widgets
    private PieChart mPieChart;
    private RecyclerView mRecyclerView;
    private TextView mNoGrievancesText;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);

        mNoGrievancesText = view.findViewById(R.id.textNoGrievances);
        mProgressBar = view.findViewById(R.id.progressBar);

        mPieChart = view.findViewById(R.id.piechart);
        mRecyclerView = view.findViewById(R.id.requestsRecyclerView);



        mPieChart.setCenterText("Grievances");
        mPieChart.setHoleRadius(35f);
        mPieChart.setCenterTextSize(12);
        mPieChart.setTransparentCircleAlpha(0);
        mPieChart.getDescription().setEnabled(false);

        setupRecyclerView();

        dashboardViewModel.getGrievancesCount().observe(this, new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {

                refreshData(integers);

                dashboardViewModel.getImportantGrievances().observe(DashboardFragment.this, new Observer<ArrayList<Grievance>>() {
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
        });

        return view;

    }


    private void setupRecyclerView() {

        Log.d(TAG, "setupRecyclerView: ");
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        adapter = new RequestsAdapter(mGrievances, this);
        mRecyclerView.setAdapter(adapter);
    }


    private void refreshData(ArrayList<Integer> values) {

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
        pieDataSet.setValueTextSize(16);
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

    private void isProcessing(boolean b) {

        if (b) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }


    }


    @Override
    public void viewGrievance(String requestID) {

        Bundle bundle = new Bundle();
        bundle.putString(Fields.DB_GR_REQUEST_ID, requestID);
        Navigation.findNavController(getView()).navigate(R.id.nav_view_grievance, bundle);

    }


}
