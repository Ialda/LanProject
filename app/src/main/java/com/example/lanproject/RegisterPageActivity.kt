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
import android.widget.Spinner
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_register_page.*

class RegisterPageActivity : AppCompatActivity() {

    var Username:EditText? = null
    var Password:EditText? = null
    var ConfirmPassword:EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        Username = findViewById<EditText>(R.id.PlainTextRegisterUsername)
        Password = findViewById<EditText>(R.id.PasswordRegisterpswd)
        ConfirmPassword = findViewById<EditText>(R.id.PasswordReRegisterpswd)

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

        if (Username?.text.toString() == "" || Password?.text.toString() == "" || ConfirmPassword?.text.toString() == "") {

            if (Username?.text.toString() == ""){
                UsernameField.error = getString(R.string.ReqFieldError)
            }
            if( Password?.text.toString() == ""){
                PasswordField.error = getString(R.string.ReqFieldError)
            }
            if (ConfirmPassword?.text.toString() == ""){
                ConfirmPasswordField.error = getString(R.string.ReqFieldError)
            }
        }
        else if (Password?.text.toString().count() < 8) {
            PasswordField.error = getString(R.string.PasswordToShort)
        }
        else if (Password?.text.toString() != ConfirmPassword?.text.toString()) {
            PasswordField.error = getString(R.string.PasswordMatchError)
            ConfirmPasswordField.error = getString(R.string.PasswordMatchError)
        }
        else
            CreateUser()
    }

    fun CreateUser(){

        val roleSpinner = findViewById<Spinner>(R.id.RoleSpinner)

        var url: String? = null
        val queue = Volley.newRequestQueue(this)

        //url = "https.//lwm.sh/~lanproject/register.php?uanme=<" + Username?.text + ">&pass=<" + Password?.text + ">&role=<" + roleSpinner?.selectedItem.toString() + ">"
        url = "https://lwm.sh/~lanproject/register.php?uname=" + Username?.text + "&pass=" + Password?.text + "&role=" + roleSpinner?.selectedItem.toString()
        //https.//lwm.sh/~lanproject/register.php?uanme=<användarnamn>&pass=<pass>&role=<Student eller Teacher>

        val stringRequest = StringRequest(Request.Method.GET, url, {
                response ->
            //Log.e("response", "YAS")
            startActivity(Intent(this, MainActivity::class.java))

        }, { error -> Log.e("response", "nei...")
            //Log.e("res", error.toString())
            //Log.e("res", url)
            //ConfirmPasswordField.error = error.toString()
            //if (error.toString() == "com.android.volley.NoConnectionError:")
            //    UsernameField.error = error.toString()
            //else if (error.toString() == "com.android.volley.)
            if (error.toString() == "com.android.volley.ClientError")
                UsernameField.error = getString(R.string.UsernameTaken)
            else if (error.networkResponse == null) {
                UsernameField.error = getString(R.string.ConnectFail)
                PasswordField.error = getString(R.string.ConnectFail)
                ConfirmPasswordField.error = getString(R.string.ConnectFail)
            }
            else{
                UsernameField.error = getString(R.string.Error)
                PasswordField.error = getString(R.string.Error)
                ConfirmPasswordField.error = error.toString()
            }
        })

        queue.add(stringRequest)
        //startActivity(Intent(this, MainActivity::class.java))
    }

}