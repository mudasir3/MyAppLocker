package com.lambdaapps.applock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.List;


public class ManageLockedApps {
    //TODO change package
    public static void resetLockedApps(Context context) {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        PackageManager packageManager = context.getPackageManager();
        preferences = context.getSharedPreferences("chosen_apps", context.MODE_PRIVATE);
        editor = preferences.edit();
        final List<ApplicationInfo> appsList = context.getPackageManager().getInstalledApplications(0);
        for (int i = 0; i < appsList.toArray().length; i++) {
            if (!appsList.get(i).packageName.equals("com.lambdaapps.applock")) {
                if (!appsList.get(i).packageName.contains("launcher3")) {
                    if (!appsList.get(i).packageName.contains("launcher")) {
                        if (!appsList.get(i).packageName.contains("trebuchet")) {
                            if (null != packageManager.getLaunchIntentForPackage(appsList.get(i).packageName)) {
                                editor.putBoolean(appsList.get(i).packageName, false).apply();
                            }
                        }
                    }
                }
            }
        }
        Toast.makeText(context, context.getString(R.string.all_apps_unlocked), Toast.LENGTH_LONG).show();
    }

    public static void lockAllApps(Context context) {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        PackageManager packageManager = context.getPackageManager();
        preferences = context.getSharedPreferences("chosen_apps", context.MODE_PRIVATE);
        editor = preferences.edit();
        final List<ApplicationInfo> appsList = context.getPackageManager().getInstalledApplications(0);
        for (int i = 0; i < appsList.toArray().length; i++) {
            //TODO change package
            if (!appsList.get(i).packageName.equals("com.lambdaapps.applock")) {
                if (!appsList.get(i).packageName.contains("launcher3")) {
                    if (!appsList.get(i).packageName.contains("launcher")) {
                        if (!appsList.get(i).packageName.contains("trebuchet")) {
                            if (null != packageManager.getLaunchIntentForPackage(appsList.get(i).packageName)) {
                                editor.putBoolean(appsList.get(i).packageName, true).apply();
                            }
                        }
                    }
                }
            }
        }
        Toast.makeText(context, context.getString(R.string.all_apps_locked), Toast.LENGTH_LONG).show();
    }
}
