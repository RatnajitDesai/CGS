package com.darpg33.hackathon.cgs.ui.CustomDialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.darpg33.hackathon.cgs.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CustomActionDialog extends BottomSheetDialogFragment implements View.OnClickListener {


    public interface SaveListener{

        void saveNotes(String note);

    }


    //vars
    private String mActionInProgress = "";
    private SaveListener mSaveListener;

    //Widgets
    private TextInputEditText mNotes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.dialog_fragment_custom_action, container, false);

        init(root);

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    public void setSaveListener(SaveListener listener)
    {
        mSaveListener = listener;
    }

    private void init(View root) {

        mNotes = root.findViewById(R.id.actionNotes);
        MaterialButton btnSubmit = root.findViewById(R.id.btnSubmit);
        MaterialButton btnCancel = root.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.btnSubmit:
            {
                mSaveListener.saveNotes(mNotes.getText().toString());
                dismiss();
                break;
            }
            case R.id.btnCancel:
            {
                dismiss();
                break;
            }

        }

    }
}
