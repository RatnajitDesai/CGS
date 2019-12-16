package com.darpg33.hackathon.cgs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.darpg33.hackathon.cgs.ui.login.LoginActivity;
import com.darpg33.hackathon.cgs.ui.signout.SignOutFragment;
import com.google.android.material.navigation.NavigationView;

public class DepartmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        SignOutFragment.SignOutUserListener {

    private static final String TAG = "DepartmentActivity";

    //vars
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    //widgets
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_search, R.id.nav_view_grievance,
                R.id.nav_dashboard, R.id.nav_home,
                R.id.nav_settings, R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.search_actionbar_menu, menu);

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


}
