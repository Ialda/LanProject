package com.example.lanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_register_page.*

class RegisterPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        PlainTextRegisterUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                UsernameField.setError(null)
            }
        })

        PasswordRegisterpswd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                PasswordField.setError(null)
            }
        })
        PasswordReRegisterpswd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ConfirmPasswordField.setError(null)
            }
        })

    }

    fun registerPageView(view: View) {

        val Username = findViewById<EditText>(R.id.PlainTextRegisterUsername)
        val Password = findViewById<EditText>(R.id.PasswordRegisterpswd)
        val ConfirmPassword = findViewById<EditText>(R.id.PasswordReRegisterpswd)

        if (Username.text.toString() == "" || Password.text.toString() == "" || ConfirmPassword.text.toString() == "") {

            if (Username.text.toString() == ""){
                UsernameField.error = getString(R.string.ReqFieldError)
            }
            if( Password.text.toString() == ""){
                PasswordField.error = getString(R.string.ReqFieldError)
            }
            if (ConfirmPassword.text.toString() == ""){
                ConfirmPasswordField.error = getString(R.string.ReqFieldError)
            }
        }

        else
        startActivity(Intent(this, MainActivity::class.java))
    }

}