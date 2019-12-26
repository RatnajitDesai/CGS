package com.darpg33.hackathon.cgs.ui.request.viewrequest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Action;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.Utils.TimeDateUtilities;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ViewActionViewHolder>{

    public interface UsernameClickListener {
        void viewProfile(String userId);
    }



    private static final String TAG = "ActionsAdapter";
    private ArrayList<Action> mActions;
    private Context mContext;
    private UsernameClickListener mUsernameClickListener;


    ActionsAdapter(ArrayList<Action> actions, UsernameClickListener listener)
    {
        mActions = actions;
        mUsernameClickListener = listener;
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

        ViewActionViewHolder(@NonNull View itemView) {
            super(itemView);
             mContext = itemView.getContext();
             actionTitle = itemView.findViewById(R.id.actionTitle);
             actionDescription = itemView.findViewById(R.id.actionDescription);
             actionTime = itemView.findViewById(R.id.actionTime);
             actionDate = itemView.findViewById(R.id.actionDate);
             actionSubmittedBy = itemView.findViewById(R.id.actionSubmittedBy);
            actionUserType = itemView.findViewById(R.id.actionUserType);
        }

    }
}
