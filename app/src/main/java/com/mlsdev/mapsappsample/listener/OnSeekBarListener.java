package com.mlsdev.mapsappsample.listener;

import android.widget.SeekBar;

import androidx.databinding.ObservableField;

public class OnSeekBarListener implements SeekBar.OnSeekBarChangeListener {
    private final ObservableField<String> progress;

    public OnSeekBarListener(ObservableField<String> progress) {
        this.progress = progress;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        progress.set(String.valueOf(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
