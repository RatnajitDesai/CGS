package com.darpg33.hackathon.cgs.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "EditProfileFragment";


    //vars
    private EditProfileViewModel editProfileViewModel;
    private User mUser;


    //widgets
    private TextInputEditText mFirstName, mLastName, mPhoneNumber, mEmailId;
    private TextView mAddress, mCountry, mState, mDistrict, mPinCode;
    private RadioGroup mGender;
    private ProgressBar mProgressBar;
    private FloatingActionButton mfab;
    private RadioButton mMale, mFemale, mTransgender;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        editProfileViewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
        init(view);
        return view;
    }


    private void init(View view) {
        Log.d(TAG, "init: ");

        mFirstName = view.findViewById(R.id.firstName);
        mLastName = view.findViewById(R.id.lastName);
        mGender = view.findViewById(R.id.rgGender);
        mMale = view.findViewById(R.id.rbMale);
        mFemale = view.findViewById(R.id.rbFemale);
        mTransgender = view.findViewById(R.id.rbTransGender);
        mPhoneNumber = view.findViewById(R.id.phoneNumber);
        mEmailId = view.findViewById(R.id.emailId);
        mAddress = view.findViewById(R.id.address);
        mCountry = view.findViewById(R.id.country);
        mState = view.findViewById(R.id.state);
        mDistrict = view.findViewById(R.id.district);
        mPinCode = view.findViewById(R.id.pinCode);
        mProgressBar = view.findViewById(R.id.progressBar);
        mfab = view.findViewById(R.id.fabSaveProfile);
        mfab.setOnClickListener(this);

        User user = Objects.requireNonNull(getArguments()).getParcelable("user");
        mUser = user;
        Log.d(TAG, "init: "+user.toString());
        setUpWidgets(user);

    }

    private void setUpWidgets(User user) {

        if (user != null) {

            mFirstName.setText(user.getFirst_name());
            mLastName.setText(user.getLast_name());

            switch (user.getGender()) {
                case "Male": {
                    mGender.check(R.id.rbMale);

                    break;
                }
                case "Female": {
                    mGender.check(R.id.rbFemale);
                    break;
                }
                case "Transgender": {
                    mGender.check(R.id.rbTransGender);
                    break;
                }
            }

            mPhoneNumber.setText(user.getPhone_number().substring(3));
            mEmailId.setText(user.getEmail_id());
            mAddress.setText(user.getAddress());
            mCountry.setText(user.getCountry());
            mState.setText(user.getState());
            mDistrict.setText(user.getDistrict());
            mPinCode.setText(user.getPin_code());

        }


    }

    @Override
    public void onClick(final View v) {

        if (v.getId() == R.id.fabSaveProfile) {

            if (checkInputs(mFirstName.getText().toString(), mLastName.getText().toString())) {

                mProgressBar.setVisibility(View.VISIBLE);
                disableViews(mFirstName,mLastName,mMale,mFemale,mTransgender,mPhoneNumber,mEmailId);
                mUser.setFirst_name(mFirstName.getText().toString());
                mUser.setLast_name(mLastName.getText().toString());

                switch (mGender.getCheckedRadioButtonId()) {
                    case R.id.rbMale: {
                        mUser.setGender("Male");
                        break;
                    }
                    case R.id.rbFemale: {
                        mUser.setGender("Female");
                        break;
                    }
                    case R.id.rbTransGender: {
                        mUser.setGender("Transgender");
                        break;
                    }
                }
                Log.d(TAG, "onClick: "+mUser.toString());

                editProfileViewModel.updateUserProfile(mUser).observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {

                        if (user != null) {

                            Snackbar.make(getView(), "Profile updated.", Snackbar.LENGTH_LONG).show();
                            Navigation.findNavController(getActivity(), R.id.fabSaveProfile).navigate(R.id.nav_profile);

                            mProgressBar.setVisibility(View.GONE);
                            disableViews(mFirstName,mLastName,mMale,mFemale,mTransgender,mPhoneNumber,mEmailId);
                        } else {
                            Snackbar.make(getView(), "Could not update profile.", Snackbar.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                            enableViews(mFirstName,mLastName,mMale,mFemale,mTransgender,mPhoneNumber,mEmailId);
                        }
                    }
                });
            }
        }
    }


    private boolean checkInputs(String firstname, String lastname)
    {
        if (firstname.isEmpty())
        {
            mFirstName.setError("Cannot be empty");
            return false;
        }
        if (lastname.isEmpty())
        {
             mLastName.setError("Cannot be empty");
            return false;
        }

        return true;
    }

    private void enableViews(View... views)
    {
        for (View v:views)
        {
            v.setEnabled(true);
        }

    }


    private void disableViews(View... views)
    {
        for (View v:views)
        {
            v.setEnabled(false);
        }

    }



}
