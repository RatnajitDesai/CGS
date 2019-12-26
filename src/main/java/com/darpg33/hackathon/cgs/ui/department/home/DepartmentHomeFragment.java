package com.darpg33.hackathon.cgs.ui.department.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.darpg33.hackathon.cgs.HelperAdapters.SectionsPagerAdapter;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.ui.request.requestlist.InProcessRequestFragment;
import com.darpg33.hackathon.cgs.ui.request.requestlist.PendingRequestFragment;
import com.darpg33.hackathon.cgs.ui.request.requestlist.ResolvedRequestFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DepartmentHomeFragment extends Fragment implements
        ViewPager.OnPageChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "WorkerHomeFragment";

    //vars
    private MenuItem prevMenuItem;
    private DepartmentHomeViewModel departmentHomeViewModel;

    //widgets
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    private BadgeDrawable pendingBadge, inProcessBadge, resolvedBadge;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: called.");

        View root = inflater.inflate(R.layout.fragment_department_home, container, false);

        //widgets
        mViewPager = root.findViewById(R.id.viewpager);
        mBottomNavigationView = root.findViewById(R.id.department_home_nav);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), 3);
        mSectionsPagerAdapter.addFragment(new PendingRequestFragment());
        mSectionsPagerAdapter.addFragment(new InProcessRequestFragment());
        mSectionsPagerAdapter.addFragment(new ResolvedRequestFragment());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        departmentHomeViewModel = ViewModelProviders.of(this).get(DepartmentHomeViewModel.class);

        setBadges();

        return root;
    }

    private void setBadges() {

        pendingBadge = mBottomNavigationView.getOrCreateBadge(R.id.menu_pending);
        inProcessBadge = mBottomNavigationView.getOrCreateBadge(R.id.menu_in_process);
        resolvedBadge = mBottomNavigationView.getOrCreateBadge(R.id.menu_resolved);
        init();
    }


    private void init()
    {

        getCountPending();
        getCountInProcess();
        getCountResolved();

    }

    private void getCountPending(){

        departmentHomeViewModel.getGrievanceCount(Fields.GR_STATUS_PENDING).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > 0) {
                    pendingBadge.setVisible(true);
                    pendingBadge.setNumber(integer);

                }
                else {
                    pendingBadge.setVisible(false);
                }
            }
        });
    }



    private void getCountInProcess(){


        departmentHomeViewModel.getGrievanceCount(Fields.GR_STATUS_IN_PROCESS).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > 0) {
                    inProcessBadge.setVisible(true);
                    inProcessBadge.setNumber(integer);
                }
                else {
                    inProcessBadge.setVisible(false);
                }
            }
        });



    }


    private void getCountResolved(){

        departmentHomeViewModel.getGrievanceCount(Fields.GR_STATUS_RESOLVED).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > 0) {

                    resolvedBadge.setVisible(true);
                    resolvedBadge.setNumber(integer);

                }
                else{

                    resolvedBadge.setVisible(false);

                }
            }
        });

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (prevMenuItem != null)
            prevMenuItem.setChecked(false);
        else
            mBottomNavigationView.getMenu().getItem(0).setChecked(false);
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
        prevMenuItem = mBottomNavigationView.getMenu().getItem(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {

            case R.id.menu_resolved:
            {
                mViewPager.setCurrentItem(2, true);
                break;
            }
            case R.id.menu_in_process:
            {
                mViewPager.setCurrentItem(1, true);
                break;
            }
            case R.id.menu_pending:
            {
                mViewPager.setCurrentItem(0, true);
                break;
            }
        }


        return false;
    }
}
