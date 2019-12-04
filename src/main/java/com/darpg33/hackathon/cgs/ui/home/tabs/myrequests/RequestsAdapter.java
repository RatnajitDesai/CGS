package com.darpg33.hackathon.cgs.ui.home.tabs.myrequests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.TimeDateUtilities;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter< RequestsAdapter.GrievanceViewHolder> {


    public interface GrievanceOnClickListener
    {
        void viewGrievance(String requestID);
    }


    private ArrayList<Grievance> mGrievances;
    private GrievanceOnClickListener mGrievanceOnClickListener;


    RequestsAdapter(ArrayList<Grievance> grievances, GrievanceOnClickListener listener)
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
    public void onBindViewHolder(@NonNull final GrievanceViewHolder holder, int position){
        StringBuffer request_id = new StringBuffer("#");

        StringBuffer title_buffer =  new StringBuffer(mGrievances.get(position).getTitle());
        int title_length = 30;
        if (title_buffer.length() > title_length)
        {
            title_buffer = new StringBuffer(title_buffer.substring(0,title_length)).append("...");
        }
        else {
            title_buffer = new StringBuffer(title_buffer);
        }

        request_id.append(mGrievances.get(position).getRequest_id());
        holder.mRequestId.setText(request_id);
        holder.mRequestStatus.setText(mGrievances.get(position).getStatus());
        holder.mRequestTimeStamp.setText(TimeDateUtilities.getDateAndTime(mGrievances.get(position).getTimestamp()));
        holder.mRequestTitle.setText(title_buffer);

    }

    @Override
    public int getItemCount() {
        return mGrievances.size();

    }


     class GrievanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mRequestStatus, mRequestId,
                mRequestTitle, mRequestTimeStamp;


         GrievanceViewHolder(@NonNull View itemView) {
             super(itemView);

             mRequestStatus = itemView.findViewById(R.id.requestStatusData);
             mRequestId = itemView.findViewById(R.id.requestId);
             mRequestTitle = itemView.findViewById(R.id.requestTitleData);
             mRequestTimeStamp = itemView.findViewById(R.id.requestTimeStamp);

             itemView.setOnClickListener(this);


        }

         @Override
         public void onClick(View v) {

             mGrievanceOnClickListener.viewGrievance(mRequestId.getText().toString().replace("#",""));

         }
     }
}
