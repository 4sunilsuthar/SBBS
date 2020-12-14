package com.devsoft.apps.sbbs

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        setContentView(R.layout.activity_splash_screen)
        val mSplashThread: Thread
        val imgCompanyLogo: ImageView = findViewById(R.id.imgCompanyLogo)
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        val outAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
        val relativeLayout: RelativeLayout = findViewById(R.id.relative_layout_splash)

        // start the animation here

        // start the animation here
        mSplashThread = object : Thread() {
            override fun run() {
                try {
                    relativeLayout.startAnimation(outAnimation)
                    imgCompanyLogo.startAnimation(animation)
                    synchronized(this) {
                        TimeUnit.MILLISECONDS.sleep(3000L)
//                        this.wait(5000)
                        //wait(4000)
//                        mSplashThread.interrupt(3000)
                    }
                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                }
                // Intent intent = new Intent(getApplicationContext(), FuelAssetTrackerActivity.class);
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        mSplashThread.start()
    }
}