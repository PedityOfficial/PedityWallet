package com.example.pwallet.peditywallet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    public void saveStringInSP(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("PEDITY_SP", Activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringFromSP(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PEDITY_SP", Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
