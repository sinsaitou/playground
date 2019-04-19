package com.playground.transitions

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Rect
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Log
import android.util.Property
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.playground.view.CircleRectView

class CustomTransition3 : Transition {

    private var startCornerRadius: Float = 0f
    private var startCornerRadiusTopLeft: Float = 0f
    private var startCornerRadiusTopRight: Float = 0f
    private var startCornerRadiusBottomRight: Float = 0f
    private var startCornerRadiusBottomLeft: Float = 0f

    private var endCornerRadius: Float = 0f
    private var endCornerRadiusTopLeft: Float = 0f
    private var endCornerRadiusTopRight: Float = 0f
    private var endCornerRadiusBottomRight: Float = 0f
    private var endCornerRadiusBottomLeft: Float = 0f

    constructor(startCornerRadius: Float, endCornerRadius: Float) {
        this.startCornerRadius = startCornerRadius
        this.endCornerRadius = endCornerRadius
    }

    constructor(
            startCornerRadiusTopLeft: Float,
            startCornerRadiusTopRight: Float,
            startCornerRadiusBottomLeft: Float,
            startCornerRadiusBottomRight: Float,
            endCornerRadiusTopLeft: Float,
            endCornerRadiusTopRight: Float,
            endCornerRadiusBottomLeft: Float,
            endCornerRadiusBottomRight: Float) {
        this.startCornerRadiusTopLeft = startCornerRadiusTopLeft
        this.startCornerRadiusTopRight = startCornerRadiusTopRight
        this.startCornerRadiusBottomLeft = startCornerRadiusBottomLeft
        this.startCornerRadiusBottomRight = startCornerRadiusBottomRight
        this.endCornerRadiusTopLeft = endCornerRadiusTopLeft
        this.endCornerRadiusTopRight = endCornerRadiusTopRight
        this.endCornerRadiusBottomLeft = endCornerRadiusBottomLeft
        this.endCornerRadiusBottomRight = endCornerRadiusBottomRight
        Log.d("★", "CustomTransition3 constructor")
    }

    private val RADIUS_PROPERTY = object : Property<CircleRectView, Float>(Float::class.java, "radius") {
        override fun set(view: CircleRectView, radius: Float) {
            view.cornerRadius = radius
            //view.invalidate()
        }

        override fun get(view: CircleRectView): Float {
            return view.cornerRadius
        }
    }

    private val TOP_LEFT_RADIUS_PROPERTY = object : Property<CircleRectView, Float>(Float::class.java, "radiusTopLeft") {
        override fun set(view: CircleRectView, radius: Float) {
            view.cornerRadiusTopLeft = radius
            //view.invalidate()
        }

        override fun get(view: CircleRectView): Float {
            return view.cornerRadiusTopLeft
        }
    }
    private val TOP_RIGHT_RADIUS_PROPERTY = object : Property<CircleRectView, Float>(Float::class.java, "radiusTopRight") {
        override fun set(view: CircleRectView, radius: Float) {
            view.cornerRadiusTopRight = radius
            //view.invalidate()
        }

        override fun get(view: CircleRectView): Float {
            return view.cornerRadiusTopRight
        }
    }
    private val BOTTOM_RIGHT_RADIUS_PROPERTY = object : Property<CircleRectView, Float>(Float::class.java, "radiusBottomRight") {
        override fun set(view: CircleRectView, radius: Float) {
            view.cornerRadiusBottomRight = radius
            //view.invalidate()
        }

        override fun get(view: CircleRectView): Float {
            return view.cornerRadiusBottomRight
        }
    }
    private val BOTTOM_LEFT_RADIUS_PROPERTY = object : Property<CircleRectView, Float>(Float::class.java, "radiusBottomLeft") {
        override fun set(view: CircleRectView, radius: Float) {
            view.cornerRadiusBottomLeft = radius
            //view.invalidate()
        }

        override fun get(view: CircleRectView): Float {
            return view.cornerRadiusBottomLeft
        }
    }

//    private val RADIUS_PROPERTY = object : Property<CircleRectView, Float>(Float::class.java, "radius") {
//        override fun set(view: CircleRectView, radius: Float) {
//            //Log.d("★", "RADIUS_PROPERTY set[$radius]")
//            view.cornerRadius = radius
//            view.invalidate()
//        }
//
//        override fun get(view: CircleRectView): Float {
//            return view.cornerRadius
//        }
//    }
//
//    private var endCornerRadius: Float = 0f
//
//    constructor(endCornerRadius: Float) : this() {
//        this.endCornerRadius = endCornerRadius
//    }
//
//
//    private val ALPHA_PROPERTY = object : Property<SquaredRoundedImageView, Float>(Float::class.java, "alpha") {
//        override fun set(view: SquaredRoundedImageView, alpha: Float) {
//            Log.d("★", "ALPHA_PROPERTY set[$alpha]")
//            view.alpha = alpha
//        }
//
//        override fun get(view: SquaredRoundedImageView): Float {
//            return view.alpha
//        }
//    }

    private val BOUNDS = "com.playground.transitions.CustomTransition3:viewBounds"
    private val PROPS = arrayOf(BOUNDS)


    override fun getTransitionProperties(): Array<String> {
        return PROPS
    }

    private fun captureValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        val bounds = Rect()
        bounds.left = view.left
        bounds.right = view.right
        bounds.top = view.top
        bounds.bottom = view.bottom
        transitionValues.values[BOUNDS] = bounds
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        Log.d("★", "CustomTransition3 captureStartValues")
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        Log.d("★", "CustomTransition3 captureEndValues")
        captureValues(transitionValues)
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
        Log.d("★", "======>>>>> CustomTransition3 createAnimator start")
        if (startValues == null || endValues == null) {
            return null
        }

        if (startValues.view !is CircleRectView) {
            return null
        }

        val view = startValues.view as CircleRectView
        val endView = endValues.view as CircleRectView

        val startRect = startValues.values[BOUNDS] as Rect
        val endRect = endValues.values[BOUNDS] as Rect

        val animator = view.scaleAnimator(startRect, endRect)

        val startX = startRect.left + view.translationX
        val startY = startRect.top + view.translationY

        val moveXTo = endRect.left + Math.round(view.translationX)
        val moveYTo = endRect.top + Math.round(view.translationY)

        val moveXAnimator = ObjectAnimator.ofFloat(view, "x", startX, moveXTo.toFloat())
        val moveYAnimator = ObjectAnimator.ofFloat(view, "y", startY, moveYTo.toFloat())

        val animatorSet = AnimatorSet()
        val w = endView.width
        val h = endView.height

        val minRadius = Math.min(w, h) / 2f
        val maxRadius = Math.hypot((w / 2f).toDouble(), (h / 2f).toDouble()).toFloat()
        Log.d("★", "**>>>> Radius[$minRadius/$maxRadius/${view.cornerRadius}] w/h[$w/$h]")

//        val radiusAnimator = if(startRect.width() < endRect.width()) {
//            endView.cornerRadius = dpToPx(endView.context, 8f).toFloat()
//            ObjectAnimator.ofFloat<CircleRectView>(endView, RADIUS_PROPERTY, dpToPx(endView.context, 8f).toFloat(), dpToPx(endView.context, endCornerRadius).toFloat())
//        } else {
//            endView.cornerRadius = dpToPx(endView.context, endCornerRadius).toFloat()
//            ObjectAnimator.ofFloat<CircleRectView>(endView, RADIUS_PROPERTY, dpToPx(endView.context, endCornerRadius).toFloat(), dpToPx(endView.context, 8f).toFloat())
//        }

        val radiusAnimator = if(startRect.width() < endRect.width()) {
            endView.cornerRadiusTopLeft = dpToPx(endView.context, startCornerRadiusTopLeft).toFloat()
            endView.cornerRadiusTopRight = dpToPx(endView.context, startCornerRadiusTopRight).toFloat()
            endView.cornerRadiusBottomRight = dpToPx(endView.context, startCornerRadiusBottomRight).toFloat()
            endView.cornerRadiusBottomLeft = dpToPx(endView.context, startCornerRadiusBottomLeft).toFloat()
            val radiusAnimatorSet = AnimatorSet().apply {
                playTogether(
                        ObjectAnimator.ofFloat<CircleRectView>(endView, TOP_LEFT_RADIUS_PROPERTY, dpToPx(endView.context, startCornerRadiusTopLeft).toFloat(), dpToPx(endView.context, endCornerRadiusTopLeft).toFloat()),
                        ObjectAnimator.ofFloat<CircleRectView>(endView, TOP_RIGHT_RADIUS_PROPERTY, dpToPx(endView.context, startCornerRadiusTopRight).toFloat(), dpToPx(endView.context, endCornerRadiusTopRight).toFloat()),
                        ObjectAnimator.ofFloat<CircleRectView>(endView, BOTTOM_RIGHT_RADIUS_PROPERTY, dpToPx(endView.context, startCornerRadiusBottomRight).toFloat(), dpToPx(endView.context, endCornerRadiusBottomRight).toFloat()),
                        ObjectAnimator.ofFloat<CircleRectView>(endView, BOTTOM_LEFT_RADIUS_PROPERTY, dpToPx(endView.context, startCornerRadiusBottomLeft).toFloat(), dpToPx(endView.context, endCornerRadiusBottomRight).toFloat())
                )
            }
            radiusAnimatorSet
        } else {
            endView.cornerRadiusTopLeft = dpToPx(endView.context, endCornerRadiusTopLeft).toFloat()
            endView.cornerRadiusTopRight = dpToPx(endView.context, endCornerRadiusTopRight).toFloat()
            endView.cornerRadiusBottomRight = dpToPx(endView.context, endCornerRadiusBottomRight).toFloat()
            endView.cornerRadiusBottomLeft = dpToPx(endView.context, endCornerRadiusBottomLeft).toFloat()
            val radiusAnimatorSet = AnimatorSet().apply {
                playTogether(
                        ObjectAnimator.ofFloat<CircleRectView>(endView, TOP_LEFT_RADIUS_PROPERTY, dpToPx(endView.context, endCornerRadiusTopLeft).toFloat(), dpToPx(endView.context, startCornerRadiusTopLeft).toFloat()),
                        ObjectAnimator.ofFloat<CircleRectView>(endView, TOP_RIGHT_RADIUS_PROPERTY, dpToPx(endView.context, endCornerRadiusTopRight).toFloat(), dpToPx(endView.context, startCornerRadiusTopRight).toFloat()),
                        ObjectAnimator.ofFloat<CircleRectView>(endView, BOTTOM_RIGHT_RADIUS_PROPERTY, dpToPx(endView.context, endCornerRadiusBottomRight).toFloat(), dpToPx(endView.context, startCornerRadiusBottomRight).toFloat()),
                        ObjectAnimator.ofFloat<CircleRectView>(endView, BOTTOM_LEFT_RADIUS_PROPERTY, dpToPx(endView.context, endCornerRadiusBottomLeft).toFloat(), dpToPx(endView.context, startCornerRadiusBottomLeft).toFloat())
                )
            }
            radiusAnimatorSet
        }
        animatorSet.playTogether(animator)
        animatorSet.playTogether(moveXAnimator, moveYAnimator, radiusAnimator)

        return animatorSet
    }


//    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues): Animator? {
//        Log.d("★", "createAnimator start")
//        if (startValues == null || endValues == null) {
//            return null
//        }
//
//        if (startValues.view !is CircleRectView) {
//            return null
//        }
//
//        val animatorSet = AnimatorSet()
//        // not ViewAnimationUtils.createCircularReveal
//        val view = startValues.view as CircleRectView
//        val endView = endValues.view as CircleRectView
//
//        val startRect = startValues.values[BOUNDS] as Rect
//        val endRect = endValues.values[BOUNDS] as Rect
//
//        Log.d("★", "start view.cornerRadius[${view.cornerRadius}] height/width[${startRect.height()}/${startRect.width()}]")
//        Log.d("★", "end view.cornerRadius[${endView.cornerRadius}] height/width[${endRect.height()}/${endRect.width()}]")
//
//        val animator = view.scaleAnimator(startRect, endRect)
//
//        val startX = startRect.left + view.translationX
//        val startY = startRect.top + view.translationY
//
//        val moveXTo = endRect.left + Math.round(view.translationX)
//        val moveYTo = endRect.top + Math.round(view.translationY)
//
//        val moveXAnimator = ObjectAnimator.ofFloat(view, "x", startX, moveXTo.toFloat())
//        val moveYAnimator = ObjectAnimator.ofFloat(view, "y", startY, moveYTo.toFloat())
//
//
//        val endImageView = endValues.view as CircleRectView
//        val w = endImageView.width
//        val h = endImageView.height
//
//        val minRadius = Math.min(w, h) / 2f
//        val maxRadius = Math.hypot((w / 2f).toDouble(), (h / 2f).toDouble()).toFloat()
//        Log.d("★", "**>>>> Radius[$minRadius/$maxRadius] w/h[$w/$h]")
//
//        val radiusAnimator = if(startRect.width() < endRect.width()) {
//            endImageView.cornerRadius = minRadius
//            ObjectAnimator.ofFloat<CircleRectView>(endImageView, RADIUS_PROPERTY, minRadius, dpToPx(endImageView.context, endCornerRadius).toFloat())
//        } else {
//            endImageView.cornerRadius = dpToPx(endImageView.context, endCornerRadius).toFloat()
//            ObjectAnimator.ofFloat<CircleRectView>(endImageView, RADIUS_PROPERTY, dpToPx(endImageView.context, endCornerRadius).toFloat(), minRadius)
//        }
//
////        val centerX = startRect.centerX()
////        val centerY = startRect.centerY()
////        val finalRadius = Math.hypot(endRect.width().toDouble(), endRect.height().toDouble()).toFloat()
////        val circulerAnimator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY,
////                (startRect.width() / 2).toFloat(), finalRadius)
////        animatorSet.playTogether(animator)
////        animatorSet.playTogether(circularTransition)
//        animatorSet.playTogether(moveXAnimator, moveYAnimator, radiusAnimator)
//
//        //return NoPauseAnimator(animatorSet)
//        return animatorSet
//        // not ViewAnimationUtils.createCircularReveal
//
//
//
//        // ViewAnimationUtils.createCircularReveal↓↓↓↓↓
////        val startRect = startValues.values[BOUNDS] as Rect
////        val endRect = endValues.values[BOUNDS] as Rect
////
////        val view = startValues.view
//
////        val circularTransition: Animator
////        if (isReveal(startRect, endRect)) {
////            circularTransition = createReveal(view, startRect, endRect)
////        } else {
////            layout(startRect, view)
////
////            circularTransition = createConceal(view, startRect, endRect)
////            circularTransition.addListener(object : AnimatorListenerAdapter() {
////                override fun onAnimationEnd(animation: Animator) {
////                    view.outlineProvider = object : ViewOutlineProvider() {
////                       override fun getOutline(view: View, outline: Outline) {
////                            endRect.left -= view.left
////                            endRect.top -= view.top
////                            endRect.right -= view.left
////                            endRect.bottom -= view.top
////                            outline.setOval(endRect)
////                           view.clipToOutline = true
////                        }
////                    }
////                }
////            })
////        }
////
////        return NoPauseAnimator(circularTransition)
//        // ViewAnimationUtils.createCircularReveal↑↑↑↑↑
//
//
//
//
////        if (endValues == null || endValues.view !is SquaredRoundedImageView) {
////            return null
////        }
////
////        val startImageView = endValues.view as SquaredRoundedImageView
////        val endImageView = endValues.view as SquaredRoundedImageView
////        val w = startImageView.width
////        val h = startImageView.height
////
////        val minRadius = Math.min(w, h) / 2f
////        //val maxRadius = Math.hypot((w / 2f).toDouble(), (h / 2f).toDouble()).toFloat()
////        val maxRadius = startImageView.cornerRadius
////        Log.d("★", "Radius[$minRadius/$maxRadius] w/h[$w/$h]")
////
////        val animatorList = ArrayList<Animator>()
////
////        //endImageView.isOval = false
//////        endImageView.cornerRadius = minRadius
////        //animatorList.add(ObjectAnimator.ofFloat<SquaredRoundedImageView>(startImageView, RADIUS_PROPERTY, minRadius, maxRadius))
////
//////        endImageView.alpha = 0f
//////        animatorList.add(ObjectAnimator.ofFloat<SquaredRoundedImageView>(startImageView, ALPHA_PROPERTY, 0f, 255f))
////
////        animatorList.add(createConceal(startImageView, startRect, endRect))
////
////        val animator = AnimatorSet()
////        animator.playTogether(animatorList)
////        return animator
//
//    }

    private fun layout(startRect: Rect, view: View) {
        view.layout(startRect.left, startRect.top, startRect.right, startRect.bottom)
    }

    private fun createReveal(view: View, from: Rect, to: Rect): Animator {

        val centerX = from.centerX()
        val centerY = from.centerY()
        val finalRadius = Math.hypot(to.width().toDouble(), to.height().toDouble()).toFloat()

        return ViewAnimationUtils.createCircularReveal(view, centerX, centerY,
                (from.width() / 2).toFloat(), finalRadius)
    }

    private fun createConceal(view: View, from: Rect, to: Rect): Animator {

        val centerX = to.centerX()
        val centerY = to.centerY()
        val initialRadius = Math.hypot(from.width().toDouble(), from.height().toDouble()).toFloat()

        return ViewAnimationUtils.createCircularReveal(view, centerX, centerY,
                initialRadius, (to.width() / 2).toFloat())
    }

    private fun isReveal(startRect: Rect, endRect: Rect): Boolean {
        return startRect.width() < endRect.width()
    }

    fun dpToPx(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

}
