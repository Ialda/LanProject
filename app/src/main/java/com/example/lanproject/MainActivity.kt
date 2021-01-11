package com.example.lanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.parseAsHtml
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.PasswordField
import kotlinx.android.synthetic.main.activity_main.UsernameField
import kotlinx.android.synthetic.main.activity_register_page.*
import org.json.JSONObject
import java.net.PasswordAuthentication

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButtonLogin.setOnClickListener { view ->
            MainPageView(view)
        }

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
    }

    fun RegisterPageview(view: View) {

        startActivity(Intent(this, RegisterPageActivity::class.java))
    }

    fun CheckUser(){
        val queue = Volley.newRequestQueue(this)
        val serverURL = "https://lwm.sh/~lanproject/auth.php"
        val Username = findViewById<EditText>(R.id.PlainTextUsername)
        val Password = findViewById<EditText>(R.id.PasswordUserpswd)

        queue.add(
            object : JsonObjectRequest(Request.Method.GET, serverURL, null,
                Response.Listener<JSONObject> { response ->
                    response?.toString(4)?.let { Log.i("LanProject", it) }
                    if (response.getString("status") == "success") {
                        LanProjectApplication.Username = Username?.text.toString()
                        LanProjectApplication.Password = Password?.text.toString()

                        startActivity(Intent(this, MainPageActivity::class.java))
                    } else {
                        Toast.makeText(this, "Unable to login, please try again later", Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    if (error.toString() == "com.android.volley.AuthFailureError") {
                        //UsernameField.error = error.toString()
                        UsernameField.error = getString(R.string.IncorrectCredentials)
                        PasswordField.error = getString(R.string.IncorrectCredentials)
                    }
                    else if (error.networkResponse == null){
                        UsernameField.error = getString(R.string.ConnectFail)
                        PasswordField.error = getString(R.string.ConnectFail)
                        ConfirmPasswordField.error = getString(R.string.ConnectFail)
                    }
                    else{
                        UsernameField.error = getString(R.string.Error)
                        PasswordField.error = getString(R.string.Error)
                        ConfirmPasswordField.error = getString(R.string.Error)
                    }
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
        )
    }
}