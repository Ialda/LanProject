package com.example.lanproject

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.SeekBar
import kotlinx.coroutines.delay

class TaskListeningFragment : Fragment(R.layout.fragment_task_listening) {
    var mediaPlayer: MediaPlayer? = null
    //var playButton: Button? = null;
    //var pauseButton: Button? = null;
    var playPauseButton: ImageButton? = null;
    //var stopButton: Button? = null;
    var seekBar: SeekBar? = null;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(context, R.raw.testsound)
        //playButton = getView()?.findViewById(R.id.PlaySound)
        //playButton?.visibility=View.INVISIBLE
        //pauseButton = getView()?.findViewById(R.id.PauseSound)
        playPauseButton = getView()?.findViewById(R.id.PlayPauseSound)
        //playPauseButton?.setBackgroundColor(Color.rgb(36,188,42)) // For Testing
        //stopButton = getView()?.findViewById(R.id.StopSound)
        seekBar = getView()?.findViewById(R.id.seekBar)
        //pauseButton?.visibility = View.INVISIBLE
        //playButton?.setOnClickListener{
        //    PlaySound()
        //}
        //pauseButton?.setOnClickListener {
        //    PauseSound()
        //}
        // Grön: app:backgroundTint="#24BC2A"
        // Röd: app:backgroundTint="#E12614"
        // ic_baseline_play_arrow_24 tint: android:tint="?attr/colorControlNormal">
        playPauseButton?.setOnClickListener {
            if(mediaPlayer?.isPlaying == true) {
                PauseSound()
                playPauseButton?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                //playPauseButton?.setBackgroundColor(Color.rgb(36,188,42))
                //playPauseButton?.setBackgroundColor(Color.GREEN)
                //background?.colorFilter(Color.GREEN)
                //setColorFilter(Color.GREEN)
                //playPauseButton?.setBackgroundColor(getResources().getColor(R.color.teal_700))
                //playPauseButton?.setColorFilter());
                //playPauseButton?.setColorFilter(Color.GREEN)
                //playPauseButton?.setColorFilter(resources.getColor(R.color.purple_500))
                //playButton?.setCompoundDrawables(context.(android.R.drawable.ic_media_pause))
                //android.R.drawable.ic_media_pause
            }
            else {
                PlaySound()
                playPauseButton?.setImageResource(R.drawable.ic_baseline_pause_24)
                //playPauseButton?.setBackgroundColor(getResources().getColor(R.color.purple_500))
                //playPauseButton?.setBackgroundColor(Color.BLUE)
            }
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        initSeekbar()

        //stopButton?.setOnClickListener{
            //PauseSound()
        //    ResetSound()
        //}
    }

    fun PlaySound() {
        mediaPlayer?.start()
        //playButton?.visibility = View.INVISIBLE
        //pauseButton?.visibility = View.VISIBLE
    }

    fun PauseSound() {
        mediaPlayer?.pause()
        //pauseButton?.visibility = View.INVISIBLE
        //playButton?.visibility = View.VISIBLE
    }

    fun ResetSound() {

        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(context, R.raw.testsound)
        //pauseButton?.visibility = View.INVISIBLE
        //playButton?.visibility = View.VISIBLE
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