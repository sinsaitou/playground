package com.playground.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Log
import android.view.ViewGroup
import com.playground.R

class CustomTransition : Transition() {

    override fun captureStartValues(transitionValues: TransitionValues) {
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues): Animator? {

        Log.d("★", "createAnimator start")
        if (startValues?.view == null || endValues.view == null) {
            return null
        }
        Log.d("★", "startValues?.view[${startValues?.view.id}] endValues.view[${endValues.view.id}]")

        startValues.view.setBackgroundResource(R.drawable.background_corner)

        val anim = ObjectAnimator.ofFloat(startValues.view, "alpha", 1f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                Log.d("★", "createAnimator onAnimationStart [${startValues.view.id}][${startValues.view.tag}][${startValues.view.javaClass.simpleName}]")
            }
        })
        return anim
    }

    companion object {
        // TransitionValues に追加するときのキーは パッケージ名:クラス名:プロパティ名
        private const val PROP_NAME_VISIBILITY = "com.playground.transitions:CustomTransition:visibility"
    }
}
