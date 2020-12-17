package com.example.lanproject

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.fragment_task_help_placeholder.view.*
import kotlinx.android.synthetic.main.fragment_task1.*
import kotlinx.android.synthetic.main.gap_spinner.*
import kotlinx.android.synthetic.main.gap_spinner.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime

data class TestResult(
    var result : Int,
    var maxResult : Int,
    val difficulty : Int, // TODO(lucas): Of which type should we represent the difficulty?
    val timeStamp : Long,
) {

}

class TaskFragment1 : Fragment(R.layout.fragment_task1) {

    private lateinit var inflater: LayoutInflater
    private var gapSpinners = mutableListOf<Spinner>()

    // construct in try-catch to avoid json parse errors. TODO: (this is lazy do better error handling in class)
    // json: a JSONObject from the api to parse
    private class taskData(json: JSONObject) {
        class taskItem(json: JSONObject) {
            class choice(json: JSONObject) {
                val text = json.getString("text")
                val correct = json.getBoolean("correct")
                val feedback = json.getString("feedback")
                override fun toString(): String = text
            }

            val text1 = json.getString("text1")
            val text2 = json.getString("text2")
            val choices = mutableListOf<choice>()
            init {
                val jsonChoices = json.getJSONArray("choices")
                for (i in 0 until jsonChoices.length()) {
                    choices.add(choice(jsonChoices.getJSONObject(i)))
                }
            }
        }

        val title = json.getString("title")
        val instructions = json.getString("instructions")
                                                                        //TODO:
        val random = json.getBoolean("random")                          //What
        val randomiseChoices = json.getBoolean("randomiseChoices")      //Do
        val showQuestionNumbers = json.getBoolean("showQuestionNumbers")//These
        val sectionsOnSamePage = json.getBoolean("sectionsOnSamePage")  //Mean?

        val items = mutableListOf<taskItem>()
        init {
            val jsonItems = json.getJSONArray("items")
            for (i in 0 until jsonItems.length()) {
                val item = jsonItems.getJSONObject(i)
                items.add(taskItem(item))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        try {
            val testData = taskData(JSONObject("""
                {
                  "title": "Legal fight hits music pirates",
                  "instructions": "Click on the gaps, then choose the best word to fill the spaces.",
                  "random": false,
                  "randomiseChoices": false,
                  "showQuestionNumbers": true,
                  "sectionsOnSamePage": false,
                  "items": [
                    {
                      "text1": "<h2>Legal fight hits music pirates</h2>The global recording industry has launched its largest wave of legal",
                      "choices": [
                        {
                          "text": "action",
                          "correct": true,
                          "feedback": "Correct"
                        },
                        {
                          "text": "activity",
                          "correct": false,
                          "feedback": "Incorrect"
                        },
                        {
                          "text": "acting",
                          "correct": false,
                          "feedback": "Incorrect"
                        },
                        {
                          "text": "acts",
                          "correct": false,
                          "feedback": "Incorrect"
                        }
                      ],
                      "text2": "&nbsp;yet."
                    },{
                      "text1": "Russian torrent site RuTracker.org has been hit with several suits in the Moscow court from the ",
                      "choices": [
                        {
                          "text": "major",
                          "correct": true,
                          "feedback": "Correct"
                        },
                        {
                          "text": "evil",
                          "correct": false,
                          "feedback": "Incorrect"
                        },
                        {
                          "text": "corrupt",
                          "correct": false,
                          "feedback": "Incorrect"
                        },
                        {
                          "text": "capitalist",
                          "correct": false,
                          "feedback": "Incorrect"
                        }
                      ],
                      "text2": " american records organisation RIAA, which has been terrorizing independent content creators and music labels for several decades."
                    },{
                      "text1": "In a courtroom speech, RIAA representative <i>Core P. Orate Shill</i>",
                      "choices": [
                        {
                          "text": "testified to",
                          "correct": true,
                          "feedback": "Correct"
                        },
                        {
                          "text": "paid off",
                          "correct": false,
                          "feedback": "Incorrect"
                        },
                        {
                          "text": "blatantly lied to",
                          "correct": false,
                          "feedback": "Incorrect"
                        },
                        {
                          "text": "begged to",
                          "correct": false,
                          "feedback": "Incorrect"
                        }
                      ],
                      "text2": "the judge, painting a grim picture of the intellectual property industry, which in recent years has increasingly had to confront the general public's unwillingness to put up with their shit."
                    }
                  ]
                }
            """.trimIndent()))

            view.findViewById<TextView>(R.id.instructionsText).text = testData.instructions
            val linearLayout = view.findViewById<LinearLayout>(R.id.linearLayout)
            val dpRatio = view.context.resources.displayMetrics.density
            val textLayoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            )
            textLayoutParams.setMargins(
                (16*dpRatio).toInt(), 0,
                (16*dpRatio).toInt(), 0
            )

            testData.items.forEach {
                val text = TextView(view.context)
                text.text = HtmlCompat.fromHtml(it.text1+"...", HtmlCompat.FROM_HTML_MODE_LEGACY)
                text.layoutParams = textLayoutParams

                val inflater = LayoutInflater.from(linearLayout.context)
                val child = inflater.inflate(R.layout.gap_spinner, null)
                val adapter = ArrayAdapter<taskData.taskItem.choice>(
                    linearLayout.context,
                    R.layout.gap_spinner_text,
                    R.id.gapSpinnerText,
                    it.choices
                )
                child.findViewById<Spinner>(R.id.gapSpinner).adapter = adapter
                (activity as TaskContainer).addQuestionCallback {
                    if ((child.gapSpinner.selectedItem as taskData.taskItem.choice).correct) 1
                    else 0
                }

                val text2 = TextView(view.context)
                text2.text = HtmlCompat.fromHtml("..."+it.text2, HtmlCompat.FROM_HTML_MODE_LEGACY)
                text2.layoutParams = textLayoutParams

                linearLayout.addView(text)
                linearLayout.addView(child)
                linearLayout.addView(text2)

                gapSpinners.add(child.gapSpinner)
            }
        } catch (e: Exception) {
            Log.e("LanProject", "JSON failed (${e.message})")
        }

        // NOTE(lucas): Finish test button
        button.setOnClickListener {
            var result : Int = 0
            var maxResult : Int = 0
            gapSpinners.forEach { spinner ->
                ++maxResult
                val choice = spinner.selectedItem as taskData.taskItem.choice
                if (choice.correct) {
                    ++result
                }
            }
            val testResult = TestResult(result, maxResult, 0 /* TODO: Use actual difficulty */, System.currentTimeMillis())
            Log.i("LanProject", "Result: ${testResult.result}/${testResult.maxResult}, ${System.currentTimeMillis()}")
            /*
            // NOTE(lucas): Parse the timestamp by doing:
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
            val date = java.util.Date(System.currentTimeMillis())
            sdf.format(date)
            // (SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")).format(java.util.Date(System.currentTimeMillis()))
             */
            (activity as TaskContainer).finishTest()
        }
    }
}