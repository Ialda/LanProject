package com.example.lanproject

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel

class TaskListeningViewModel : ViewModel(){
    var mediaPlayer: MediaPlayer? = null
    init{
        /*if (mediaPlayer == null)
        {
            //val audio:String =
            mediaPlayer = MediaPlayer.create(context, R.raw.testsound)
            //mediaPlayer = MediaPlayer.create(context, testData.)
        }*/
    }
    fun PlaySound() {
        mediaPlayer?.start()
    }

    fun PauseSound() {
        mediaPlayer?.pause()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
    }
}