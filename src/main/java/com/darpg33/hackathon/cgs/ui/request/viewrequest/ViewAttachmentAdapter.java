package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.darpg33.hackathon.cgs.Model.Attachment;
import com.darpg33.hackathon.cgs.R;

import java.util.ArrayList;

public class ViewAttachmentAdapter extends RecyclerView.Adapter<ViewAttachmentAdapter.ViewAttachmentViewHolder>{


    public interface ImageViewListener
    {
        void viewImage(Attachment attachment);
    }
    public interface DocumentViewListener
    {
        void viewDocument(Attachment attachment);
    }
    public interface LocationViewListener
    {
        void viewLocation(Attachment attachment);
    }

    private static final String TAG = "ViewAttachmentAdapter";
    private ArrayList<Attachment> mAttachments;
    private Context mContext;
    private ImageViewListener mImageViewListener;
    private DocumentViewListener mDocumentViewListener;
    private LocationViewListener mLocationViewListener;

    ViewAttachmentAdapter(ArrayList<Attachment> attachments,ImageViewListener imageViewListener,DocumentViewListener documentViewListener
    ,LocationViewListener locationViewListener)
    {
        mAttachments = attachments;
        mImageViewListener = imageViewListener;
        mDocumentViewListener = documentViewListener;
        mLocationViewListener = locationViewListener;
    }

    @NonNull
    @Override
    public ViewAttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_attachment_view_list,parent,false);
        return new ViewAttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAttachmentAdapter.ViewAttachmentViewHolder holder, final int position) {


        Log.d(TAG, "onBindViewHolder: "+mAttachments.get(position).toString());
        switch (mAttachments.get(position).getAttachmentType())
        {
            case "location":
            {
                Glide.with(mContext)
                        .load(R.drawable.ic_icon_map)
                        .placeholder(R.drawable.ic_icon_map)
                        .into(holder.view);
                break;
            }
            case "image":
            {

                Glide.with(mContext)
                        .load(mAttachments.get(position).getAttachmentPath())
                        .placeholder(R.drawable.ic_icon_share_image)
                        .into(holder.view);
                break;
            }
            case "document":
            {
                Glide.with(mContext)
                        .load(R.drawable.ic_icon_file)
                        .placeholder(R.drawable.ic_icon_file)
                        .into(holder.view);
                break;
            }


        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (mAttachments.get(position).getAttachmentType())
                {

                    case "image":
                    {
                        mImageViewListener.viewImage(mAttachments.get(position));
                        break;
                    }
                    case "document":
                    {
                        mDocumentViewListener.viewDocument(mAttachments.get(position));
                        break;
                    }
                    case "location":
                    {
                        mLocationViewListener.viewLocation(mAttachments.get(position));
                        break;
                    }
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return mAttachments.size();
    }


    class ViewAttachmentViewHolder extends RecyclerView.ViewHolder{


        private ImageView view;

         ViewAttachmentViewHolder(@NonNull View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            view = itemView.findViewById(R.id.ivAttachment);
        }

    }
}
