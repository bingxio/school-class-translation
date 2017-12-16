package com.meniao.classweb.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ViewDebug;

import javax.crypto.spec.DESedeKeySpec;

/**
 * Created by Meniao Company on 2017/9/10.
 */

public class SharedPreferenceUtils {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferenceUtils(Context context, String filename) {
        sharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defauleValue) {
        return sharedPreferences.getString(key, defauleValue);
    }

    public void deleteAll() {
        editor.clear();
        editor.commit();
    }

    public void deleteOnKey(String key) {
        editor.remove(key);
        editor.commit();
    }
}
