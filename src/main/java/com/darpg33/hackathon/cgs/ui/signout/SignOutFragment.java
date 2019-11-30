package com.darpg33.hackathon.cgs.ui.signout;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.darpg33.hackathon.cgs.R;
import com.google.android.material.button.MaterialButton;

public class SignOutFragment extends DialogFragment implements View.OnClickListener {
    private SignOutViewModel signOutViewModel;

    public interface SignOutUserListener{

        void checkUserSignedOutOrNot(boolean isUserSignedOut);
    }

    private SignOutUserListener mListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        signOutViewModel =
                ViewModelProviders.of(this).get(SignOutViewModel.class);

        View root = inflater.inflate(R.layout.fragment_sign_out, container, false);

        init(root);

        return root;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }


    private void init(View root) {

        MaterialButton btnSignOut = root.findViewById(R.id.btnSignOut);
        MaterialButton btnCancel = root.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);

    }

    public void setSignOutUserListener(SignOutUserListener listener)
    {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnSignOut:
            {

                signOutViewModel.signOutCurrentUser().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean userSignedOut) {
                        if(userSignedOut)
                        {
                            Toast.makeText(getContext(), "Signing out.", Toast.LENGTH_SHORT).show();
                            mListener.checkUserSignedOutOrNot(true);
                            dismiss();
                        }
                        else {
                            Toast.makeText(getContext(), "Unable to sign out user.Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            }
            case R.id.btnCancel:
            {
                mListener.checkUserSignedOutOrNot(false);
                dismiss();
                break;
            }


        }


    }
}
