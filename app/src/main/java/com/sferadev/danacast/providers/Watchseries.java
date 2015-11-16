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

public class Watchseries {
    public static ArrayList<EntryModel> getSearchResults(final String query) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://watchseries.ag/search/" + query.replace(" ", "%20"))
                            .get();
                    Elements elements = document.getElementsByClass("lview").first()
                            .getElementsByTag("tr");
                    for (Element element : elements) {
                        Element row = element.getElementsByTag("td").get(1);
                        String title = row.getElementsByTag("a").first().text();
                        String url = row.getElementsByTag("a").first().attr("abs:href");
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
                    Document document = Jsoup.connect("http://watchseries.ag").get();
                    Elements elements = document.getElementsByClass("div-home-inside-left").first()
                            .getElementsByTag("a");
                    for (Element element : elements) {
                        String title = element.text();
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
                    Elements seasonsTitle = document.select("div.tab.selected").first().getElementsByTag("h2");
                    Elements seasons = document.select("div.tab.selected").first().getElementsByTag("ul");
                    for (int i = 0; i < (seasons.size()); i++) {
                        String season = seasonsTitle.get(i).getElementsByTag("a").text();
                        Elements elements = seasons.get(i).getElementsByTag("a");
                        for (Element element : elements) {
                            String title = season + " - " + element.getElementsByTag("span").get(1).text();
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
                    Elements tabs = document.getElementsByClass("tab-lang");
                    for (Element tab : tabs) {
                        String language = tab.getElementsByTag("h2").first().text().split(" ")[0];
                        Elements elements = tab.getElementsByTag("tr");
                        for (Element element : elements) {
                            String title = WordUtils.capitalize(element.getElementsByTag("span").first().text());
                            String linkUrl = element.getElementsByTag("a").first().attr("abs:href");
                            result.add(new EntryModel(Constants.TYPE_LINK, title + " (" + language + ")", linkUrl));
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
                    Element element = document.getElementsByClass("myButton").first();
                    result[0] = element.attr("href");
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
