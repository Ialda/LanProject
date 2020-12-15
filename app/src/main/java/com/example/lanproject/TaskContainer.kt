package com.example.lanproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    // TODO: Fan, this is quick and dirty
    // Store java class names instead of initialized fragments...
    // Fragments for Tasks 1-5
    private val taskFragments = listOf(
        TaskFragment1(),
        TaskHelpPlaceholder(), // placeholders
        TaskHelpPlaceholder(),
        TaskHelpPlaceholder(),
        TaskHelpPlaceholder()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_container)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = PageAdapter(this, listOf(
            taskFragments[intent.getIntExtra("taskID", 0)], // Task Fragment
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