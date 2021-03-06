package com.darpg33.hackathon.cgs.ui.dialogs.viewProfile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

public class ViewProfileDialog extends BottomSheetDialogFragment {

    private static final String TAG = "ViewProfileDialog";

    //widgets
    private TextView mUsername, mGender, mPhoneNumber, mEmail, mAddress;
    private TextView mOfficerUsername, mOfficerPost, mOfficerDepartment;
    private RelativeLayout mCitizenLayout;
    private LinearLayout mOfficerLayout;

    public ViewProfileDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_view_profile, container, false);

        mCitizenLayout = view.findViewById(R.id.citizenRelativeLayout);
        mOfficerLayout = view.findViewById(R.id.linOfficerLayout);
        mUsername = view.findViewById(R.id.username);
        mGender = view.findViewById(R.id.gender);
        mPhoneNumber = view.findViewById(R.id.phoneNumber);
        mEmail = view.findViewById(R.id.emailId);
        mAddress = view.findViewById(R.id.address);
        mOfficerDepartment = view.findViewById(R.id.officerDepartment);
        mOfficerPost = view.findViewById(R.id.officerPost);
        mOfficerUsername = view.findViewById(R.id.officerUsername);

        Bundle bundle = getArguments();
        if (bundle != null) {
            init(bundle);
        }
        return view;
    }

    private void init(Bundle bundle) {
        Log.d(TAG, "init: ");
        User user = bundle.getParcelable(Fields.BUNDLE_USER_INFO);

        if (user != null) {
            if (Fields.USER_TYPE_CITIZEN.equals(user.getUser_type())) {
                mOfficerLayout.setVisibility(View.GONE);
                mCitizenLayout.setVisibility(View.VISIBLE);
                mUsername.setText(String.format(Locale.ENGLISH, "%s %s", user.getFirst_name(), user.getLast_name()));
                mGender.setText(user.getGender());
                mPhoneNumber.setText(user.getPhone_number());
                mAddress.setText(String.format("%S, %S, %S,%S, %S", user.getAddress(), user.getDistrict(), user.getState(), user.getCountry(), user.getPin_code()));
                mEmail.setText(user.getEmail_id());
            } else {
                mCitizenLayout.setVisibility(View.GONE);
                mOfficerLayout.setVisibility(View.VISIBLE);
                mOfficerUsername.setText(String.format(Locale.ENGLISH, "%s %s", user.getFirst_name(), user.getLast_name()));
                mOfficerPost.setText(user.getUser_type());
                mOfficerDepartment.setText(user.getUser_department());
            }
        }
    }
}
