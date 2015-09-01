package com.sferadev.danacast.model;

import com.sferadev.danacast.utils.ContentUtils;

import java.util.ArrayList;

import static com.sferadev.danacast.model.Provider.PROVIDER_GNULA;
import static com.sferadev.danacast.model.Provider.PROVIDER_JKANIME;
import static com.sferadev.danacast.model.Provider.PROVIDER_MUSIC163;
import static com.sferadev.danacast.model.Provider.PROVIDER_PORDEDE_MOVIES;
import static com.sferadev.danacast.model.Provider.PROVIDER_PORDEDE_SHOWS;
import static com.sferadev.danacast.model.Provider.PROVIDER_SERIESBLANCO;
import static com.sferadev.danacast.model.Provider.PROVIDER_SERIESYONKIS;
import static com.sferadev.danacast.model.Provider.PROVIDER_WATCHSERIES;
import static com.sferadev.danacast.model.Provider.PROVIDER_ZPELICULAS;
import static com.sferadev.danacast.model.Provider.providerNames;

public class Category {
    public static final int CATEGORY_SHOWS = 0;
    public static final int CATEGORY_MOVIES = 1;
    public static final int CATEGORY_ANIME = 2;
    public static final int CATEGORY_MUSIC = 3;
    public static final int CATEGORY_ABOUT = 4;

    public static final int ABOUT_GOOGLE_PLUS = 0;
    public static final int ABOUT_PAYPAL = 1;
    public static final int ABOUT_WEBSITE = 2;

    public static String[] categoryNames = new String[]{
            "TV Series",
            "Movies",
            "Anime",
            "Music",
            "About"
    };

    public static String[] aboutNames = new String[]{
            "Google Plus Community",
            "PayPal donations",
            "DanaCast website"
    };

    public static String[] aboutLinks = new String[]{
            "https://plus.google.com/u/0/communities/117989078772127331921",
            "https://paypal.me/sfera",
            "http://danacast.me"
    };

    public static ArrayList<EntryModel> getCategories() {
        ArrayList<EntryModel> items = new ArrayList<>();
        items.add(new EntryModel(ContentUtils.TYPE_CATEGORY, categoryNames[CATEGORY_SHOWS], null, null));
        items.add(new EntryModel(ContentUtils.TYPE_CATEGORY, categoryNames[CATEGORY_MOVIES], null, null));
        items.add(new EntryModel(ContentUtils.TYPE_CATEGORY, categoryNames[CATEGORY_ANIME], null, null));
        items.add(new EntryModel(ContentUtils.TYPE_CATEGORY, categoryNames[CATEGORY_MUSIC], null, null));
        items.add(new EntryModel(ContentUtils.TYPE_CATEGORY, categoryNames[CATEGORY_ABOUT], null, null));
        return items;
    }

    public static ArrayList<EntryModel> getProviders(int category) {
        ArrayList<EntryModel> items = new ArrayList<>();
        switch (category) {
            case CATEGORY_SHOWS:
                items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, PROVIDER_SERIESBLANCO, providerNames[PROVIDER_SERIESBLANCO], null, null));
                items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, PROVIDER_SERIESYONKIS, providerNames[PROVIDER_SERIESYONKIS], null, null));
                items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, PROVIDER_WATCHSERIES, providerNames[PROVIDER_WATCHSERIES], null, null));
                items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, PROVIDER_PORDEDE_SHOWS, providerNames[PROVIDER_PORDEDE_SHOWS], null, null));
                break;
            case CATEGORY_MOVIES:
                items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, PROVIDER_GNULA, providerNames[PROVIDER_GNULA], null, null));
                items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, PROVIDER_ZPELICULAS, providerNames[PROVIDER_ZPELICULAS], null, null));
                items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, PROVIDER_PORDEDE_MOVIES, providerNames[PROVIDER_PORDEDE_MOVIES], null, null));
                break;
            case CATEGORY_ANIME:
                items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, PROVIDER_JKANIME, providerNames[PROVIDER_JKANIME], null, null));
                break;
            case CATEGORY_MUSIC:
                items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, PROVIDER_MUSIC163, providerNames[PROVIDER_MUSIC163], null, null));
                break;
            case CATEGORY_ABOUT:
                items.add(new EntryModel(ContentUtils.TYPE_EXTERNAL, aboutNames[ABOUT_GOOGLE_PLUS], aboutLinks[ABOUT_GOOGLE_PLUS], null));
                items.add(new EntryModel(ContentUtils.TYPE_EXTERNAL, aboutNames[ABOUT_PAYPAL], aboutLinks[ABOUT_PAYPAL], null));
                items.add(new EntryModel(ContentUtils.TYPE_EXTERNAL, aboutNames[ABOUT_WEBSITE], aboutLinks[ABOUT_WEBSITE], null));
                break;
        }
        return items;
    }
}
