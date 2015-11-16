package com.sferadev.danacast.providers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sferadev.danacast.R;
import com.sferadev.danacast.activities.MainActivity;
import com.sferadev.danacast.helpers.Constants;
import com.sferadev.danacast.models.EntryModel;
import com.sferadev.danacast.utils.PreferenceUtils;

import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Pordede {
    public static ArrayList<EntryModel> getSearchResults(final Context context, final String query) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.pordede.com/search/" +
                            query.replace(" ", "-"))
                            .cookies(loginResponse(context).cookies())
                            .referrer("http://www.google.com")
                            .get();
                    Elements elements = document.select("div.listContainer.listCovers.clearfix").first()
                            .select("div.ddItemContainer.modelContainer");
                    for (Element element : elements) {
                        String title = element.getElementsByClass("title").text();
                        String url = element.select("a.defaultLink.extended").first().attr("abs:href");
                        boolean isShow = element.getElementsByClass("searchType").text().equals("Serie");
                        result.add(new EntryModel(isShow ? Constants.TYPE_SHOW : Constants.TYPE_MOVIE,
                                title, url));
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

    public static ArrayList<EntryModel> getPopularShows(final Context context) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.pordede.com/series/index/showlist/viewed")
                            .cookies(loginResponse(context).cookies())
                            .referrer("http://www.google.com")
                            .get();
                    Elements elements = document.getElementsByClass("listContainer").first().getElementsByClass("ddItemContainer");
                    for (Element element : elements) {
                        String title = element.getElementsByClass("title").first().text();
                        String showUrl = element.getElementsByClass("extended").first().attr("abs:href");
                        String picUrl = element.getElementsByClass("centeredPicFalse").first().attr("src");
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

    public static ArrayList<EntryModel> getPopularMovies(final Context context) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.pordede.com/pelis/index/showlist/viewed")
                            .cookies(loginResponse(context).cookies())
                            .referrer("http://www.google.com")
                            .get();
                    Elements elements = document.getElementsByClass("listContainer").first().getElementsByClass("ddItemContainer");
                    for (Element element : elements) {
                        String title = element.getElementsByClass("title").first().text();
                        String showUrl = element.getElementsByClass("extended").first().attr("abs:href");
                        String picUrl = element.getElementsByClass("centeredPicFalse").first().attr("src");
                        result.add(new EntryModel(Constants.TYPE_MOVIE, title, showUrl));
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

    public static ArrayList<EntryModel> getEpisodeList(final Context context, final String url) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url)
                            .cookies(loginResponse(context).cookies())
                            .referrer("http://www.google.com")
                            .get();
                    Elements seasons = document.getElementsByClass("episodes");
                    for (Element season : seasons) {
                        Elements episodes = season.select("div.modelContainer.defaultPopup");
                        String seasonName = season.getElementsByClass("checkSeason").first().attr("data-num");
                        for (Element link : episodes) {
                            String title = seasonName + "x" + link.getElementsByClass("title").first().text();
                            String episodeUrl = link.getElementsByClass("title").first().attr("abs:href");
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

    public static ArrayList<EntryModel> getEpisodeLinks(final Context context, final String url) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url)
                            .cookies(loginResponse(context).cookies())
                            .referrer("http://www.google.com")
                            .get();
                    Elements elements = document.select("div.linksContainer.online.tabContent").first()
                            .select("a.a.aporteLink.done");
                    for (Element element : elements) {
                        String title = WordUtils.capitalize(element.getElementsByClass("hostimage").first()
                                .child(0).attr("src").replace("http://www.pordede.com/images/hosts/popup_", "").split("\\.")[0]);
                        String linkUrl = element.attr("abs:href");
                        String language = WordUtils.capitalize(element.getElementsByClass("flags").first()
                                .child(0).classNames().toArray()[1].toString());
                        String quality = element.getElementsByClass("quality").first().text();
                        result.add(new EntryModel(Constants.TYPE_LINK, title +
                                " (" + language + " | " + quality + ")", linkUrl));
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

    public static ArrayList<EntryModel> getMovieLinks(final Context context, final String url) {
        final ArrayList<EntryModel> result = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] splitUrl = url.split("\\/");
                    Document document = Jsoup.connect("http://www.pordede.com/links/view/slug/" +
                            splitUrl[splitUrl.length - 1] + "/what/peli")
                            .cookies(loginResponse(context).cookies())
                            .referrer("http://www.google.com")
                            .get();
                    Elements elements = document.select("div.linksContainer.online.tabContent").first()
                            .select("a.a.aporteLink.done");
                    for (Element element : elements) {
                        String title = WordUtils.capitalize(element.getElementsByClass("hostimage").first()
                                .child(0).attr("src").replace("http://www.pordede.com/images/hosts/popup_", "").split("\\.")[0]);
                        String linkUrl = element.attr("abs:href");
                        String language = WordUtils.capitalize(element.getElementsByClass("flags").first()
                                .child(0).classNames().toArray()[1].toString());
                        String quality = element.getElementsByClass("quality").first().text();
                        result.add(new EntryModel(Constants.TYPE_LINK, title +
                                " (" + language + " | " + quality + ")", linkUrl));
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

    public static String getExternalLink(final Context context, final String url) {
        final String[] result = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url)
                            .cookies(loginResponse(context).cookies())
                            .referrer("http://www.google.com")
                            .get();
                    Element element = document.select("p.links").first().child(0);
                    result[0] = Jsoup.connect(element.attr("abs:href"))
                            .cookies(loginResponse(context).cookies())
                            .followRedirects(true)
                            .execute().url().toExternalForm();
                } catch (IOException | IndexOutOfBoundsException e) {
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

    private static Connection.Response loginResponse(Context context) throws IOException {
        return Jsoup.connect("http://www.pordede.com/site/login")
                .data("LoginForm[username]", PreferenceUtils.getPreference(context,
                        PreferenceUtils.PROPERTY_PORDEDE_USER, null))
                .data("LoginForm[password]", PreferenceUtils.getPreference(context,
                        PreferenceUtils.PROPERTY_PORDEDE_PASS, null))
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .method(Connection.Method.POST).timeout(0).execute();
    }

    public static boolean isLoggedInCredentials(Context context) {
        return PreferenceUtils.getPreference(context,
                PreferenceUtils.PROPERTY_PORDEDE_USER, null) != null;
    }

    public static void loginWithCredentials(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_pordede_login, null);
        builder.setView(view)
                .setPositiveButton(R.string.login_signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText usernameEditText = (EditText) view.findViewById(R.id.username);
                        String username = usernameEditText.getText().toString().trim();
                        EditText passwordEditText = (EditText) view.findViewById(R.id.password);
                        String password = passwordEditText.getText().toString().trim();
                        if (checkUser(username, password)) {
                            PreferenceUtils.setPreference(context, PreferenceUtils.PROPERTY_PORDEDE_USER,
                                    username);
                            PreferenceUtils.setPreference(context, PreferenceUtils.PROPERTY_PORDEDE_PASS,
                                    password);
                            Toast.makeText(context, context.getString(R.string.login_success), Toast.LENGTH_LONG).show();
                            restartApp(context);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, context.getString(R.string.login_failure), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    private static boolean checkUser(final String username, final String password) {
        final boolean[] result = new boolean[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection.Response loginResponse = Jsoup.connect("http://www.pordede.com/site/login")
                            .data("LoginForm[username]", username)
                            .data("LoginForm[password]", password)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .method(Connection.Method.POST).timeout(0).execute();

                    Document document = Jsoup.connect("http://www.pordede.com/series/index/showlist/viewed")
                            .cookies(loginResponse.cookies())
                            .referrer("http://www.google.com")
                            .get();
                    document.getElementsByClass("listContainer").first().getElementsByClass("ddItemContainer");
                    result[0] = true;
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                    result[0] = false;
                }
            }
        });
        thread.start();
        try {
            thread.join();
            return result[0];
        } catch (InterruptedException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }

    }

    private static void restartApp(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        ((Activity) context).finish();
        context.startActivity(intent);
    }
}
