package com.sferadev.danacast.helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sferadev.danacast.App;
import com.sferadev.danacast.R;
import com.sferadev.danacast.models.EntryModel;
import com.sferadev.danacast.utils.PreferenceUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class History {
    public static ArrayList<EntryModel> historyCategories() {
        String[] historyNames = App.getContext().getResources().getStringArray(R.array.history_names);
        ArrayList<EntryModel> items = new ArrayList<>();
        items.add(new EntryModel(Constants.TYPE_HISTORY, historyNames[Constants.HISTORY_TV_SHOWS], null, null));
        items.add(new EntryModel(Constants.TYPE_HISTORY, historyNames[Constants.HISTORY_EPISODES], null, null));
        items.add(new EntryModel(Constants.TYPE_HISTORY, historyNames[Constants.HISTORY_MOVIES], null, null));
        items.add(new EntryModel(Constants.TYPE_HISTORY, historyNames[Constants.HISTORY_LINKS], null, null));
        return items;
    }

    public static ArrayList<EntryModel> getHistory(int historyCategory) {
        int type = 0;
        switch (historyCategory) {
            case Constants.HISTORY_TV_SHOWS:
                type = Constants.TYPE_SHOW;
                break;
            case Constants.HISTORY_EPISODES:
                type = Constants.TYPE_EPISODE;
                break;
            case Constants.HISTORY_MOVIES:
                type = Constants.TYPE_MOVIE;
                break;
            case Constants.HISTORY_LINKS:
                type = Constants.TYPE_LINK;
                break;
        }
        return entryFromJSON(PreferenceUtils.getPreference(App.getContext(), PreferenceUtils.PROPERTY_HISTORY, null), true, type);
    }

    public static String entryToJSON(ArrayList<EntryModel> arrayList) {
        return new Gson().toJson(arrayList);
    }

    public static ArrayList<EntryModel> entryFromJSON(String json, boolean reverse) {
        return entryFromJSON(json, reverse, -1);
    }

    public static ArrayList<EntryModel> entryFromJSON(String json, boolean reverse, int contentType) {
        ArrayList<EntryModel> result = new ArrayList<>();
        if (json == null) return result;
        Type type = new TypeToken<List<EntryModel>>() {}.getType();
        ArrayList<EntryModel> parseResult = new Gson().fromJson(json, type);
        if (reverse) Collections.reverse(parseResult);
        for (int i = 0; i < parseResult.size(); ++i) {
            if (parseResult.get(i).getType() == contentType || contentType == -1) result.add(parseResult.get(i));
        }
        return result;
    }
}
