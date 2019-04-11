package com.playground

import android.app.Application
import android.graphics.Color
import android.util.Log
import jp.wasabeef.takt.Seat
import jp.wasabeef.takt.Takt



class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Takt.stock(this)
                .seat(Seat.BOTTOM_RIGHT)
                .showOverlaySetting(true)
                .interval(250)
                .color(Color.BLACK)
                .size(14f)
                .alpha(.5f)
                .listener { fps ->
                    Log.d("Excellent!", "$fps fps")

                    // Logcat
                    // jp.wasabeef.example.takt D/Excellent!﹕ 59.28853754940712 fps
                    // jp.wasabeef.example.takt D/Excellent!﹕ 59.523809523809526 fps
                    // jp.wasabeef.example.takt D/Excellent!﹕ 59.05511811023622 fps
                    // jp.wasabeef.example.takt D/Excellent!﹕ 55.33596837944664 fps
                    // jp.wasabeef.example.takt D/Excellent!﹕ 59.523809523809526 fps
                }
                .play()
    }
}