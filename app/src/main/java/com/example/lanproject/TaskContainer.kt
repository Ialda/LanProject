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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.task_container_bottomsheet.*
import kotlinx.coroutines.withTimeout

class TaskContainer : AppCompatActivity() {
    private inner class PageAdapter(activity: TaskContainer, private val pages: List<Fragment>) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = pages.count()
        override fun createFragment(position: Int): Fragment = pages[position]
    }

    // Fragments for Tasks 1-5
    // TODO: only show the tasks we have in the UI
    private val taskFragments = listOf(
        TaskFragment1::class.java,
        TaskListeningFragment::class.java,
    )

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
    fun finishTest() {
        var tot = 0
        var numQuestions = 0
        questionCallbacks.forEach{
            tot+=it()
            numQuestions++
        }

        val testResult = TestResult(tot, numQuestions, intent.getIntExtra("difficulty", 0), System.currentTimeMillis())
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
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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
        viewPager.adapter = PageAdapter(this, listOf(
            taskFragments[intent.getIntExtra("taskID", 0)].newInstance(),   // Task Fragment
            TaskHelpPlaceholder()                                           // Help Fragment
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
            //TODO: submit test to db when ready
            startActivity(Intent(this, LandingPageActivity::class.java))
            finish()
        }
    }
}