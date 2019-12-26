package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.Utils.TimeDateUtilities;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ViewActionViewHolder> implements
        ViewAttachmentAdapter.DocumentViewListener,
        ViewAttachmentAdapter.ImageViewListener,
        ViewAttachmentAdapter.LocationViewListener {


    @Override
    public void viewImage(Attachment attachment) {

        Log.d(TAG, "viewImage: ");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(attachment.getAttachmentPath()), "image/*");
        mIntentListener.startIntent(intent);
    }

    @Override
    public void viewDocument(Attachment attachment) {

        Log.d(TAG, "viewDocument: ");

        String mimeType = attachment.getAttachment_name().substring(attachment.getAttachment_name().lastIndexOf(".") + 1);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (mimeType) {
            case "pdf": {
                intent.setDataAndType(Uri.parse(attachment.getAttachmentPath()), "application/pdf");
                break;
            }
            case "doc":
            case "docx": {
                intent.setDataAndType(Uri.parse(attachment.getAttachmentPath()), "application/msword");
                break;
            }
            case "txt": {
                intent.setDataAndType(Uri.parse(attachment.getAttachmentPath()), "text/plain");
                break;
            }
            case "zip": {
                intent.setDataAndType(Uri.parse(attachment.getAttachmentPath()), "application/zip");
                break;
            }
        }
        mIntentListener.startIntent(intent);
    }

    @Override
    public void viewLocation(Attachment attachment) {

        Log.d(TAG, "viewLocation: ");
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f&z=16", attachment.getGeoPoint().getLatitude(),
                attachment.getGeoPoint().getLongitude(),
                attachment.getGeoPoint().getLatitude(),
                attachment.getGeoPoint().getLongitude()
        );

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mIntentListener.startIntent(intent);

    }

    public interface IntentListener {
        void startIntent(Intent intent);
    }


    public interface UsernameClickListener {
        void viewProfile(String userId);
    }


    private static final String TAG = "ActionsAdapter";
    private ArrayList<Action> mActions;
    private Context mContext;
    private ViewAttachmentAdapter mViewAttachmentAdapter;
    private UsernameClickListener mUsernameClickListener;
    private IntentListener mIntentListener;


    ActionsAdapter(ArrayList<Action> actions, UsernameClickListener listener, IntentListener intentListener) {
        mActions = actions;
        mUsernameClickListener = listener;
        mIntentListener = intentListener;
    }

    @NonNull
    @Override
    public ViewActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_grievance_action,parent,false);
        return new ViewActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionsAdapter.ViewActionViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: "+mActions.get(position).toString());

        if (mActions.get(position).getAttachments() != null) {

            // setup recycler view
            LinearLayoutManager manager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            holder.mRecyclerView.setLayoutManager(manager);
            mViewAttachmentAdapter = new ViewAttachmentAdapter(mActions.get(position).getAttachments(), this, this, this);
            holder.mRecyclerView.setAdapter(mViewAttachmentAdapter);

        } else {
            holder.mRecyclerView.setVisibility(View.GONE);
        }

        holder.actionTitle.setText(mActions.get(position).getAction_info());
        holder.actionDescription.setText(mActions.get(position).getAction_description());
        holder.actionTime.setText(TimeDateUtilities.getTime(mActions.get(position).getTimestamp()));
        holder.actionDate.setText(TimeDateUtilities.getDate(mActions.get(position).getTimestamp()));

        if (position == 0) {
            holder.actionSubmittedBy.setVisibility(View.GONE);
            holder.actionUserType.setText(Fields.USER_TYPE_MEDIATOR);
        } else {
            if (mActions.get(position).getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                holder.actionSubmittedBy.setText("You");
            } else {
                holder.actionSubmittedBy.setText(mActions.get(position).getUsername());
            }
            holder.actionUserType.setText(mActions.get(position).getUser_type());
        }

        holder.actionSubmittedBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsernameClickListener.viewProfile(mActions.get(position).getUser_id());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mActions.size();
    }


    class ViewActionViewHolder extends RecyclerView.ViewHolder{

        private TextView actionTitle, actionDescription, actionTime, actionDate, actionSubmittedBy, actionUserType;
        private RecyclerView mRecyclerView;

        ViewActionViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            actionTitle = itemView.findViewById(R.id.actionTitle);
            actionDescription = itemView.findViewById(R.id.actionDescription);
            actionTime = itemView.findViewById(R.id.actionTime);
            actionDate = itemView.findViewById(R.id.actionDate);
            actionSubmittedBy = itemView.findViewById(R.id.actionSubmittedBy);
            actionUserType = itemView.findViewById(R.id.actionUserType);
            mRecyclerView = itemView.findViewById(R.id.attachmentsRecyclerView);
        }

    }
}
