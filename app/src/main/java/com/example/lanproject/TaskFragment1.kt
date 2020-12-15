package com.example.lanproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Spinner
import kotlinx.android.synthetic.main.fragment_task_help_placeholder.view.*
import kotlinx.android.synthetic.main.fragment_task1.*
import kotlinx.android.synthetic.main.gap_spinner.*

class TaskFragment1 : Fragment(R.layout.fragment_task1) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val view = LayoutInflater.from(cardView5.context)
        val spinner = view.inflate(R.layout.gap_spinner, null)
    }
}