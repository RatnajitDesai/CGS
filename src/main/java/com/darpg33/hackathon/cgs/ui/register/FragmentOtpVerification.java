package com.darpg33.hackathon.cgs.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.R;
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

public class FragmentOtpVerification extends Fragment {

    private static final String TAG = "FragmentOtpVerification";


    //vars
    private String mVerificationCode, mCode, mEmail, mPassword, mPhone_number,
            mFirstname, mLastname,mGender,
            mAddress,mPincode,mCountry, mState,mDistrict;
    private PhoneAuthProvider.ForceResendingToken mToken;
    private FirebaseAuth mAuth;
    private Context mContext;
    private ProgressBar mProgressBar;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private RegisterViewModel registerViewModel;

    //widgets
    private TextInputEditText mOtp;
    private TextView mResendCode, mTimer;
    private MaterialButton mVerify;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_otp_verification,container,false);
        init(view);
        mAuth = FirebaseAuth.getInstance();

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar.setVisibility(View.VISIBLE);
                disableViews(mOtp,mResendCode,mVerify);

                final PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationCode, mOtp.getText().toString());
                
                mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Log.d(TAG, "onSuccess: pone number verified.");
                        final FirebaseUser user = mAuth.getCurrentUser();

                        if (user!=null)
                        {

                            AuthCredential emailAuthCredential = EmailAuthProvider.getCredential(mEmail, mPassword);

                            user.linkWithCredential(emailAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Phone number verified.", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(mContext, "Email verification link sent to your email.", Toast.LENGTH_SHORT).show();

                                            User NewUser = new User();

                                            NewUser.setUser_id(user.getUid());
                                            NewUser.setTimestamp(new Timestamp(new Date()));
                                            NewUser.setEmail_id(mEmail);
                                            NewUser.setFirst_name(mFirstname);
                                            NewUser.setLast_name(mLastname);
                                            NewUser.setGender(mGender);
                                            NewUser.setAddress(mAddress);
                                            NewUser.setPhone_number(mPhone_number);
                                            NewUser.setPin_code(mPincode);
                                            NewUser.setCountry(mCountry);
                                            NewUser.setState(mState);
                                            NewUser.setDistrict(mDistrict);
                                            NewUser.setRegistered(true);
                                            saveUserToDatabase(NewUser);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Log.e(TAG, "onFailure: "+e.getMessage());
                                            mProgressBar.setVisibility(View.GONE);
                                            enableViews(mOtp,mResendCode,mVerify);
                                            Toast.makeText(mContext, "Unable to send verification mail.\n Please retry after sometime.Or change your email ID.", Toast.LENGTH_LONG).show();
                                            user.delete();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e instanceof FirebaseAuthUserCollisionException)
                                    {
                                        Log.d(TAG, "onFailure: User with this email id already exists.");
                                        Toast.makeText(mContext, "User with email :"+mEmail+" already exists.", Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                        enableViews(mOtp,mResendCode,mVerify);
                                        user.delete();
                                    }
                                    else {
                                        Toast.makeText(mContext, "Unable to register. Email already exists.", Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                        enableViews(mOtp,mResendCode,mVerify);
                                        user.delete();
                                    }
                                }
                            });

                        }

                    }
                });

            }
        });



        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                if (credential.getSmsCode() != null)
                {
                    mOtp.setText(credential.getSmsCode());
                }

                User NewUser = new User();
                NewUser.setUser_id(mAuth.getCurrentUser().getUid());
                NewUser.setTimestamp(new Timestamp(new Date()));
                NewUser.setEmail_id(mEmail);
                NewUser.setFirst_name(mFirstname);
                NewUser.setLast_name(mLastname);
                NewUser.setGender(mGender);
                NewUser.setAddress(mAddress);
                NewUser.setPhone_number(mPhone_number);
                NewUser.setPin_code(mPincode);
                NewUser.setCountry(mCountry);
                NewUser.setState(mState);
                NewUser.setDistrict(mDistrict);
                NewUser.setRegistered(true);
                saveUserToDatabase(NewUser);
                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    //  Invalid request
                    // ...
                    Toast.makeText(mContext, "Please check the number and try again.", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    enableViews(mOtp,mResendCode,mVerify);

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(mContext, "Unable to verify your at the moment.Please try again after some time.", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    enableViews(mOtp,mResendCode,mVerify);
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

                Log.d(TAG, "onCodeSent:" + verificationId+" "+token);

                mVerificationCode = verificationId;
                mToken = token;
                Toast.makeText(getContext(), "OTP sent to your number", Toast.LENGTH_SHORT).show();

                // Save verification ID and resending token so we can use them later


                // ...
            }
        };

        mResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(mPhone_number,mToken);
                startTimer();
            }
        });




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
                    Toast.makeText(mContext, "Verify your email address and Sign In.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onChanged: User saved.");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
                else
                {
                    Toast.makeText(mContext, "Error occurred.Unable to register user.Try again.", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    mAuth.getCurrentUser().delete();
                    mProgressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onChanged: User not saved.");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }

            }
        });

    }

    private void init(View view){

        Log.d(TAG, "init: ");
        mContext = getContext();
        mOtp = view.findViewById(R.id.etOtp);

        mResendCode = view.findViewById(R.id.txtResendCode);
        mTimer = view.findViewById(R.id.txtTimer);
        mProgressBar = view.findViewById(R.id.progressBar);
        mVerify = view.findViewById(R.id.verifyPhoneNumber);
        Bundle bundle = getArguments();
        //get parameters to be saved to database from bundle

        if (bundle != null)
        {
        mVerificationCode = bundle.getString(getString(R.string.verification_Id));
        mEmail = bundle.getString(getString(R.string.email_id));
        mPassword = bundle.getString(getString(R.string.password));
        mPhone_number = bundle.getString(getString(R.string.phone_number));
        mToken = bundle.getParcelable(getString(R.string.resend_token));
        mFirstname = bundle.getString(getString(R.string.first_name));
        mLastname = bundle.getString(getString(R.string.last_name));
        mGender = bundle.getString(getString(R.string.gender));
        mAddress = bundle.getString(getString(R.string.address));
        mPincode = bundle.getString(getString(R.string.pin_code));
        mCountry = bundle.getString(getString(R.string.country));
        mState = bundle.getString(getString(R.string.state));
        mDistrict = bundle.getString(getString(R.string.district));
        startTimer();

        }
    }

    private void startTimer() {
        mResendCode.setVisibility(View.GONE);
        new CountDownTimer(59000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                mTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mResendCode.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    private void resendVerificationCode(String phoneNumber,PhoneAuthProvider.ForceResendingToken token){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                Objects.requireNonNull(getActivity()),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);
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
