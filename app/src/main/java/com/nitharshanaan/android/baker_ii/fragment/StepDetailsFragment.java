package com.nitharshanaan.android.baker_ii.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nitharshanaan.android.baker_ii.R;
import com.nitharshanaan.android.baker_ii.data.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment {

    private static final String TAG = StepDetailsFragment.class.getSimpleName();
    private static final String POSITION_MILLISECONDS = "position_milliseconds";
    private static final String PLAY_WHEN_READY = "state_playing";
    private static final String STEP_POSITION = "step_position";
    private static MediaSessionCompat mediaSession;
    public int curPosition = -1;
    @BindView(R.id.player_view_step)
    SimpleExoPlayerView playerView;
    @BindView(R.id.text_view_step_description)
    TextView textViewStepDescription;
    @BindView(R.id.imgView_recipe)
    ImageView imgView_recipe;
    @BindView(R.id.button_previous_step)
    Button buttonPreviousStep;
    @BindView(R.id.button_next_step)
    Button buttonNextStep;
    List<Step> steps = null;
    private SimpleExoPlayer exoPlayer;
    private PlaybackStateCompat.Builder stateBuilder;
    private long playbackPosition;
    private boolean playWhenReady = true;

    public StepDetailsFragment() {

    }

    public static StepDetailsFragment newInstance(Bundle bundle) {
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setArguments(bundle);
        return stepDetailsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, view);
        playerView = view.findViewById(R.id.player_view_step);

        buttonNextStep = view.findViewById(R.id.button_next_step);
        //playerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));

        Context context = getContext();
        Bundle bundle = getArguments();


        if (bundle != null) {
            steps = bundle.getParcelableArrayList(getString(R.string.steps));
            changeStep(bundle.getInt(getString(R.string.step_position)));
        }

        buttonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStep(curPosition + 1);
            }
        });
        buttonPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStep(curPosition - 1);
            }
        });


        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        ActionBar actionBar = null;
        if (activity != null)
            actionBar = activity.getSupportActionBar();


        if (!TextUtils.isEmpty(steps.get(curPosition).getVideoUrl())) {

            initializeMediaSession(context);
        }

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && !getResources().getBoolean(R.bool.isTablet)) {
            if (actionBar != null)
                actionBar.hide();

            if (activity != null)
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            imgView_recipe.setVisibility(View.GONE);
            textViewStepDescription.setVisibility(View.GONE);
            buttonPreviousStep.setVisibility(View.GONE);
            buttonNextStep.setVisibility(View.GONE);
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textViewStepDescription.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.player_view_step);
            textViewStepDescription.setLayoutParams(layoutParams);
        }

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(POSITION_MILLISECONDS);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            curPosition = savedInstanceState.getInt(STEP_POSITION);
            changeStep(curPosition);
        }

        return view;
    }

    public void changeStep(int newPosition) {
        if (newPosition < 0)
            newPosition = 0;
        if (newPosition >= steps.size())
            newPosition = steps.size() - 1;

        curPosition = newPosition;

        if (curPosition == 0) {
            buttonNextStep.setVisibility(View.VISIBLE);
            buttonPreviousStep.setVisibility(View.GONE);
        } else if (curPosition == steps.size() - 1) {
            buttonNextStep.setVisibility(View.GONE);
            buttonPreviousStep.setVisibility(View.VISIBLE);
        } else {
            buttonNextStep.setVisibility(View.VISIBLE);
            buttonPreviousStep.setVisibility(View.VISIBLE);
        }

        updateStepData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (exoPlayer != null) {
            outState.putLong(POSITION_MILLISECONDS, exoPlayer.getCurrentPosition());
            outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        }
        outState.putInt(STEP_POSITION, curPosition);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateStepData();
        if (exoPlayer != null && steps.get(curPosition).getVideoUrl() != null) {
            exoPlayer.seekTo(playbackPosition);
            exoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer == null) {
            return;
        }

        playbackPosition = exoPlayer.getCurrentPosition();
        playWhenReady = exoPlayer.getPlayWhenReady();
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void initializeMediaSession(Context context) {
        mediaSession = new MediaSessionCompat(context, TAG);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );

        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE
                );

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setActive(true);

    }

    private void updateStepData() {
        textViewStepDescription.setText(steps.get(curPosition).getDescription());
        if (!TextUtils.isEmpty(steps.get(curPosition).getVideoUrl())) {
            initializePlayer(Uri.parse(steps.get(curPosition).getVideoUrl()), getContext());
            imgView_recipe.setVisibility(View.INVISIBLE);
            playerView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textViewStepDescription.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.player_view_step);
            textViewStepDescription.setLayoutParams(layoutParams);
        } else {
            Log.e(TAG, "Has " + (imgView_recipe == null));
            playerView.setVisibility(View.GONE);
            if (exoPlayer != null)
                exoPlayer.setPlayWhenReady(false);
            imgView_recipe.setVisibility(View.VISIBLE);
            String imageUrl = steps.get(curPosition).getThumbnailUrl();
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.with(getContext())
                        .load(imageUrl)
                        .into(imgView_recipe);
            } else {
                Picasso.with(getContext())
                        .load(R.drawable.bakingprogress)
                        .into(imgView_recipe);
            }

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textViewStepDescription.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.imgView_recipe);
            textViewStepDescription.setLayoutParams(layoutParams);
        }
    }

    private void initializePlayer(Uri uri, Context context) {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "Baking Master");
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        } else {
            String userAgent = Util.getUserAgent(getContext(), "Baking Master");
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
        if (uri.equals(Uri.EMPTY))
            playerView.setVisibility(View.GONE);
        else
            playerView.setVisibility(View.VISIBLE);
    }
}
