package com.mobymagic.bakingapp.ui.steps;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobymagic.bakingapp.R;
import com.mobymagic.bakingapp.data.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.Toast;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mobymagic.bakingapp.databinding.FragmentStepsBinding;

public class StepsFragment extends Fragment implements ExoPlayer.EventListener {

    // region Constants
    private static final String ARG_STEP = "ARG_STEP";
    // endregion

    // region Member Variables
    private FragmentStepsBinding binding;
    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    // endregion

    // region Factory Method
    public static StepsFragment newInstance(Step step) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_STEP, step);
        StepsFragment fragment = new StepsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    // endregion

    // region Lifecycle Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if ((arguments != null) && (arguments.containsKey(ARG_STEP))) {
            mStep = arguments.getParcelable(ARG_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mStep.getVideoURL()!=null && !mStep.getVideoURL().matches("")) {
            initializeMediaSession();
            initializePlayer(Uri.parse(mStep.getVideoURL()));

            // If we are in landscape orientation and we are on a phone, go fullscreen
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                    !getResources().getBoolean(R.bool.is_tablet)) {
                hideSystemUI();
                binding.stepDescriptionTv.setVisibility(View.GONE);
                binding.exoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                binding.exoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                binding.stepDescriptionTv.setText(mStep.getDescription());
            }
        }
        else {
            binding.exoPlayerView.setVisibility(View.GONE);
            binding.stepDescriptionTv.setText(mStep.getDescription());
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();

        if(mMediaSession!=null) {
            mMediaSession.setActive(false);
            mMediaSession = null;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if(!isVisibleToUser) {
                if (mExoPlayer != null) {
                    //Pause video when user swipes away in view pager
                    mExoPlayer.setPlayWhenReady(false);
                }
            }
        }
    }
    // endregion

    private void hideSystemUI() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null) actionBar.hide();
        
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), StepsFragment.class.getSimpleName());
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new VideoSessionCallback());
        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            binding.exoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), "RecipeStepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {}

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

    @Override
    public void onLoadingChanged(boolean isLoading) {}

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
        }
        
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        error.printStackTrace();
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPositionDiscontinuity() {}

    private class VideoSessionCallback extends MediaSessionCompat.Callback {
        
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
        
    }

}