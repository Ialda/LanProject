package com.example.lanproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TaskContainer : AppCompatActivity() {
    private inner class PageAdapter(activity: TaskContainer, private val pages: List<Fragment>) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = pages.count()
        override fun createFragment(position: Int): Fragment = pages[position]
    }

    // Fragments for Tasks 1-5
    private val taskFragments = listOf(
        TaskFragment1::class.java,
        TaskListeningFragment::class.java,
        TaskHelpPlaceholder::class.java, // placeholders
        TaskHelpPlaceholder::class.java,
        TaskHelpPlaceholder::class.java
    )

    private val questionCallbacks = mutableListOf<() -> Int>()

    fun addQuestionCallback(callback: () -> Int) {
        questionCallbacks.add(callback)
    }

    fun finishTest() {
        var tot = 0
        var numQuestions = 0
        questionCallbacks.forEach{
            tot+=it()
            numQuestions++
        }

        Log.i("LanProject", "======================================")
        Log.i("LanProject", "Total score: $tot out of $numQuestions")
        Log.i("LanProject", "======================================")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_container)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = PageAdapter(this, listOf(
            taskFragments[intent.getIntExtra("taskID", 0)].newInstance(), // Task Fragment
            TaskHelpPlaceholder()                                             // Help Fragment
        ))

        val tabTexts = listOf("Task", "Learning Materials")
        TabLayoutMediator(  // TODO: prettify all this
            findViewById(R.id.tabLayout),
            viewPager
        ) {
            tab: TabLayout.Tab, position: Int ->
            tab.text = if (position <= 1) tabTexts[position] else "Naj nu vart de fel"
        }.attach()
    }
}