package com.topstar.thepredictor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;


public class DailyTips extends Fragment {
    View view;
    RecyclerView mrecycler;
    LinearLayoutManager mlinearlayout;
    TextView loading;
    DatabaseReference mdatabaseRef;
    FirebaseRecyclerAdapter<Tips, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Tips> smartselect;


    private final String TAG = DailyTips.class.getSimpleName();

    private FrameLayout mBannerParentLayout;
    private IronSourceBannerLayout mIronSourceBannerLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dailytips, container, false);
        mdatabaseRef = FirebaseDatabase.getInstance().getReference().child("Tips1");
        loading = view.findViewById(R.id.loadwait);
        IronSource.init((Activity) getContext(), "10e9b4415", IronSource.AD_UNIT.BANNER);

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        mBannerParentLayout = view.findViewById(R.id.banner_footer);
        createAndloadBanner();
        mrecycler = view.findViewById(R.id.recycler1);
        mrecycler.setHasFixedSize(false);
        mlinearlayout = new LinearLayoutManager(getContext());
        mlinearlayout.setReverseLayout(true);
        mlinearlayout.setStackFromEnd(true);
        mrecycler.setLayoutManager(mlinearlayout);
        smartselect = new FirebaseRecyclerOptions.Builder<Tips>().setQuery(mdatabaseRef, Tips.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Tips, ViewHolder>(smartselect) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Tips model) {
                final String item_key = getRef(position).getKey();
                holder.setTitle(model.getTipsTitle());
                holder.setDetails(model.getTipsDetails());
                holder.setTime(model.getTipsDate());
                loading.setVisibility(View.GONE);
                holder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent adDetails = new Intent(view.getContext(), PostDetails.class);
                        adDetails.putExtra("postkey", item_key);
                        adDetails.putExtra("selection", "Tips1");
                        startActivity(adDetails);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewcard, parent, false);
                ViewHolder viewHolder1 = new ViewHolder(itemView);
                return viewHolder1;

            }
        };
        mrecycler.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }
    private void createAndloadBanner() {
        // choose banner size
        ISBannerSize size = ISBannerSize.BANNER;

        // instantiate IronSourceBanner object, using the IronSource.createBanner API
        mIronSourceBannerLayout = IronSource.createBanner((Activity) getContext(), size);

        // add IronSourceBanner to your container
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mBannerParentLayout.addView(mIronSourceBannerLayout, 0, layoutParams);

        if (mIronSourceBannerLayout != null) {
            // set the banner listener
            mIronSourceBannerLayout.setBannerListener(new BannerListener() {
                @Override
                public void onBannerAdLoaded() {
                    Log.d(TAG, "onBannerAdLoaded");
                    // since banner container was "gone" by default, we need to make it visible as soon as the banner is ready
                    mBannerParentLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onBannerAdLoadFailed(IronSourceError error) {
                    Log.d(TAG, "onBannerAdLoadFailed" + " " + error);
                }

                @Override
                public void onBannerAdClicked() {
                    Log.d(TAG, "onBannerAdClicked");
                }

                @Override
                public void onBannerAdScreenPresented() {
                    Log.d(TAG, "onBannerAdScreenPresented");
                }

                @Override
                public void onBannerAdScreenDismissed() {
                    Log.d(TAG, "onBannerAdScreenDismissed");
                }

                @Override
                public void onBannerAdLeftApplication() {
                    Log.d(TAG, "onBannerAdLeftApplication");
                }
            });

            // load ad into the created banner
            IronSource.loadBanner(mIronSourceBannerLayout);
        } else {
            Toast.makeText(getActivity(), "IronSource.createBanner returned null", Toast.LENGTH_LONG).show();
        }
    }





    /**
     * Destroys IronSource Banner and removes it from the container
     *
     */
    private void destroyAndDetachBanner() {
        IronSource.destroyBanner(mIronSourceBannerLayout);
        if (mBannerParentLayout != null) {
            mBannerParentLayout.removeView(mIronSourceBannerLayout);
        }

    }
    public void onResume() {
        super.onResume();
        IronSource.onResume(getActivity());
    }
    public void onPause() {
        super.onPause();
        IronSource.onPause(getActivity());
    }

    @Override
    public void onDestroyView() {
        destroyAndDetachBanner();
        super.onDestroyView();
    }

}
