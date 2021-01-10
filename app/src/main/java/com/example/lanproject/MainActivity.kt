package com.example.lanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.net.PasswordAuthentication

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButtonLogin.setOnClickListener { view ->
            MainPageView(view)
        }
        /*PasswordUserpswd.setOnFocusChangeListener{ view, hasfocus ->
            if (hasfocus)
                PasswordTextlayout.setError(null)
        }*/

        PlainTextUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                UsernameField.setError(null)
            }
        })

        PasswordUserpswd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                PasswordField.setError(null)
            }
        })
    }

    fun MainPageView(view: View) {
        val Username = findViewById<EditText>(R.id.PlainTextUsername)
        val Password = findViewById<EditText>(R.id.PasswordUserpswd)

        if (Username.text.toString() == "" || Password.text.toString() == "") {

            if (Username.text.toString() == ""){
                UsernameField.error = getString(R.string.ReqFieldError)
            }
            if( Password.text.toString() == ""){
                PasswordField.error = getString(R.string.ReqFieldError)
            }
        }

        else
            startActivity(Intent(this, MainPageActivity::class.java))
    }

    fun RegisterPageview(view: View) {

        startActivity(Intent(this, RegisterPageActivity::class.java))
    }
}