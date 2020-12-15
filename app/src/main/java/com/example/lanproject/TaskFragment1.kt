package com.example.lanproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.json.JSONObject

class TaskFragment1 : Fragment(R.layout.fragment_task1) {

    // construct in try-catch to avoid json parse errors. (this is lazy do better)
    // json: a JSONObject from the api to parse
    private class taskData(json: JSONObject) {
        class taskItem(json: JSONObject) {
            class choice(json: JSONObject) {
                val text = json.getString("text")
                val correct = json.getBoolean("correct")
                val feedback = json.getString("feedback")
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
                                                                        // TODO:
        val random = json.getBoolean("random")                          // What
        val randomiseChoices = json.getBoolean("randomiseChoices")      // Do
        val showQuestionNumbers = json.getBoolean("showQuestionNumbers")// These
        val sectionsOnSamePage = json.getBoolean("sectionsOnSamePage")  // Mean?

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
                  "title": "Legalfighthitsmusicpirates",
                  "instructions": "Clickonthegaps,thenchoosethebestwordtofillthespaces.",
                  "random": false,
                  "randomiseChoices": false,
                  "showQuestionNumbers": true,
                  "sectionsOnSamePage": false,
                  "items": [
                    {
                      "text1": "<h2>Legalfighthitsmusicpirates</h2>Theglobalrecordingindustryhaslauncheditslargestwaveoflegal",
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
                      "text2": "&nbsp;"
                    }
                  ]
                }
            """.trimIndent()))
            Log.i("LanProject", testData.title)
            Log.i("LanProject", testData.instructions)
            Log.i("LanProject", if (testData.random) "random=true" else "random=false")
            Log.i("LanProject", if (testData.randomiseChoices) "randomiseChoices=true" else "randomiseChoices=false")
            Log.i("LanProject", if (testData.showQuestionNumbers) "showQuestionNumbers=true" else "showQuestionNumbers=false")
            Log.i("LanProject", if (testData.sectionsOnSamePage) "sectionsOnSamePage=true" else "sectionsOnSamePage=false")
            testData.items.forEach{
                Log.i("LanProject", "\t"+it.text1)
                    it.choices.forEach{
                        Log.i("LanProject", "\t\t"+it.text)
                        Log.i("LanProject", "\t\t"+if (it.correct) "correct=true" else "correct=false")
                        Log.i("LanProject", "\t\t"+it.feedback)
                    }
                Log.i("LanProject", "\t"+it.text2)
            }
        } catch (e: Exception) {
            Log.e("LanProject", "JSON failed")
        }
    }
}