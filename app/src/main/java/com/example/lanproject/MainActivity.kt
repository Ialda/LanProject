package com.example.lanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.net.PasswordAuthentication

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun MainPageView(view: View) {
        val Username = findViewById<EditText>(R.id.PlainTextUsername)
        val Password = findViewById<EditText>(R.id.PasswordUserpswd)

        if (Username.text.toString() == "User" && Password.text.toString() == "Login") {
            startActivity(Intent(this, MainPageActivity::class.java))
        }
    }
}