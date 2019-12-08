package com.darpg33.hackathon.cgs.ui.request.newrequest;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.Model.Grievance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

 class NewGrievanceRepository {

    private static final String TAG = "NewGrievanceRepository";
    private FirebaseFirestore db;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private FirebaseAuth mFirebaseAuth;

    NewGrievanceRepository(){

        db = FirebaseFirestore.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

     MutableLiveData<HashMap<String,HashMap<String,Object>>> uploadAttachments(final Grievance grievance, final String request_id)
    {
        final MutableLiveData<HashMap<String,HashMap<String,Object>>> attachmentLiveData = new MutableLiveData<>();
        grievance.setRequest_id(request_id);

        final HashMap<String,HashMap<String,Object>> attachmentMap = new HashMap<>();
        if (grievance.getAttachment().size()>0)
        {
            Log.d(TAG, "uploadAttachments: total attachments :"+grievance.getAttachment().size());
            for (final Attachment attachment:grievance.getAttachment())
            {
                Log.d(TAG, "uploadAttachments: attachmentpath :"+attachment.getAttachmentPath());
                if (!attachment.getAttachmentType().equals("location")) {
                    mStorageReference = mFirebaseStorage.getReference("Requests/" + request_id + "/attachments/" + attachment.getAttachment_name());

                    UploadTask uploadTask = mStorageReference.putFile(attachment.getAttachmentUri());

//                    mStorageReference.putFile(attachment.getAttachmentUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            if (taskSnapshot.getTask().isSuccessful())
//                            {
//
//                            }
//
//                            }
//                    }).addOnCanceledListener(new OnCanceledListener() {
//                        @Override
//                        public void onCanceled() {
//                            mStorageReference.delete();
//                        }
//                    });



                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            if (taskSnapshot!=null) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();

                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Log.d(TAG, "onComplete: document uploaded....." + uri.toString());
                                        HashMap<String, Object> attachmentDetails = new HashMap<>();
                                        attachmentDetails.put("name", attachment.getAttachment_name());
                                        attachmentDetails.put("type", attachment.getAttachmentType());
                                        attachmentDetails.put("path", Objects.requireNonNull(uri).toString());
                                        attachmentDetails.put("timestamp", attachment.getTimestamp());
                                        attachmentMap.put("attachment" + attachmentMap.size(), attachmentDetails);

                                        if (grievance.getAttachment().size() == attachmentMap.size()) {
                                            Log.d(TAG, "onComplete: all files uploaded.");
                                            attachmentLiveData.setValue(attachmentMap);
                                        }

                                    }
                                });
                            }
                        }
                    });


//                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                            if (!task.isSuccessful()) {
//                                throw task.getException();
//                            }
//
//                            // Continue with the task to get the download URL
//                            return mStorageReference.getDownloadUrl();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//
//                            if (task.isSuccessful())
//                            {
//
//                                Uri uri = task.getResult();
//
//
//                                Log.d(TAG, "onComplete: document uploaded....." + uri.toString());
//                                HashMap<String, Object> attachmentDetails = new HashMap<>();
//                                attachmentDetails.put("name", attachment.getAttachment_name());
//                                attachmentDetails.put("type", attachment.getAttachmentType());
//                                attachmentDetails.put("path", Objects.requireNonNull(uri).toString());
//                                attachmentDetails.put("timestamp", attachment.getTimestamp());
//                                attachmentMap.put("attachment" + attachmentMap.size(), attachmentDetails);
//
//                                if (grievance.getAttachment().size() == attachmentMap.size()) {
//                                    Log.d(TAG, "onComplete: all files uploaded.");
//                                    attachmentLiveData.setValue(attachmentMap);
//                                }
//
//                            }
//
////                                mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                                    @Override
////                                    public void onSuccess(Uri uri) {
////
////
////                                    }
////                                });
//
//                        }
//                    });

//                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            if (taskSnapshot.getTask().isSuccessful()) {
//
//                                mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        Log.d(TAG, "onComplete: document uploaded....." + uri.toString());
//                                        HashMap<String, Object> attachmentDetails = new HashMap<>();
//                                        attachmentDetails.put("name", attachment.getAttachment_name());
//                                        attachmentDetails.put("type", attachment.getAttachmentType());
//                                        attachmentDetails.put("path", Objects.requireNonNull(uri).toString());
//                                        attachmentDetails.put("timestamp", attachment.getTimestamp());
//                                        attachmentMap.put("attachment" + attachmentMap.size(), attachmentDetails);
//
//                                        if (grievance.getAttachment().size() == attachmentMap.size()) {
//                                            Log.d(TAG, "onComplete: all files uploaded.");
//                                            attachmentLiveData.setValue(attachmentMap);
//                                        }
//                                    }
//                                });
//
//                            }
//                        }
//                    });

                }
                else {
                    HashMap<String, Object> attachmentDetails = new HashMap<>();
                    GeoPoint point = new GeoPoint(attachment.getAddress().getLatitude(),attachment.getAddress().getLongitude());
                    attachmentDetails.put("address",attachment.getAddress().getAddressLine(0));
                    attachmentDetails.put("geo_point",point);
                    attachmentDetails.put("timestamp",attachment.getTimestamp());
                    attachmentDetails.put("type",attachment.getAttachmentType());
                    attachmentMap.put("attachment" + attachmentMap.size(), attachmentDetails);

                    if (grievance.getAttachment().size() == attachmentMap.size()) {
                        Log.d(TAG, "onComplete: all files uploaded." + attachmentMap.toString());
                        attachmentLiveData.setValue(attachmentMap);
                    }

                }

//                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "onComplete: document uploaded....."+attachment.getAttachmentPath());
//                            Uri downloadUri = task.getResult();
//                            HashMap<String, Object> attachmentDetails = new HashMap<>();
//                            attachmentDetails.put("name",attachment.getAttachment_name());
//                            attachmentDetails.put("type",attachment.getAttachmentType());
//                            attachmentDetails.put("path", Objects.requireNonNull(downloadUri).toString());
//                            attachmentDetails.put("timestamp",attachment.getTimestamp());
//                            attachmentMap.put("attachment"+attachmentMap.size(),attachmentDetails);
//
//                            if (grievance.getAttachment().size()==attachmentMap.size())
//                            {
//                                Log.d(TAG, "onComplete: all files uploaded.");
//                                attachmentLiveData.setValue(attachmentMap);
//                            }
//                        }
////                        else {
////                            // Handle failures
////                            // ...
////                            uploadTask.cancel();
////                            mFirebaseStorage.getReference("Requests/"+request_id+"/").delete();
////                            attachmentLiveData.setValue(null);
////                        }
//                    }
//                });
            }

        }
        else {

            attachmentLiveData.setValue(attachmentMap);

        }

        return attachmentLiveData;
    }





     MutableLiveData<Grievance> submitNewRequest(final Grievance grievance, HashMap<String, HashMap<String, Object>> attachmentMap) {

            final MutableLiveData<Grievance> grievanceMutableLiveData = new MutableLiveData<>();



            final HashMap<String,Object> grievanceMap = new HashMap<>();
            grievanceMap.put("grievance_request_id",grievance.getRequest_id());
            grievanceMap.put("grievance_user_id", Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid());
            grievanceMap.put("grievance_title",grievance.getTitle());
            grievanceMap.put("grievance_category",grievance.getCategory());
            grievanceMap.put("grievance_description",grievance.getDescription());
            grievanceMap.put("grievance_timestamp",new Timestamp(new Date()));
            grievanceMap.put("grievance_status",grievance.getStatus());
            grievanceMap.put("grievance_attachments",attachmentMap);


            grievance.setUser_id(mFirebaseAuth.getCurrentUser().getUid());

            db.collection("Users")
                    .document(grievance.getUser_id())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists())
                            {

                                String firstname = documentSnapshot.getString("first_name");
                                String lastname = documentSnapshot.getString("last_name");
                                String username = firstname+" "+lastname;
                                String user_email = documentSnapshot.getString("email_id");

                                final Action action = new Action();
                                action.setAction_performed("SUBMIT");
                                action.setAction_description("Our executive will look into this as soon as possible.");
                                action.setUsername(username);
                                action.setUser_type("citizen");
                                action.setEmail_id(user_email);


                                final HashMap<String, Object> actionHashmap = new HashMap<>();
                                actionHashmap.put("action_performed",action.getAction_performed() );
                                actionHashmap.put("action_info",action.getAction_info());
                                actionHashmap.put("action_description",action.getAction_description());
                                actionHashmap.put("action_username",action.getUsername());
                                actionHashmap.put("action_user_type",action.getUser_type());
                                actionHashmap.put("action_user_email",action.getEmail_id());
                                actionHashmap.put("action_user_id",mFirebaseAuth.getCurrentUser().getUid());
                                actionHashmap.put("action_timestamp",new Timestamp(new Date()));


                                //adding request to requests  database

                                db.collection("Requests")
                                        .document(grievance.getRequest_id())
                                        .set(grievanceMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        //adding request to user's MyRequests database

                                        db.collection("Users")
                                                .document(grievance.getUser_id())
                                                .collection("MyRequests")
                                                .document(grievance.getRequest_id())
                                                .set(grievanceMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                //adding action to 'Requests/request_id' database

                                                db.collection("Requests")
                                                        .document(grievance.getRequest_id())
                                                        .collection("Actions")
                                                        .document().set(actionHashmap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                grievanceMutableLiveData.setValue(grievance);

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "onFailure: Failed to load action.");

                                                        grievanceMutableLiveData.setValue(null);
                                                        mStorageReference.delete();
                                                        db.collection("Requests")
                                                                .document(grievance.getRequest_id())
                                                                .delete();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Log.e(TAG, "onFailure: MyRequest"+e.getMessage() );

                                                grievanceMutableLiveData.setValue(null);
                                                mStorageReference.delete();
                                                db.collection("Requests")
                                                        .document(grievance.getRequest_id())
                                                        .delete();
                                            }
                                        });

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: Request "+e.getMessage() );
                                                mStorageReference.delete();
                                                grievanceMutableLiveData.setValue(null);
                                            }
                                        });
                                }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: Unable to get user info"+e.getMessage());
                }
            });


        return grievanceMutableLiveData;
    }


     MutableLiveData<String> getNewRequestId()
    {

        Log.d(TAG, "getNewRequestId:");
        final MutableLiveData<String> request_id = new MutableLiveData<>();

        db.collection("Requests")
                .orderBy("grievance_timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null)
                            {
                                int i = 10000000;
                                for (QueryDocumentSnapshot document:task.getResult())
                                {
                                    if (document!= null)
                                    {
                                        Log.d(TAG, document.getId());
                                        i = Integer.parseInt(document.getId());
                                        i++;
                                        request_id.setValue(String.valueOf(i));
                                        Log.d(TAG, "onComplete: request id: "+i);
                                        break;
                                    }
                                    else {
                                        request_id.setValue(String.valueOf(i));
                                        Log.d(TAG, "onComplete: request id: "+i);
                                        break;
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            request_id.setValue(null);
                        }
                    }
                });

        return request_id;
    }


}
