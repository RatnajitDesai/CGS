package com.darpg33.hackathon.cgs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.darpg33.hackathon.cgs.HelperViewModels.ActivityHelperViewModel;
import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.ui.login.LoginActivity;
import com.darpg33.hackathon.cgs.ui.signout.SignOutFragment;
import com.google.android.material.navigation.NavigationView;

public class DepartmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        SignOutFragment.SignOutUserListener {

    private static final String TAG = "DepartmentActivity";

    //vars
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private NavigationView navigationView;
    private ActivityHelperViewModel activityHelperViewModel;



    //widgets
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_view_grievance,
                R.id.nav_dashboard,
                R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        activityHelperViewModel = ViewModelProviders.of(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ActivityHelperViewModel.class);

        navigationView.setNavigationItemSelectedListener(this);

        setDrawerHeader();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Log.d(TAG, "onNavigationItemSelected: item clicked." + menuItem.getItemId());

        switch (menuItem.getItemId()) {

            case R.id.nav_search_grievance: {
                Log.d(TAG, "onNavigationItemSelected: nav_search ");
                navController.navigate(R.id.nav_search);
                closeDrawer();
                return true;
            }
            case R.id.nav_dashboard: {
                Log.d(TAG, "onNavigationItemSelected: nav_home ");
                navController.navigate(R.id.nav_dashboard);
                closeDrawer();
                return true;
            }
            case R.id.nav_department_home: {
                Log.d(TAG, "onNavigationItemSelected: nav_mediator_home ");
                navController.navigate(R.id.nav_department_home);
                closeDrawer();
                return true;
            }
            case R.id.nav_settings: {
                Log.d(TAG, "onNavigationItemSelected: nav_settings ");
                navController.navigate(R.id.nav_settings);
                closeDrawer();
                return true;
            }
            case R.id.nav_sign_out: {
                Log.d(TAG, "onNavigationItemSelected: nav_sign_out ");
                SignOutFragment fragment = new SignOutFragment();
                fragment.setSignOutUserListener(this);
                fragment.show(getSupportFragmentManager(), "Sign out Fragment.");
                closeDrawer();
                return true;
            }
        }

        return false;
    }

    private void closeDrawer() {
        Log.d(TAG, "closeDrawer:");
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void checkUserSignedOutOrNot(boolean userSignedOut) {

        if (userSignedOut) {

            finish();
            startActivity(new Intent(DepartmentActivity.this, LoginActivity.class));

        }

    }

    private void setDrawerHeader() {

        final TextView username = navigationView.getHeaderView(0).findViewById(R.id.drawer_username);
        final TextView email = navigationView.getHeaderView(0).findViewById(R.id.drawer_user_email);
        final TextView dept = navigationView.getHeaderView(0).findViewById(R.id.drawer_user_department);

        activityHelperViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if (user != null) {
                    String username1 = user.getFirst_name() + " " + user.getLast_name();
                    username.setText(username1);
                    email.setText(user.getEmail_id());
                    dept.setText(user.getUser_department());
                }

            }
        });


    }


}
