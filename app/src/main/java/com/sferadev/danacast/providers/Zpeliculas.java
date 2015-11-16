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

public class Zpeliculas {
    public static ArrayList<EntryModel> getSearchResults(final String query) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.zpeliculas.com/index.php?story={" +
                            query.replace(" ", "+") + "}&do=search&subaction=search").get();
                    Elements elements = document.getElementsByClass("shortmovies");
                    for (Element element : elements) {
                        String title = element.getElementsByTag("h2").first().attr("title");
                        String showUrl = element.getElementsByTag("a").first().attr("abs:href");
                        result.add(new EntryModel(Constants.TYPE_MOVIE, title, showUrl, null));
                    }
                } catch (IOException | NullPointerException e) {
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
                    Document document = Jsoup.connect("http://www.zpeliculas.com/").get();
                    Elements elements = document.getElementsByClass("shortmovies");
                    for (Element element : elements) {
                        String title = element.getElementsByTag("h2").first().attr("title");
                        String showUrl = element.getElementsByTag("a").first().attr("abs:href");
                        result.add(new EntryModel(Constants.TYPE_MOVIE, title, showUrl, null));
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

    public static ArrayList<EntryModel> getMovieLinks(final String url) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements elements = document.getElementsByTag("tbody").first().getElementsByTag("tr");
                    for (Element element : elements) {
                        String linkUrl = element.getElementsByTag("a").first().attr("abs:href");
                        String title = WordUtils.capitalize(linkUrl.replace("http://", "").split("\\.")[0]);
                        String language = element.getElementsByTag("div").first().attr("title");
                        result.add(new EntryModel(Constants.TYPE_LINK, title + " (" + language + ")", linkUrl, null));
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
}
