package com.playground.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeableViewPager : ViewPager {

    private var mUnSwipe = true

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return !mUnSwipe && super.onInterceptTouchEvent(arg0)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return !mUnSwipe && super.onTouchEvent(event)
    }

    fun setUnSwipe(unSwipe: Boolean) {
        this.mUnSwipe = unSwipe
    }
}
