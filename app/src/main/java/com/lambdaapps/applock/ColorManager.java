package com.lambdaapps.applock;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ColorManager {
    Context context;
    ColorManager(Context context1){
        context = context1;
    }
    public String getColor(){
        File file = new File("/data/data/com.lambdaapps.applock/files/color");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        }
        catch (IOException e) {

        }
        return text.toString();
    }
    public boolean isDark(){
        return getColor().equals(Statics.DARK);
    }
    public boolean isLight(){
        return getColor().equals(Statics.LIGHT);
    }
    public void changeColor(String color){
        try {
            FileOutputStream fOut = context.openFileOutput("color",  context.MODE_PRIVATE);
            fOut.write(color.getBytes());
            fOut.flush();
            fOut.close();
        }
        catch (Exception e) {
            Log.e("Hata kodu3", "File write failed: " + e.toString());
        }
    }
    public void resetColor(){
        try {
            String a ="";
            FileOutputStream fOut = context.openFileOutput("color",  context.MODE_PRIVATE);
            fOut.write(a.getBytes());
            fOut.flush();
            fOut.close();
        }
        catch (Exception e) {
            Log.e("Hata kodu3", "File write failed: " + e.toString());
        }
    }
}
