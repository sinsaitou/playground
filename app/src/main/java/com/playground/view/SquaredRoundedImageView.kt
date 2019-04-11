package com.playground.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import com.makeramen.roundedimageview.RoundedImageView
import com.playground.R


class SquaredRoundedImageView : RoundedImageView {

  private var isSquared: Boolean = false

  constructor(context: Context?) : this(context, null)
  constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    var a: TypedArray? = null
    try {
      a = context?.obtainStyledAttributes(
        attrs, R.styleable.SquaredRoundedImageView, defStyleAttr, 0) ?: return

      isSquared = a.getBoolean(R.styleable.SquaredRoundedImageView_isSquared, false)
    } finally {
      a?.recycle()
    }
  }

  public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    if(isSquared) {
      super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    if(isSquared) {
      super.onSizeChanged(w, w, oldw, oldh)
    } else {
      super.onSizeChanged(w, h, oldw, oldh)
    }
  }

//  fun animator(startHeight: Int, startWidth: Int, endHeight: Int, endWidth: Int): Animator {
//    val animatorSet = AnimatorSet()
//
//    Log.d("★", "startHeight =" + startHeight
//            + ", startWidth =" + startWidth
//            + ", endHeight = " + endHeight
//            + " endWidth =" + endWidth)
//
//    val heightAnimator = ValueAnimator.ofInt(startHeight, endHeight)
//    val widthAnimator = ValueAnimator.ofInt(startWidth, endWidth)
//
//    heightAnimator.addUpdateListener { valueAnimator ->
//      val height = valueAnimator.animatedValue as Int
//      val layoutParams = layoutParams
//      layoutParams.height = height
//
//      setLayoutParams(layoutParams)
//      requestLayoutSupport()
//    }
//
//    widthAnimator.addUpdateListener { valueAnimator ->
//      val width = valueAnimator.animatedValue as Int
//      val layoutParams = layoutParams
//      layoutParams.width = width
//
//      setLayoutParams(layoutParams)
//      requestLayoutSupport()
//    }
//
//    val radiusAnimator = ValueAnimator.ofFloat(cornerRadius, DEFAULT_RADIUS)
//
//    radiusAnimator.interpolator = AccelerateInterpolator()
//    radiusAnimator.addUpdateListener { animator -> cornerRadius = animator.animatedValue as Float }
//
//    animatorSet.playTogether(heightAnimator, widthAnimator, radiusAnimator)
//
//    return animatorSet
//  }

  fun animator(startRect: Rect, endRect: Rect): Animator {
    val animatorSet = AnimatorSet()

    Log.d(CircleRectView::class.java.simpleName, "startHeight =" + startRect.height()
            + ", startWidth =" + startRect.width()
            + ", endHeight = " + endRect.height()
            + " endWidth =" + endRect.width())

    val heightAnimator = ValueAnimator.ofInt(startRect.height(), endRect.height())
    val widthAnimator = ValueAnimator.ofInt(startRect.width(), endRect.width())

    heightAnimator.addUpdateListener { valueAnimator ->
      val `val` = valueAnimator.animatedValue as Int
      val layoutParams = layoutParams
      layoutParams.height = `val`

      Log.d(CircleRectView::class.java.simpleName, "height updated =$`val`")

      setLayoutParams(layoutParams)
      requestLayoutSupport()
    }

    widthAnimator.addUpdateListener { valueAnimator ->
      val `val` = valueAnimator.animatedValue as Int
      val layoutParams = layoutParams
      layoutParams.width = `val`

      setLayoutParams(layoutParams)
      requestLayoutSupport()
    }

  //        val radiusAnimator = ValueAnimator.ofFloat(startRadius, endRadius)
  //
  //        radiusAnimator.interpolator = AccelerateInterpolator()
  //        radiusAnimator.addUpdateListener { animator -> cornerRadius = animator.animatedValue as Float }
    val circularReveal = if(startRect.width() < endRect.width()) {
      val centerX = startRect.centerX()
      val centerY = startRect.centerY()
      val finalRadius = Math.hypot(endRect.width().toDouble(), endRect.height().toDouble()).toFloat()

      ViewAnimationUtils.createCircularReveal(this, centerX, centerY,
              (startRect.width() / 2).toFloat(), finalRadius)
    } else {
      val centerX = endRect.centerX()
      val centerY = endRect.centerY()
      val initialRadius = Math.hypot(startRect.width().toDouble(), endRect.height().toDouble()).toFloat()

      ViewAnimationUtils.createCircularReveal(this, centerX, centerY,
              initialRadius, (endRect.width() / 2).toFloat())
    }


    animatorSet.playTogether(heightAnimator, widthAnimator, circularReveal)

    return animatorSet
  }

  fun animator2(startRect: Rect, endRect: Rect): Animator {
    val animatorSet = AnimatorSet()

    Log.d(CircleRectView::class.java.simpleName, "startHeight =" + startRect.height()
            + ", startWidth =" + startRect.width()
            + ", endHeight = " + endRect.height()
            + " endWidth =" + endRect.width())

    val heightAnimator = ValueAnimator.ofInt(startRect.height(), endRect.height())
    val widthAnimator = ValueAnimator.ofInt(startRect.width(), endRect.width())

    heightAnimator.addUpdateListener { valueAnimator ->
      val `val` = valueAnimator.animatedValue as Int
      val layoutParams = layoutParams
      layoutParams.height = `val`

      Log.d(CircleRectView::class.java.simpleName, "height updated =$`val`")

      setLayoutParams(layoutParams)
      requestLayoutSupport()
    }

    widthAnimator.addUpdateListener { valueAnimator ->
      val `val` = valueAnimator.animatedValue as Int
      val layoutParams = layoutParams
      layoutParams.width = `val`

      setLayoutParams(layoutParams)
      requestLayoutSupport()
    }

    val radiusAnimator = ValueAnimator.ofFloat(30f, 8f)

    radiusAnimator.interpolator = AccelerateInterpolator()
    radiusAnimator.addUpdateListener { animator -> cornerRadius = animator.animatedValue as Float }


    animatorSet.playTogether(heightAnimator, widthAnimator, radiusAnimator)

    return animatorSet
  }

  private fun requestLayoutSupport() {
    val parent = parent as View
    val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.EXACTLY)
    parent.measure(widthSpec, heightSpec)
    parent.layout(parent.left, parent.top, parent.right, parent.bottom)
  }


}
