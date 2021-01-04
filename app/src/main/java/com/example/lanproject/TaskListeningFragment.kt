package com.example.lanproject

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.fragment_task1.*
import org.json.JSONObject
import kotlinx.android.synthetic.main.fragment_task_listening.*
import kotlinx.android.synthetic.main.edit_text.*
import kotlinx.android.synthetic.main.edit_text.view.*

class TaskListeningFragment : Fragment(R.layout.fragment_task_listening), TaskFragment {
    var mediaPlayer: MediaPlayer? = null
    var playPauseButton: ImageButton? = null;
    var seekBar: SeekBar? = null;
    var mediaTime: TextView? = null;
    var timeSek: Int = 0;
    var timeMin: Int = 0;
    var timeLengthSek: Int = 0;
    var timeLengthMin: Int = 0;

    private var difficulty : Int? = null
    private lateinit var inflater : LayoutInflater
    private var editTextMenus = mutableListOf<EditText>()

    override fun init(difficulty: Int) {
        this.difficulty = difficulty
    }

    private class TaskData(json: JSONObject) {
        class TaskItem(json: JSONObject) {
            val text = json.getString("text")
            val gap = json.getString("gap")

            override fun toString(): String {
                return text
            }
        }
        val items = mutableListOf<TaskItem>()
        val title = json.getString("title")
        val instructions = json.getString("instructions")
        init {
            val jsonValues = json.getJSONArray("items")
            for (i in 0 until jsonValues.length()) {
                items.add(TaskItem(jsonValues.getJSONObject(i)))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        try {
            val testData = TaskData(JSONObject("""
                {
                    "title": "Cambridge English: CAE Listening 2",
                    "instructions": "You will hear a radio programme about the life of the singer, Lena Horne. For questions 1-8, complete the sentences.",
                    "audio": "testsound.mp3",
                    "items": [
                        {
                            "text": "The 'talented tenth' was a label given to those African Americans who had good social positions and were ",
                            "gap": "educated"
                        },
                        {
                            "text": ". <br><br> She left school and began her singing career at the well-known ",
                            "gap": "Cotton Club"
                        },
                        {
                            "text": ".<br><br> Her mother was keen that Lena's singing career would bring about the collapse of ",
                            "gap": "racial barriers"
                        },
                        {
                            "text": ".<br><br> Lena refused to sing for audiences of servicemen and prisoners which were ",
                            "gap": "segregated"
                        },
                        {
                            "text": ".<br><br> When Lena entered Hollywood, black actors were generally only hired to act in the roles of ",
                            "gap": "maids and butlers"
                        },
                        {
                            "text": ".<br><br> While she was working for Hollywood, Lena found that, during the ",
                            "gap": "editing process"
                        },
                        {
                            "text": ", much of her spoken work was removed from the film.<br><br> Lena spent a lot of the 1950s working in ",
                            "gap": "nightclubs"
                        }
                    ]
                }
            """.trimIndent()))

            inflater = LayoutInflater.from(linearLayout2.context)

            // NOTE(lucas): Copy-pasta from TaskFragment1
            // Should probably use a xml layout styling for the text views (as well as input and selection views) in all tasks. This will do for now though.
            val dpRatio = view.context.resources.displayMetrics.density
            val textLayoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            )
            textLayoutParams.setMargins(
                (16 * dpRatio).toInt(), 0,
                (16 * dpRatio).toInt(), 0
            )

            listeningInstructionsText.text = testData.instructions
            listeningTaskTitle.text = testData.title
            testData.items.forEach { item ->
                val text = TextView(view.context)
                text.text = HtmlCompat.fromHtml(item.text, HtmlCompat.FROM_HTML_MODE_LEGACY)
                text.layoutParams = textLayoutParams

                val child = inflater.inflate(R.layout.edit_text, null)

                (activity as TaskContainer).addQuestionCallback {
                    var scoreValue = 0
                    val editText = child.editText
                    editText.isEnabled = false
                    editText.isClickable = false
                    scoreValue = if (editText.text.toString().equals(item.gap.toString(), ignoreCase = true)) 1 else 0

                    if (scoreValue == 1) {
                        child.findViewById<ImageView>(R.id.successIcon).visibility = View.VISIBLE
                    }
                    else {
                        child.findViewById<ImageView>(R.id.failIcon).visibility = View.VISIBLE
                    }
                    scoreValue
                }

                linearLayout2.addView(text)
                linearLayout2.addView(child)

                editTextMenus.add(child.editText)
            }

            // TODO: Use the JSON data to see which audio file we should use for this test
            mediaPlayer = MediaPlayer.create(context, R.raw.testsound)
            playPauseButton = getView()?.findViewById(R.id.PlayPauseSound)
            seekBar = getView()?.findViewById(R.id.seekBar)
            mediaTime = getView()?.findViewById(R.id.mediaTime)

            playPauseButton?.setOnClickListener {
                if(mediaPlayer?.isPlaying == true) {
                    PauseSound()
                    playPauseButton?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                else {
                    PlaySound()
                    playPauseButton?.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }

            var rawTimeLength = mediaPlayer?.duration!!

            timeLengthSek = (rawTimeLength % 60000)/1000
            timeLengthMin = rawTimeLength/60000

            mediaTime?.text = ("00:00 / " + timeLengthMin.toString() + ":" + timeLengthSek.toString())

            seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if(fromUser) mediaPlayer?.seekTo(progress)

                    timeSek = (progress % 60000)/1000
                    timeMin = progress/60000


                    mediaTime?.text = (timeMin.toString() + ":" + timeSek.toString() + " / " +
                            timeLengthMin.toString() + ":" + timeLengthSek.toString())


                    //totalTime?.text = (timeLengthMin.toString() + ":" + timeLengthSek.toString())
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            })
            initSeekbar()
        } catch (e: Exception) {
            Log.e("LanProject", "JSON failed (${e.message})")
        }

        finishTestButton.setOnClickListener {
            it.visibility = View.INVISIBLE
            (activity as TaskContainer).finishTest()
        }
    }

    fun PlaySound() {
        mediaPlayer?.start()
    }

    fun PauseSound() {
        mediaPlayer?.pause()
    }

    private fun initSeekbar() {
        seekBar?.max = mediaPlayer!!.duration
        val handler = Handler()
        handler.postDelayed(object: Runnable {
            override fun run(){
                try {
                    seekBar?.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                }
                catch (e: Exception){
                    seekBar?.progress = 0
                }
            }
        }, 0)
    }

}