package com.darpg33.hackathon.cgs.ui.register;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.darpg33.hackathon.cgs.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class PersonalInfoFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "PersonalInfoFragment";

    //vars
    private Context mContext;
    private RegisterViewModel registerViewModel;
    private View view;



    //widgets
    private MaterialButton mNext;
    private TextInputEditText mEtFirstName, mEtLastName;
    private RadioGroup mRgGender;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_personal_jnfo, container, false);

        init(view);

        registerViewModel = new RegisterViewModel();


        mNext.setOnClickListener(this);



        return view;
    }




    private void init(View view)
    {

        mContext = getContext();

        mEtFirstName = view.findViewById(R.id.firstName);
        mEtLastName = view.findViewById(R.id.lastName);
        mRgGender = view.findViewById(R.id.rgGender);
        mNext = view.findViewById(R.id.btnNext);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnNext) {
            String firstName = mEtFirstName.getText().toString();
            String lastName = mEtLastName.getText().toString();
            String gender;
            switch (mRgGender.getCheckedRadioButtonId()) {
                case R.id.rbMale: {
                    gender = "Male";
                    break;
                }
                case R.id.rbFemale: {
                    gender = "Female";
                    break;
                }
                case R.id.rbTransGender: {
                    gender = "Transgender";
                    break;
                }
                default: {
                    gender = "NA";
                    break;
                }
            }


            if (checkInputs(firstName, lastName)) {
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.first_name), firstName);
                bundle.putString(getString(R.string.last_name), lastName);
                bundle.putString(getString(R.string.gender), gender);
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.btnNext).navigate(R.id.nav_address_info, bundle);
            }
        }

    }




    private boolean checkInputs(String firstName, String lastName)
    {
        if (firstName.isEmpty())
        {
            mEtFirstName.setError("Cannot be empty");
            return false;
        }
        if (lastName.isEmpty())
        {
            mEtLastName.setError("Cannot be empty");
            return false;
        }

        return true;
    }


}
