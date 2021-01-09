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
    }

    fun CreateNewHistory(view: View) {
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
        /*var DiffRes = "placeholder"
        when (ResultList[0].difficulty) {
            0 -> "Easy"
            1 -> "Medium"
            2 -> "Hard"
        }*/
        val Point = "3"
        val MaxPoint = "10"

        val queue = Volley.newRequestQueue(this)
        val url = "https://lwm.sh/~lanproject/GetUserHistory.php"

        val stringRequest =
                StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->

                    var strRes = response.toString()
                    var testvalues = strRes.split(";");
                    TextViewActRes.text = testvalues[0];
                    TextViewPointRes.text = testvalues[1] + "/" + testvalues[2]
                    TextViewDiffRes.text = testvalues[3]
                    TextViewDateRes.text = testvalues[4]

                }, Response.ErrorListener {error ->
                    if(error.networkResponse == null) {
                        TextViewActRes.text = "No response"
                    }
                    else
                    {
                        TextViewActRes.text = "Error!"
                    }
                })
        queue.add(stringRequest)

        TextViewPointRes.text = "$Point/$MaxPoint"
        TextViewDiffRes.text = "Medium"
        TextViewDateRes.text = Date().toString()

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
    }
}