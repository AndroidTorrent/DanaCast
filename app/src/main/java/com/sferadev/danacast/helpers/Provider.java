package com.sferadev.danacast.helpers;

import android.content.Context;

import com.sferadev.danacast.models.EntryModel;
import com.sferadev.danacast.providers.Gnula;
import com.sferadev.danacast.providers.Jkanime;
import com.sferadev.danacast.providers.Music163;
import com.sferadev.danacast.providers.Pordede;
import com.sferadev.danacast.providers.Seriesyonkis;
import com.sferadev.danacast.providers.Soundcloud;
import com.sferadev.danacast.providers.Watchseries;
import com.sferadev.danacast.providers.Zpeliculas;

import java.util.ArrayList;

public class Provider {
    public static ArrayList<EntryModel> getSearchResults(Context context, int provider, String query) {
        switch (provider) {
            case Constants.PROVIDER_SERIESYONKIS:
                return Seriesyonkis.getSearchResults(query);
            case Constants.PROVIDER_WATCHSERIES:
                return Watchseries.getSearchResults(query);
            case Constants.PROVIDER_PORDEDE_SHOWS:
            case Constants.PROVIDER_PORDEDE_MOVIES:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return Category.getCategories();
                }
                return Pordede.getSearchResults(context, query);
            case Constants.PROVIDER_GNULA:
                return Gnula.getSearchResults(query);
            case Constants.PROVIDER_ZPELICULAS:
                return Zpeliculas.getSearchResults(query);
            case Constants.PROVIDER_JKANIME:
                return Jkanime.getSearchResults(query);
            case Constants.PROVIDER_MUSIC163:
                return Music163.getSearchResults(query);
            case Constants.PROVIDER_SOUNDCLOUD:
                return Soundcloud.getSearchResults(query);
        }
        return Category.getCategories();
    }

    public static ArrayList<EntryModel> getPopularContent(Context context, int provider) {
        switch (provider) {
            case Constants.PROVIDER_SERIESYONKIS:
                return Seriesyonkis.getPopularContent();
            case Constants.PROVIDER_WATCHSERIES:
                return Watchseries.getPopularContent();
            case Constants.PROVIDER_PORDEDE_SHOWS:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return Category.getCategories();
                }
                return Pordede.getPopularShows(context);
            case Constants.PROVIDER_PORDEDE_MOVIES:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return Category.getCategories();
                }
                return Pordede.getPopularMovies(context);
            case Constants.PROVIDER_GNULA:
                return Gnula.getPopularContent();
            case Constants.PROVIDER_ZPELICULAS:
                return Zpeliculas.getPopularContent();
            case Constants.PROVIDER_JKANIME:
                return Jkanime.getPopularContent();
            case Constants.PROVIDER_MUSIC163:
                return Music163.getPopularContent();
            case Constants.PROVIDER_SOUNDCLOUD:
                return Soundcloud.getPopularContent();
        }
        return Category.getCategories();
    }

    public static ArrayList<EntryModel> getEpisodeList(Context context, int provider, String url) {
        switch (provider) {
            case Constants.PROVIDER_SERIESYONKIS:
                return Seriesyonkis.getEpisodeList(url);
            case Constants.PROVIDER_WATCHSERIES:
                return Watchseries.getEpisodeList(url);
            case Constants.PROVIDER_PORDEDE_SHOWS:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return Category.getCategories();
                }
                return Pordede.getEpisodeList(context, url);
            case Constants.PROVIDER_JKANIME:
                return Jkanime.getEpisodeList(url);
            case Constants.PROVIDER_SOUNDCLOUD:
                return Soundcloud.getEpisodeList(url);
        }
        return Category.getCategories();
    }

    public static ArrayList<EntryModel> getEpisodeLinks(Context context, int provider, String url) {
        switch (provider) {
            case Constants.PROVIDER_SERIESYONKIS:
                return Seriesyonkis.getEpisodeLinks(url);
            case Constants.PROVIDER_WATCHSERIES:
                return Watchseries.getEpisodeLinks(url);
            case Constants.PROVIDER_PORDEDE_SHOWS:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return Category.getCategories();
                }
                return Pordede.getEpisodeLinks(context, url);
            case Constants.PROVIDER_JKANIME:
                return Jkanime.getEpisodeLinks(url);
        }
        return Category.getCategories();
    }

    public static ArrayList<EntryModel> getMovieLinks(Context context, int provider, String url) {
        switch (provider) {
            case Constants.PROVIDER_PORDEDE_MOVIES:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return Category.getCategories();
                }
                return Pordede.getMovieLinks(context, url);
            case Constants.PROVIDER_GNULA:
                return Gnula.getMovieLinks(url);
            case Constants.PROVIDER_ZPELICULAS:
                return Zpeliculas.getMovieLinks(url);
        }
        return Category.getCategories();
    }

    public static String getExternalLink(Context context, String url) {
        switch (detectProvider(url)) {
            case Constants.PROVIDER_SERIESYONKIS:
                return Seriesyonkis.getExternalLink(url);
            case Constants.PROVIDER_WATCHSERIES:
                return Watchseries.getExternalLink(url);
            case Constants.PROVIDER_PORDEDE_SHOWS:
            case Constants.PROVIDER_PORDEDE_MOVIES:
                if (!Pordede.isLoggedInCredentials(context)) {
                    Pordede.loginWithCredentials(context);
                    return null;
                }
                return Pordede.getExternalLink(context, url);
            case Constants.PROVIDER_JKANIME:
                return Jkanime.getExternalLink(url);
            case Constants.PROVIDER_MUSIC163:
                return Music163.getExternalLink(url);
        }
        return url;
    }

    public static int detectProvider(String url) {
        if (url.contains("seriesyonkis")) {
            return Constants.PROVIDER_SERIESYONKIS;
        } else if (url.contains("watchseries")) {
            return Constants.PROVIDER_WATCHSERIES;
        } else if (url.contains("pordede")) {
            return Constants.PROVIDER_PORDEDE_SHOWS;
        } else if (url.contains("jkanime")) {
            return Constants.PROVIDER_JKANIME;
        } else if (url.contains("music163")) {
            return Constants.PROVIDER_MUSIC163;
        } else {
            return -1;
        }
    }
}
