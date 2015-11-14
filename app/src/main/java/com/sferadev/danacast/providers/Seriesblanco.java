package com.sferadev.danacast.providers;

import com.sferadev.danacast.helpers.Constants;
import com.sferadev.danacast.models.EntryModel;

import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Seriesblanco {
    public static ArrayList<EntryModel> getSearchResults(final String query) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://seriesblanco.com/search.php" +
                            "?q1=" + query.replace(" ", "+")).get();
                    Elements elements = document.getElementsByClass("post-header").first()
                            .getElementsByTag("a");
                    for (Element element : elements) {
                        if (element.hasText()) {
                            String title = element.text();
                            String url = element.attr("abs:href");
                            result.add(new EntryModel(Constants.TYPE_SHOW, title, url, null));
                        }
                    }
                } catch (IOException e) {
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

    public static ArrayList<EntryModel> getPopularContent() {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.seriesblanco.com/").get();
                    Elements elements = document.select("#PopularPosts1").get(2)
                            .getElementsByTag("li");
                    for (Element element : elements) {
                        String title = element.getElementsByTag("img").first().attr("title");
                        String showUrl = element.getElementsByTag("a").first().attr("abs:href");
                        String pic = element.getElementsByTag("img").first().attr("src");
                        result.add(new EntryModel(Constants.TYPE_SHOW, title, showUrl, pic));
                    }
                } catch (IOException e) {
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

    public static ArrayList<EntryModel> getEpisodeList(final String url) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements tables = document.getElementsByClass("zebra");
                    for (Element table : tables) {
                        Elements elements = table.getElementsByTag("tbody").first()
                                .getElementsByTag("a");
                        for (Element element : elements) {
                            if (element.hasText()) {
                                String title = element.text();
                                String episodeUrl = element.attr("abs:href");
                                result.add(new EntryModel(Constants.TYPE_EPISODE, title, episodeUrl, null));
                            }
                        }
                    }
                } catch (IOException e) {
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

    public static ArrayList<EntryModel> getEpisodeLinks(final String url) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements elements = document.getElementsByClass("zebra").first()
                            .getElementsByTag("tr");
                    for (Element element : elements) {
                        if (!element.getElementsByTag("a").isEmpty()) {
                            String title = WordUtils.capitalize(element.getElementsByTag("img").get(1)
                                    .attr("src").replace("/servidores/", "").split("\\.")[0]);
                            String linkUrl = element.getElementsByTag("a").first().attr("abs:href");
                            String language = element.getElementsByTag("img").get(0)
                                    .attr("src").replace("/banderas/", "").split("\\.")[0].toUpperCase();
                            result.add(new EntryModel(Constants.TYPE_LINK, title + " (" + language + ")", linkUrl, null));
                        }
                    }
                } catch (IOException e) {
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

    public static String getExternalLink(final String url) {
        final String[] result = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Element element = document.select("input[type=button]").first();
                    result[0] = element.attr("onclick").split("\"")[1];
                } catch (IOException e) {
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
            return null;
        }
    }
}
