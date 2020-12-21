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

        val extras = Bundle()
        extras.putInt("taskID", Activity.selectedItemPosition)
        extras.putInt("difficulty", Difficulty.progress)

        startActivity(Intent(this, TaskContainer::class.java).putExtras(extras))
        finish()

        /*when (Activity.selectedItem.toString()) {
            "Activity 1" -> {startActivity(Intent(this, TaskContainer::class.java).putExtra("taskID", 0))} // NOTE (linus): how do send fragment in intent? :/
            "Activity 2" -> {startActivity(Intent(this, TaskContainer::class.java).putExtra("taskID", 1))}
            "Activity 3" -> {startActivity(Intent(this, TaskContainer::class.java).putExtra("taskID", 2))}
            "Activity 4" -> {startActivity(Intent(this, TaskContainer::class.java).putExtra("taskID", 3))}
            "Activity 5" -> {startActivity(Intent(this, TaskContainer::class.java).putExtra("taskID", 4))}
        }*/
    }

    fun ShowHistory(view: View) {
        startActivity(Intent(this, HistoryPageActivity::class.java))
    }
}