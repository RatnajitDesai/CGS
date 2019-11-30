package com.darpg33.hackathon.cgs.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ProfileFragment";

    //vars
    private ProfileViewModel profileViewModel;
    private Context mContext;
    private User mUser;


    //widgets
    private TextInputEditText mFirstName, mLastName,mPhoneNumber, mEmailId;
    private TextView mAddress, mCountry, mState, mDistrict, mPinCode;
    private RadioGroup mGender;
    private FloatingActionButton mfab;
    private RadioButton mMale, mFemale, mTransgender;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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
        mfab = view.findViewById(R.id.fabEditProfile);
        mfab.setOnClickListener(this);
        mContext = getContext();
        setUpWidgets();

    }

    private void setUpWidgets() {

        profileViewModel.getUserInfo().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if (user != null)
                {

                    mUser = user;

                    mFirstName.setText(user.getFirst_name());
                    mLastName.setText(user.getLast_name());

                    switch (user.getGender())
                    {
                        case "Male":
                        {
                            mGender.check(R.id.rbMale);
                            mMale.setEnabled(true);
                            mFemale.setEnabled(false);
                            mTransgender.setEnabled(false);
                            break;
                        }
                        case "Female":
                        {
                            mGender.check(R.id.rbFemale);
                            mMale.setEnabled(false);
                            mFemale.setEnabled(true);
                            mTransgender.setEnabled(false);
                            break;
                        }
                        case "Transgender":
                        {
                            mGender.check(R.id.rbTransGender);
                            mMale.setEnabled(false);
                            mFemale.setEnabled(false);
                            mTransgender.setEnabled(true);
                            break;
                        }
                    }

                    mPhoneNumber.setText(user.getPhone_number());
                    mEmailId.setText(user.getEmail_id());
                    mAddress.setText(user.getAddress());
                    mCountry.setText(user.getCountry());
                    mState.setText(user.getState());
                    mDistrict.setText(user.getDistrict());
                    mPinCode.setText(user.getPin_code());

                }
                else {
                    Toast.makeText(mContext, "Unable to retrieve information right now.\nPlease check your network connection.", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigate(R.id.nav_home);
                }

            }
        });
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fabEditProfile)
        {

            if (mUser != null)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("user",mUser);
                Navigation.findNavController(getActivity(),R.id.fabEditProfile).navigate(R.id.nav_edit_profile,bundle);

            }
        }
    }





}
