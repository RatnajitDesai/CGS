package com.darpg33.hackathon.cgs.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.Utils.PatternChecker;
import com.darpg33.hackathon.cgs.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ContactInfoFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ContactInfoFragment";

    //widgets
    private TextInputEditText  mEtPhoneNumber, mEtEmailId, mEtPassword, mEtConfPassword;
    private MaterialButton mRegister;

    //vars
    private RegisterViewModel registerViewModel;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Context mContext;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private String phone_number,email, password, confPassword, firstname, lastname, gender, address,pincode, country, state, district;
    private Bundle mBundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_info,container,false);
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        init(view);

        mRegister.setOnClickListener(this);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                //signInWithPhoneAuthCredential(credential);

                mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Log.d(TAG, "onSuccess: phone number verified.");
                        final FirebaseUser user = mAuth.getCurrentUser();

                        if (user!=null) {

                            final AuthCredential emailAuthCredential = EmailAuthProvider.getCredential(email, password);

                            user.linkWithCredential(emailAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            User NewUser = new User();
                                            NewUser.setUser_id(user.getUid());
                                            NewUser.setTimestamp(new Timestamp(new Date()));
                                            NewUser.setUser_type(Fields.USER_TYPE_CITIZEN);
                                            NewUser.setEmail_id(email);
                                            NewUser.setFirst_name(firstname);
                                            NewUser.setLast_name(lastname);
                                            NewUser.setGender(gender);
                                            NewUser.setAddress(address);
                                            NewUser.setPhone_number(phone_number);
                                            NewUser.setPin_code(pincode);
                                            NewUser.setCountry(country);
                                            NewUser.setState(state);
                                            NewUser.setDistrict(district);
                                            NewUser.setRegistered(true);
                                            saveUserToDatabase(NewUser);
                                            getActivity().finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: "+e.getMessage() );
                                            Toast.makeText(mContext, "Unable to send verification mail.\n Please retry after some time.Or try with other email ID.", Toast.LENGTH_LONG).show();
                                            mProgressBar.setVisibility(View.GONE);
                                            enableViews(mEtEmailId,mEtPhoneNumber,mEtPassword,mEtConfPassword, mRegister);
                                            user.delete();
                                            getActivity().finish();
                                        }
                                    });
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (e instanceof FirebaseAuthUserCollisionException) {
                                                Log.d(TAG, "onFailure: User with this email id already exists.");
                                                Toast.makeText(mContext, "User with email :"+email+" already exists.", Toast.LENGTH_SHORT).show();
                                                mProgressBar.setVisibility(View.GONE);
                                                enableViews(mEtEmailId,mEtPhoneNumber,mEtPassword,mEtConfPassword, mRegister);
                                                user.delete();
                                            } else {
                                                Toast.makeText(mContext, "Unable to register. Email already exists.", Toast.LENGTH_SHORT).show();
                                                mProgressBar.setVisibility(View.GONE);
                                                enableViews(mEtEmailId,mEtPhoneNumber,mEtPassword,mEtConfPassword, mRegister);
                                                user.delete();

                                            }
                                        }
                                    });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if (e instanceof FirebaseAuthUserCollisionException) {
                            Log.d(TAG, "onFailure: User with this phone number already exists.");
                            Toast.makeText(mContext, "User with this phone number already exists.", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                            enableViews(mEtEmailId, mEtPhoneNumber, mEtPassword, mEtConfPassword, mRegister);
                        } else {
                            Toast.makeText(mContext, "Unable to register with given phone number.", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                            enableViews(mEtEmailId, mEtPhoneNumber, mEtPassword, mEtConfPassword, mRegister);
                        }


                    }
                });
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(mContext, "OTP denied.", Toast.LENGTH_SHORT).show();
                    mAuth.getCurrentUser().delete();
                    mProgressBar.setVisibility(View.GONE);
                    enableViews(mEtEmailId,mEtPhoneNumber,mEtPassword,mEtConfPassword, mRegister);

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(mContext, "Too many request.Please try again after some time.", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    mAuth.getCurrentUser().delete();
                    enableViews(mEtEmailId, mEtPhoneNumber, mEtPassword, mEtConfPassword, mRegister);
                } else if (e instanceof FirebaseAuthUserCollisionException) {

                    Toast.makeText(mContext, "User already exists!", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    mAuth.getCurrentUser().delete();
                    enableViews(mEtEmailId,mEtPhoneNumber,mEtPassword,mEtConfPassword, mRegister);

                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                Log.d(TAG, "onCodeSent:" + verificationId);
                mBundle = new Bundle();

                mBundle.putString(getString(R.string.first_name),firstname);
                mBundle.putString(getString(R.string.last_name),lastname);
                mBundle.putString(getString(R.string.gender),gender);
                mBundle.putString(getString(R.string.address),address);
                mBundle.putString(getString(R.string.pin_code),pincode);
                mBundle.putString(getString(R.string.country),country);
                mBundle.putString(getString(R.string.state),state);
                mBundle.putString(getString(R.string.district),district);
                mBundle.putString(getString(R.string.phone_number),phone_number);
                mBundle.putString(getString(R.string.email_id),email);
                mBundle.putString(getString(R.string.password),password);
                mBundle.putString(getString(R.string.verification_Id), verificationId); // verification code required to construct credential.
                mBundle.putParcelable(getString(R.string.resend_token),token);
                mProgressBar.setVisibility(View.GONE);
                enableViews(mEtEmailId,mEtPhoneNumber,mEtPassword,mEtConfPassword, mRegister);
                Navigation.findNavController(getView()).navigate(R.id.nav_otp_verification, mBundle);
                Toast.makeText(getContext(), "New OTP sent to your number", Toast.LENGTH_SHORT).show();

                // Save verification ID and resending token so we can use them later


                // ...
            }
        };


        return view;
    }

    private void saveUserToDatabase(User newUser) {


        Log.d(TAG, "saveUserToDatabase: saving user."+newUser.toString());

        registerViewModel.saveUserToDatabase(newUser).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if (user!=null)
                {
                    mAuth.signOut();
                    Toast.makeText(mContext, "Phone number verified successfully.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "Verify your email address and Sign In.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onChanged: User saved.");
                    mProgressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
                else
                {
                    Toast.makeText(mContext, "Error occurred.Unable to register user.Try again.", Toast.LENGTH_SHORT).show();
                    mAuth.getCurrentUser().delete();
                    mAuth.signOut();
                    mProgressBar.setVisibility(View.GONE);
                    enableViews(mEtEmailId,mEtPhoneNumber,mEtPassword,mEtConfPassword, mRegister);
                    Log.d(TAG, "onChanged: User not saved.");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }

            }
        });


    }

    private void init(View view)
    {

        mContext = getContext();
        mAuth = FirebaseAuth.getInstance();
        mProgressBar = view.findViewById(R.id.progressBar);
        mEtPhoneNumber = view.findViewById(R.id.phoneNumber);
        mEtEmailId = view.findViewById(R.id.emailId);
        mEtPassword = view.findViewById(R.id.password);
        mEtConfPassword = view.findViewById(R.id.confirmPassword);
        mRegister = view.findViewById(R.id.signUpBtn);
        if (getArguments() != null)
        {

            Bundle bundle = getArguments();
            firstname = bundle.getBundle(getString(R.string.personal_info)).getString(getString(R.string.first_name));
            lastname = bundle.getBundle(getString(R.string.personal_info)).getString(getString(R.string.last_name));
            gender = bundle.getBundle(getString(R.string.personal_info)).getString(getString(R.string.gender));
            address = bundle.getString(getString(R.string.address));
            pincode = bundle.getString(getString(R.string.pin_code));
            country = bundle.getString(getString(R.string.country));
            state = bundle.getString(getString(R.string.state));
            district = bundle.getString(getString(R.string.district));
            
        }
        else {

            Toast.makeText(mContext, "Provide all the details on previous screen.", Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.GONE);

        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.signUpBtn)
        {


            phone_number = "+91"+mEtPhoneNumber.getText().toString();
            email = mEtEmailId.getText().toString();
            password = mEtPassword.getText().toString();
            confPassword = mEtConfPassword.getText().toString();

            if (checkInputs(mEtPhoneNumber.getText().toString(),mEtEmailId.getText().toString(),
                    mEtPassword.getText().toString(), mEtConfPassword.getText().toString())) {


                registerViewModel.checkIfUserExists(phone_number, email).observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {

                        if (!aBoolean) //user does not exists
                        {
                            Log.d(TAG, "onClick: clicked.");
                            mProgressBar.setVisibility(View.VISIBLE);
                            disableViews(mEtEmailId, mEtPassword, mEtConfPassword, mEtPhoneNumber, mRegister);
                            verifyPhoneNumber(phone_number);
                        } else {
                            mProgressBar.setVisibility(View.GONE);
                            enableViews(mEtEmailId, mEtPassword, mEtConfPassword, mEtPhoneNumber, mRegister);
                            Toast.makeText(mContext, "user with given phone/Email already exists.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
    }

    private void verifyPhoneNumber(String phone_number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone_number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout,
                Objects.requireNonNull(getActivity()), // Activity (for callback binding)
                mCallbacks);

    }



    private boolean checkInputs(String phoneNumber,
                               String emailId,String password,
                               String confPassword)
    {
        if (phoneNumber.isEmpty() || emailId.isEmpty() || password.isEmpty() || confPassword.isEmpty())
        {
            Toast.makeText(mContext, "Please enter all the details", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!emailId.contains("@"))
        {
            mEtEmailId.setError("Invalid Email");
            return false;
        }
        if (password.length() < 8 || password.length() > 16)
        {
            mEtPassword.setError("Password should be 8 to 16 characters.");
            return false;
        }
        else if (!password.equals(confPassword))
        {
            mEtConfPassword.setError("Password did not match!");
            return false;
        }
        if (!PatternChecker.isValidPhoneNumber(phoneNumber))
        {
            mEtPhoneNumber.setError("Enter valid phone number");
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
