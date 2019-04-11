package com.playground.transitions

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Property
import android.view.ViewGroup
import com.playground.view.CoverView
import java.util.*


class CoverViewTransition : Transition {

    private val mStartShape: Int

    constructor(shape: Int) {
        mStartShape = shape
    }

    override fun getTransitionProperties(): Array<String> {
        return sTransitionProperties
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        // Add fake value to force calling of createAnimator method
        captureValues(transitionValues, "start")
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        // Add fake value to force calling of createAnimator method
        captureValues(transitionValues, "end")
    }

    private fun captureValues(transitionValues: TransitionValues, value: Any) {
        if (transitionValues.view is CoverView) {
            transitionValues.values[PROPNAME_RADIUS] = value
        }
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues, endValues: TransitionValues?): Animator? {

        if (endValues == null || endValues.view !is CoverView) {
            return null
        }

        val coverView = endValues.view as CoverView
        val minRadius = coverView.minRadius
        val maxRadius = coverView.maxRadius

        val startRadius: Float
        val endRadius: Float

        if (mStartShape == CoverView.SHAPE_RECTANGLE) {
            startRadius = maxRadius
            endRadius = minRadius
        } else {
            startRadius = minRadius
            endRadius = maxRadius
        }

        val animatorList = ArrayList<Animator>()

        coverView.transitionRadius = startRadius
        animatorList.add(ObjectAnimator.ofFloat(coverView, RADIUS_PROPERTY, startRadius, endRadius))

        val animator = AnimatorSet()
        animator.playTogether(animatorList)
        return animator
    }

    companion object {

        private val PROPNAME_RADIUS = CoverViewTransition::class.java.name + ":radius"
        private val sTransitionProperties = arrayOf(PROPNAME_RADIUS)

        private val RADIUS_PROPERTY = object : Property<CoverView, Float>(Float::class.java, "radius") {
            override fun set(view: CoverView, radius: Float) {
                view.transitionRadius = radius
            }

            override fun get(view: CoverView): Float {
                return view.transitionRadius
            }
        }
    }

}
