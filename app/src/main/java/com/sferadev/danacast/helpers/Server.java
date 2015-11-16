package com.sferadev.danacast.helpers;

import com.sferadev.danacast.servers.Nowvideo;
import com.sferadev.danacast.servers.Streamcloud;
import com.sferadev.danacast.servers.Vidgg;

public class Server {
    public static boolean isSupported(String url) {
        return url.contains("streamcloud") ||
                url.contains("nowvideo") ||
                url.contains("vidgg") ||
                url.contains("jkmedia") ||
                url.contains("music") ||
                url.contains("soundcloud") ||
                url.contains("m3u8") ||
                url.contains("mp3") ||
                url.contains("radio") ||
                url.contains("emisoras") ||
                url.contains("live") ||
                url.contains("stream");
    }

    public static String getVideoPath(String url) {
        try {
            if (url.contains("streamcloud")) {
                return Streamcloud.getVideoPath(url);
            } else if (url.contains("nowvideo")) {
                return Nowvideo.getVideoPath(url);
            } else if (url.contains("vidgg")) {
                return Vidgg.getVideoPath(url);
            } else return url;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
