package com.mlsdev.mapsappsample.listener

import android.widget.SeekBar

import androidx.databinding.ObservableField

class OnSeekBarListener(private val progress: ObservableField<String>) : SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        progress.set(seekBar.progress.toString())
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}
