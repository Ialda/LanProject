package com.example.lanproject

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.edit_text.view.*
import kotlinx.android.synthetic.main.fragment_task_listening.*
import org.json.JSONObject

class TaskListeningFragment : Fragment(R.layout.fragment_task_listening), TaskFragment {
    private lateinit var viewModel: TaskListeningViewModel
    var playPauseButton: ImageButton? = null
    var seekBar: SeekBar? = null
    var mediaTime: TextView? = null
    var timeSek: Int = 0
    var timeMin: Int = 0
    var timeLengthSek: Int = 0
    var timeLengthMin: Int = 0
    private var difficulty : Int? = null
    private lateinit var inflater : LayoutInflater
    private var editTextMenus = mutableListOf<TextInputLayout>()
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
        val audio = json.getString("audio")
        init {
            val jsonValues = json.getJSONArray("items")
            for (i in 0 until jsonValues.length()) {
                items.add(TaskItem(jsonValues.getJSONObject(i)))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val editTextContents = arrayListOf<String>()
        editTextMenus.forEach{
            editTextContents.add(it.editText?.getText().toString())
        }

        outState.putStringArrayList("editTextContents", editTextContents)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Restore saved strings
        savedInstanceState?.getStringArrayList("editTextContents")?.forEachIndexed { index, it ->
            editTextMenus[index].editText?.setText(it)
        }

        if ((activity as TaskContainer).finishedTest) {
            finishTestButton.visibility = View.INVISIBLE
            (activity as TaskContainer).finishTest()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TaskListeningViewModel::class.java)

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
                    val editText = child.EditTextField
                    editText.isEnabled = false
                    editText.isClickable = false
                    scoreValue = if (editText.editText?.text.toString().equals(item.gap.toString(), ignoreCase = true)) 1 else 0

                    if (scoreValue == 1) {
                        child.findViewById<TextInputLayout>(R.id.EditTextField).setEndIconDrawable(R.drawable.ic_baseline_check_24)
                    }
                    else {
                        child.findViewById<TextInputLayout>(R.id.EditTextField).setEndIconDrawable(R.drawable.ic_baseline_close_24)
                    }
                    scoreValue
                }

                linearLayout2.addView(text)
                linearLayout2.addView(child)

                editTextMenus.add(child.EditTextField)
            }

            if (viewModel.mediaPlayer == null)
            {
                val audio:String = testData.audio.replace(".mp3", "")
                viewModel.mediaPlayer = MediaPlayer.create(context, resources.getIdentifier(audio, "raw", activity?.packageName))
            }
            playPauseButton = getView()?.findViewById(R.id.PlayPauseSound)
            seekBar = getView()?.findViewById(R.id.seekBar)
            mediaTime = getView()?.findViewById(R.id.mediaTime)

            if(viewModel.mediaPlayer?.isPlaying == true)
            {
                playPauseButton?.setImageResource(R.drawable.ic_baseline_pause_24)
            }

            playPauseButton?.setOnClickListener {
                if(viewModel.mediaPlayer?.isPlaying == true) {
                    PauseSound()
                    playPauseButton?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                else {
                    PlaySound()
                    playPauseButton?.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }

            val rawTimeLength = viewModel.mediaPlayer?.duration!!

            timeLengthSek = (rawTimeLength % 60000)/1000
            timeLengthMin = rawTimeLength/60000

            mediaTime?.text = ("00:00 / " + timeLengthMin.toString() + ":" + timeLengthSek.toString())

            seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) viewModel.mediaPlayer?.seekTo(progress)

                    timeSek = (progress % 60000) / 1000
                    timeMin = progress / 60000

                    mediaTime?.text = (timeMin.toString() + ":" + timeSek.toString() + " / " +
                            timeLengthMin.toString() + ":" + timeLengthSek.toString())
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
            viewModel.ClearMediaplayer()
            playPauseButton?.visibility = View.INVISIBLE
            seekBar?.visibility = View.INVISIBLE
        }
    }

    fun PlaySound() {
        viewModel.PlaySound()
    }

    fun PauseSound() {
        viewModel.PauseSound()
    }

    private fun initSeekbar() {
        seekBar?.max = viewModel.mediaPlayer!!.duration
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    seekBar?.progress = viewModel.mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    seekBar?.progress = 0
                }
            }
        }, 0)
    }

}