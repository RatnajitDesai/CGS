package com.darpg33.hackathon.cgs.ui.CustomDialogs;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.Utils.FileUtilities;
import com.darpg33.hackathon.cgs.Utils.Permissions;
import com.darpg33.hackathon.cgs.ui.request.newrequest.AttachmentAdapter;
import com.darpg33.hackathon.cgs.ui.request.newrequest.ChooseAttachmentBottomSheet;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class CustomActionDialog extends BottomSheetDialogFragment implements View.OnClickListener, ChooseAttachmentBottomSheet.PhotoUriListener,
        ChooseAttachmentBottomSheet.DocumentUriListener,
        ChooseAttachmentBottomSheet.CameraUriListener,
        ChooseAttachmentBottomSheet.LocationListener, AttachmentAdapter.RemoveItemListener {

    private static final String TAG = "CustomActionDialog";

    @Override
    public void removeItem(Attachment attachment, int position) {

        mAttachments.remove(attachment);
        mAttachmentAdapter.notifyItemRemoved(position);
        setAttachmentsButton();

    }


    public interface SaveListener{
        void addNote(String note, ArrayList<Attachment> mAttachments);
    }

    public interface AssignListener{
        void assignRequest(String note, String assignedTo, String priority, ArrayList<Attachment> attachment);
    }

    public interface AssignWorkerListener {
        void assignToWorker(String note, User assignedTo, String priority, String ActionDone, ArrayList<Attachment> mAttachments);
    }

    public interface CompleteListener{
        void completeRequest(String note, ArrayList<Attachment> attachment);
    }

    public interface RejectListener{

        void rejectRequest(String note, ArrayList<Attachment> attachment);

    }

    public interface ForwardListener{
        void forwardRequest(String note, String forwardedTo, ArrayList<Attachment> attachment);
    }

    //vars
    private SaveListener mSaveListener;
    private AssignListener mAssignListener;
    private ForwardListener mForwardListener;
    private RejectListener mRejectListener;
    private CompleteListener mCompleteListener;
    private AssignWorkerListener mAssignWorkerListener;
    private String mActionBy, mActionDone;
    private CustomActionDialogViewModel customActionDialogViewModel;
    private ArrayList<String> mUsers;
    private ArrayList<User> userArrayList;
    private MaterialButton btnAddAttachment;
    private Context mContext;
    private static final int VERIFY_PERMISSIONS_REQUEST = 201;
    private ArrayList<Attachment> mAttachments = new ArrayList<>();


    //Widgets
    private TextInputEditText mNotes;
    private TextView mActionTitle;
    private RecyclerView mAttachmentRecyclerView;
    private AttachmentAdapter mAttachmentAdapter;
    private Toolbar mToolbar;
    private RelativeLayout mRelativeLayout;
    private AppCompatSpinner mAssignTo, mPriority, mAssignWorker;
    private LinearLayout mlinAssignTo, mlinPriority, mlinAssignWorker;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            mActionDone = bundle.getString("action_done");
            mActionBy = bundle.getString("action_by");


        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.dialog_fragment_custom_action, container, false);
        mToolbar = root.findViewById(R.id.toolbar);
        mContext = getContext();
        mAssignTo = root.findViewById(R.id.spAssignTo);
        mPriority = root.findViewById(R.id.spPriority);
        mlinAssignTo = root.findViewById(R.id.linAssignSpinner);
        mlinPriority = root.findViewById(R.id.linSpinnerPriority);
        mlinAssignWorker = root.findViewById(R.id.linAssignWorkerSpinner);
        mAssignWorker = root.findViewById(R.id.spAssignToWorker);
        mActionTitle = root.findViewById(R.id.action_toolbar_title);
        mRelativeLayout = root.findViewById(R.id.relativeLayout);
        mAttachmentRecyclerView = root.findViewById(R.id.attachmentsRecyclerView);

        mUsers = new ArrayList<>();
        userArrayList = new ArrayList<>();
        mActionTitle.setText(mActionDone);


        mNotes = root.findViewById(R.id.actionNotes);
        mProgressBar = root.findViewById(R.id.progressBar);
        MaterialButton btnSubmit = root.findViewById(R.id.btnSubmit);
        MaterialButton btnCancel = root.findViewById(R.id.btnCancel);
        btnAddAttachment = root.findViewById(R.id.btnAddAttachment);

        customActionDialogViewModel = ViewModelProviders.of(this).get(CustomActionDialogViewModel.class);

        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnAddAttachment.setOnClickListener(this);
        init(mActionBy);
        setupRecyclerView();
        return root;
    }

    private void setupRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mAttachmentRecyclerView.setLayoutManager(manager);
        mAttachmentAdapter = new AttachmentAdapter(mAttachments);
        mAttachmentAdapter.setRemoveItemListener(this);
        mAttachmentRecyclerView.setAdapter(mAttachmentAdapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));
    }

    private void init(String mActionBy) {

        switch (mActionBy)
        {
            case Fields
                    .USER_TYPE_CITIZEN:
            {
                Log.d(TAG, "init: citizen");
                makeViewsInvisible(mlinAssignWorker, mlinAssignTo, mlinPriority);
                break;
            }
            case Fields
                    .USER_TYPE_MEDIATOR:
            {
                Log.d(TAG, "init: mediator");
                switch (mActionDone)
                {
                    case "ASSIGN":
                    {
                        makeViewsInvisible(mlinAssignWorker, mlinPriority);
                        break;
                    }
                    case "REJECT":
                    case "SAVE": {
                        makeViewsInvisible(mlinAssignWorker, mlinPriority, mlinAssignTo);
                        break;
                    }
                }
                break;
            }
            case Fields.USER_TYPE_DEP_INCHARGE:
            {
                Log.d(TAG, "init: department in-charge");
                switch (mActionDone)
                {
                    case "ASSIGN":
                    {
                        getUsersInDepartment();
                        break;
                    }
                    case "REJECT":
                    {
                        makeViewsInvisible(mlinAssignWorker, mlinPriority, mlinAssignTo);
                        break;
                    }
                    case "FORWARD":
                    {
                        makeViewsInvisible(mlinAssignWorker, mlinPriority);
                        break;
                    }
                    case "COMPLETE":
                    {
                        makeViewsInvisible(mlinAssignWorker, mlinAssignTo, mlinPriority, mlinAssignWorker);
                        break;
                    }
                    case "SAVE":
                    {
                        makeViewsInvisible(mlinPriority, mlinAssignTo, mlinAssignWorker);
                    }
                }

                break;
            }
            case Fields.USER_TYPE_DEP_WORKER:
            {
                Log.d(TAG, "init: worker");
                switch (mActionDone)
                {
                    case "REJECT":
                    {
                        makeViewsInvisible(mlinPriority, mlinAssignTo, mlinAssignWorker);
                        break;
                    }
                    case "FORWARD":
                    {
                        makeViewsInvisible(mlinPriority, mlinAssignWorker);
                        break;
                    }
                    case "COMPLETE":
                    {
                        makeViewsInvisible(mlinAssignTo, mlinPriority, mlinAssignWorker);
                        break;
                    }
                    case "SAVE":
                    {
                        makeViewsInvisible(mlinPriority, mlinAssignTo, mlinAssignWorker);
                    }
                }
                break;
            }
        }
    }

    private void getUsersInDepartment() {

        isProcessing(true);

        customActionDialogViewModel.getUsersInDepartment().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {


                if (!users.isEmpty()) {
                    userArrayList.addAll(users);
                    for (User user :
                            users) {

                        String username = user.getFirst_name() + " " + user.getLast_name();
                        mUsers.add(username);
                    }

                    setupAssignedWorkerSpinner(mUsers);
                }

            }
        });


    }

    private void setupAssignedWorkerSpinner(ArrayList<String> mUsers) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mUsers);
        mAssignWorker.setAdapter(adapter);
        isProcessing(false);

    }


    private void makeViewsInvisible(View... views) {

        for (View view:
             views) {

            view.setVisibility(View.GONE);

        }


    }

    public void setSaveListener(SaveListener listener)
    {
        mSaveListener = listener;
    }

    public void setAssignListener(AssignListener listener)
    {
        mAssignListener = listener;
    }

    public void setForwardListener(ForwardListener listener)
    {
        mForwardListener = listener;
    }

    public void setCompleteListener(CompleteListener listener)
    {
        mCompleteListener = listener;
    }

    public void setRejectListener(RejectListener listener)
    {
        mRejectListener = listener;
    }

    public void setAssignWorkerListener(AssignWorkerListener listener) {
        mAssignWorkerListener = listener;
    }


    @Override
    public void getPhotoUri(Uri uri, String display_name, long file_size) {

        if (FileUtilities.checkFileSize(file_size)) {
            Log.d(TAG, "getPhotoUri: file_size: " + file_size);
            Attachment attachment = new Attachment(display_name, "image", uri, new Timestamp(new Date()));
            mAttachments.add(attachment);
            setAttachmentsButton();
            int position = mAttachments.size() - 1;
            Log.d(TAG, "getPhotoUri: setAttachmentsButton :" + position + 1);
            mAttachmentAdapter.notifyItemInserted(position);
        } else {
            Toast.makeText(mContext, "Max. file should be less than 3 MB.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAttachmentsButton() {

        Log.e(TAG, "setAttachmentsButton: " + mAttachments.size());
        if (mAttachments.size() >= 3) {
            btnAddAttachment.setEnabled(false);
        } else {
            btnAddAttachment.setEnabled(true);
        }
    }

    @Override
    public void getDocumentUri(Uri uri, String display_name, long file_size) {
        Log.e(TAG, "getDocumentUri: " + uri.toString());
        Log.d(TAG, "getDocumentUri: display name: " + display_name);

        if (FileUtilities.checkFileSize(file_size)) {
            Log.d(TAG, "getDocumentUri: file_size: " + file_size);
            Attachment attachment = new Attachment(display_name, "document", uri, new Timestamp(new Date()));
            mAttachments.add(attachment);
            setAttachmentsButton();
            int position = mAttachments.size() - 1;
            Log.d(TAG, "getDocumentUri: setAttachmentsButton :" + position + 1);
            mAttachmentAdapter.notifyItemInserted(position);
        } else {
            Toast.makeText(mContext, "Max. file should be less than 3 MB.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getCameraUri(Uri uri, String display_name, long file_size) {

        Log.e(TAG, "getCameraUri: " + uri.toString());
        Log.d(TAG, "getCameraUri: display name: " + display_name);

        if (FileUtilities.checkFileSize(file_size)) {
            Log.d(TAG, "getCameraUri: file_size: " + file_size);
            Attachment attachment = new Attachment(display_name, "image", uri, new Timestamp(new Date()));
            mAttachments.add(attachment);
            setAttachmentsButton();
            int position = mAttachments.size() - 1;
            Log.d(TAG, "getCameraUri: setAttachmentsButton :" + position + 1);
            mAttachmentAdapter.notifyItemInserted(position);
        } else {
            Toast.makeText(mContext, "Max. file should be less than 3 MB.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getLocation(Address address) {

        Attachment attachment = new Attachment(address, "location", new Timestamp(new Date()));
        mAttachments.add(attachment);
        setAttachmentsButton();
        int position = mAttachments.size() - 1;
        mAttachmentAdapter.notifyItemInserted(position);

    }





    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.btnSubmit:
            {
                saveAction(mActionDone);
                break;
            }
            case R.id.btnCancel:
            {
                dismiss();
                break;
            }
            case R.id.btnAddAttachment: {
                showBottomSheet();
                break;
            }

        }

    }


    private void showBottomSheet() {

        Log.d(TAG, "showBottomSheet: ");

        if (checkPermissionsArray(Permissions.PERMISSIONS)) {
            ChooseAttachmentBottomSheet bottomSheet = new ChooseAttachmentBottomSheet();
            bottomSheet.setPhotoUriListener(this);
            bottomSheet.setDocumentUriListener(this);
            bottomSheet.setCameraUriListener(this);
            bottomSheet.setLocationListener(this);
            bottomSheet.show(Objects.requireNonNull(getFragmentManager()), "ChooseAttachmentBottomSheet");
        } else {

            verifyPermissions(Permissions.PERMISSIONS);

        }

    }



    private void saveAction(String mActionDone) {

        switch (mActionDone)
        {
            case "ASSIGN":
            {

                switch (mActionBy) {

                    case Fields.USER_TYPE_MEDIATOR: {
                        if (checkInputs(mAssignTo, mNotes, mPriority)) {
                            mAssignListener.assignRequest(mNotes.getText().toString(),
                                    mAssignTo.getSelectedItem().toString(),
                                    mPriority.getSelectedItem().toString(), mAttachments);
                        }
                        break;
                    }
                    case Fields.USER_TYPE_DEP_INCHARGE: {
                        if (checkInputs(mAssignWorker, mNotes, mPriority)) {
                            int i = mAssignWorker.getSelectedItemPosition();

                            User user = userArrayList.get(i);
                            mAssignWorkerListener.assignToWorker(mNotes.getText().toString(),
                                    user,
                                    mPriority.getSelectedItem().toString(), mActionDone, mAttachments);
                        }
                        break;
                    }
                }
                    dismiss();
                break;
            }
            case "FORWARD":
            {
                if (checkInputs(mAssignTo,mNotes))
                {
                    mForwardListener.forwardRequest(mNotes.getText().toString(), mAssignTo.getSelectedItem().toString(), mAttachments);
                    dismiss();
                }
                break;
            }
            case "REJECT":
            {
                if (checkInputs(mNotes))
                {
                    mRejectListener.rejectRequest(mNotes.getText().toString(), mAttachments);
                    dismiss();
                }
                break;
            }
            case "SAVE":
            {
                if (checkInputs(mNotes))
                {
                    mSaveListener.addNote(mNotes.getText().toString(), mAttachments);
                    dismiss();
                }
                break;
            }
            case "COMPLETE":
            {
                if (checkInputs(mNotes))
                {
                    mCompleteListener.completeRequest(mNotes.getText().toString(), mAttachments);
                    dismiss();
                }
                break;
            }
        }
    }

    private boolean checkInputs(View... views) {

        for (View view :
                views) {

            try {

                if (view instanceof TextView) {
                    if (((TextView) view).getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Enter all the details.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else if (view instanceof AppCompatSpinner) {
                    if (((AppCompatSpinner) view).getSelectedItem().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Enter all the details.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
            catch (Exception e)
            {
                Log.e(TAG, "checkInputs: "+e.getMessage() );
            }

        }

        return true;
    }


    private void isProcessing(boolean b) {

        if (b) {
            mRelativeLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mRelativeLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }

    }


    /**
     * Verify all the @param permissions passed to the array
     *
     * @param permissions - CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
     */
    private void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: verifying permissions");
        ActivityCompat.requestPermissions(
                Objects.requireNonNull(getActivity()),
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    /**
     * check permissions array
     *
     * @param permissions - CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
     * @return permission granted or not
     */

    private boolean checkPermissionsArray(String[] permissions) {
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
     *
     * @param permission - single permission
     * @return permission granted or not
     */
    private boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: checking individual permission");
        int permissionRequest = ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), permission);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: \n permission was not granted for " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions: \n permission was granted for " + permission);
            return true;
        }
    }



}
