package com.example.lanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.widget.SeekBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.task_container_bottomsheet.*
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import android.util.Base64

class TaskContainer : AppCompatActivity() {
    private inner class PageAdapter(activity: TaskContainer, private val pages: List<Fragment>) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = pages.count()
        override fun createFragment(position: Int): Fragment = pages[position]
    }

    // Fragments for Tasks 1-5
    private val taskFragments = listOf(
        TaskFragment1::class.java,
        TaskListeningFragment::class.java
    )
    private val taskNames = listOf(
        "Reading",
        "Listening"
    )
    private lateinit var taskFragment: TaskFragment

    private val questionCallbacks = mutableListOf<() -> Int>()

    // Call this function to add a callback to be run when the user has finished the test.
    // Each question in the test should have its own callback which disables further input,
    // marks the answer as right or wrong in the UI and returns how many points the user got
    // on the question.
    // Usage example (from task fragment):
    // (activity as TaskContainer).addQuestionCallback {
    //      if (userAnsweredCorrectly)
    //          return 1
    //      else
    //          return 0
    // }
    // NOTE(linus): For now, we assume each question can only give you one point. You can return
    // more than one point, but the calculated max score will be wrong. EZ fix, tell me if ya need it
    fun addQuestionCallback(callback: () -> Int) {
        questionCallbacks.add(callback)
    }

    // Call this function when the user wants to finish the test (i.e. when they press the "finish
    // test" button). It will call all the registered question callbacks, calculate the score and
    // show the score-UI to the user.
    // Example:
    // (activity as TaskContainer).finishTest()
    var finishedTest: Boolean = false
        private set

    lateinit var queue: RequestQueue
    var submitted = false
    lateinit var testResult: TestResult
    fun finishTest() {
        finishedTest = true

        var tot = 0
        var numQuestions = 0
        questionCallbacks.forEach{
            tot+=it()
            numQuestions++
        }

        //TODO Store Date, not Long
        testResult = TestResult(tot, numQuestions, intent.getIntExtra("difficulty", 0), System.currentTimeMillis())
        /*
        // NOTE(lucas): Parse the timestamp by doing:
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
        val date = java.util.Date(System.currentTimeMillis())
        sdf.format(date)
        // (SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")).format(java.util.Date(System.currentTimeMillis()))
         */

        Log.d("LanProject", "======================================")
        Log.d("LanProject", "Total score: $tot out of $numQuestions")
        Log.d("LanProject", "======================================")

        bottomSheet.visibility = View.VISIBLE
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    btmSheetExpand.setImageResource(R.drawable.ic_baseline_expand_less_24)
                else
                    btmSheetExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
            }
        })
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        setProgress(tot, numQuestions)

    }

    private fun setProgress(prog: Int, max: Int) {
        val circleProg = Math.max(prog / max.toFloat(), 0.05f)
        val circle1Rotation = /*-270f +*/ 360f * Math.min(0.5f, circleProg)

        scoreColorCircle2.visibility = View.INVISIBLE
        scoreText.visibility = View.INVISIBLE

        scoreColorCircle1.startAnimation(RotateAnimation(
            0f, circle1Rotation,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            fillAfter = true
            duration = 160
            interpolator = LinearInterpolator()

            if (circleProg > 0.5f) {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        scoreColorCircle2.visibility = View.VISIBLE

                        val circle2Rotation = /*-90f +*/ 360f * Math.max(0f, circleProg - 0.5f)
                        scoreColorCircle2.startAnimation(RotateAnimation(
                            0f, circle2Rotation,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f
                        ).apply {
                            fillAfter = true
                            duration = 160
                            interpolator = LinearInterpolator()

                            setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationRepeat(animation: Animation?) {}
                                override fun onAnimationStart(animation: Animation?) {}
                                override fun onAnimationEnd(animation: Animation?) {
                                    scoreText.visibility = View.VISIBLE
                                    scoreText.text = "$prog/$max"

                                    scoreText.startAnimation(ScaleAnimation(
                                        0f, 1f,
                                        0f, 1f,
                                        Animation.RELATIVE_TO_SELF, 0.5f,
                                        Animation.RELATIVE_TO_SELF, 0.5f
                                    ).apply {
                                        duration = 160
                                    })
                                }
                            })
                        })
                    }
                })
            } else {
                scoreText.visibility = View.VISIBLE
                scoreText.text = "$prog/$max"

                scoreText.startAnimation(ScaleAnimation(
                    0f, 1f,
                    0f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                ).apply {
                    duration = 160
                })
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_container)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        taskFragment = if (savedInstanceState != null) {
            supportFragmentManager.getFragment(savedInstanceState, "taskFragment") as TaskFragment
        } else {
            // TODO: Error handling for below, or is it actually better to have an outright crash
            //  (because something would have gone entirely wrong if an incorrect class found its way)
            //  into here
            taskFragments[intent.getIntExtra("taskID", 0)].newInstance() as TaskFragment
        }

        taskFragment.init(intent.getIntExtra("difficulty", 0))
        viewPager.adapter = PageAdapter(this, listOf(
            taskFragment as Fragment,   // Task Fragment
            TaskHelpPlaceholder()       // Help Fragment
        ))

        val tabTexts = listOf("Task", "Learning Materials")
        TabLayoutMediator(  // TODO: prettify all this
            findViewById(R.id.tabLayout),
            viewPager
        ) {
            tab: TabLayout.Tab, position: Int ->
            tab.text = if (position <= 1) tabTexts[position] else "Naj nu vart de fel"
        }.attach()

        btmSheetExpand.setOnClickListener {
            if (BottomSheetBehavior.from(bottomSheet).state == BottomSheetBehavior.STATE_COLLAPSED)
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            else {
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
                btmSheetExpand.setImageResource(R.drawable.ic_baseline_expand_less_24)
            }
        }
        bottomSheet.visibility = View.GONE

        btmSheetReturn.setOnClickListener {
            queue = Volley.newRequestQueue(this)
            val url = "https://lwm.sh/~lanproject/complete.php?maxResult=${testResult.maxResult}&result=${testResult.result}&difficulty=${
                    when (intent.getIntExtra("difficulty", 0)) {
                        0 -> "Easy"
                        1 -> "Normal"
                        else -> "Hard"
                    }
                }&activity=${taskNames[intent.getIntExtra("taskID", 0)]}"
            queue.add(
                object : JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener<JSONObject> { response ->
                        response?.toString(4)?.let { Log.i("LanProject", it) }
                        if (response.getString("status") == "success") {
                            finish()
                        } else {
                            Toast.makeText(this, "Unable to post result to database, please try again later", Toast.LENGTH_LONG).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        Log.i("LanProject", error.toString())
                        Toast.makeText(this, "Unable to post result to database, please try again later", Toast.LENGTH_LONG).show()
                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val user = "testUser"
                        val pass = "hejsan"
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "Basic ${Base64.encodeToString("$user:$pass".toByteArray(),Base64.DEFAULT)}"
                        return headers
                    }
                }
            )
            // startActivity(Intent(this, LandingPageActivity::class.java))
            // startActivity(Intent(this, MainPageActivity::class.java))
        }

        // Updates the value, doesn't actually call finishTest(). That is left to the fragment,
        // as it must register all its questions first
        finishedTest = savedInstanceState?.getBoolean("finishedTest") ?: false
        submitted = savedInstanceState?.getBoolean("submitted") ?: false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("finishedTest", finishedTest)
        outState.putBoolean("submitted", submitted)
        supportFragmentManager.putFragment(outState, "taskFragment", taskFragment as Fragment)
    }
}