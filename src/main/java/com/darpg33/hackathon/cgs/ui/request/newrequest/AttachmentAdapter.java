package com.darpg33.hackathon.cgs.ui.request.newrequest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.R;

import java.util.ArrayList;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>{

    private ArrayList<Attachment> mAttachments;
    private Context mContext;
    private RemoveItemListener removeItemListener;
    public boolean isClickable = true;

    public AttachmentAdapter(ArrayList<Attachment> attachments)
    {
        mAttachments = attachments;
    }


    public interface RemoveItemListener {

        void removeItem(Attachment attachment,int position);

    }

    public void setRemoveItemListener(RemoveItemListener listener)
    {
        removeItemListener = listener;
    }

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_attachment, parent,false);
        return new AttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttachmentViewHolder holder, int position) {
        position = holder.getAdapterPosition();

        holder.mAttachmentName.setText(mAttachments.get(position).getAttachment_name());

        switch (mAttachments.get(position).getAttachmentType())
        {
            case "image":
            {
                holder.mAttachmentName.setText(mAttachments.get(position).getAttachment_name());
                Glide.with(mContext)
                        .load(mAttachments.get(position).getAttachmentUri())
                        .placeholder(R.drawable.ic_icon_share_image)
                        .into(holder.mAttachmentType);
                break;
            }
            case "location":
            {
                holder.mAttachmentName.setText(mAttachments.get(position).getAddress().getAddressLine(0));
                holder.mAttachmentType.setImageResource(R.drawable.ic_icon_map);
                break;
            }
            case "document":
            {
                holder.mAttachmentName.setText(mAttachments.get(position).getAttachment_name());
                holder.mAttachmentType.setImageResource(R.drawable.ic_icon_file);
                break;
            }
        }

        holder.mDeleteAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable) {
                    int position = holder.getAdapterPosition();
                    removeItemListener.removeItem(mAttachments.get(position), position);
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return mAttachments.size();
    }

     class AttachmentViewHolder extends RecyclerView.ViewHolder {


        private TextView mAttachmentName;
        private ImageView mAttachmentType, mDeleteAttachment;

         AttachmentViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext =itemView.getContext();
            mAttachmentName = itemView.findViewById(R.id.txtAttachmentName);
            mAttachmentType = itemView.findViewById(R.id.ivAttachmentType);
            mDeleteAttachment = itemView.findViewById(R.id.ivDeleteAttachment);

        }

    }
}
