package com.lambdaapps.applock;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.unity3d.ads.UnityAds;

import java.util.Random;

public class EnterNormalPIN extends AppCompatActivity {

    Button one, two, three, four, five, six, seven, eight, nine, zero, delete, done;
    EditText pin_enter;
    String a = "";
    SaveState saveState;
    String app;
    String main;
    boolean abc;
    private AdView mAdView;
    ImageView appIconIv;
    TextView appNameTv;
    SaveLogs saveLogs;
    private InterstitialAd mInterstitialAd;
    private int random_no = 0;
    /**
     * StartApp Native Ad declaration
     */
    private StartAppAd startAppAd;
    private AdRequest adRequest;

    final private UnityAdsListener unityAdsListener = new UnityAdsListener();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.enter_normal_pin);

        UnityAds.initialize(this, getString(R.string.unity_id), unityAdsListener);
        StartAppSDK.init(this, getString(R.string.startapp_key), true);

        saveState = new SaveState(this);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        mAdView = (AdView) findViewById(R.id.adView2);
        adRequest = new AdRequest.Builder()
                .addTestDevice("33BE2250B43518CCDA7DE426D04EE232").build();

        saveLogs = new SaveLogs(this);
        mAdView.loadAd(adRequest);
        app = b.getString("app");
        try {
            main = b.getString("main");
            abc = main.equals("true");
        } catch (Exception e) {

        }

        random_no = getRandomNumberInRange(1, 3);

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
        } else if (random_no == 3) {
            startAppAd.loadAd();
            startAppAd.showAd();
        } else {
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
        }

        pin_enter = (EditText) findViewById(R.id.pin_enter);
        appIconIv = (ImageView) findViewById(R.id.appIconPIN);
        appNameTv = (TextView) findViewById(R.id.appNameTv);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        zero = (Button) findViewById(R.id.zero);
        delete = (Button) findViewById(R.id.delete);
        done = (Button) findViewById(R.id.done);

        appIconIv.setImageDrawable(getAppIcon(app));
        appNameTv.setText(getAppName(app));

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                pin_enter.setText(a + "1");
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                pin_enter.setText(a + "2");
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                pin_enter.setText(a + "3");
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                pin_enter.setText(a + "4");
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                pin_enter.setText(a + "5");
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                pin_enter.setText(a + "6");
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                pin_enter.setText(a + "7");
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                pin_enter.setText(a + "8");
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                pin_enter.setText(a + "9");
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                pin_enter.setText(a + "0");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();
                String[] b = a.split("");
                String c = a.replace(b[b.length - 1], "");
                pin_enter.setText(c);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = pin_enter.getText().toString();

                if (PINManager.getPIN() != null) {
                    if (abc) {
                        if (PINManager.getPIN().equals(a)) {
                            saveLogs.saveLogs(getString(R.string.app_name), true);
                            startMain();
                        }
                    } else {
                        if (PINManager.getPIN().equals(a)) {
                            saveState.saveState("false");
                            saveLogs.saveLogs(app, true);
                            startApp(app);
                            finish();
                        } else {
                            Toast.makeText(EnterNormalPIN.this, getString(R.string.wrong_pin), Toast.LENGTH_LONG).show();
                            saveLogs.saveLogs(app, false);
                        }
                    }
                } else {
                    Toast.makeText(EnterNormalPIN.this, getString(R.string.pin_not_exist), Toast.LENGTH_LONG).show();
                    saveLogs.saveLogs(app, false);
                }
            }
        });
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
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

        } else if (random_no == 3) {
            startAppAd.onResume();
        } else {
            startAppAd.onResume();
        }

        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
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
        if (abc) {
            startMain();
        } else {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packagename);
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
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

    private void startMain() {
        Intent i = new Intent(EnterNormalPIN.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

