package com.sferadev.danacast.providers;

import android.util.Base64;

import com.sferadev.danacast.helpers.Constants;
import com.sferadev.danacast.models.EntryModel;
import com.sferadev.danacast.utils.NetworkUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Music163 {
    public static ArrayList<EntryModel> getSearchResults(final String query) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://music.163.com/api/search/get");
                    httpPost.addHeader("Cookie", "appver=2.6.1");
                    httpPost.addHeader("Referer", "http://music.163.com");

                    List<NameValuePair> querys = new ArrayList<NameValuePair>();
                    querys.add(new BasicNameValuePair("s", query));
                    querys.add(new BasicNameValuePair("type", "1"));  // type 1 means searching songs
                    querys.add(new BasicNameValuePair("offset", "0"));
                    querys.add(new BasicNameValuePair("sub", "false"));
                    querys.add(new BasicNameValuePair("limit", "20"));
                    httpPost.setEntity(new UrlEncodedFormEntity(querys, HTTP.UTF_8));

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    JSONObject response = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    JSONArray tracks = response.getJSONObject("result").getJSONArray("songs");
                    for (int i = 0; i < 20; i++) {
                        JSONObject song = tracks.getJSONObject(i);
                        result.add(new EntryModel(Constants.TYPE_SONG, song.getString("name"), "music163=" +
                                String.valueOf(song.getInt("id"))));
                    }
                } catch (IOException | JSONException e) {
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
                            "http://music.163.com/api/playlist/detail?id=60198"));
                    JSONArray tracks = response.getJSONObject("result").getJSONArray("tracks");
                    for (int i = 0; i < 20; i++) {
                        JSONObject song = tracks.getJSONObject(i);
                        result.add(new EntryModel(Constants.TYPE_SONG, song.getString("name"), "music163=" +
                                String.valueOf(song.getInt("id"))));
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

    public static String getExternalLink(final String url) {
        final String id = url.replace("music163=", "");
        final String[] result = new String[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    List<NameValuePair> querys = new ArrayList<NameValuePair>();
                    querys.add(new BasicNameValuePair("ids", "[" + id + "]"));
                    HttpGet httpGet = new HttpGet("http://music.163.com/api/song/detail" + "?" + URLEncodedUtils.format(querys, "utf-8"));
                    httpGet.addHeader("Cookie", "appver=2.6.1");
                    httpGet.addHeader("Referer", "http://music.163.com");

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                        /* try "hMusic" than "mMusic" than "lMusic" than "bMusic" */
                        String[] audio_qualities = {"hMusic", "mMusic", "lMusic", "bMusic"};
                        for (String quality : audio_qualities) {
                            /* pick the best quality we found and save it in a new temporary array */
                            JSONObject jsonTmp = new JSONObject(jsonObject.getJSONArray("songs").getJSONObject(0).getString(quality));
                            if (jsonTmp != null) {
                                /* Search for dfsId */
                                final String dfsid = jsonTmp.getString("dfsId");
                                /* Decrypt dfsId */
                                final String encrypted_dfsid = encrypt_dfsId(dfsid);
                                /* Build the final URL */
                                result[0] = String.format("http://m1.music.126.net/%s/%s.mp3", encrypted_dfsid, dfsid);
                            }
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
            return result[0];
        } catch (InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String encrypt_dfsId(String dfsid) {
        String result = "";
        byte[] byte1 = "3go8&$8*3*3h0k(2)2".getBytes();
        byte[] byte2 = dfsid.getBytes();
        final int byte1_len = byte1.length;
        for (int i = 0; i < byte2.length; i++) {
            byte2[i] = (byte) (byte2[i] ^ byte1[i % byte1_len]);
        }
        byte[] md5bytes = new byte[0];
        try {
            md5bytes = MessageDigest.getInstance("MD5").digest(byte2);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        final String b64 = Base64.encodeToString(md5bytes, Base64.DEFAULT);
        /* cleanup */
        result = b64.replace("/", "_");
        result = result.replace("+", "-");
        /* remove linebreak */
        result = result.replace("\n", "").replace("\r", "");
        return result;
    }
}
