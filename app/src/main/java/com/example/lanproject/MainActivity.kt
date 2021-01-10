package com.example.lanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.widget.EditText
import androidx.core.text.parseAsHtml
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.PasswordField
import kotlinx.android.synthetic.main.activity_main.UsernameField
import kotlinx.android.synthetic.main.activity_register_page.*
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
            CheckUser()
            //startActivity(Intent(this, MainPageActivity::class.java))
    }

    fun RegisterPageview(view: View) {

        startActivity(Intent(this, RegisterPageActivity::class.java))
    }

    fun CheckUser(){
        val queue = Volley.newRequestQueue(this)
        val serverURL = "https://lwm.sh/~lanproject/auth.php"
        val Username = findViewById<EditText>(R.id.PlainTextUsername)
        val Password = findViewById<EditText>(R.id.PasswordUserpswd)

        val stringRequest = object: StringRequest(
            Method.GET, serverURL,
            { response ->
                startActivity(Intent(this, MainPageActivity::class.java))
            },
            { error ->
                UsernameField.error = getString(R.string.IncorrectCredentials)
                PasswordField.error = getString(R.string.IncorrectCredentials)
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val user = Username?.text
                val pass = Password?.text
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Basic ${
                    Base64.encodeToString("$user:$pass".toByteArray(),
                        Base64.DEFAULT)}"
                return headers
            }
        }

        queue.add(stringRequest)
    }

}