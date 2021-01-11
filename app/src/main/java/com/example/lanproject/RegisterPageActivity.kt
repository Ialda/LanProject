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
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_page.*
import kotlinx.android.synthetic.main.activity_register_page.PasswordField
import kotlinx.android.synthetic.main.activity_register_page.UsernameField
import org.json.JSONObject

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

        //Removes error status from textboxes when typed in
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

        url = "https://lwm.sh/~lanproject/register.php?uname=" + Username?.text + "&pass=" + Password?.text + "&role=" + roleSpinner?.selectedItem.toString()
        //https.//lwm.sh/~lanproject/register.php?uanme=<användarnamn>&pass=<pass>&role=<Student eller Teacher>

        queue.add(
                object : JsonObjectRequest(Request.Method.GET, url, null,
                        Response.Listener<JSONObject> { response ->
                            response?.toString(4)?.let { Log.i("LanProject", it) }
                            if (response.getString("status") == "success") {
                                startActivity(Intent(this, MainActivity::class.java))
                            } else {
                                UsernameField.error = getString(R.string.Error)
                                PasswordField.error = getString(R.string.Error)
                                ConfirmPasswordField.error = getString(R.string.Error)
                            }
                        },
                        { error ->
                            if (error.networkResponse == null){
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
                {}
        )

    }

}