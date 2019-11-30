package com.darpg33.hackathon.cgs.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.darpg33.hackathon.cgs.MainActivity;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.ui.dialogs.auth.ResetPasswordDialogFragment;
import com.darpg33.hackathon.cgs.ui.register.RegisterActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        setContentView(R.layout.activity_login);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            loadUI();
        }

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
                                    mProgressBar.setVisibility(View.GONE);
                                }
                                else{
                                    loadUI();
                                    mProgressBar.setVisibility(View.GONE);
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



    private void loadUI()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
