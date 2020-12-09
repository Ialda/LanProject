package com.example.lanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Spinner

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
    }

    fun ActivityView(view: View) {
        val Difficulty = findViewById<SeekBar>(R.id.SeekBarDifficulty)
        val Activity = findViewById<Spinner>(R.id.SpinnerActivityChoice)

        when (Activity.selectedItem.toString()) {
            "Activity 1" -> {startActivity(Intent(this, Portal1Activity::class.java))}
            "Activity 2" -> {startActivity(Intent(this, Portal2Activity::class.java))}
            "Activity 3" -> {startActivity(Intent(this, Portal3Activity::class.java))}
            "Activity 4" -> {startActivity(Intent(this, Portal4Activity::class.java))}
            "Activity 5" -> {startActivity(Intent(this, Portal5Activity::class.java))}
        }
    }
}