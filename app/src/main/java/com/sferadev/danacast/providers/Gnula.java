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

public class Gnula {
    public static ArrayList<EntryModel> getSearchResults(final String query) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("https://www.google.com/search?q=site%3Agnula.nu+" +
                            query.replace(" ", "+")).get();
                    Elements elements = document.getElementsByClass("srg").first().getElementsByTag("h3");
                    for (Element element : elements) {
                        Element link = element.getElementsByTag("a").first();
                        String title = link.text().replace("Ver ", "").split("online \\|")[0];
                        String url = link.attr("abs:href");
                        if (link.text().contains("Ver"))
                            result.add(new EntryModel(Constants.TYPE_MOVIE, title, url, null));
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
                    Document document = Jsoup.connect("http://gnula.nu/").get();
                    Elements elements = document.getElementsByClass("widget-content").get(1)
                            .getElementsByTag("td").first().getElementsByTag("a");
                    for (Element element : elements) {
                        String title = element.getElementsByTag("img").first().attr("title").split("\\[")[0];
                        String showUrl = element.attr("abs:href");
                        String picUrl = element.getElementsByTag("img").first().attr("src");
                        result.add(new EntryModel(Constants.TYPE_MOVIE, title, showUrl, picUrl));
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
                    Elements elements = document.getElementsByClass("contenedor_tab");
                    Elements languages = document.getElementsByTag("em");
                    for (int i = 0; i < (elements.size()); i++) {
                        String language = WordUtils.capitalize(languages.get(i).text().replace(" ", "").split(",")[1]);
                        for (Element element : elements.get(i).getElementsByTag("a")) {
                            String title = WordUtils.capitalize(element.getElementsByTag("span").first().attr("class"));
                            String linkUrl = element.attr("abs:href");
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
}
