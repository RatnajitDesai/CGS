package com.darpg33.hackathon.cgs.ui.home.tabs.actions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Notification;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.TimeDateUtilities;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private static final String TAG = "NotificationAdapter";


    public interface NotificationOnClickListener {
        void viewGrievance(String requestID);
    }


    private ArrayList<Notification> mNotifications;
    private NotificationOnClickListener mNotificationListener;


    NotificationAdapter(ArrayList<Notification> notifications, NotificationOnClickListener listener) {

        mNotifications = notifications;
        mNotificationListener = listener;

    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_notification, parent, false);

        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, final int position) {


        holder.mNotificationTitle.setText(mNotifications.get(position).getTitle());
        holder.mNotificationBody.setText(mNotifications.get(position).getBody());
        holder.mNotificationTimestamp.setText(TimeDateUtilities.getDateAndTime(mNotifications.get(position).getTimestamp()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNotificationListener.viewGrievance(mNotifications.get(position).getRequestId());

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }


    class NotificationViewHolder extends RecyclerView.ViewHolder {

        private TextView mNotificationTitle, mNotificationBody, mNotificationTimestamp;


        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            mNotificationTitle = itemView.findViewById(R.id.notificationTitle);
            mNotificationBody = itemView.findViewById(R.id.notificationBody);
            mNotificationTimestamp = itemView.findViewById(R.id.notificationTimeStamp);


        }


    }
}
