package com.darpg33.hackathon.cgs.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.darpg33.hackathon.cgs.DepartmentActivity;
import com.darpg33.hackathon.cgs.MainActivity;
import com.darpg33.hackathon.cgs.MediatorActivity;
import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.WorkerActivity;
import com.darpg33.hackathon.cgs.ui.dialogs.auth.ResetPasswordDialogFragment;
import com.darpg33.hackathon.cgs.ui.register.RegisterActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";

    //vars
     LoginViewModel loginViewModel;

    //widgets
    private MaterialButton mSignIn;
    private TextView mCreateAccount, mForgotPassword;
    private TextInputEditText mEmail,mPassword;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mSignIn = findViewById(R.id.signInBtn);
        mCreateAccount = findViewById(R.id.createAccountLink);
        mForgotPassword = findViewById(R.id.forgotPasswordLink);
        mEmail = findViewById(R.id.emailId);
        mPassword = findViewById(R.id.password);
        mProgressBar = findViewById(R.id.progressBar);
        mSignIn.setOnClickListener(this);
        mCreateAccount.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            loadUI(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.signInBtn:
            {

                mProgressBar.setVisibility(View.VISIBLE);

                if (checkInputs(mEmail.getText().toString(), mPassword.getText().toString()))
                {

                    loginViewModel.signInUser(mEmail.getText().toString(), mPassword.getText().toString());

                    loginViewModel.user.observe(LoginActivity.this, new Observer<FirebaseUser>() {
                        @Override
                        public void onChanged(FirebaseUser user) {
                            if (user != null)
                            {
                                if (!user.isEmailVerified())
                                {

                                    Toast.makeText(LoginActivity.this, "Email ID is not verified.", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    mProgressBar.setVisibility(View.GONE);

                                }
                                else{
                                    loadUI(user.getUid());
                                }
                            }
                            else {
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Incorrect Email/password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                break;
            }
            case R.id.createAccountLink:
            {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            }
            case R.id.forgotPasswordLink:
            {
                Log.d(TAG, "onClick: Forgot password.");
                ResetPasswordDialogFragment fragment = new ResetPasswordDialogFragment();
                fragment.show(getSupportFragmentManager(),"LoginActivity");
                break;
            }

        }
    }


    private boolean checkInputs(String email, String password)
    {
        if (email.isEmpty())
        {
            mEmail.setError("Cannot be empty");
            return false;
        }
        if (password.isEmpty())
        {
            mPassword.setError("Cannot be empty");
            return false;
        }

        return true;
    }


    //Load ui based on user

    private void loadUI(String uid)
    {

        loginViewModel.getUser(uid).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if (user!= null)
                {
                    switch (user.getUser_type()){
                        case Fields.USER_TYPE_CITIZEN:
                        {
                            mProgressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(getString(R.string.user_type),user.getUser_type());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            mProgressBar.setVisibility(View.GONE);

                            break;
                        }
                        case Fields.USER_TYPE_MEDIATOR:
                        {
                            Intent intent = new Intent(LoginActivity.this, MediatorActivity.class);
                            intent.putExtra(getString(R.string.user_type),user.getUser_type());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            mProgressBar.setVisibility(View.GONE);
                            break;
                        }
                        case Fields.USER_TYPE_DEP_INCHARGE:
                        {
                            Intent intent = new Intent(LoginActivity.this, DepartmentActivity.class);
                            intent.putExtra(getString(R.string.user_type),user.getUser_type());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            mProgressBar.setVisibility(View.GONE);
                            break;
                        }
                        case Fields.USER_TYPE_DEP_WORKER:
                        {
                            Intent intent = new Intent(LoginActivity.this, WorkerActivity.class);
                            intent.putExtra(getString(R.string.user_type),user.getUser_type());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            mProgressBar.setVisibility(View.GONE);
                            break;
                        }

                    }


                }
            }
        });



    }


    private void setAppLocale(String locale) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(locale));
        resources.updateConfiguration(configuration, metrics);
        Log.d(TAG, "setAppLocale: :" + locale);

        //shared preferences
        SharedPreferences.Editor editor = getSharedPreferences(Fields.SHARED_PREF_SETTINGS, Context.MODE_PRIVATE).edit();
        editor.putString(Fields.SHARED_PREF_SETTINGS_LOCALE, locale);
        editor.apply();

    }

    private void loadLocale() {
        SharedPreferences preferences = getSharedPreferences(Fields.SHARED_PREF_SETTINGS, Activity.MODE_PRIVATE);
        String locale = preferences.getString(Fields.SHARED_PREF_SETTINGS_LOCALE, Locale.getDefault().getLanguage());
        setAppLocale(locale);
    }

}
