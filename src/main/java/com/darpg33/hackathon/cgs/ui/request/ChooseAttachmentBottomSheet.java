package com.darpg33.hackathon.cgs.ui.request;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.darpg33.hackathon.cgs.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;


public class ChooseAttachmentBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "ChooseAttachmentBottom";
    private static final int GET_PHOTO_REQUEST = 101;
    private static final int GET_DOC_REQUEST = 102;




    public interface PhotoUriListener
    {
         void getPhotoUri(Uri uri, String display_name, long file_size);
    }

    public interface DocumentUriListener
    {
        void getDocumentUri(Uri uri, String display_name, long file_size);
    }
    //vars
    private PhotoUriListener mPhotoUriListener;
    private DocumentUriListener mDocumentUriListener;



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
    }

    private void startLocationIntent()
    {

    }

    private void startDocumentIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, GET_DOC_REQUEST);
    }

    private void startPhotosIntent() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_PHOTO_REQUEST);
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



}
