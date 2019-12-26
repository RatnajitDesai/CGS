package com.darpg33.hackathon.cgs.ui.register;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


class RegisterRepository {

    private static final String TAG = "RegisterRepository";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


     MutableLiveData<User> registerUser(final User user, String password) {

        final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

        mAuth.createUserWithEmailAndPassword(user.getEmail_id(),password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            final FirebaseUser firebaseUser = Objects.requireNonNull(task.getResult()).getUser();
                            if (firebaseUser != null)
                            {
                                firebaseUser.sendEmailVerification()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                user.setRegistered(true);
                                                user.setTimestamp(new Timestamp(new Date()));
                                                user.setUser_id(firebaseUser.getUid());
                                                userMutableLiveData.setValue(user);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "onFailure: email verification failed."+e.getMessage() );
                                        user.setRegistered(false);
                                        userMutableLiveData.setValue(user);
                                        firebaseUser.delete();
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
                userMutableLiveData.setValue(null);
            }
        });

        return userMutableLiveData;
    }


     MutableLiveData<User> saveUserToDatabase(final User user)
    {

        final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

        final HashMap<String, Object> user1 = new HashMap<>();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        user1.put(Fields.DB_USER_USER_ID, user.getUser_id());
                        user1.put(Fields.DB_USER_USER_TYPE, user.getUser_type());
                        user1.put(Fields.DB_USER_FIRSTNAME, user.getFirst_name());
                        user1.put(Fields.DB_USER_LASTNAME, user.getLast_name());
                        user1.put(Fields.DB_USER_GENDER, user.getGender());
                        user1.put(Fields.DB_USER_PHONE_NUMBER, user.getPhone_number());
                        user1.put(Fields.DB_USER_EMAIL_ID, user.getEmail_id());
                        user1.put(Fields.DB_USER_ADDRESS, user.getAddress());
                        user1.put(Fields.DB_USER_PINCODE, user.getPin_code());
                        user1.put(Fields.DB_USER_COUNTRY, user.getCountry());
                        user1.put(Fields.DB_USER_STATE, user.getState());
                        user1.put(Fields.DB_USER_DISTRICT, user.getDistrict());
                        user1.put(Fields.DB_USER_TIMESTAMP, user.getTimestamp());
                        user1.put(Fields.DB_USER_REGISTERED, user.getRegistered());
                        user1.put(Fields.REG_TOKEN, token);

                        db.collection(Fields.DBC_USERS)
                                .document(user.getUser_id())
                                .set(user1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "onSuccess: db stored for user :" + user.toString());
                                        user.setRegistered(true);
                                        userMutableLiveData.setValue(user);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Log.e(TAG, "onFailure: " + e.getMessage());
                                        Objects.requireNonNull(mAuth.getCurrentUser()).delete();
                                        userMutableLiveData.setValue(null);
                                    }
                                });
                    }
                });



        return  userMutableLiveData;
    }


}