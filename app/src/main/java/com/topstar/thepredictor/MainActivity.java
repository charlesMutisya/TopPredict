package com.topstar.thepredictor;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ironsource.adapters.supersonicads.SupersonicConfig;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.impressionData.ImpressionData;
import com.ironsource.mediationsdk.impressionData.ImpressionDataListener;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.sdk.OfferwallListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.topstar.thepredictor.BuildConfig.DEBUG;

public class MainActivity extends AppCompatActivity implements RewardedVideoListener, OfferwallListener, InterstitialListener, ImpressionDataListener {
    private SectionsPagerAdapter sectionsPagerAdapter;
    private boolean doubleBack = false;
    private final String TAG = MainActivity.class.getSimpleName();
    private final String APP_KEY = "10e9b4415";
    private final String FALLBACK_USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FirebaseMessaging.getInstance().subscribeToTopic("Tips1");
        AudienceNetworkInitializeHelper.initialize(this);

        FabSpeedDial fabSpeedDial= findViewById(R.id.fabspeed);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int idc = menuItem.getItemId();
                    if (idc== R.id.action_mail){
                        Intent feedback = new Intent(MainActivity.this, FeedBack.class);

                        startActivity(feedback);

                    }else if (idc== R.id.action_rate){
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
                        }

                    }else  if (idc==R.id.action_share){
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "We deliver daily wins to you all for FREE. Download here https://play.google.com/store/apps/details?id=com.topstar.thepredictor";
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " The Winning Stars");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(sharingIntent);

                    }



                return super.onMenuItemSelected(menuItem);
            }
        });
        startIronSourceInitTask();
        IronSource.getAdvertiserId(this);
        //Network Connectivity Status
        IronSource.shouldTrackNetworkState(this, true);


    }
    private void startIronSourceInitTask(){
        String advertisingId = IronSource.getAdvertiserId(this);
        // we're using an advertisingId as the 'userId'
        initIronSource(APP_KEY, advertisingId);

    }

    private void initIronSource(String appKey, String userId) {
        // Be sure to set a listener to each product that is being initiated
        // set the IronSource rewarded video listener
        IronSource.setRewardedVideoListener(this);
        // set the IronSource offerwall listener
        IronSource.setOfferwallListener(this);
        // set client side callbacks for the offerwall
        SupersonicConfig.getConfigObj().setClientSideCallbacks(true);
        // set the interstitial listener
        IronSource.setInterstitialListener(this);
        // add the Impression Data listener

        IronSource.addImpressionDataListener(this);

        // set the IronSource user id
        IronSource.setUserId(userId);
        // init the IronSource SDK
        /**
         *Ad Units should be in the type of IronSource.Ad_Unit.AdUnitName, example
         */
        IronSource.init(this, appKey, IronSource.AD_UNIT.INTERSTITIAL, IronSource.AD_UNIT.BANNER);
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewardedVideoAvailabilityChanged(boolean b) {

    }

    @Override
    public void onRewardedVideoAdStarted() {

    }

    @Override
    public void onRewardedVideoAdEnded() {

    }

    @Override
    public void onRewardedVideoAdRewarded(Placement placement) {

    }

    @Override
    public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {

    }

    @Override
    public void onRewardedVideoAdClicked(Placement placement) {

    }

    @Override
    public void onOfferwallAvailable(boolean b) {

    }

    @Override
    public void onOfferwallOpened() {

    }

    @Override
    public void onOfferwallShowFailed(IronSourceError ironSourceError) {

    }

    @Override
    public boolean onOfferwallAdCredited(int i, int i1, boolean b) {
        return false;
    }

    @Override
    public void onGetOfferwallCreditsFailed(IronSourceError ironSourceError) {

    }

    @Override
    public void onOfferwallClosed() {

    }

    @Override
    public void onInterstitialAdReady() {

    }

    @Override
    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {

    }

    @Override
    public void onInterstitialAdOpened() {

    }

    @Override
    public void onInterstitialAdClosed() {

    }

    @Override
    public void onInterstitialAdShowSucceeded() {

    }

    @Override
    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {

    }

    @Override
    public void onInterstitialAdClicked() {

    }

    @Override
    public void onImpressionSuccess(ImpressionData impressionData) {

    }


    class  SectionsPagerAdapter extends FragmentPagerAdapter{

        public SectionsPagerAdapter(MainActivity mainActivity, @NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return  new DailyTips();
                case 1:
                    return  new Livescore();
            }
            return new DailyTips();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Daily Tips";
                case 1:
                    return  "Livescore";
            }
            return "Daily Tips";
        }


    }

    public static class AudienceNetworkInitializeHelper
            implements AudienceNetworkAds.InitListener {

        /**
         * It's recommended to call this method from Application.onCreate().
         * Otherwise you can call it from all Activity.onCreate()
         * methods for Activities that contain ads.
         *
         * @param context Application or Activity.
         */
        static void initialize(Context context) {
            if (!AudienceNetworkAds.isInitialized(context)) {
                if (DEBUG) {
                    AdSettings.turnOnSDKDebugger(context);
                }

                AudienceNetworkAds
                        .buildInitSettings(context)
                        .withInitListener(new AudienceNetworkInitializeHelper())
                        .initialize();
            }
        }

        @Override
        public void onInitialized(AudienceNetworkAds.InitResult result) {
            Log.d(AudienceNetworkAds.TAG, result.getMessage());
        }
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
        if (doubleBack) {
            super.onBackPressed();
            return;
        }
        this.doubleBack = true;
        Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2000);
    }
}



