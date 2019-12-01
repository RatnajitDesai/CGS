package com.darpg33.hackathon.cgs.ui.request;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.Model.Grievance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
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

 class GrievanceRepository {

    private static final String TAG = "GrievanceRepository";
    private FirebaseFirestore db;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private FirebaseAuth mFirebaseAuth;

    GrievanceRepository(){

        db = FirebaseFirestore.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }


     MutableLiveData<HashMap<String,HashMap<String,Object>>> uploadAttachments(final Grievance grievance, final String request_id)
    {
        final MutableLiveData<HashMap<String,HashMap<String,Object>>> attachmentLiveData = new MutableLiveData<>();
        grievance.setRequest_id(request_id);
        mStorageReference = mFirebaseStorage.getReference("Requests/"+request_id+"/attachments/");

        final HashMap<String,HashMap<String,Object>> attachmentMap = new HashMap<>();
        if (grievance.getAttachment().size()>0)
        {
            Log.d(TAG, "uploadAttachments: total attachments :"+grievance.getAttachment().size());
            for (final Attachment attachment:grievance.getAttachment())
            {
                Log.d(TAG, "uploadAttachments: attachmentpath :"+attachment.getAttachmentPath());
                if (!attachment.getAttachmentType().equals("location")) {
                    mStorageReference = mFirebaseStorage.getReference("Requests/" + request_id + "/attachments/" + attachment.getAttachment_name());
                    final UploadTask uploadTask = mStorageReference.putFile(attachment.getAttachmentUri());
                    mStorageReference.putFile(attachment.getAttachmentUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uri = mStorageReference.getDownloadUrl();

                                Log.d(TAG, "onComplete: document uploaded....." + uri.toString());
                                HashMap<String, Object> attachmentDetails = new HashMap<>();
                                attachmentDetails.put("name", attachment.getAttachment_name());
                                attachmentDetails.put("type", attachment.getAttachmentType());
                                attachmentDetails.put("path", Objects.requireNonNull(uri).toString());
                                attachmentDetails.put("timestamp", attachment.getTimestamp());
                                attachmentMap.put("attachment" + attachmentMap.size(), attachmentDetails);

                                if (grievance.getAttachment().size() == attachmentMap.size()) {
                                    Log.d(TAG, "onComplete: all files uploaded." + attachmentMap.toString());
                                    attachmentLiveData.setValue(attachmentMap);
                                }
                            }
                    });

//
//                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                            Task<Uri> downloadUrl = null;
//                            try {
//                                if (!task.isSuccessful()) {
//                                    throw task.getException();
//                                }
//                                else {
//                                    downloadUrl = mStorageReference.getDownloadUrl();
//                                    return downloadUrl;
//                                }
//                            }
//                            catch (Exception e)
//                            {
//                                Log.e(TAG, "then: "+e.getMessage() );
//
//                            }
//
//                            // Continue with the task to get the download URL
//                            //
//                            // }
//                            return downloadUrl;
//
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            if (uri!=null) {
//                                Log.d(TAG, "onComplete: document uploaded....." + uri.toString());
//                                HashMap<String, Object> attachmentDetails = new HashMap<>();
//                                attachmentDetails.put("name", attachment.getAttachment_name());
//                                attachmentDetails.put("type", attachment.getAttachmentType());
//                                attachmentDetails.put("path", Objects.requireNonNull(uri).toString());
//                                attachmentDetails.put("timestamp", attachment.getTimestamp());
//                                attachmentMap.put("attachment" + attachmentMap.size(), attachmentDetails);
//
//                                if (grievance.getAttachment().size() == attachmentMap.size()) {
//                                    Log.d(TAG, "onComplete: all files uploaded." + attachmentMap.toString());
//                                    attachmentLiveData.setValue(attachmentMap);
//                                }
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

            db.collection("Requests")
                    .document(grievance.getRequest_id())
                    .set(grievanceMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    db.collection("Users")
                            .document(grievance.getUser_id())
                            .collection("MyRequests")
                            .document(grievance.getRequest_id())
                            .set(grievanceMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            grievanceMutableLiveData.setValue(grievance);
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
                                for (QueryDocumentSnapshot document:task.getResult())
                                {
                                    if (document!= null)
                                    {
                                        Log.d(TAG, document.getId());
                                        int i = Integer.parseInt(document.getId());
                                        i++;
                                        request_id.setValue(String.valueOf(i));
                                        Log.d(TAG, "onComplete: request id: "+i);
                                        break;
                                    }
                                    else {
                                        request_id.setValue("10000000");
                                        Log.d(TAG, "onComplete: request id: 10000000");
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
