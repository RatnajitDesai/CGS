package com.darpg33.hackathon.cgs.ui.request.newrequest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.Utils.FileUtilities;
import com.darpg33.hackathon.cgs.Utils.Permissions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class NewGrievanceFragment extends Fragment implements View.OnClickListener,
        ChooseAttachmentBottomSheet.PhotoUriListener,
        ChooseAttachmentBottomSheet.DocumentUriListener,
        ChooseAttachmentBottomSheet.CameraUriListener,
        ChooseAttachmentBottomSheet.LocationListener,
        AttachmentAdapter.RemoveItemListener {

    private static final String TAG = "NewGrievanceFragment";
    private static final int VERIFY_PERMISSIONS_REQUEST = 201;


    //vars
    private NewGrievanceViewModel grievanceViewModel;
    private Context mContext;
    private ArrayList<Attachment> mAttachments;
    private AttachmentAdapter mAttachmentAdapter;

    //widgets
    private ScrollView scrollView ;
    private TextInputEditText mGrievanceTitle, mGrievanceDescription;
    private RadioGroup mPrivacy;
    private ProgressBar mProgressBar;
    private RecyclerView mAttachmentsRecycler;
    private Spinner mGrievanceCategory;
    private RadioButton mPublic, mPrivate;
    private MaterialButton mSubmit,mAttachmentButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        grievanceViewModel =
                ViewModelProviders.of(this).get(NewGrievanceViewModel.class);

        View view = inflater.inflate(R.layout.fragment_new_grievance, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        scrollView = view.findViewById(R.id.scrollView);

        mGrievanceTitle = view.findViewById(R.id.grievanceTitle);
        mGrievanceDescription = view.findViewById(R.id.grievanceDescription);
        mPrivacy = view.findViewById(R.id.rgPrivacy);
        mPrivate = view.findViewById(R.id.rbPrivate);
        mPublic = view.findViewById(R.id.rbPublic);
        mProgressBar = view.findViewById(R.id.progressBar);

        mAttachmentButton = view.findViewById(R.id.btnAttachment);
        mContext = getContext();
        mGrievanceCategory = view.findViewById(R.id.grievanceCategory);

        mSubmit = view.findViewById(R.id.btnSubmit);
        mAttachments = new ArrayList<>();

        mSubmit.setOnClickListener(this);
        mAttachmentButton.setOnClickListener(this);
        mAttachmentsRecycler = view.findViewById(R.id.attachmentsRecyclerView);
        setupRecyclerView();

    }

    private void setupRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mAttachmentsRecycler.setLayoutManager(linearLayoutManager);
        mAttachmentAdapter = new AttachmentAdapter(mAttachments);
        mAttachmentAdapter.setRemoveItemListener(this);
        mAttachmentsRecycler.setAdapter(mAttachmentAdapter);
    }


    private boolean checkInputs(String grievanceTitle, String grievanceCategory, String grievanceDescription)
    {
        boolean flag = true;
        if (grievanceTitle.isEmpty())
        {
            mGrievanceTitle.setError("Cannot be empty");
            flag = false;
        }
        if (grievanceDescription.isEmpty())
        {
            mGrievanceDescription.setError("Cannot be empty");
            flag = false;
        }
        if (mGrievanceCategory.getSelectedItemPosition() == 0)
        {
            mGrievanceCategory.setBackgroundResource(R.drawable.transaparent_background_with_red_rounded_border);
            flag = false;
        }
        if (mPrivacy.getCheckedRadioButtonId() == -1)
        {
            mPrivacy.setBackgroundResource(R.drawable.transaparent_background_with_red_rounded_border);
            flag = false;
        }
        return flag;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit)
        {
            if (checkInputs(mGrievanceTitle.getText().toString(), mGrievanceCategory.getSelectedItem().toString()
                    ,mGrievanceDescription.getText().toString()))
            {
                mProgressBar.setVisibility(View.VISIBLE);
                mAttachmentsRecycler.setClickable(false);
                disableViews(mGrievanceTitle,mGrievanceCategory, mGrievanceDescription,
                        mPrivacy, mPrivate,mPublic, mAttachmentButton, mSubmit);
                Grievance grievance = new Grievance();

                switch (mPrivacy.getCheckedRadioButtonId()) {
                    case R.id.rbPublic: {
                        grievance.setPrivacy("public");
                        break;
                    }

                    case R.id.rbPrivate: {
                        grievance.setPrivacy("private");
                        break;
                    }
                }
                grievance.setTitle(mGrievanceTitle.getText().toString());
                grievance.setCategory(mGrievanceCategory.getSelectedItem().toString());
                grievance.setDescription(mGrievanceDescription.getText().toString());
                grievance.setAttachment(mAttachments);
                grievance.setStatus(Fields.GR_STATUS_PENDING);
                grievance.setAttachment(mAttachments);
                submitNewRequest(grievance);
            }
        }
        else {

            if (v.getId() == R.id.btnAttachment){

                showBottomSheet();

            }

        }
    }

    private void showBottomSheet() {

        Log.d(TAG, "showBottomSheet: ");

        if (checkPermissionsArray(Permissions.PERMISSIONS)){
            ChooseAttachmentBottomSheet bottomSheet = new ChooseAttachmentBottomSheet();
            bottomSheet.setPhotoUriListener(this);
            bottomSheet.setDocumentUriListener(this);
            bottomSheet.setCameraUriListener(this);
            bottomSheet.setLocationListener(this);
            bottomSheet.show(Objects.requireNonNull(getFragmentManager()),"ChooseAttachmentBottomSheet");
        }
        else {

            verifyPermissions(Permissions.PERMISSIONS);

        }

    }

    private void submitNewRequest(final Grievance grievance)
    {

        grievanceViewModel.getNewRequestId().observe(this, new Observer<String>() {

            @Override
            public void onChanged(String request_id) {

                if (request_id != null)
                {

                    grievanceViewModel.uploadAttachments(grievance,request_id).observe(NewGrievanceFragment.this, new Observer<HashMap<String, HashMap<String, Object>>>() {
                        @Override
                        public void onChanged(HashMap<String, HashMap<String, Object>> map) {

                                if (map != null) {

                                        grievanceViewModel.submitNewRequest(grievance, map).observe(NewGrievanceFragment.this, new Observer<Grievance>() {
                                            @Override
                                            public void onChanged(Grievance grievance) {
                                                if (grievance != null) {
                                                    Toast.makeText(mContext, "Request raised : " + grievance.getRequest_id(), Toast.LENGTH_SHORT).show();

                                                    //navigate to home
                                                    Navigation.findNavController(getView()).navigate(R.id.action_nav_new_grievance_to_nav_home2);

                                                }
                                                else {

                                                    Toast.makeText(mContext, "Error occurred while raising request.Please try again.", Toast.LENGTH_SHORT).show();
                                                    mProgressBar.setVisibility(View.GONE);
                                                    mAttachmentsRecycler.setClickable(true);
                                                    enableViews(mGrievanceTitle,mGrievanceCategory, mGrievanceDescription,
                                                            mPrivacy, mPrivate,mPublic, mAttachmentButton, mSubmit);
                                                }
                                            }
                                        });
                                } else
                                    {
                                    Toast.makeText(mContext, "Error occurred while uploading files.", Toast.LENGTH_SHORT).show();
                                    mProgressBar.setVisibility(View.GONE);
                                    mAttachmentsRecycler.setClickable(false);
                                    enableViews(mGrievanceTitle,mGrievanceCategory, mGrievanceDescription,
                                            mPrivacy, mPrivate,mPublic, mAttachmentButton, mSubmit);
                                }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void getPhotoUri(Uri uri, String display_name, long file_size) {

                if (FileUtilities.checkFileSize(file_size))
                {

                    Log.d(TAG, "getPhotoUri: file_size: "+file_size);
                    Attachment attachment = new Attachment(display_name,"image", uri,new Timestamp(new Date()));
                    mAttachments.add(attachment);
                    setAttachmentsButton();
                    int position = mAttachments.size()-1;
                    Log.d(TAG, "getPhotoUri: setAttachmentsButton :"+position+1);
                    mAttachmentAdapter.notifyItemInserted(position);

                }
                else {
                    Toast.makeText(mContext, "Max. file should be less than 3 MB.", Toast.LENGTH_SHORT).show();
                }
    }

    private void setAttachmentsButton() {

        Log.e(TAG, "setAttachmentsButton: "+mAttachments.size() );
        if (mAttachments.size() >= 5 )
        {
            mAttachmentButton.setEnabled(false);
        }
        else {
            mAttachmentButton.setEnabled(true);
        }
    }

    @Override
    public void getDocumentUri(Uri uri, String display_name, long file_size) {
        Log.e(TAG, "getDocumentUri: "+uri.toString());
        Log.d(TAG, "getDocumentUri: display name: "+display_name);

        if (FileUtilities.checkFileSize(file_size))
        {
            Log.d(TAG, "getDocumentUri: file_size: "+file_size);
            Attachment attachment = new Attachment(display_name,"document", uri,new Timestamp(new Date()));
            mAttachments.add(attachment);
            setAttachmentsButton();
            int position = mAttachments.size()-1;
            Log.d(TAG, "getDocumentUri: setAttachmentsButton :"+position+1);
            mAttachmentAdapter.notifyItemInserted(position);
        }
        else {
            Toast.makeText(mContext, "Max. file should be less than 3 MB.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getCameraUri(Uri uri, String display_name, long file_size) {

        Log.e(TAG, "getCameraUri: "+uri.toString());
        Log.d(TAG, "getCameraUri: display name: "+display_name);

        if (FileUtilities.checkFileSize(file_size))
        {
            Log.d(TAG, "getCameraUri: file_size: "+file_size);
            Attachment attachment = new Attachment(display_name,"image", uri,new Timestamp(new Date()));
            mAttachments.add(attachment);
            setAttachmentsButton();
            int position = mAttachments.size()-1;
            Log.d(TAG, "getCameraUri: setAttachmentsButton :"+position+1);
            mAttachmentAdapter.notifyItemInserted(position);
        }
        else {
            Toast.makeText(mContext, "Max. file should be less than 3 MB.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getLocation(Address address) {

        Attachment attachment = new Attachment(address,"location",new Timestamp(new Date()));
        mAttachments.add(attachment);
        setAttachmentsButton();
        int position = mAttachments.size()-1;
        mAttachmentAdapter.notifyItemInserted(position);

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
        int permissionRequest =ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),permission);
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




    @Override
    public void removeItem(Attachment attachment, int position) {

            mAttachments.remove(attachment);
            mAttachmentAdapter.notifyItemRemoved(position);
            setAttachmentsButton();
     }

    private void enableViews(View... views)
    {
        mAttachmentAdapter.isClickable = true;
        for (View v:views)
        {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views)
    {
        mAttachmentAdapter.isClickable = false ;
        mAttachmentsRecycler.setEnabled(false);
        for (View v:views)
        {
            v.setEnabled(false);
        }

    }
}
