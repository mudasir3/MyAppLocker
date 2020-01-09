package com.lambdaapps.applock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.startapp.android.publish.ads.nativead.NativeAdDetails;
import com.startapp.android.publish.ads.nativead.NativeAdPreferences;
import com.startapp.android.publish.ads.nativead.StartAppNativeAd;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.Random;

public class SetLockTypeActivity extends AppCompatActivity {

    SwitchCompat setPattern, setNormalPIN;
    Context context;
    Button start;

    final private UnityAdsListener unityAdsListener = new UnityAdsListener();
    /**
     * StartApp Native Ad declaration
     */
    private StartAppNativeAd startAppNativeAd;
    private NativeAdDetails nativeAd;
    private StartAppAd startAppAd;

    private AdView mAdView;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private int random_no=0;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.set_lock_type);

        UnityAds.initialize(this, getString(R.string.unity_id), unityAdsListener);
        StartAppSDK.init(this, getString(R.string.startapp_key), true);

        context = SetLockTypeActivity.this;
        setPattern = (SwitchCompat) findViewById(R.id.setPattern);
        setNormalPIN = (SwitchCompat) findViewById(R.id.setNormalPINSwitch);
        start = (Button) findViewById(R.id.strt);

        startAppAd = new StartAppAd(this);
        startAppNativeAd = new StartAppNativeAd(this);
        nativeAd = null;
        //StartApp Ads
        startAppNativeAd.loadAd(
                new NativeAdPreferences()
                        .setAdsNumber(1)
                        .setAutoBitmapDownload(true)
                        .setImageSize(NativeAdPreferences.NativeAdBitmapSize.SIZE150X150),
                nativeAdListener);

        mAdView = (AdView) this.findViewById(R.id.adView2);

        adRequest = new AdRequest.Builder()
                .addTestDevice("33BE2250B43518CCDA7DE426D04EE232").build();

        mAdView.loadAd(adRequest);

        random_no=getRandomNumberInRange(1,3);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersetial_ad_unit_id));
        mInterstitialAd.loadAd(adRequest);
        if (random_no == 1) {
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
        } else if (random_no == 2) {
            if (UnityAds.isReady("rewardedVideo")) { //Make sure a video is available & the placement is valid.
                UnityAds.show(this, "rewardedVideo");
            }
        } else if (random_no==3){
            startAppAd.loadAd();
            startAppAd.showAd();
        } else {
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
        }

        ManageLockType.setLockType("", context);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setPattern.isChecked() && !setNormalPIN.isChecked()) {
                    startPattern();
                }
                if (!setPattern.isChecked() && setNormalPIN.isChecked()) {
                    startSetNormalPIN();
                }
                if (!setPattern.isChecked() && !setNormalPIN.isChecked()) {
                    Toast.makeText(context, getString(R.string.select_lock_type), Toast.LENGTH_SHORT).show();
                }
            }
        });
        setNormalPIN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ManageLockType.setLockType("pin", context);
                    setPattern.setChecked(false);
                }
                if (!b) {
                    ManageLockType.setLockType("tp", context);
                    setPattern.setChecked(true);
                }
            }
        });

        setPattern.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ManageLockType.setLockType("pattern", context);
                    setNormalPIN.setChecked(false);
                }
                if (!b) {
                    ManageLockType.setLockType("tp", context);
                    setNormalPIN.setChecked(true);
                }
            }
        });
    }

    private void startMain() {
        Intent i = new Intent(SetLockTypeActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void startSetNormalPIN() {
        Intent i = new Intent(SetLockTypeActivity.this, SetNormalPIN.class);
        i.putExtra("try", "true");
        startActivity(i);
        finish();
    }

    private void startPattern() {
        Intent i = new Intent(SetLockTypeActivity.this, SetPatternLockActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        //StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    protected void onResume() {
        if (mAdView != null) {
            mAdView.resume();
        }
        if (random_no==1) {
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
        } else if (random_no==2) {
            if (UnityAds.isReady("rewardedVideo")) { //Make sure a video is available & the placement is valid.
                UnityAds.show(this, "rewardedVideo");
            }

        } else if (random_no==3) {
            startAppAd.onResume();
        } else{
            startAppAd.onResume();
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void showHelpForTimePIN() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setTitle("Time PIN");
        alertDialogBuilder
                .setMessage(getString(R.string.help_time_pin))
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    /**
     * Native Ad Callback
     */
    private AdEventListener nativeAdListener = new AdEventListener() {

        @Override
        public void onReceiveAd(Ad ad) {

            // Get the native ad
            ArrayList<NativeAdDetails> nativeAdsList = startAppNativeAd.getNativeAds();
            if (nativeAdsList.size() > 0) {
                nativeAd = nativeAdsList.get(0);
            }

            // Verify that an ad was retrieved
            if (nativeAd != null) {

                // When ad is received and displayed - we MUST send impression
                nativeAd.sendImpression(SetLockTypeActivity.this);

            }
        }

        @Override
        public void onFailedToReceiveAd(Ad ad) {

            // Error occurred while loading the native ad
        }
    };
}
