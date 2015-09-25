package com.sferadev.danacast.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sferadev.danacast.models.EntryModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreferenceUtils {
    public static final String PROPERTY_PORDEDE_USER = "pordedeUser";
    public static final String PROPERTY_PORDEDE_PASS = "pordedePass";
    public static final String PROPERTY_LAST_PROVIDER = "lastProvider";
    public static final String PROPERTY_HISTORY = "historyArray";

    public static String getPreference(Context context, String key, String defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    public static void setPreference(Context context, String preference, String value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(preference, value);
        editor.apply();
    }

    public static int getPreference(Context context, String key, int defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    public static void setPreference(Context context, String preference, int value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(preference, value);
        editor.apply();
    }

    public static void removePreference(Context context, String preference) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(preference);
        editor.apply();
    }

    public static String entryToJSON(ArrayList<EntryModel> arrayList) {
        return new Gson().toJson(arrayList);
    }

    public static ArrayList<EntryModel> entryFromJSON(String json, boolean reverse) {
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<EntryModel>>() {
        }.getType();
        ArrayList<EntryModel> result = new Gson().fromJson(json, type);
        if (reverse) Collections.reverse(result);
        return result;
    }
}