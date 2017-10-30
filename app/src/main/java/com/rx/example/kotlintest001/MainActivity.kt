package com.rx.example.kotlintest001

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rx.example.kotlintest001.view.activity.ActivityRcvMain
import io.realm.Realm

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val intent = Intent(applicationContext, ActivityRcvMain::class.java)
        applicationContext.startActivity(intent)
        finish()
    }
}
