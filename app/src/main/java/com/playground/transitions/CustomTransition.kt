package com.playground.transitions

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Log
import android.util.Property
import android.view.ViewGroup
import com.makeramen.roundedimageview.RoundedImageView

class CustomTransition : Transition() {

    private val RADIUS_PROPERTY = object : Property<RoundedImageView, Float>(Float::class.java, "radius") {
        override fun set(view: RoundedImageView, radius: Float) {
            //Log.d("★", "RADIUS_PROPERTY set[$radius]")
            view.cornerRadius = radius
        }

        override fun get(view: RoundedImageView): Float {
            return view.cornerRadius
        }
    }

    private val ALPHA_PROPERTY = object : Property<RoundedImageView, Float>(Float::class.java, "alpha") {
        override fun set(view: RoundedImageView, alpha: Float) {
            //Log.d("★", "ALPHA_PROPERTY set[$alpha]")
            view.alpha = alpha
        }

        override fun get(view: RoundedImageView): Float {
            return view.alpha
        }
    }

//    private val RADIUS_PROPERTY = object : Property<CardView, Float>(Float::class.java, "radius") {
//        override fun set(view: CardView, radius: Float) {
//            //Log.d("★", "RADIUS_PROPERTY set[$radius]")
//            view.radius = radius
//        }
//
//        override fun get(view: CardView): Float {
//            return view.radius
//        }
//    }
//
//    private val ALPHA_PROPERTY = object : Property<CardView, Float>(Float::class.java, "alpha") {
//        override fun set(view: CardView, alpha: Float) {
//            //Log.d("★", "ALPHA_PROPERTY set[$alpha]")
//            view.alpha = alpha
//        }
//
//        override fun get(view: CardView): Float {
//            return view.alpha
//        }
//    }

    override fun captureStartValues(transitionValues: TransitionValues) {
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues): Animator? {
        Log.d("★", "createAnimator start")
        if (endValues == null || endValues.view !is RoundedImageView) {
            return null
        }

        val startImageView = endValues.view as RoundedImageView
        val endImageView = endValues.view as RoundedImageView
        val w = endImageView.width
        val h = endImageView.height

        val minRadius = Math.min(w, h) / 2f
        val maxRadius = Math.hypot((w / 2f).toDouble(), (h / 2f).toDouble()).toFloat()
        Log.d("★", "Radius[$minRadius/$maxRadius] w/h[$w/$h]")

        val animatorList = ArrayList<Animator>()

        endImageView.isOval = false
        endImageView.cornerRadius = minRadius
        animatorList.add(ObjectAnimator.ofFloat<RoundedImageView>(endImageView, RADIUS_PROPERTY, minRadius, 8f))

        endImageView.alpha = 0f
        animatorList.add(ObjectAnimator.ofFloat<RoundedImageView>(endImageView, ALPHA_PROPERTY, 0f, 255f))

        //animatorList.add(ViewAnimationUtils.createCircularReveal(endImageView, w / 2, h / 2, minRadius / 2, minRadius))

        val animator = AnimatorSet()
        animator.playTogether(animatorList)
        return animator

    }

}
