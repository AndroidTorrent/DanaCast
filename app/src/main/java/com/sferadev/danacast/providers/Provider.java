package com.sferadev.danacast.providers;

import android.content.Context;

import com.sferadev.danacast.model.EntryModel;
import com.sferadev.danacast.utils.ContentUtils;

import java.util.ArrayList;

public class Provider {
    public static final int PROVIDER_SERIESBLANCO = 0;
    public static final int PROVIDER_PORDEDE = 1;

    public static ArrayList<EntryModel> getProviders() {
        ArrayList<EntryModel> items = new ArrayList<>();
        items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, "SeriesBlanco", null, null));
        items.add(new EntryModel(ContentUtils.TYPE_PROVIDER, "Pordede", null, null));
        return items;
    }

    public static ArrayList<EntryModel> getSearchResults(Context context, int provider, String query) {
        switch (provider) {
            case PROVIDER_SERIESBLANCO:
                return Seriesblanco.getSearchResults(query);
            case PROVIDER_PORDEDE:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return getProviders();
                }
                return Pordede.getSearchResults(context, query);
        }
        return getProviders();
    }

    public static ArrayList<EntryModel> getPopularContent(Context context, int provider) {
        switch (provider) {
            case PROVIDER_SERIESBLANCO:
                return Seriesblanco.getPopularContent();
            case PROVIDER_PORDEDE:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return getProviders();
                }
                return Pordede.getPopularContent(context);
        }
        return getProviders();
    }

    public static ArrayList<EntryModel> getEpisodeList(Context context, int provider, String url) {
        switch (provider) {
            case PROVIDER_SERIESBLANCO:
                return Seriesblanco.getEpisodeList(url);
            case PROVIDER_PORDEDE:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return getProviders();
                }
                return Pordede.getEpisodeList(context, url);
        }
        return getProviders();
    }

    public static ArrayList<EntryModel> getEpisodeLinks(Context context, int provider, String url) {
        switch (provider) {
            case PROVIDER_SERIESBLANCO:
                return Seriesblanco.getEpisodeLinks(url);
            case PROVIDER_PORDEDE:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return getProviders();
                }
                return Pordede.getEpisodeLinks(context, url);
        }
        return getProviders();
    }

    public static ArrayList<EntryModel> getMovieLinks(Context context, int provider, String url) {
        switch (provider) {
            case PROVIDER_PORDEDE:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return getProviders();
                }
                return Pordede.getMovieLinks(context, url);
        }
        return getProviders();
    }

    public static String getExternalLink(Context context, int provider, String url) {
        switch (provider) {
            case PROVIDER_SERIESBLANCO:
                return Seriesblanco.getExternalLink(url);
            case PROVIDER_PORDEDE:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return null;
                }
                return Pordede.getExternalLink(context, url);
        }
        return null;
    }
}
