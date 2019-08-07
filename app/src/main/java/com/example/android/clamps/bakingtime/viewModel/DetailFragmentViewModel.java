package com.example.android.clamps.bakingtime.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.android.clamps.bakingtime.model.Step;

public class DetailFragmentViewModel extends AndroidViewModel {
    private Step step;
    private int stepPosition;

    private int windowIndex = 0;
    private long playBackPosition = 0;
    private boolean playWhenReady = true;
    private boolean isLandscape = false;

    public DetailFragmentViewModel(@NonNull Application application) {
        super(application);
    }
    public void init() {
        windowIndex = 0;
        playBackPosition = 0;
        playWhenReady = false;

    }


    public boolean isLandscape() {
        return isLandscape;
    }

    public void setLandscape(boolean landscape) {
        isLandscape = landscape;
    }


    public boolean isPlayWhenReady() {
        return playWhenReady;
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.playWhenReady = playWhenReady;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public int getStepPosition() {
        return stepPosition;
    }

    public void setStepPosition(int stepPosition) {
        this.stepPosition = stepPosition;
    }

    public int getWindowIndex() {
        return windowIndex;
    }

    public void setWindowIndex(int windowIndex) {
        this.windowIndex = windowIndex;
    }

    public long getPlayBackPosition() {
        return playBackPosition;
    }

    public void setPlayBackPosition(long playBackPosition) {
        this.playBackPosition = playBackPosition;
    }
}
