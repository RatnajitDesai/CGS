package com.darpg33.hackathon.cgs.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.darpg33.hackathon.cgs.HelperAdapters.SectionsPagerAdapter;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.ui.home.tabs.actions.NotificationsFragment;
import com.darpg33.hackathon.cgs.ui.home.tabs.feed.FeedsFragment;
import com.darpg33.hackathon.cgs.ui.home.tabs.myrequests.MyRequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener {

    private static final String TAG = "HomeFragment";

    //vars
    private MenuItem prevMenuItem;

    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: called.");

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //widgets
        FloatingActionButton mFab = root.findViewById(R.id.fabNewRequest);
        mViewPager = root.findViewById(R.id.viewpager);
        mBottomNavigationView = root.findViewById(R.id.citizen_home_nav);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), 3);
        mSectionsPagerAdapter.addFragment(new FeedsFragment());
        mSectionsPagerAdapter.addFragment(new MyRequestsFragment());
        mSectionsPagerAdapter.addFragment(new NotificationsFragment());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mFab.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fabNewRequest) {
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.nav_new_grievance);
        }
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

            case R.id.menu_actions:
            {
                mViewPager.setCurrentItem(2, true);
                break;
            }
            case R.id.menu_my_requests:
            {
                mViewPager.setCurrentItem(1, true);
                break;
            }
            case R.id.menu_feed:
            {
                mViewPager.setCurrentItem(0, true);
                break;
            }
        }


        return false;
    }
}