package com.example.lanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class LandingPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
    }

    fun MainPageView(view: View) {
        startActivity(Intent(this, MainPageActivity::class.java))
    }

    fun Logout(view: View) {
        finish()
    }
}