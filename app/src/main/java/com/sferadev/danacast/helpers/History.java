package com.sferadev.danacast.helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sferadev.danacast.App;
import com.sferadev.danacast.models.EntryModel;
import com.sferadev.danacast.utils.PreferenceUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class History {

    public static ArrayList<EntryModel> getHistory() {
        return entryFromJSON(PreferenceUtils.getPreference(App.getContext(),
                PreferenceUtils.PROPERTY_HISTORY, null), true);
    }

    public static String entryToJSON(ArrayList<EntryModel> arrayList) {
        return new Gson().toJson(arrayList);
    }

    public static ArrayList<EntryModel> entryFromJSON(String json, boolean reverse) {
        ArrayList<EntryModel> result = new ArrayList<>();
        if (json == null) return result;
        Type type = new TypeToken<List<EntryModel>>() {}.getType();
        ArrayList<EntryModel> parseResult = new Gson().fromJson(json, type);
        if (reverse) Collections.reverse(parseResult);
        for (int i = 0; i < parseResult.size(); ++i) {
            result.add(parseResult.get(i));
        }
        return result;
    }
}
