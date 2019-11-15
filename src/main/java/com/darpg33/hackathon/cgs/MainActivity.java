package com.darpg33.hackathon.cgs;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    //vars
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    //widgets
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_grievance, R.id.nav_home, R.id.nav_profile,
                R.id.nav_settings, R.id.nav_share, R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Create new grievance request.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                navController.navigate(R.id.nav_grievance);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

            case R.id.nav_grievance: {
                Log.d(TAG, "onNavigationItemSelected: nav_grievance ");
                navController.navigate(R.id.nav_grievance);
                return true;
            }

            case R.id.nav_home: {
                Log.d(TAG, "onNavigationItemSelected: nav_home ");
                navController.navigate(R.id.nav_home);
                closeDrawer();
                return true;
            }
            case R.id.nav_profile: {
                Log.d(TAG, "onNavigationItemSelected: nav_profile ");
                navController.navigate(R.id.nav_profile);
                closeDrawer();
                return true;
            }
            case R.id.nav_settings: {
                Log.d(TAG, "onNavigationItemSelected: nav_settings ");
                navController.navigate(R.id.nav_settings);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            case R.id.nav_share: {
                Log.d(TAG, "onNavigationItemSelected: nav_share ");
                navController.navigate(R.id.nav_share);
                closeDrawer();
                return true;
            }
            case R.id.nav_sign_out: {
                Log.d(TAG, "onNavigationItemSelected: nav_sign_out ");
                navController.navigate(R.id.nav_sign_out);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
