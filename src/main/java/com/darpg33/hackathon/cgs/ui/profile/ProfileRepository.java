package com.darpg33.hackathon.cgs.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.HashMap;

 class ProfileRepository {

     private FirebaseUser mUser;

    ProfileRepository(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

     MutableLiveData<User> getUserInfo() {

        final MutableLiveData<User> data = new MutableLiveData<>();


        db.collection("Users")
                .document(mUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot != null)
                {

                    User user = new User();

                    user.setUser_id(mUser.getUid());
                    user.setFirst_name(documentSnapshot.getString("first_name"));
                    user.setLast_name(documentSnapshot.getString("last_name"));
                    user.setGender(documentSnapshot.getString("gender"));
                    user.setPhone_number(documentSnapshot.getString("phone_number"));
                    user.setEmail_id(documentSnapshot.getString("email_id"));
                    user.setAddress(documentSnapshot.getString("address"));
                    user.setCountry(documentSnapshot.getString("country"));
                    user.setState(documentSnapshot.getString("state"));
                    user.setDistrict(documentSnapshot.getString("district"));
                    user.setPin_code(documentSnapshot.getString("pin_code"));
                    user.setTimestamp(documentSnapshot.getTimestamp("timestamp"));
                    user.setRegistered(documentSnapshot.getBoolean("registered"));
                    data.setValue(user);
                    
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseFirestoreException)
                {
                    data.setValue(null);
                }
            }
        });







        return data;
    }



     MutableLiveData<User> updateUserProfile(final User user) {

        final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();


        HashMap<String,Object> userProfileUpdate = new HashMap<>();

        userProfileUpdate.put("first_name",user.getFirst_name());
        userProfileUpdate.put("last_name",user.getLast_name());
        userProfileUpdate.put("gender",user.getGender());
        userProfileUpdate.put("email_id",user.getEmail_id());
        userProfileUpdate.put("address",user.getAddress());
        userProfileUpdate.put("phone_number",user.getPhone_number());
        userProfileUpdate.put("country",user.getCountry());
        userProfileUpdate.put("state",user.getState());
        userProfileUpdate.put("district",user.getDistrict());
        userProfileUpdate.put("pin_code",user.getPin_code());
        userProfileUpdate.put("timestamp",new Timestamp(new Date()));


        db.collection("Users")
                .document(user.getUser_id())
                .update(userProfileUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                userMutableLiveData.setValue(user);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                userMutableLiveData.setValue(null);
            }
        });


        return userMutableLiveData;
    }
}
