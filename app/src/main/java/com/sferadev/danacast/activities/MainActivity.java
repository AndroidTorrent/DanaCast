package com.sferadev.danacast.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.sv244.torrentstream.StreamStatus;
import com.github.sv244.torrentstream.Torrent;
import com.github.sv244.torrentstream.TorrentOptions;
import com.github.sv244.torrentstream.TorrentStream;
import com.github.sv244.torrentstream.listeners.TorrentListener;
import com.google.android.libraries.cast.companionlibrary.cast.BaseCastManager;
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager;
import com.google.android.libraries.cast.companionlibrary.widgets.MiniController;
import com.sferadev.danacast.App;
import com.sferadev.danacast.R;
import com.sferadev.danacast.helpers.Category;
import com.sferadev.danacast.helpers.Constants;
import com.sferadev.danacast.helpers.History;
import com.sferadev.danacast.helpers.Provider;
import com.sferadev.danacast.models.EntryModel;
import com.sferadev.danacast.utils.ContentUtils;
import com.sferadev.danacast.utils.NetworkUtils;
import com.sferadev.danacast.utils.PreferenceUtils;
import com.sferadev.danacast.utils.UpdateUtils;
import com.sferadev.danacast.utils.WebServer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, TorrentListener {

    private VideoCastManager mCastManager;
    private MiniController mMini;
    private SwipeRefreshLayout mRefresh;
    private SearchView searchView;

    private ArrayList<ArrayList<EntryModel>> mContent = new ArrayList<>();
    private ArrayAdapter mAdapter;
    private String LAST_CONTENT;

    public static ArrayList<EntryModel> mHistory = new ArrayList<>();

    private TorrentStream mTorrentStream;
    private ProgressDialog torrentProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseCastManager.checkGooglePlayServices(this);
        setContentView(R.layout.activity_main);

        mCastManager = VideoCastManager.getInstance();
        mCastManager.reconnectSessionIfPossible();
        mMini = (MiniController) findViewById(R.id.miniController);
        mCastManager.addMiniController(mMini);
        mCastManager.setCastControllerImmersive(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        mRefresh.setColorSchemeResources(R.color.colorAccent);
        mRefresh.setOnRefreshListener(this);

        ListView mListView = (ListView) findViewById(R.id.listview);

        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                EntryModel entry = mContent.get(mContent.size() - 1).get(position);
                if (entry.getLink() != null) {
                    ContentUtils.addToClipboard(MainActivity.this, entry.getLink());
                }
                return false;
            }
        });

        handleIntent(getIntent());

        List<String> items = new ArrayList<>();
        for (EntryModel object : mContent.get(mContent.size() - 1))
            items.add(object.getTitle());
        mAdapter = new ArrayAdapter<>(
                this,
                R.layout.list_row,
                R.id.drawer_text,
                items);
        mListView.setAdapter(mAdapter);

        TorrentOptions torrentOptions = new TorrentOptions();
        ContentUtils.mFilesPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + File.separator + "DanaCast" + File.separator;
        torrentOptions.setSaveLocation(ContentUtils.mFilesPath + "TorrentCache");
        torrentOptions.setRemoveFilesAfterStop(true);

        mTorrentStream = TorrentStream.init(torrentOptions);
        mTorrentStream.addListener(this);

        UpdateUtils.checkUpdates(this);

        ContentUtils.removeLocalFiles("TorrentCache");

        mHistory = History.entryFromJSON(PreferenceUtils.getPreference(App.getContext(), PreferenceUtils.PROPERTY_HISTORY, null), false);
    }

    @Override
    public void onResume() {
        mCastManager = VideoCastManager.getInstance();
        if (null != mCastManager) {
            mCastManager.incrementUiCounter();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        mCastManager.decrementUiCounter();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (null != mCastManager) {
            mMini.removeOnMiniControllerChangedListener(mCastManager);
            mCastManager.removeMiniController(mMini);
        }
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onRefresh() {
        // Handle the refresh of mRefresh
        mRefresh.setRefreshing(true);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(false);
                //mContent.add(Category.getCategories());
                updateListview();
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mCastManager.addMediaRouterButton(menu, R.id.media_route_menu_item);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = null;
        if (searchItem != null) searchView = (SearchView) searchItem.getActionView();
        if (searchView != null)
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                mHistory = new ArrayList<>();
                PreferenceUtils.removePreference(MainActivity.this, PreferenceUtils.PROPERTY_HISTORY);
                mContent.add(History.historyCategories());
                updateListview();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EntryModel entry = mContent.get(mContent.size() - 1).get(position);
        switch (entry.getType()) {
            case Constants.TYPE_CATEGORY:
                mContent.add(Category.getProviders(position));
                updateListview();
                break;
            case Constants.TYPE_HISTORY:
                mContent.add(History.getHistory(position));
                updateListview();
                break;
            case Constants.TYPE_PROVIDER:
                PreferenceUtils.setPreference(this, PreferenceUtils.PROPERTY_LAST_PROVIDER,
                        mContent.get(mContent.size() - 1).get(position).getId());
                mContent.add(Provider.getPopularContent(this, getProvider()));
                updateListview();
                break;
            case Constants.TYPE_SHOW:
                mHistory.add(entry);
                ArrayList<EntryModel> episodes = Provider.getEpisodeList(this, getProvider(), entry.getLink());
                if (!episodes.isEmpty()) {
                    LAST_CONTENT = entry.getTitle();
                    mContent.add(episodes);
                    updateListview();
                }
                break;
            case Constants.TYPE_EPISODE:
                mHistory.add(entry);
                ArrayList<EntryModel> episodeLinks = Provider.getEpisodeLinks(this, getProvider(), entry.getLink());
                if (LAST_CONTENT != null && LAST_CONTENT.contains("|")) LAST_CONTENT =
                        LAST_CONTENT.substring(0, LAST_CONTENT.lastIndexOf("|") - 1);
                if (!episodeLinks.isEmpty()) {
                    LAST_CONTENT = LAST_CONTENT + " | " + entry.getTitle();
                    mContent.add(episodeLinks);
                    updateListview();
                }
                break;
            case Constants.TYPE_MOVIE:
                mHistory.add(entry);
                ArrayList<EntryModel> movieLinks = Provider.getMovieLinks(this, getProvider(), entry.getLink());
                if (!movieLinks.isEmpty()) {
                    LAST_CONTENT = entry.getTitle();
                    mContent.add(movieLinks);
                    updateListview();
                }
                break;
            case Constants.TYPE_LIVE:
                entry.setLink("http://crossorigin.me/" + entry.getLink());
            case Constants.TYPE_SONG:
                LAST_CONTENT = entry.getTitle();
            case Constants.TYPE_LINK:
                entry.setTitle(entry.getTitle() + ": " + LAST_CONTENT.replace("| ", ""));
                ContentUtils.loadIntentDialog(this, entry.getTitle(),
                        entry, Provider.getExternalLink(this, getProvider(),
                                entry.getLink()));
                break;
            case Constants.TYPE_EXTERNAL:
                NetworkUtils.openChromeTab(this, entry.getLink());
                break;
            case Constants.TYPE_TORRENT:
                if (mTorrentStream.isStreaming()) mTorrentStream.stopStream();
                mTorrentStream.startStream(entry.getLink());
                break;
            case Constants.TYPE_FILE:
                if (mCastManager.isConnected()) {
                    try {
                        Log.d("Dana", entry.getLink());
                        new WebServer(entry.getLink()).start();
                        ContentUtils.loadFileChromecast(this, Constants.TYPE_FILE,
                                entry.getLink(), "http://" +
                                        NetworkUtils.getIPAddress(this) + ":8080");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ContentUtils.loadFileExternal(this, Constants.TYPE_FILE, Uri.fromFile(new File(entry.getLink())));
                }
                break;
            case Constants.TYPE_FOLDER:
                mContent.add(ContentUtils.listFiles(entry.getLink()));
                updateListview();
                break;
        }
        PreferenceUtils.setPreference(this, PreferenceUtils.PROPERTY_HISTORY, History.entryToJSON(mHistory));
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            mContent.trimToSize();
            if (mContent.size() > 1) {
                mContent.remove(mContent.size() - 1);
                updateListview();
            } else finish();
        }
    }

    @SuppressWarnings("unchecked")
    private void updateListview() {
        List<String> items = new ArrayList<>();
        for (EntryModel object : mContent.get(mContent.size() - 1)) items.add(object.getTitle());
        mAdapter.clear();
        mAdapter.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mContent.add(Provider.getSearchResults(this, getProvider(), query));
            updateListview();
        } else {
            mContent.add(Category.getCategories());
        }
    }

    private int getProvider() {
        return PreferenceUtils.getPreference(this, PreferenceUtils.PROPERTY_LAST_PROVIDER, 0);
    }


    @Override
    public void onStreamPrepared(Torrent torrent) {
        Log.d("Dana", "Torrent: onStreamPrepared");
        torrentProgressDialog = new ProgressDialog(this);
        torrentProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        torrentProgressDialog.setMessage("Loading. Please wait...");
        torrentProgressDialog.setCanceledOnTouchOutside(false);
        torrentProgressDialog.show();
        torrent.startDownload();
    }

    @Override
    public void onStreamStarted(Torrent torrent) {
        Log.d("Dana", "Torrent: onStreamStarted");
    }

    @Override
    public void onStreamError(Torrent torrent, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStreamReady(Torrent torrent) {
        torrentProgressDialog.cancel();
        ContentUtils.loadOptionsDialog(this, torrent);
    }

    @Override
    public void onStreamProgress(Torrent torrent, StreamStatus streamStatus) {
        if (streamStatus.bufferProgress <= 100 && torrentProgressDialog.getProgress() < 100
                && torrentProgressDialog.getProgress() != streamStatus.bufferProgress) {
            torrentProgressDialog.setProgress(streamStatus.bufferProgress);
        }
    }

    @Override
    public void onStreamStopped() {
        Log.d("Dana", "Torrent: onStreamStopped");
    }
}
