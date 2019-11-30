package com.darpg33.hackathon.cgs.ui.register;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.PatternChecker;
import com.darpg33.hackathon.cgs.Utils.SpinnerListProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddressInfoFragment extends Fragment {

    private static final String TAG = "AddressInfoFragment";

    //vars
    private Context mContext;


    //widgets
    private TextInputEditText mEtAddress, mEtPinCode;
    private Spinner mSpSelectCountry, mSpSelectState, mSpSelectDistrict;
    private MaterialButton mNext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_address_info,container,false);
        init(view);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: clicked.");

                String address, pincode, country, state, district;

                address = mEtAddress.getText().toString();
                pincode = mEtPinCode.getText().toString();
                country = mSpSelectCountry.getSelectedItem().toString();
                state = mSpSelectState.getSelectedItem().toString();
                district = mSpSelectDistrict.getSelectedItem().toString();


                if (checkInputs(address,pincode,country,state,district))
                {

                    Bundle bundle = new Bundle();

                    if (getArguments()!= null)
                    {

                        bundle.putBundle(getString(R.string.personal_info),getArguments());
                        bundle.putString(getString(R.string.address),address);
                        bundle.putString(getString(R.string.pin_code),pincode);
                        bundle.putString(getString(R.string.country),country);
                        bundle.putString(getString(R.string.state),state);
                        bundle.putString(getString(R.string.district),district);

                        Navigation.findNavController(Objects.requireNonNull(getActivity()),R.id.btnNext).navigate(R.id.nav_contact_info, bundle);
                    }
                    else {

                        Toast.makeText(mContext, "Please enter all details on previous screen.", Toast.LENGTH_SHORT).show();
                    }

                }





            }
        });

        return view;
    }

    private void init(View view)
    {

        mContext = getContext();
        mEtAddress = view.findViewById(R.id.address);
        mEtPinCode = view.findViewById(R.id.pinCode);

        final int listSize = SpinnerListProvider.getCountryList().size()-1;
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, SpinnerListProvider.getCountryList()){
            @Override
            public int getCount() {

                return listSize;
            }
        };

        mSpSelectCountry = view.findViewById(R.id.spinner_select_country);
        mSpSelectCountry.setAdapter(countryAdapter);
        mSpSelectCountry.setSelection(listSize);


        final int stateListSize = SpinnerListProvider.getStateList().size()-1;
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, SpinnerListProvider.getStateList()){
            @Override
            public int getCount() {
                return stateListSize;
            }
        };
        mSpSelectState = view.findViewById(R.id.spinner_select_state);
        mSpSelectState.setAdapter(stateAdapter);
        mSpSelectState.setSelection(stateListSize);


        final int districtListSize = SpinnerListProvider.getDistrictList().size() - 1;
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, SpinnerListProvider.getDistrictList()){

            @Override
            public int getCount() {
                return districtListSize;
            }
        };
        mSpSelectDistrict = view.findViewById(R.id.spinner_select_district);
        mSpSelectDistrict.setAdapter(districtAdapter);
        mSpSelectDistrict.setSelection(districtListSize);

        mNext = view.findViewById(R.id.btnNext);


    }


    private boolean checkInputs(String address, String pinCode, String country, String state, String district)
    {
        if (pinCode.isEmpty() || address.isEmpty())
        {
            Toast.makeText(mContext, "Please enter all the details", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!PatternChecker.isValidPinCode(pinCode))
        {
            mEtPinCode.setError("Enter valid pin code");
            return false;
        }
        if(country.equals("Select Country"))
        {
            mSpSelectCountry.setFocusable(true);
            return false;
        }
        if (state.equals("Select State"))
        {
            mSpSelectState.setFocusable(true);
            return false;
        }
        if (district.equals("Select District"))
        {
            mSpSelectDistrict.setFocusable(true);
            return false;
        }

        return true;
    }



}
