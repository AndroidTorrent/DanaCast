package com.sferadev.danacast.providers;

import com.sferadev.danacast.helpers.Constants;
import com.sferadev.danacast.models.EntryModel;
import com.sferadev.danacast.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Soundcloud {
    public static final String CLIENT_ID = "bf86e564bfadae5cdce425714c8309e7";

    public static ArrayList<EntryModel> getSearchResults(final String query) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray tracks = new JSONArray(NetworkUtils.getURLOutput(
                            "http://api.soundcloud.com/tracks?client_id=" + CLIENT_ID + "&q=" + query));
                    JSONArray users = new JSONArray(NetworkUtils.getURLOutput(
                            "http://api.soundcloud.com/users?client_id=" + CLIENT_ID + "&q=" + query));
                    JSONArray groups = new JSONArray(NetworkUtils.getURLOutput(
                            "http://api.soundcloud.com/groups?client_id=" + CLIENT_ID + "&q=" + query));
                    for (int i = 0; i < tracks.length(); i++) {
                        JSONObject song = tracks.getJSONObject(i);
                        if (song.getBoolean("streamable"))
                            result.add(new EntryModel(Constants.TYPE_SONG, "Song: " + song.getString("title"),
                                    song.getString("stream_url") + "?client_id=" + CLIENT_ID, null));
                    }
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        result.add(new EntryModel(Constants.TYPE_SHOW, "User: " + user.getString("username"),
                                user.getString("uri"), null));
                    }
                    for (int i = 0; i < groups.length(); i++) {
                        JSONObject group = groups.getJSONObject(i);
                        result.add(new EntryModel(Constants.TYPE_SHOW, "Group: " + group.getString("name"),
                                group.getString("uri"), null));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
            return result;
        } catch (InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<EntryModel> getPopularContent() {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject response = new JSONObject(NetworkUtils.getURLOutput(
                            "http://api.soundcloud.com/playlists/104935439?client_id=" + CLIENT_ID));
                    JSONArray tracks = response.getJSONArray("tracks");
                    for (int i = 0; i < tracks.length(); i++) {
                        JSONObject song = tracks.getJSONObject(i);
                        if (song.getBoolean("streamable"))
                            result.add(new EntryModel(Constants.TYPE_SONG, song.getString("title"),
                                    song.getString("stream_url") + "?client_id=" + CLIENT_ID, null));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
            return result;
        } catch (InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<EntryModel> getEpisodeList(final String url) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray tracks = new JSONArray(NetworkUtils.getURLOutput(url + "/tracks?client_id=" + CLIENT_ID));
                    for (int i = 0; i < tracks.length(); i++) {
                        JSONObject song = tracks.getJSONObject(i);
                        if (song.getBoolean("streamable"))
                            result.add(new EntryModel(Constants.TYPE_SONG, "Song: " + song.getString("title"),
                                    song.getString("stream_url") + "?client_id=" + CLIENT_ID, null));
                    }
                    if (url.contains("user")) {
                        JSONArray playlists = new JSONArray(NetworkUtils.getURLOutput(url + "/playlists?client_id=" + CLIENT_ID))
                                .getJSONObject(0).getJSONArray("tracks");
                        JSONArray favorites = new JSONArray(NetworkUtils.getURLOutput(url + "/favorites?client_id=" + CLIENT_ID));
                        for (int i = 0; i < playlists.length(); i++) {
                            JSONObject song = playlists.getJSONObject(i);
                            if (song.getBoolean("streamable"))
                                result.add(new EntryModel(Constants.TYPE_SONG, "Playlist: " + song.getString("title"),
                                        song.getString("stream_url") + "?client_id=" + CLIENT_ID, null));
                        }
                        for (int i = 0; i < favorites.length(); i++) {
                            JSONObject song = favorites.getJSONObject(i);
                            if (song.getBoolean("streamable"))
                                result.add(new EntryModel(Constants.TYPE_SONG, "Favorite: " + song.getString("title"),
                                        song.getString("stream_url") + "?client_id=" + CLIENT_ID, null));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
            return result;
        } catch (InterruptedException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

}
