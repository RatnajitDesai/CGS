package com.darpg33.hackathon.cgs.ui.request.requestlist;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.Utils.TimeDateUtilities;

import java.util.ArrayList;
import java.util.Locale;

public class RequestsAdapter extends RecyclerView.Adapter< RequestsAdapter.GrievanceViewHolder> {

    private static final String TAG = "RequestsAdapter";

    public interface GrievanceOnClickListener
    {
        void viewGrievance(String requestID);
    }


    private ArrayList<Grievance> mGrievances;
    private GrievanceOnClickListener mGrievanceOnClickListener;


    public RequestsAdapter(ArrayList<Grievance> grievances, GrievanceOnClickListener listener)
    {
        mGrievances = grievances;
        mGrievanceOnClickListener = listener;
    }


    @NonNull
    @Override
    public GrievanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_request_list, parent,false);

        return new GrievanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GrievanceViewHolder holder, final int position) {

        StringBuffer title_buffer =  new StringBuffer(mGrievances.get(position).getTitle());


        switch (mGrievances.get(position).getStatus()) {
            case Fields
                    .GR_STATUS_PENDING: {
                holder.mRequestStatus.setTextColor(Color.RED);
                break;
            }
            case Fields
                    .GR_STATUS_IN_PROCESS: {
                holder.mRequestStatus.setTextColor(Color.BLUE);
                break;
            }
            case Fields
                    .GR_STATUS_RESOLVED: {
                holder.mRequestStatus.setTextColor(Color.rgb(34, 139, 34));
                break;
            }
        }

        holder.mRequestId.setText(String.format(Locale.ENGLISH, "CG#%s", mGrievances.get(position).getRequest_id()));
        holder.mRequestStatus.setText(mGrievances.get(position).getStatus());
        holder.mRequestTimeStamp.setText(TimeDateUtilities.getDateAndTime(mGrievances.get(position).getTimestamp()));
        holder.mRequestTitle.setText(title_buffer);

        if (mGrievances.get(position).getPriority() != null) {
            String priority = mGrievances.get(position).getPriority();
            if (priority.equalsIgnoreCase("High")) {
                holder.mRequestPriority.setVisibility(View.VISIBLE);
                holder.mRequestPriority.setTextColor(Color.RED);

            } else if (priority.equalsIgnoreCase("Medium")) {

                holder.mRequestPriority.setVisibility(View.VISIBLE);
                holder.mRequestPriority.setTextColor(Color.BLUE);

            }
        } else {
            holder.mRequestPriority.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGrievanceOnClickListener.viewGrievance(mGrievances.get(position).getRequest_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGrievances.size();
    }


    class GrievanceViewHolder extends RecyclerView.ViewHolder {

         private TextView mRequestStatus, mRequestId, mRequestPriority,
                mRequestTitle, mRequestTimeStamp;


         GrievanceViewHolder(@NonNull View itemView) {
             super(itemView);

             mRequestStatus = itemView.findViewById(R.id.requestStatusData);
             mRequestId = itemView.findViewById(R.id.requestId);
             mRequestPriority = itemView.findViewById(R.id.requestPriorityData);
             mRequestTitle = itemView.findViewById(R.id.requestTitleData);
             mRequestTimeStamp = itemView.findViewById(R.id.requestTimeStamp);
         }
    }
}
