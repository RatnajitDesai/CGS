package com.darpg33.hackathon.cgs.ui.signout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.darpg33.hackathon.cgs.R;

public class SignOutFragment extends Fragment {
    private SignOutViewModel signOutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        signOutViewModel =
                ViewModelProviders.of(this).get(SignOutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sign_out, container, false);
        final TextView textView = root.findViewById(R.id.text_share);

        signOutViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
