package com.darpg33.hackathon.cgs.ui.request;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.darpg33.hackathon.cgs.MapsActivity;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Permissions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.Objects;


public class ChooseAttachmentBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener
{
    private static final String TAG = "ChooseAttachmentBottom";
    private static final int GET_PHOTO_REQUEST = 101;
    private static final int GET_DOC_REQUEST = 102;
    private static final int GET_CAMERA_REQUEST = 103;
    private static final int GET_LOCATION_REQUEST = 104;
    private static final int VERIFY_PERMISSIONS_REQUEST = 201;
    private static final int ERROR_DIALOG_REQUEST = 9001;



    public interface LocationListener
    {
        void getLocation(Address address);
    }
    public interface PhotoUriListener
    {
         void getPhotoUri(Uri uri, String display_name, long file_size);
    }

    public interface DocumentUriListener
    {
        void getDocumentUri(Uri uri, String display_name, long file_size);
    }

    public interface CameraUriListener
    {
        void getCameraUri(Uri uri, String display_name, long file_size);
    }
    //vars
    private PhotoUriListener mPhotoUriListener;
    private DocumentUriListener mDocumentUriListener;
    private CameraUriListener mCameraUriListener;
    private LocationListener mLocationListener;


    //widgets
    private MaterialButton mLocation, mDocument, mPhotos, mCamera;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_attachment_bottom_sheet,container,false);
        init(view);
        return view;
    }

    private void init(View view) {

        Log.d(TAG, "init: ");

        mLocation = view.findViewById(R.id.location);
        mDocument = view.findViewById(R.id.document);
        mCamera = view.findViewById(R.id.camera);
        mPhotos = view.findViewById(R.id.photos);

        mLocation.setOnClickListener(this);
        mDocument.setOnClickListener(this);
        mCamera.setOnClickListener(this);
        mPhotos.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.location:
            {
                startLocationIntent();
                break;
            }
            case R.id.photos:
            {
                startPhotosIntent();
                break;
            }
            case R.id.document:
            {
                startDocumentIntent();
                break;
            }
            case R.id.camera:
            {
                startCameraIntent();
                break;
            }
        }

    }

    private void startCameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, GET_CAMERA_REQUEST);

    }

    private void startLocationIntent()
    {
        if (checkPermissionsArray(Permissions.LOCATION_PERMISSIONS) && isServicesOK())
        {
            Intent intent = new Intent(getActivity(), MapsActivity.class);

            startActivityForResult(intent,GET_LOCATION_REQUEST);
        }
        else {
            verifyPermissions(Permissions.LOCATION_PERMISSIONS);
        }
    }

    private void startDocumentIntent() {
        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(Intent.createChooser(intent,"Select Document"), GET_DOC_REQUEST);
    }

    private void startPhotosIntent() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_PHOTO_REQUEST);
    }




    /**
     * Verify all the @param permissions passed to the array
     * @param permissions - CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
     */
    private void verifyPermissions(String[] permissions)
    {
        Log.d(TAG, "verifyPermissions: verifying permissions");
        ActivityCompat.requestPermissions(
                Objects.requireNonNull(getActivity()),
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    /**
     * check permissions array
     * @param permissions - CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
     * @return permission granted or not
     */

    private boolean checkPermissionsArray(String[] permissions)
    {
        Log.d(TAG, "checkPermissionsArray: Checking permissions array.");
        for (String check : permissions) {
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }

    /**
     * check individual permission if it has been granted
     * @param permission - single permission
     * @return permission granted or not
     */
    private boolean checkPermissions(String permission)
    {
        Log.d(TAG, "checkPermissions: checking individual permission");
        int permissionRequest = ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),permission);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "checkPermissions: \n permission was not granted for "+ permission);
            return false;
        }
        else {
            Log.d(TAG, "checkPermissions: \n permission was granted for " + permission);
            return true;
        }
    }



    private boolean isServicesOK(){

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if (available == ConnectionResult.SUCCESS)
        {
            Log.d(TAG, "isServicesOK: google play services available.");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG, "isServicesOK: User resolvable error.");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(),available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Log.d(TAG, "isServicesOK: can't use map.");
        }
        return false;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {

        switch (requestCode) {
            case GET_PHOTO_REQUEST: {
                Log.d(TAG, "onActivityResult: resultCode : " + resultCode);

                if (intent != null) {
                    Uri uri = intent.getData();

                    Cursor cursor =
                            getContext().getContentResolver().query(uri, null, null, null, null);
                    /*
                     * Get the column indexes of the data in the Cursor,
                     * move to the first row in the Cursor, get the data,
                     * and display it.
                     */
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    cursor.moveToFirst();
                    mPhotoUriListener.getPhotoUri(uri,cursor.getString(nameIndex),cursor.getLong(sizeIndex));
                    cursor.close();
                    dismiss();
                }

                break;
            }
            case GET_DOC_REQUEST: {
                Log.d(TAG, "onActivityResult: resultCode : " + resultCode);

                if (intent != null) {
                    Uri uri = intent.getData();

                    Cursor cursor =
                            getContext().getContentResolver().query(uri, null, null, null, null);
                    /*
                     * Get the column indexes of the data in the Cursor,
                     * move to the first row in the Cursor, get the data,
                     * and display it.
                     */
                    int nameIndex = Objects.requireNonNull(cursor).getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    cursor.moveToFirst();
                    mDocumentUriListener.getDocumentUri(uri,cursor.getString(nameIndex),cursor.getLong(sizeIndex));
                    cursor.close();
                    dismiss();
                }

                break;
            }
            case GET_CAMERA_REQUEST: {
                Log.d(TAG, "onActivityResult: resultCode : " + resultCode);

                if (intent != null) {
                    Uri uri = intent.getData();
                    File file = new File(uri.getPath());
                    mCameraUriListener.getCameraUri(uri,file.getName()+".jpg",file.length());
                    dismiss();
                }
                break;
            }
            case GET_LOCATION_REQUEST: {
                Log.d(TAG, "onActivityResult: resultCode : " + resultCode);

                if (intent != null) {

                    Address address = intent.getParcelableExtra("location");
                    mLocationListener.getLocation(address);
                    dismiss();

                }

                break;
            }
        }
    }

     void setPhotoUriListener(PhotoUriListener mPhotoUriListener)
    {
        this.mPhotoUriListener = mPhotoUriListener;
    }

     void setDocumentUriListener(DocumentUriListener listener)
    {
        this.mDocumentUriListener = listener;
    }

    void setLocationListener(LocationListener listener)
    {
        this.mLocationListener = listener;
    }

    void setCameraUriListener(CameraUriListener listener)
    {
        this.mCameraUriListener = listener;
    }

}
