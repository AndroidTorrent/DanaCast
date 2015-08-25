package com.sferadev.danacast.utils;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager;
import com.sferadev.danacast.servers.Server;

public class ContentUtils {
    public static final int TYPE_PROVIDER = 0;
    public static final int TYPE_SHOW = 1;
    public static final int TYPE_EPISODE = 2;
    public static final int TYPE_LINK = 3;
    public static final int TYPE_MOVIE = 4;

    static String[] dialogOptions = {"Chromecast", "Download", "Open with..."};

    public static void loadIntentDialog(final Context context, final String url) {
        final String[] finalUrl = new String[1];
        final ProgressDialog dialog = new ProgressDialog(context);
        if (!Server.isSupported(url)) {
            NetworkUtils.openChromeTab(context, url);
            return;
        }
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Loading. Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                finalUrl[0] = Server.getVideoPath(url);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    if (finalUrl[0] == null) {
                        Toast.makeText(context, "The requested link doesn't work", Toast.LENGTH_LONG).show();
                        return;
                    }
                    loadOptionsDialog(context, finalUrl[0]);
                }
            }

        };
        task.execute();
    }

    private static void loadOptionsDialog(final Context context, final String url) {
        AlertDialog dialog = new AlertDialog.Builder(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                .setTitle(url)
                .setItems(dialogOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                loadVideoChromecast(context, url);
                                break;
                            case 1:
                                loadVideoDownload(context, url);
                                break;
                            case 2:
                                loadVideoExternal(context, url);
                                break;
                        }
                    }
                })
                .create();
        dialog.show();
    }

    private static void loadVideoChromecast(Context context, String url) {
        if (!VideoCastManager.getInstance().isConnected()) return;
        MediaMetadata mediaMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_TV_SHOW);
        mediaMetadata.putString(MediaMetadata.KEY_TITLE, url);
        //mediaMetadata.addImage(new WebImage(Uri.parse(uriString)));
        MediaInfo mSelectedMedia = new MediaInfo.Builder(url)
                .setContentType("video/mp4")
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setMetadata(mediaMetadata)
                .build();
        VideoCastManager.getInstance().startVideoCastControllerActivity(context, mSelectedMedia, 0, true);
    }

    private static void loadVideoDownload(Context context, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        downloadManager.enqueue(request);
    }

    private static void loadVideoExternal(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "video/*");
        context.startActivity(intent);
    }
}
