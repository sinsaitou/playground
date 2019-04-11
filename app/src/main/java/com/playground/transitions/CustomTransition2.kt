package com.playground.transitions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Log
import android.view.View
import android.view.ViewGroup



class CustomTransition2 : Transition() {

    private val PROP_NAME_VISIBILITY = "com.playground.transition:CustomTransition2:visibility"
    private val PROPS = arrayOf(PROP_NAME_VISIBILITY)

    override fun getTransitionProperties(): Array<String> {
        return PROPS
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        transitionValues.values[PROP_NAME_VISIBILITY] = transitionValues.view.visibility
    }

    override fun captureEndValues(transitionValues: TransitionValues) = Unit

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues): Animator? {
        Log.d("★", "createAnimator start")
        if (startValues?.view == null) {
            return null
        }

        val view = startValues.view
        val visibility = startValues.values[PROP_NAME_VISIBILITY] as Int
        val isEnter = visibility != View.VISIBLE

        view.visibility = View.VISIBLE
        view.alpha = if (isEnter) 0f else 1f

        val anim = ObjectAnimator.ofFloat(view, "alpha", if (isEnter) 1f else 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.alpha = 1f
                view.visibility = if (isEnter) View.VISIBLE else View.INVISIBLE
                super.onAnimationEnd(animation)
            }
        })
        return anim

    }
}
