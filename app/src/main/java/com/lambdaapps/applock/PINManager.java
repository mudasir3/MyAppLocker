package com.lambdaapps.applock;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.security.GeneralSecurityException;

public class PINManager {
    public static String AB = "qwEwfbfs";

    public static void setPIN(String PIN, Context context) {
        try {
            String encryptedMsg = AESCrypt.encrypt(AB, PIN);
            FileOutputStream fileOutputStream = context.openFileOutput("pin", Context.MODE_PRIVATE);
            fileOutputStream.write(encryptedMsg.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(context, context.getString(R.string.pin_setted), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
    }

    public static String getPIN() {
        File file = new File("/data/data/com.lambdaapps.applock/files/pin");
        StringBuilder text = null;
        if (file.exists()) {
            text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close();
            } catch (Exception e) {
                Log.d("okuma hatası", "no 1");
            }
        }

        String messageAfterDecrypt = null;
        if (text != null) {
            try {
                messageAfterDecrypt = AESCrypt.decrypt(AB, text.toString());
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
        return messageAfterDecrypt;
    }
}
