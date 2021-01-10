package com.example.lanproject

import android.app.Application
import android.content.Intent

class LanProjectApplication : Application() {
    //val profileName=intent.getStringExtra("Username")
    //var Password:String? = null
    companion object {
        @JvmField
        var Username: String = "defaultValue"
        var Password: String = "defaultValue"
    }
}