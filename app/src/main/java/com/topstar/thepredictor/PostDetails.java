package com.topstar.thepredictor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;

public class PostDetails extends AppCompatActivity {
    DatabaseReference mRef;
    String postKey;
    TextView tvTitle, tvBody, tvTime;
    ProgressDialog pd;
    String selection;


    private AdView adView;
    private final String TAG = PostDetails.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_post_details);
        postKey = getIntent().getExtras().getString("postkey");
        selection = getIntent().getExtras().getString("selection");
        tvBody = findViewById(R.id.tvBody);
        tvTitle = findViewById(R.id.tvTitle);
        tvTime = findViewById(R.id.post_time);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        lInterstitial();




        if (postKey != null) {

            mRef = FirebaseDatabase.getInstance().getReference().child("Tips1").child(postKey);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String title = dataSnapshot.child("tipsTitle").getValue().toString();
                    String body = dataSnapshot.child("tipsDetails").getValue().toString();
                    String date1 = dataSnapshot.child("tipsDate").getValue().toString();

                    if (title != null) {
                        tvTitle.setText(title.toUpperCase());
                        pd.dismiss();
                    } else {
                        Toast.makeText(PostDetails.this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                    if (body != null) {
                        tvBody.setText(body);

                    }
                    if (date1 != null) {
                        tvTime.setText(date1);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        adView= findViewById(R.id.adViewpicks);
        AdRequest adRequest = new AdRequest.Builder().build();
       adView.loadAd(adRequest);
    }

    private  void  lInterstitial(){
        IronSource.init(this, "10e9b4415", IronSource.AD_UNIT.INTERSTITIAL);
        IronSource.loadInterstitial();
        IronSource.setInterstitialListener(new InterstitialListener() {


            @Override
            public void onInterstitialAdReady() {
                if (IronSource.isInterstitialReady()) {
                    //show the interstitial
                    IronSource.showInterstitial();
                }
            }
            /**
             * invoked when there is no Interstitial Ad available after calling load function.
             */


            @Override
            public void onInterstitialAdLoadFailed(IronSourceError error) {
            }
            /**
             * Invoked when the Interstitial Ad Unit is opened
             */
            @Override
            public void onInterstitialAdOpened() {
            }
            /*
             * Invoked when the ad is closed and the user is about to return to the application.
             */
            @Override
            public void onInterstitialAdClosed() {
            }
            /**
             * Invoked when Interstitial ad failed to show.
             * @param error - An object which represents the reason of showInterstitial failure.
             */
            @Override
            public void onInterstitialAdShowFailed(IronSourceError error) {
            }
            /*
             * Invoked when the end user clicked on the interstitial ad, for supported networks only.
             */
            @Override
            public void onInterstitialAdClicked() {
                Log.d(TAG, "onInterstitialAdClicked");

            }
            @Override
            public void onInterstitialAdShowSucceeded() {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menub, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
    finish();

        }
        if (id == R.id.feedback) {
            startActivity(new Intent(this, FeedBack.class));

        } else if (id == R.id.menu_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = " We deliver daily wins to you all for FREE. Download here https://play.google.com/store/apps/details?id=com.topstar.thepredictor";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " Smart Spesa Tips");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(sharingIntent);
        } else if (id == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.privacyP) {
            Uri uri = Uri.parse("https://odibetsprivacypolicy.blogspot.com/2021/04/smart-spesa-policy.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}