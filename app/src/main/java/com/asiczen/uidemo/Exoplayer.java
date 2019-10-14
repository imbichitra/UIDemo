package com.asiczen.uidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.UUID;

public class Exoplayer extends AppCompatActivity {

    PlayerView playerView,playerView1;
    SimpleExoPlayer player,player1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);

        playerView = findViewById(R.id.playerView);
        playerView1 = findViewById(R.id.playerView1);
    }

    private void initializePlayer() {
        DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
        String drmLicenseUrl = "https://streamtvdivavodwedev.keydelivery.westeurope.media.azure.net/Widevine/?kid=bde8f0b3-1701-4a36-b98e-46f7bb38a71f";
        UUID drmSchemeUuid = Util.getDrmUuid(String.valueOf(C.WIDEVINE_UUID));
        try {
            drmSessionManager =
                    buildDrmSessionManagerV18(
                            drmSchemeUuid, drmLicenseUrl, false);
        } catch (UnsupportedDrmException e) {
            e.printStackTrace();
        }
        if (player == null){

            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl(),
                    drmSessionManager);
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);
            //player.seekTo(currentWindow,playbackPosition);
        }
        String urlS = "https://vod-d-main-streamtv.akamaized.net/vod/c3f7fdda-7cca-4aeb-9144-6bbd8a964162/Music_FluteBarcelona_60fps_1080p.ism/manifest(format=mpd-time-csf)";
        String url = "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Sending+Data+to+a+New+Activity+with+Intent+Extras.mp4";
        Uri uri = Uri.parse(urlS);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource,true,false);
    }
    private void initializePlayer1() {
        DefaultDrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
        String drmLicenseUrl = "https://streamtvdivavodwedev.keydelivery.westeurope.media.azure.net/Widevine/?kid=bde8f0b3-1701-4a36-b98e-46f7bb38a71f";
        UUID drmSchemeUuid = Util.getDrmUuid(String.valueOf(C.WIDEVINE_UUID));
        try {
            drmSessionManager =
                    buildDrmSessionManagerV18(
                            drmSchemeUuid, drmLicenseUrl, false);
        } catch (UnsupportedDrmException e) {
            e.printStackTrace();
        }
        if (player1 == null){

            player1 = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl(),
                    drmSessionManager);
            playerView1.setPlayer(player1);
            player1.setPlayWhenReady(true);
            //player.seekTo(currentWindow,playbackPosition);
        }
        String urlS = "https://vod-d-main-streamtv.akamaized.net/vod/c3f7fdda-7cca-4aeb-9144-6bbd8a964162/Music_FluteBarcelona_60fps_1080p.ism/manifest(format=mpd-time-csf)";
        String url = "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Sending+Data+to+a+New+Activity+with+Intent+Extras.mp4";
        Uri uri = Uri.parse(urlS);
        MediaSource mediaSource = buildMediaSource(uri);
        player1.prepare(mediaSource,true,false);
    }


    private MediaSource buildMediaSource(Uri uri) {

        String userAgent = "exoplayer-codelab";
        try {
            if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {
                DefaultDataSourceFactory dataSourceFactory =
                        new DefaultDataSourceFactory(this,
                                Util.getUserAgent(this,userAgent));
                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            } else if (uri.getLastPathSegment().contains("m3u8")) {

                return new HlsMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                        .createMediaSource(uri);
            } else {
                DashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(
                        new DefaultHttpDataSourceFactory("ua", new DefaultBandwidthMeter()));
                DefaultHttpDataSourceFactory manifestDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);
                return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri);
            }
        }catch (Exception e){
            e.printStackTrace();
            DefaultDataSourceFactory dataSourceFactory =
                    new DefaultDataSourceFactory(this,
                            Util.getUserAgent(this,userAgent));
            return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        }
    }
    private DefaultDrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(
            UUID uuid, String licenseUrl, boolean multiSession)
            throws UnsupportedDrmException {
        HttpDataSource.Factory licenseDataSourceFactory =new DefaultHttpDataSourceFactory(Util.getUserAgent(this,"Demo"));
                //((DemoApplication) getApplication()).buildHttpDataSourceFactory();
        HttpMediaDrmCallback drmCallback =
                new HttpMediaDrmCallback(licenseUrl, licenseDataSourceFactory);
        /*if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }*/
        //releaseMediaDrm();
        FrameworkMediaDrm mediaDrm = FrameworkMediaDrm.newInstance(uuid);
        return new DefaultDrmSessionManager<>(uuid, mediaDrm, drmCallback, null, multiSession);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializePlayer();
        initializePlayer1();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player!=null){
            player.release();
        }
        if (player1!=null){
            player1.release();
        }
    }
}
