package com.sferadev.danacast.servers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Vidgg {
    public static String getVideoPath(final String url) {
        final String[] result = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String id = url.substring(url.lastIndexOf("/") + 1);
                    Document doc = Jsoup.connect("http://www.vidgg.to/mobile/video.php?id=" + id)
                            .timeout(3000)
                            .get();
                    result[0] = doc.select("source[type=video/mp4]").attr("src");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
            return result[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
