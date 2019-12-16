package com.darpg33.hackathon.cgs.ui.CustomDialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.darpg33.hackathon.cgs.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CustomActionDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "CustomActionDialog";

    public interface SaveListener{
        void addNote(String note);
    }

    public interface AssignListener{
        void assignRequest(String note, String assignedTo, String priority);
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
    private String mActionBy, mActionDone;

    //Widgets
    private TextInputEditText mNotes;
    private Toolbar mToolbar;
    private AppCompatSpinner mAssignTo,mPriority;
    private LinearLayout mlinAssignTo, mlinPriority;
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
        mNotes = root.findViewById(R.id.actionNotes);
        mProgressBar = root.findViewById(R.id.progressBar);
        MaterialButton btnSubmit = root.findViewById(R.id.btnSubmit);
        MaterialButton btnCancel = root.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        init(mActionBy);
        return root;
    }

    private void init(String mActionBy) {

        switch (mActionBy)
        {
            case "citizen":
            {
                Log.d(TAG, "init: citizen");
                makeViewsInvisible(mlinAssignTo,mlinPriority);
                break;
            }
            case "mediator":
            {
                Log.d(TAG, "init: mediator");
                switch (mActionDone)
                {
                    case "ASSIGN":
                    {
                        makeViewsInvisible(mlinPriority);
                        break;
                    }
                    case "REJECT":
                    case "SAVE": {
                        makeViewsInvisible(mlinPriority,mlinAssignTo);
                        break;
                    }
                }
                break;
            }
            case "dep_incharge":
            {
                Log.d(TAG, "init: dep_incharge");
                switch (mActionDone)
                {
                    case "ASSIGN":
                    {

                        break;
                    }
                    case "REJECT":
                    {
                        makeViewsInvisible(mlinPriority,mlinAssignTo);
                        break;
                    }
                    case "FORWARD":
                    {
                        makeViewsInvisible(mlinPriority);
                        break;
                    }
                    case "COMPLETE":
                    {
                        makeViewsInvisible(mlinAssignTo,mlinPriority);
                        break;
                    }
                    case "SAVE":
                    {
                        makeViewsInvisible(mlinPriority,mlinAssignTo);
                    }
                }

                break;
            }
            case "worker":
            {
                Log.d(TAG, "init: worker");
                switch (mActionDone)
                {
                    case "REJECT":
                    {
                        makeViewsInvisible(mlinPriority,mlinAssignTo);
                        break;
                    }
                    case "FORWARD":
                    {
                        makeViewsInvisible(mlinPriority);
                        break;
                    }
                    case "COMPLETE":
                    {
                        makeViewsInvisible(mlinAssignTo,mlinPriority);
                        break;
                    }
                    case "SAVE":
                    {
                        makeViewsInvisible(mlinPriority,mlinAssignTo);
                    }
                }
                break;
            }
        }
    }



    private void makeViewsInvisible(View... views) {

        for (View view:
             views) {

            view.setVisibility(View.GONE);

        }


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setTitle(mActionDone);
        return dialog;
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
                if (checkInputs(mAssignTo,mNotes,mPriority))
                {
                    mAssignListener.assignRequest(mNotes.getText().toString(),
                            mAssignTo.getSelectedItem().toString(),
                            mPriority.getSelectedItem().toString());
                    dismiss();

                }
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
}
