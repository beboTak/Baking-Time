package com.example.android.clamps.bakingtime;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.android.clamps.bakingtime.model.Step;
import com.example.android.clamps.bakingtime.viewModel.DetailFragmentViewModel;
import com.example.android.clamps.bakingtime.viewModel.DetailsActivityViewModel;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class StepTData extends Fragment {
    public static final String STEP_URI =  "step_uri";
    public static final String STEP_VIDEO_POSITION =  "step_video_position";
    public static final String STEP_PLAY_WHEN_READY =  "step_play_when_ready";
    public static final String STEP_PLAY_WINDOW_INDEX =  "step_play_window_index";
    public static final String STEP_SINGLE =  "step_single";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private DetailFragmentViewModel viewModel;
    private SimpleExoPlayer player;
    private ArrayList<Step> steps;
    @BindView(R.id.play_video)
    PlayerView playerView;
    @BindView(R.id.des_card)
    CardView cardView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.tv_step_title)
    TextView step_title;
    @BindView(R.id.showing)
    ImageView showing;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.previous)
    Button previous;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.step_details_fragment,container,false);
        ButterKnife.bind(this,view);
        viewModel= ViewModelProviders.of(this).get(DetailFragmentViewModel.class);
        if (viewModel.getStep()!=null){}
        else{
            Bundle bundle=getArguments();
            if (bundle!=null)
            {
                steps=bundle.getParcelableArrayList("data");
                viewModel.setStepPosition(bundle.getInt("pos",0));
                viewModel.setStep(steps.get(viewModel.getStepPosition()));
                viewModel.setPlayWhenReady(true);
            }
        }
        setPortraitOrLandscapeConfigurations();

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
        if(player != null){
            player.setPlayWhenReady(viewModel.isPlayWhenReady());
            player.seekTo(viewModel.getPlayBackPosition());
        }
    }

    @Override
    public void onPause() {

        super.onPause();
        if (player!=null)
        {
            updateStartPosition();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(player != null){
            updateStartPosition();
            if (Util.SDK_INT > 23) {
                releasePlayer();
            }
        }

    }
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStartPosition();
        outState.putString(STEP_URI, viewModel.getStep().getVideoURL());
        outState.putParcelable(STEP_SINGLE, viewModel.getStep());
        outState.putLong(STEP_VIDEO_POSITION, viewModel.getPlayBackPosition());
        outState.putBoolean(STEP_PLAY_WHEN_READY, viewModel.isPlayWhenReady());

    }

    private void initializePlayer() {
        if(viewModel.getStep() != null) {
            String des;
            des=viewModel.getStep().getDescription();
            description.setText(des);
            step_title.setText(viewModel.getStep().getShortDescription());

            TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
            playerView.setPlayer(player);

            MediaSource mediaSource = buildMediaSource();

            if( mediaSource == null ) {
                playerView.setVisibility(View.INVISIBLE);
                showing.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
                playerView.setUseArtwork(true);
                playerView.setUseController(false);

            } else {

                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
                player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                playerView.setVisibility(View.VISIBLE);
                showing.setVisibility(View.INVISIBLE);
                player.prepare(mediaSource, true, false);
                playerView.setUseArtwork(true);
                player.setPlayWhenReady(viewModel.isPlayWhenReady());
                player.seekTo(viewModel.getWindowIndex(),viewModel.getPlayBackPosition());
            }

        }
        else {


        }
    }

    private void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            //player.removeListener(componentListener);
            //player.removeVideoDebugListener(componentListener);
            //player.removeAudioDebugListener(componentListener);
            player.stop();
            player.release();
            player = null;
        }
    }
    private void setPortraitOrLandscapeConfigurations()
    {

            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
                if (viewModel.getStep().getVideoURL().equals("")){
                   playerView.setVisibility(View.GONE);
                   showing.setVisibility(View.VISIBLE);
                }
                android.view.ViewGroup.LayoutParams params=playerView.getLayoutParams();
                cardView.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                previous.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                step_title.setVisibility(View.GONE);
                params.width= ViewGroup.LayoutParams.MATCH_PARENT;
                params.height=ViewGroup.LayoutParams.MATCH_PARENT;
                playerView.setLayoutParams(params);

            }
            else {
                previous.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
            }



    }

    private MediaSource buildMediaSource() {

        if (viewModel.getStep().getVideoURL().equals("")) {
            final MediaSource mediaSource = null;
            return mediaSource;
        }

        Uri uri = Uri.parse(viewModel.getStep().getVideoURL());

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(Util.getUserAgent(getActivity(), getResources().getString(R.string.app_name)))).
                createMediaSource(uri);


    }
    private void updateStartPosition() {
        if (player != null) {
            viewModel.setPlayWhenReady(player.getPlayWhenReady());
            viewModel.setWindowIndex(player.getCurrentWindowIndex());
            viewModel.setPlayBackPosition(player.getCurrentPosition());
        }
    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    private class ComponentListener extends Player.DefaultEventListener implements
            VideoRendererEventListener, AudioRendererEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case Player.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case Player.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case Player.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);
        }

        // Implementing VideoRendererEventListener.

        @Override
        public void onVideoEnabled(DecoderCounters counters) {
            // Do nothing.
        }

        @Override
        public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
            // Do nothing.
        }

        @Override
        public void onVideoInputFormatChanged(Format format) {
            // Do nothing.
        }

        @Override
        public void onDroppedFrames(int count, long elapsedMs) {
            // Do nothing.
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            // Do nothing.
        }

        @Override
        public void onRenderedFirstFrame(Surface surface) {
            // Do nothing.
        }

        @Override
        public void onVideoDisabled(DecoderCounters counters) {
            // Do nothing.
        }

        // Implementing AudioRendererEventListener.

        @Override
        public void onAudioEnabled(DecoderCounters counters) {
            // Do nothing.
        }

        @Override
        public void onAudioSessionId(int audioSessionId) {
            // Do nothing.
        }

        @Override
        public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
            // Do nothing.
        }

        @Override
        public void onAudioInputFormatChanged(Format format) {
            // Do nothing.
        }

        @Override
        public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
            // Do nothing.
        }

        @Override
        public void onAudioDisabled(DecoderCounters counters) {
            // Do nothing.
        }

    }
}
