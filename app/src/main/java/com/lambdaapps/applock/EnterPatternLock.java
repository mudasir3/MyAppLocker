package com.lambdaapps.applock;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amnix.materiallockview.MaterialLockView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.startapp.android.publish.ads.nativead.NativeAdDetails;
import com.startapp.android.publish.ads.nativead.NativeAdPreferences;
import com.startapp.android.publish.ads.nativead.StartAppNativeAd;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.unity3d.ads.UnityAds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class EnterPatternLock extends AppCompatActivity {
    MaterialLockView materialLockView;
    Context context;
    RelativeLayout re;
    String app = null;
    TextView appNamePattern;
    ImageView appIconPattern;
    SaveState saveState;
    String main;
    boolean abc;
    SaveLogs saveLogs;
    int WRONG_PATTERN_COUNTER = 0;
    private FirebaseAnalytics mFirebaseAnalytics;
    private AdView mAdView;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private int random_no=0;
    /**
     * StartApp Native Ad declaration
     */
    private StartAppAd startAppAd;

    final private UnityAdsListener unityAdsListener = new UnityAdsListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_pattern_lock);

        UnityAds.initialize(this, getString(R.string.unity_id), unityAdsListener);
        StartAppSDK.init(this, getString(R.string.startapp_key), true);

        context = EnterPatternLock.this;
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        app = b.getString("app");
        try {
            main = b.getString("main");
            abc = main.equals("true");
        } catch (Exception e) {

        }
        saveLogs = new SaveLogs(this);

        mAdView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder()
                .addTestDevice("33BE2250B43518CCDA7DE426D04EE232").build();

        random_no=getRandomNumberInRange(1,3);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersetial_ad_unit_id));
        mInterstitialAd.loadAd(adRequest);
        startAppAd = new StartAppAd(this);

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAdView.loadAd(adRequest);
        appIconPattern = (ImageView) findViewById(R.id.appIconPattern);
        appNamePattern = (TextView) findViewById(R.id.appNamePattern);
        saveState = new SaveState(this);
        appIconPattern.setImageDrawable(getAppIcon(app));
        appNamePattern.setText(getAppName(app));
        materialLockView = (MaterialLockView) findViewById(R.id.enterPatternLockView);
        re = (RelativeLayout) findViewById(R.id.re);
        mFirebaseAnalytics.logEvent("app", b);
        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternStart() {
                super.onPatternStart();
            }

            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                super.onPatternDetected(pattern, SimplePattern);
                if (getPattern().equals(SimplePattern)) {
                    if (abc) {
                        saveLogs.saveLogs(getString(R.string.app_name), true);
                        startMain();
                    } else {
                        saveState.saveState("false");
                        saveLogs.saveLogs(app, true);
                        startApp(app);
                        finish();
                    }
                } else {
                    materialLockView.clearPattern();
                    saveLogs.saveLogs(app, false);
                    WRONG_PATTERN_COUNTER = WRONG_PATTERN_COUNTER + 1;
                    if (WRONG_PATTERN_COUNTER == 3) {
                        finish();
                    }
                    Toast.makeText(context, getString(R.string.wrong_pattern), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
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

    private void startMain() {
        Intent i = new Intent(EnterPatternLock.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            Log.d("Focus debug", "Lost focus !");
        }
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private Drawable getAppIcon(String packagename) {
        Drawable icon = null;
        try {
            icon = getPackageManager().getApplicationIcon(packagename);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }

    private void startApp(String packagename) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packagename);
        if (launchIntent != null) {
            startActivity(launchIntent);
        }
    }

    private String getAppName(String packagename) {
        PackageManager packageManager = getApplicationContext().getPackageManager();
        String appName = null;
        try {
            appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packagename, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    public String getPattern() {
        File file = new File("/data/data/com.lambdaapps.applock/files/pattern");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            Log.d("okuma hatası", "no 1");
        }
        return text.toString();
    }
}
