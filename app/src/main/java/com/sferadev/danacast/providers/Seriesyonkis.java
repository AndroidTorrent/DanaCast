package com.sferadev.danacast.providers;

import com.sferadev.danacast.helpers.Constants;
import com.sferadev.danacast.models.EntryModel;

import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Seriesyonkis {
    public static ArrayList<EntryModel> getSearchResults(final String query) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection.Response response = Jsoup.connect("http://www.seriesyonkis.sx/buscar/serie")
                            .data("keyword", query)
                            .data("search_type", "serie")
                            .method(Connection.Method.POST)
                            .execute();
                    Elements elements = response.parse().getElementsByClass("results").first()
                            .getElementsByTag("aside");
                    for (Element element : elements) {
                        String title = element.getElementsByTag("a").first().text();
                        String url = element.getElementsByTag("a").first().attr("abs:href");
                        result.add(new EntryModel(Constants.TYPE_SHOW, title, url));
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
                    Document document = Jsoup.connect("http://www.seriesyonkis.sx/").get();
                    Elements elements = document.getElementsByClass("sidebar-home-box").first()
                            .getElementsByTag("ul").first().getElementsByTag("a");
                    for (Element element : elements) {
                        String title = element.attr("title");
                        String showUrl = element.attr("abs:href");
                        result.add(new EntryModel(Constants.TYPE_SHOW, title, showUrl));
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
                    Elements elements = document.getElementsByClass("menu").first().getElementsByTag("a");
                    for (Element element : elements) {
                        if (element.hasText()) {
                            String title = element.text();
                            String episodeUrl = element.attr("abs:href");
                            result.add(new EntryModel(Constants.TYPE_EPISODE, title, episodeUrl));
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
                    Elements elements = document.getElementsByClass("layout-profile").first()
                            .getElementsByClass("episodes").first().getElementsByTag("tbody")
                            .first().getElementsByTag("tr");
                    for (Element element : elements) {
                        String[] tempTitle = element.getElementsByTag("a").first().attr("title").split("\\s+");
                        String title = WordUtils.capitalize(tempTitle[tempTitle.length - 1].replaceAll("\\.\\w+", ""));
                        String linkUrl = element.getElementsByTag("a").first().attr("abs:href");
                        String language = element.getElementsByClass("flags").first().attr("title");
                        result.add(new EntryModel(Constants.TYPE_LINK, title + " (" + language + ")", linkUrl));
                    }
                } catch (NullPointerException | IOException e) {
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
                    Element element = document.getElementsByClass("down").first();
                    result[0] = element.getElementsByTag("a").first().attr("href");
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
