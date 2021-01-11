package com.example.lanproject

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class HistoryPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_page)

        val queue = Volley.newRequestQueue(this)
        val url = "https://lwm.sh/~lanproject/GetUserHistory.php?user='" + LanProjectApplication.Username + "'"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val strRes = response.toString()
            val testvalues = strRes.split(";")

            findViewById<TextView>(R.id.HistoryEntryLoadAmount).text = testvalues[0]

            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels

            var textSize: Float = width.toFloat() / 45
            if (height * 2 < width)
                textSize /= 2
            if (textSize < 1)
                textSize = 1f

            findViewById<TextView>(R.id.HistoryEntryLoadText).text = height.toString() + " - " + width.toString()

            var i = true
            var x = 1
            while (i) {
                i = CreateNewHistory(x, testvalues, textSize)
                x += 5
            }
        }, { error ->
            if (error.networkResponse == null) {
                findViewById<TextView>(R.id.HistoryEntryLoadText).text = "No response"
            } else {
                findViewById<TextView>(R.id.HistoryEntryLoadText).text = "Error!"
            }
        })

        queue.add(stringRequest)
    }

    fun CreateNewHistory(X: Int, testvalues: List<String>, TextSize: Float): Boolean {
        if (testvalues[0].toString().toInt() == 0) {
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

        TextViewAct.text = getString(R.string.activity)
        TextViewPoint.text = getString(R.string.points)
        TextViewDiff.text = getString(R.string.difficulty)
        TextViewDate.text = getString(R.string.date)

        TextViewActRes.text = testvalues[X]
        TextViewPointRes.text = testvalues[X + 1] + "/" + testvalues[X + 2]
        TextViewDiffRes.text = testvalues[X + 3]
        TextViewDateRes.text = testvalues[X + 4]

        TextViewAct.textSize = TextSize
        TextViewPoint.textSize = TextSize
        TextViewDiff.textSize = TextSize
        TextViewDate.textSize = TextSize
        TextViewActRes.textSize = TextSize
        TextViewPointRes.textSize = TextSize
        TextViewDiffRes.textSize = TextSize
        TextViewDateRes.textSize = TextSize

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

        if ((testvalues[0].toString().toInt() * 5) + 1 == X + 5)
            return false

        return true
    }
}