package com.darpg33.hackathon.cgs.ui.home.tabs.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darpg33.hackathon.cgs.Model.Grievance;
import com.darpg33.hackathon.cgs.Model.User;
import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.darpg33.hackathon.cgs.ui.dialogs.viewProfile.ViewProfileDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class FeedsFragment extends Fragment implements FeedAdapter.GrievanceOnClickListener, FeedAdapter.UpvoteClickListener, FeedAdapter.UsernameClickListener {

    private static final String TAG = "FeedsFragment";

    //vars
    private FeedAdapter mFeedAdapter;
    private ArrayList<Grievance> mGrievances;
    private FeedsViewModel mFeedsViewModel;

    //widgets
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        mRecyclerView = view.findViewById(R.id.feedsRecyclerView);
        mGrievances = new ArrayList<>();
        mFeedsViewModel = ViewModelProviders.of(this).get(FeedsViewModel.class);
        setupRecyclerView();
        init();

        return view;
    }

    private void init() {

        //to filter requests from users district
        mFeedsViewModel.getUserDistrict().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    getAllRequests(s);
                }
            }
        });
    }

    private void getAllRequests(String user_district) {

        Log.d(TAG, "getAllRequests: called.");

        mFeedsViewModel.getAllRequests(user_district).observe(this, new Observer<ArrayList<Grievance>>() {
            @Override
            public void onChanged(ArrayList<Grievance> grievances) {

                Log.d(TAG, "onChanged: get all feeds." + grievances.size());
                //PagedList should be used instead
                mGrievances.clear();
                mGrievances.addAll(grievances);
                mFeedAdapter.notifyDataSetChanged();
            }
        });
    }


    private void setupRecyclerView() {

        Log.d(TAG, "setupRecyclerView: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mFeedAdapter = new FeedAdapter(mGrievances, this, this, this);
        mRecyclerView.setAdapter(mFeedAdapter);

    }

    @Override
    public void viewGrievance(String requestID) {

        Bundle bundle = new Bundle();
        bundle.putString(Fields.DB_GR_REQUEST_ID, requestID);
        Navigation.findNavController(getActivity(), R.id.requestsRecyclerView).navigate(R.id.nav_view_grievance, bundle);

    }

    @Override
    public void setUpvote(final String userId, String requestId, final int position) {

        mFeedsViewModel.setUpvote(userId, requestId).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean upvoted) {

                if (upvoted) {
                    if (mGrievances.size() > 0) {
                        Log.d(TAG, "onChanged: setting upvote. " + mGrievances.toString() + " " + mGrievances.size());
                        mGrievances.get(position).getUpvotes().add(userId);
                        mFeedAdapter.notifyItemChanged(position, mGrievances.get(position).getUpvotes());
                    }
                }
            }
        });
    }

    @Override
    public void resetUpvote(final String userId, String requestId, final int position) {

        Log.d(TAG, "onChanged: resetting upvote.");

        mFeedsViewModel.resetUpvote(userId, requestId).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean upvoteCanceled) {
                if (upvoteCanceled) {

                    if (mGrievances.size() > 0) {
                        mGrievances.get(position).getUpvotes().remove(userId);
                        mFeedAdapter.notifyItemChanged(position, mGrievances.get(position).getUpvotes());
                    }
                }
            }
        });
    }


    @Override
    public void viewProfile(String userId) {

        mFeedsViewModel.getUser(userId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if (user != null) {

                    if (user.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                        Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.nav_profile);

                    } else {
                        ViewProfileDialog dialog = new ViewProfileDialog();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(Fields.BUNDLE_USER_INFO, user);
                        dialog.setArguments(bundle);
                        dialog.show(Objects.requireNonNull(getFragmentManager()), "ViewProfileDialog");
                    }

                }

            }

        });
    }
}
