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
import androidx.viewpager.widget.ViewPager;

import com.darpg33.hackathon.cgs.HelperAdapters.SectionsPagerAdapter;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.ui.home.tabs.actions.ActionItemsFragment;
import com.darpg33.hackathon.cgs.ui.home.tabs.feed.FeedsFragment;
import com.darpg33.hackathon.cgs.ui.home.tabs.myrequests.MyRequestsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    //vars
    private HomeViewModel homeViewModel;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    //widgets
    private FloatingActionButton mFab;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: called.");
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mFab = root.findViewById(R.id.fabNewRequest);
        mViewPager = root.findViewById(R.id.viewpager);
        mTabLayout = root.findViewById(R.id.tabLayout);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),3);

        mSectionsPagerAdapter.addFragment(new FeedsFragment(), "Feed");
        mSectionsPagerAdapter.addFragment(new MyRequestsFragment(),"Requests");
        mSectionsPagerAdapter.addFragment(new ActionItemsFragment(),"Actions");
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setTabIcons();
        mFab.setOnClickListener(this);
        return root;

    }

    private void setTabIcons() {

        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_menu_home);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_icon_myrequests);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_icon_notifications);


    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fabNewRequest) {
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.nav_new_grievance);
        }
    }
}