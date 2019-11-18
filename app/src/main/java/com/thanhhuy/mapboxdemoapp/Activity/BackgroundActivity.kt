package com.thanhhuy.mapboxdemoapp.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.thanhhuy.mapboxdemoapp.R
import kotlinx.android.synthetic.main.activity_background.*

class BackgroundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background)

        navigate_register_activity.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        navigate_login_activity.setOnClickListener {
            val intent = Intent(this, LoginActivitykotlin::class.java)
            startActivity(intent)
        }
    }
}
