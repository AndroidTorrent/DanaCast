package com.sferadev.danacast.helpers;

import com.sferadev.danacast.App;
import com.sferadev.danacast.R;
import com.sferadev.danacast.models.EntryModel;
import com.sferadev.danacast.utils.ContentUtils;

import java.util.ArrayList;

public class Category {
    public static ArrayList<EntryModel> getCategories() {
        String[] categoryNames = App.getContext().getResources().getStringArray(R.array.category_names);
        ArrayList<EntryModel> items = new ArrayList<>();
        items.add(new EntryModel(Constants.TYPE_CATEGORY, categoryNames[Constants.CATEGORY_SHOWS], null));
        items.add(new EntryModel(Constants.TYPE_CATEGORY, categoryNames[Constants.CATEGORY_MOVIES], null));
        items.add(new EntryModel(Constants.TYPE_CATEGORY, categoryNames[Constants.CATEGORY_ANIME], null));
        items.add(new EntryModel(Constants.TYPE_CATEGORY, categoryNames[Constants.CATEGORY_MUSIC], null));
        items.add(new EntryModel(Constants.TYPE_CATEGORY, categoryNames[Constants.CATEGORY_FILES], null));
        items.add(new EntryModel(Constants.TYPE_CATEGORY, categoryNames[Constants.CATEGORY_HISTORY], null));
        items.add(new EntryModel(Constants.TYPE_CATEGORY, categoryNames[Constants.CATEGORY_ABOUT], null));
        return items;
    }

    public static ArrayList<EntryModel> getProviders(int category) {
        String[] providerNames = App.getContext().getResources().getStringArray(R.array.provider_names);
        String[] aboutNames = App.getContext().getResources().getStringArray(R.array.about_names);
        String[] aboutLinks = App.getContext().getResources().getStringArray(R.array.about_links);
        ArrayList<EntryModel> items = new ArrayList<>();
        switch (category) {
            case Constants.CATEGORY_SHOWS:
                items.add(new EntryModel(Constants.TYPE_PROVIDER, Constants.PROVIDER_SERIESYONKIS, providerNames[Constants.PROVIDER_SERIESYONKIS], null));
                items.add(new EntryModel(Constants.TYPE_PROVIDER, Constants.PROVIDER_WATCHSERIES, providerNames[Constants.PROVIDER_WATCHSERIES], null));
                items.add(new EntryModel(Constants.TYPE_PROVIDER, Constants.PROVIDER_PORDEDE_SHOWS, providerNames[Constants.PROVIDER_PORDEDE_SHOWS], null));
                break;
            case Constants.CATEGORY_MOVIES:
                items.add(new EntryModel(Constants.TYPE_PROVIDER, Constants.PROVIDER_GNULA, providerNames[Constants.PROVIDER_GNULA], null));
                items.add(new EntryModel(Constants.TYPE_PROVIDER, Constants.PROVIDER_ZPELICULAS, providerNames[Constants.PROVIDER_ZPELICULAS], null));
                items.add(new EntryModel(Constants.TYPE_PROVIDER, Constants.PROVIDER_PORDEDE_MOVIES, providerNames[Constants.PROVIDER_PORDEDE_MOVIES], null));
                break;
            case Constants.CATEGORY_ANIME:
                items.add(new EntryModel(Constants.TYPE_PROVIDER, Constants.PROVIDER_JKANIME, providerNames[Constants.PROVIDER_JKANIME], null));
                break;
            case Constants.CATEGORY_MUSIC:
                items.add(new EntryModel(Constants.TYPE_PROVIDER, Constants.PROVIDER_MUSIC163, providerNames[Constants.PROVIDER_MUSIC163], null));
                items.add(new EntryModel(Constants.TYPE_PROVIDER, Constants.PROVIDER_SOUNDCLOUD, providerNames[Constants.PROVIDER_SOUNDCLOUD], null));
                break;
            case Constants.CATEGORY_FILES:
                return ContentUtils.listPartitions();
            case Constants.CATEGORY_HISTORY:
                return History.getHistory();
            case Constants.CATEGORY_ABOUT:
                items.add(new EntryModel(Constants.TYPE_EXTERNAL, aboutNames[Constants.ABOUT_GOOGLE_PLUS], aboutLinks[Constants.ABOUT_GOOGLE_PLUS]));
                items.add(new EntryModel(Constants.TYPE_EXTERNAL, aboutNames[Constants.ABOUT_WEBSITE], aboutLinks[Constants.ABOUT_WEBSITE]));
                break;
        }
        return items;
    }
}
