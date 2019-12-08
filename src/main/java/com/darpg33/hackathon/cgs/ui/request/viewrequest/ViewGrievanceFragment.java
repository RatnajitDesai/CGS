package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.TimeDateUtilities;
import com.darpg33.hackathon.cgs.ui.CustomDialogs.CustomActionDialog;
import com.google.android.material.button.MaterialButton;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ViewGrievanceFragment extends Fragment implements ViewAttachmentAdapter.ImageViewListener,
        ViewAttachmentAdapter.DocumentViewListener,
        ViewAttachmentAdapter.LocationViewListener,
        View.OnClickListener,
        CustomActionDialog.SaveListener {

    private static final String TAG = "ViewGrievanceFragment";

    //vars
    private ViewGrievanceViewModel viewGrievanceViewModel;
    private CustomActionViewModel customActionViewModel;
    private ViewAttachmentAdapter mAttachmentAdapter;
    private ActionsAdapter mActionsAdapter;
    private ArrayList<Attachment> mAttachments = new ArrayList<>();
    private ArrayList<Action> mActions = new ArrayList<>();
    private String mRequestId,mUserType;


    //widgets
    private TextView mTitle,  mTimeStamp, mStatus;
    private ExpandableTextView mDescription;
    private ProgressBar mProgressBar;
    private RelativeLayout mViewRelativeLayout;
    private MaterialButton mAssign, mReject, mForward, mComplete, mSave;
    private RecyclerView mAttachmentRecyclerView, mActionsRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_grievance, container, false);
        mTitle = view.findViewById(R.id.grievanceTitle);
        mViewRelativeLayout = view.findViewById(R.id.viewFragmentRelativeLayout);
        mDescription = view.findViewById(R.id.grievanceDescription);
        mTimeStamp = view.findViewById(R.id.grievance_timestamp);
        mStatus = view.findViewById(R.id.grievance_status);
        mProgressBar = view.findViewById(R.id.progressBar);

        mAssign = view.findViewById(R.id.btnAssign);
        mReject = view.findViewById(R.id.btnReject);
        mForward = view.findViewById(R.id.btnForward);
        mComplete = view.findViewById(R.id.btnComplete);
        mSave = view.findViewById(R.id.btnSave);

        mAssign.setOnClickListener(this);
        mReject.setOnClickListener(this);
        mForward.setOnClickListener(this);
        mComplete.setOnClickListener(this);
        mSave.setOnClickListener(this);

        mAttachmentRecyclerView = view.findViewById(R.id.attachmentsRecyclerView);
        mActionsRecyclerView = view.findViewById(R.id.actionsRecyclerView);

        setupRecyclerView();
        viewGrievanceViewModel = ViewModelProviders.of(this).get(ViewGrievanceViewModel.class);
        customActionViewModel = ViewModelProviders.of(this).get(CustomActionViewModel.class);
        init();
        return view;
    }

    private void init() {
        setProcessing(true);
        Log.d(TAG, "init.");
        setProcessing(true);

        setupViews();

        Bundle bundle = getArguments();

        if (bundle!=null)
        {
            setGrievanceView(bundle);
        }

    }

    private void setupViews() {

        setProcessing(true);

        viewGrievanceViewModel.getUserType().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                
                mUserType = s;

                switch (s)
                {
                    case "citizen":
                    {
                        removeViews(mAssign,mReject,mForward,mComplete);
                        setProcessing(false);
                        break;
                    }
                    case "mediator":
                    {
                        removeViews(mForward,mComplete);
                        setProcessing(false);
                        break;
                    }

                }


            }
        });
    }


    private void removeViews(View... views)
    {

        for (View view:views)
        {
            view.setVisibility(View.GONE);
        }

    }




    private void setGrievanceView(Bundle bundle) {

        mRequestId = bundle.getString("grievance_request_id");

        viewGrievanceViewModel.getGrievanceData(mRequestId).observe(this, new Observer<Grievance>() {
            @Override
            public void onChanged(Grievance grievance) {
                if (grievance != null)
                {

                    viewGrievanceViewModel.getGrievanceAttachments(mRequestId).observe(ViewGrievanceFragment.this, new Observer<ArrayList<Attachment>>() {
                        @Override
                        public void onChanged(ArrayList<Attachment> attachments) {
                            if (attachments!=null)
                            {
                                mAttachments.addAll(attachments);
                                mAttachmentAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                    mTitle.setText(grievance.getTitle());
                    mDescription.setText(grievance.getDescription());
                    mTimeStamp.setText(TimeDateUtilities.getDateAndTime(grievance.getTimestamp()));
                    mStatus.setText(grievance.getStatus());
                    getActions();
                    setProcessing(false);
                }
                else {
                    setProcessing(false);
                    Toast.makeText(getContext(), "Unable to retrieve data at the moment.Please try again.", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getActivity(),R.id.nav_view_grievance).navigate(R.id.nav_home);
                }
            }
        });

    }




    private void getActions() {

        viewGrievanceViewModel.getGrievanceActions(mRequestId).observe(this, new Observer<ArrayList<Action>>() {
            @Override
            public void onChanged(ArrayList<Action> actions) {
                if (actions != null)
                {
                    if (!actions.isEmpty())
                    {
                        mActions.clear();
                        mActions.addAll(actions);
                        mActionsAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Error occurred. Please check after some time.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void setupRecyclerView(){
        //attachments recycler
        LinearLayoutManager attachmentsLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mAttachmentRecyclerView.setLayoutManager(attachmentsLinearLayout);
        mAttachmentAdapter = new ViewAttachmentAdapter(mAttachments, this, this, this);
        mAttachmentRecyclerView.setAdapter(mAttachmentAdapter);

        //actions recycler
        LinearLayoutManager actionsLinearLayout = new LinearLayoutManager(getContext());
        mActionsRecyclerView.setLayoutManager(actionsLinearLayout);
        mActionsAdapter = new ActionsAdapter(mActions);
        mActionsRecyclerView.setAdapter(mActionsAdapter);
    }


    private void setProcessing(boolean b)
    {
        if (b)
        {
            mViewRelativeLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else {

            mViewRelativeLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void viewImage(Attachment attachment) {

        Log.d(TAG, "viewImage: ");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(attachment.getAttachmentPath()), "image/*");
        startActivity(intent);
    }

    @Override
    public void viewDocument(Attachment attachment) {

        Log.d(TAG, "viewDocument: ");
        
        String mimeType = attachment.getAttachment_name().substring(attachment.getAttachment_name().lastIndexOf(".")+1);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (mimeType)
        {
            case "pdf":
            {
                intent.setDataAndType(Uri.parse(attachment.getAttachmentPath()), "application/pdf");
                break;
            }
            case "doc":
            case "docx":
            {
                intent.setDataAndType(Uri.parse(attachment.getAttachmentPath()), "application/msword");
                break;
            }
            case "txt":
            {
                intent.setDataAndType(Uri.parse(attachment.getAttachmentPath()), "text/plain");
                break;
            }
            case "zip":
            {
                intent.setDataAndType(Uri.parse(attachment.getAttachmentPath()), "application/zip");
                break;
            }
        }
        startActivity(intent);
    }

    @Override
    public void viewLocation(Attachment attachment) {

        Log.d(TAG, "viewLocation: ");
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", attachment.getGeoPoint().getLatitude(),
                attachment.getGeoPoint().getLongitude());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        getContext().startActivity(intent);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnSave:
            {
                Log.d(TAG, "onClick: save.");
                CustomActionDialog fragment = new CustomActionDialog();
                fragment.setSaveListener(this);
                fragment.show(Objects.requireNonNull(getFragmentManager()), "Sign out Fragment.");
                break;
            }
            case R.id.btnAssign:
            {
                Log.d(TAG, "onClick: assign.");


                break;
            }
            case R.id.btnReject:
            {
                Log.d(TAG, "onClick: reject.");


                break;
            }
            case R.id.btnForward:
            {
                Log.d(TAG, "onClick: forward.");


                break;
            }
            case R.id.btnComplete:
            {
                Log.d(TAG, "onClick: complete.");

                break;
            }
        }

    }

    @Override
    public void saveNotes(String note) {

        setProcessing(true);

        Action action = new Action();
        action.setAction_request_id(mRequestId);
        action.setAction_description(note);
        action.setAction_performed("SAVE");
        action.setUser_type(mUserType);

        customActionViewModel.saveAction(action).observe(this, new Observer<Action>() {
            @Override
            public void onChanged(Action action) {

                if (action!=null)
                {
                    setProcessing(false);
                    Toast.makeText(getContext(), "Action saved.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setProcessing(false);
                    Toast.makeText(getContext(), "Action cannot be processed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}


