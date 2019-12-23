package com.darpg33.hackathon.cgs.ui.CustomDialogs;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class CustomActionDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "CustomActionDialog";

    public interface SaveListener{
        void addNote(String note);
    }

    public interface AssignListener{
        void assignRequest(String note, String assignedTo, String priority);
    }

    public interface AssignWorkerListener {
        void assignToWorker(String note, User assignedTo, String priority);
    }

    public interface CompleteListener{
        void completeRequest(String note);
    }

    public interface RejectListener{

        void rejectRequest(String note);

    }

    public interface ForwardListener{
        void forwardRequest(String note, String forwardedTo);
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

    //Widgets
    private TextInputEditText mNotes;
    private TextView mActionTitle;
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
        mAssignTo = root.findViewById(R.id.spAssignTo);
        mPriority = root.findViewById(R.id.spPriority);
        mlinAssignTo = root.findViewById(R.id.linAssignSpinner);
        mlinPriority = root.findViewById(R.id.linSpinnerPriority);
        mlinAssignWorker = root.findViewById(R.id.linAssignWorkerSpinner);
        mAssignWorker = root.findViewById(R.id.spAssignToWorker);
        mActionTitle = root.findViewById(R.id.action_toolbar_title);
        mRelativeLayout = root.findViewById(R.id.relativeLayout);

        mUsers = new ArrayList<>();
        userArrayList = new ArrayList<>();
        mActionTitle.setText(mActionDone);


        mNotes = root.findViewById(R.id.actionNotes);
        mProgressBar = root.findViewById(R.id.progressBar);
        MaterialButton btnSubmit = root.findViewById(R.id.btnSubmit);
        MaterialButton btnCancel = root.findViewById(R.id.btnCancel);

        customActionDialogViewModel = ViewModelProviders.of(this).get(CustomActionDialogViewModel.class);

        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        init(mActionBy);
        return root;
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
                                    mPriority.getSelectedItem().toString());
                        }
                        break;
                    }
                    case Fields.USER_TYPE_DEP_INCHARGE: {
                        if (checkInputs(mAssignWorker, mNotes, mPriority)) {
                            int i = mAssignWorker.getSelectedItemPosition();

                            User user = userArrayList.get(i);
                            mAssignWorkerListener.assignToWorker(mNotes.getText().toString(),
                                    user,
                                    mPriority.getSelectedItem().toString());
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
                    mForwardListener.forwardRequest(mNotes.getText().toString(), mAssignTo.getSelectedItem().toString());
                    dismiss();
                }
                break;
            }
            case "REJECT":
            {
                if (checkInputs(mNotes))
                {
                    mRejectListener.rejectRequest(mNotes.getText().toString());
                    dismiss();
                }
                break;
            }
            case "SAVE":
            {
                if (checkInputs(mNotes))
                {
                    mSaveListener.addNote(mNotes.getText().toString());
                    dismiss();
                }
                break;
            }
            case "COMPLETE":
            {
                if (checkInputs(mNotes))
                {
                    mCompleteListener.completeRequest(mNotes.getText().toString());
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
}
