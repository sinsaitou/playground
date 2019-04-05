package com.playground.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.drawable.GradientDrawable
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Log
import android.view.ViewGroup

class CustomTransition : Transition() {

    override fun captureStartValues(transitionValues: TransitionValues) {
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        // end の値は使わないので何もしない
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues): Animator? {

        Log.d("★", "createAnimator start")
        if (startValues?.view == null || endValues.view == null) {
            return null
        }

        val anim = ObjectAnimator.ofFloat(startValues.view, "radius", 8.0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                Log.d("★", "createAnimator onAnimationStart")
                val shape = GradientDrawable().apply {
                    cornerRadius = 8f
                }
                startValues.view.background = shape
            }
            override fun onAnimationEnd(animation: Animator?) {
                Log.d("★", "createAnimator onAnimationEnd")
                super.onAnimationEnd(animation)
                val shape = GradientDrawable().apply {
                    cornerRadius = 8f
                }
                endValues.view.background = shape
            }
        })
        return anim
    }

    companion object {
        // TransitionValues に追加するときのキーは パッケージ名:クラス名:プロパティ名
        private const val PROP_NAME_VISIBILITY = "jp.developer.retia.activitytransitiontest.transitions:CustomTransition:visibility"
    }
}
