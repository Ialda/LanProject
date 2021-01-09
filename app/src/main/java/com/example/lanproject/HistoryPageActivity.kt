package com.example.lanproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.net.URL
import java.util.*
import kotlin.text.Charsets.UTF_8

class HistoryPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_page)

        var i = true
        var x = 0
        while (i) {
            i = CreateNewHistory(x)
            x += 5
        }
    }

    fun CreateNewHistory(x : Int) : Boolean {
        val X = x
        //Tillfällig stoppunkt. Behöver fungera dynamiskt mot hur många entries som finns.
        if (X == 15) {
            return false
        }

        val HistoryView = findViewById<LinearLayout>(R.id.LinearLayoutRes)
        val HorView = LinearLayout(this)
        val LinearView = LinearLayout(this)
        val LinearViewRes = LinearLayout(this)

        val TextViewAct = TextView(this)
        val TextViewPoint = TextView(this)
        val TextViewDiff = TextView(this)
        val TextViewDate = TextView(this)
        val TextViewActRes = TextView(this)
        val TextViewPointRes = TextView(this)
        val TextViewDiffRes = TextView(this)
        val TextViewDateRes = TextView(this)

        val LinearParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        LinearParams.setMargins(25)
        LinearView.orientation = LinearLayout.VERTICAL
        LinearViewRes.orientation = LinearLayout.VERTICAL

        //testing area
        /*val ResultList = listOf(int, int, int, date)
        ResultList[0].result = 3
        ResultList[0].maxResult = 10
        ResultList[0].difficulty = 1
        ResultList[0].timeStamp = Date()*/
        //end of area

        TextViewAct.text = getString(R.string.activity)
        TextViewPoint.text = getString(R.string.points)
        TextViewDiff.text = getString(R.string.difficulty)
        TextViewDate.text = getString(R.string.date)

        //Hämtar för tillfället alla entires, ska endast hämta för inloggad användare!
        val queue = Volley.newRequestQueue(this)
        val url = "https://lwm.sh/~lanproject/GetUserHistory.php"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val strRes = response.toString()
            val testvalues = strRes.split(";")
            TextViewActRes.text = testvalues[X]
            TextViewPointRes.text = testvalues[X + 1] + "/" + testvalues[X + 2]
            TextViewDiffRes.text = testvalues[X + 3]
            TextViewDateRes.text = testvalues[X + 4] }, { error ->
            if(error.networkResponse == null) {
                TextViewActRes.text = "No response"
            } else {
                TextViewActRes.text = "Error!"
            }
        })

        queue.add(stringRequest)

        TextViewAct.textSize = 24f
        TextViewPoint.textSize = 24f
        TextViewDiff.textSize = 24f
        TextViewDate.textSize = 24f
        TextViewActRes.textSize = 24f
        TextViewPointRes.textSize = 24f
        TextViewDiffRes.textSize = 24f
        TextViewDateRes.textSize = 24f

        LinearView.addView(TextViewAct)
        LinearView.addView(TextViewPoint)
        LinearView.addView(TextViewDiff)
        LinearView.addView(TextViewDate)
        LinearViewRes.addView(TextViewActRes)
        LinearViewRes.addView(TextViewPointRes)
        LinearViewRes.addView(TextViewDiffRes)
        LinearViewRes.addView(TextViewDateRes)

        HorView.addView(LinearView, LinearParams)
        HorView.addView(LinearViewRes, LinearParams)

        HistoryView.addView(HorView)

        return true
    }
}