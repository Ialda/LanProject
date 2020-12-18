package com.example.lanproject

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import kotlinx.coroutines.delay

class TaskListeningFragment : Fragment(R.layout.fragment_task_listening) {
    var mediaPlayer: MediaPlayer? = null
    var playPauseButton: ImageButton? = null;
    var seekBar: SeekBar? = null;
    var mediaTime: TextView? = null;
    var timeSek:Int = 0;
    var timeMin:Int = 0;
    var timeLengthSek:Int = 0;
    var timeLengthMin:Int = 0;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(context, R.raw.testsound)
        playPauseButton = getView()?.findViewById(R.id.PlayPauseSound)
        seekBar = getView()?.findViewById(R.id.seekBar)
        mediaTime = getView()?.findViewById(R.id.mediaTime)

        playPauseButton?.setOnClickListener {
            if(mediaPlayer?.isPlaying == true) {
                PauseSound()
                playPauseButton?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
            else {
                PlaySound()
                playPauseButton?.setImageResource(R.drawable.ic_baseline_pause_24)
            }
        }

        var rawTimeLength = mediaPlayer?.duration!!

        timeLengthSek = (rawTimeLength % 60000)/1000
        timeLengthMin = rawTimeLength/60000

        mediaTime?.text = ("00:00 / " + timeLengthMin.toString() + ":" + timeLengthSek.toString())

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer?.seekTo(progress)

                timeSek = (progress % 60000)/1000
                timeMin = progress/60000


                mediaTime?.text = (timeMin.toString() + ":" + timeSek.toString() + " / " +
                        timeLengthMin.toString() + ":" + timeLengthSek.toString())


                //totalTime?.text = (timeLengthMin.toString() + ":" + timeLengthSek.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        initSeekbar()

    }

    fun PlaySound() {
        mediaPlayer?.start()
    }

    fun PauseSound() {
        mediaPlayer?.pause()
    }

    private fun initSeekbar() {
        seekBar?.max = mediaPlayer!!.duration
        val handler = Handler()
        handler.postDelayed(object: Runnable {
            override fun run(){
                try {
                    seekBar?.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                }
                catch (e: Exception){
                    seekBar?.progress = 0
                }
            }
        }, 0)
    }

}