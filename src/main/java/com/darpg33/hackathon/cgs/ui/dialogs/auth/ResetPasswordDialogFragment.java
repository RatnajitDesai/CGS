package com.darpg33.hackathon.cgs.ui.dialogs.auth;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.darpg33.hackathon.cgs.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class ResetPasswordDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "ResetPasswordDialogFrag";

    //vars
    private ResetPasswordViewModel resetPasswordViewModel;
    private Context mContext;
    //widgets
    private TextInputEditText mEmail;
    private ProgressBar mProgressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragmant_reset_password,container,false);

        mContext = getContext();
        mEmail = view.findViewById(R.id.emailId);
        mProgressBar = view.findViewById(R.id.progressBar);
        MaterialButton mReset = view.findViewById(R.id.resetPassword);
        MaterialButton mCancel = view.findViewById(R.id.cancel);

        mReset.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        resetPasswordViewModel = ViewModelProviders.of(this).get(ResetPasswordViewModel.class);

        return view;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.resetPassword:
             {
                 if (!mEmail.getText().toString().isEmpty())
                 {
                     resetAccountPasswordFor(mEmail.getText().toString());
                 }
                 else {
                     mEmail.setError("Cannot be empty.");
                 }
                break;
             }
            case R.id.cancel:
            {
                dismiss();
                break;
            }
        }
    }

    private void resetAccountPasswordFor(final String email)
    {
        Log.d(TAG, "resetAccountFor: email : "+email);
        mProgressBar.setVisibility(View.VISIBLE);

        resetPasswordViewModel.resetAccountPasswordFor(email);

        resetPasswordViewModel.resetPasswordLinkSent.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean mailSent) {
                if (mailSent)
                {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "reset password instructions sent to:\n"+email, Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                else {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Unable to reset password for:\n"+email+"\nPlease enter correct registered email id.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
