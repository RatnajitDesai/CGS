package com.darpg33.hackathon.cgs.ui.home.tabs.feed;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.Utils.TimeDateUtilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.GrievanceViewHolder> {

    private static final String TAG = "RequestsAdapter";

    //vars
    private FirebaseUser mCurrentUser;

    public interface GrievanceOnClickListener {
        void viewGrievance(String requestID);
    }

    public interface UsernameClickListener {
        void viewProfile(String userId);
    }

    public interface UpvoteClickListener {
        void setUpvote(String userId, String requestId, int position);

        void resetUpvote(String userId, String requestId, int position);
    }


    private ArrayList<Grievance> mGrievances;
    private GrievanceOnClickListener mGrievanceOnClickListener;
    private UpvoteClickListener mUpvoteClickListener;
    private UsernameClickListener mUsernameClickListener;
    private Context mContext;


    FeedAdapter(ArrayList<Grievance> grievances, GrievanceOnClickListener listener, UpvoteClickListener upvoteClickListener, UsernameClickListener usernameClickListener) {
        mGrievances = grievances;
        mGrievanceOnClickListener = listener;
        mUpvoteClickListener = upvoteClickListener;
        mUsernameClickListener = usernameClickListener;
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @NonNull
    @Override
    public GrievanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_feed_post, parent, false);
        return new GrievanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GrievanceViewHolder holder, final int position) {


        StringBuffer title_buffer = new StringBuffer(mGrievances.get(position).getTitle());
        int title_length = 30;
        if (title_buffer.length() > title_length) {
            title_buffer = new StringBuffer(title_buffer.substring(0, title_length)).append("...");
        } else {
            title_buffer = new StringBuffer(title_buffer);
        }

        switch (mGrievances.get(position).getStatus()) {
            case Fields
                    .GR_STATUS_PENDING: {
                holder.mGrievanceStatus.setTextColor(Color.RED);
                break;
            }
            case Fields
                    .GR_STATUS_IN_PROCESS: {
                holder.mGrievanceStatus.setTextColor(Color.BLUE);
                break;
            }
            case Fields
                    .GR_STATUS_RESOLVED: {
                holder.mGrievanceStatus.setTextColor(Color.GREEN);
                break;
            }
        }

        holder.mGrievanceDescription.setText(mGrievances.get(position).getDescription());
        Log.d(TAG, "onBindViewHolder: username :" + mGrievances.get(position).getRequestedBy());
        holder.mGrievanceUsername.setText(mGrievances.get(position).getRequestedBy()); // user name
        holder.mGrievanceId.setText(String.format(Locale.ENGLISH, "CG#%s", mGrievances.get(position).getRequest_id()));
        holder.mGrievanceStatus.setText(mGrievances.get(position).getStatus());
        holder.mGrievanceTimestamp.setText(TimeDateUtilities.getDateAndTime(mGrievances.get(position).getTimestamp()));
        holder.mGrievanceTitle.setText(title_buffer);

        if (mGrievances.get(position).getUpvotes() != null) {
            Log.d(TAG, "onBindViewHolder: upvotes size :" + mGrievances.get(position).getUpvotes().size());
            holder.mGrievanceUpvotes.setText(String.format(Locale.ENGLISH, "%d", (mGrievances.get(position).getUpvotes().size())));
        }


        //initialize Toggle button
        if (mGrievances.get(position).getUpvotes() != null) {
            Log.d(TAG, "onBindViewHolder: initialize button .");
            if (mGrievances.get(position).getUpvotes().contains(mCurrentUser.getUid())) {
                Log.d(TAG, "onBindViewHolder: initialize  button ." + true);
                holder.mUpvote.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_thumb_up_colored_24dp, 0, 0, 0);
            } else {
                Log.d(TAG, "onBindViewHolder: initialize button ." + false);
                holder.mUpvote.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_thumb_up_grey_24dp, 0, 0, 0);
            }
        }


        holder.mUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: clicked.");

                if (mGrievances.get(position).getUpvotes() != null) {

                    if (mGrievances.get(position).getUpvotes().contains(mCurrentUser.getUid())) {
                        Log.d(TAG, "onClick: resetting upvote." + mCurrentUser.getUid());
                        holder.mUpvote.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_thumb_up_colored_24dp, 0, 0, 0);
                        mUpvoteClickListener.resetUpvote(mCurrentUser.getUid(),
                                mGrievances.get(position).getRequest_id(), position);
                    } else {
                        Log.d(TAG, "onClick: setting upvote." + mCurrentUser.getUid());
                        holder.mUpvote.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_thumb_up_grey_24dp, 0, 0, 0);
                        mUpvoteClickListener.setUpvote(mCurrentUser.getUid(),
                                mGrievances.get(position).getRequest_id(), position);
                    }
                } else {
                    Log.d(TAG, "onClick: setting upvote." + mCurrentUser.getUid());
                    holder.mUpvote.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_thumb_up_grey_24dp, 0, 0, 0);
                    mUpvoteClickListener.setUpvote(mCurrentUser.getUid(),
                            mGrievances.get(position).getRequest_id(), position);
                }
            }
        });

        holder.mGrievanceDescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mGrievanceOnClickListener.viewGrievance(mGrievances.get(position).getRequest_id());
            }
        });


        holder.mGrievanceUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsernameClickListener.viewProfile(mGrievances.get(position).getUser_id());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mGrievances.size();
    }


    class GrievanceViewHolder extends RecyclerView.ViewHolder {
        private TextView mGrievanceStatus, mGrievanceId, mGrievanceTitle, mGrievanceUsername,
                mGrievanceDescription, mGrievanceTimestamp, mGrievanceUpvotes;
        private AppCompatButton mUpvote;

        GrievanceViewHolder(@NonNull View itemView) {

            super(itemView);
            mContext = itemView.getContext();
            mGrievanceStatus = itemView.findViewById(R.id.grievanceStatus);
            mGrievanceUsername = itemView.findViewById(R.id.grievanceUsername);
            mGrievanceId = itemView.findViewById(R.id.grievanceId);
            mGrievanceTitle = itemView.findViewById(R.id.grievanceTitle);
            mGrievanceDescription = itemView.findViewById(R.id.grievanceDescription);
            mGrievanceTimestamp = itemView.findViewById(R.id.grievanceTimestamp);
            mGrievanceUpvotes = itemView.findViewById(R.id.grievanceUpvotes);
            mUpvote = itemView.findViewById(R.id.btnGrievanceUpvote);

        }


    }
}
