package com.example.lanproject

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HistoryPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_page)

        val queue = Volley.newRequestQueue(this)
        val url = "https://lwm.sh/~lanproject/GetUserHistory.php?user='" + LanProjectApplication.Username + "'"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val strRes = response.toString()
            val testvalues = strRes.split(";")

            //Getting screen size
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels

            //Setting text size
            var textSize: Float = width.toFloat() / 45
            if (height * 2 < width)
                textSize /= 2
            if (textSize < 1)
                textSize = 1f

            var i = true
            var x = 1
            while (i) {
                i = CreateNewHistory(x, testvalues, textSize)
                x += 5
            }
        }, { error ->
            if (error.networkResponse == null) {
                findViewById<TextView>(R.id.HistoryEntryLoadText).text = getString(R.string.ConnectFail)
            } else {
                findViewById<TextView>(R.id.HistoryEntryLoadText).text = getString(R.string.Error)
            }
        })

        queue.add(stringRequest)
    }

    fun CreateNewHistory(X: Int, testvalues: List<String>, TextSize: Float): Boolean {
        //Catch if no entries
        if (testvalues[0].toString().toInt() == 0) {
            return false
        }

        val HistoryView = findViewById<LinearLayout>(R.id.LinearLayoutRes)
        val CardView = CardView(this)
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

        //Set parameters
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

        if (Build.VERSION.SDK_INT >= 26) {
            TextViewDateRes.text = TimeZoneConvertion(testvalues[X + 4])
        } else {
            TextViewDateRes.text = testvalues[X + 4]
            findViewById<TextView>(R.id.HistoryEntryLoadText).text = getString(R.string.SdkWarning)
        }

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
        CardView.addView((HorView))

        HistoryView.addView(CardView, LinearParams)

        //Return false if there are no more entries
        if ((testvalues[0].toString().toInt() * 5) + 1 == X + 5)
            return false

        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun TimeZoneConvertion(TimeToConvert : String) : String {
        //Converts the timestamp from UTC to the systemdefault timezone
        val resultOfParsing = OffsetDateTime.parse(TimeToConvert + "+0000", DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss[.SSS]Z"))
        val currentTimeZone: ZonedDateTime = resultOfParsing.atZoneSameInstant(ZoneId.systemDefault())
        return currentTimeZone.format(DateTimeFormatter.ofPattern(("uuuu-MM-dd HH:mm:ss")))
    }
}