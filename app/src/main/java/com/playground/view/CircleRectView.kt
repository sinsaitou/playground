package com.playground.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.playground.R


class CircleRectView : AppCompatImageView {

    private var circleRadius: Int = 0
    var cornerRadius: Float = 0.toFloat()
    private val eps = 0.001f

    private var bitmapRect: RectF? = null
    private var clipPath: Path? = null

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CircleRectView, defStyleAttr, defStyleRes)
        init(a)
    }

    private fun init(a: TypedArray) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

        if (a.hasValue(R.styleable.CircleRectView_circleRadius)) {
            circleRadius = a.getDimensionPixelSize(R.styleable.CircleRectView_circleRadius, 0)
            cornerRadius = circleRadius.toFloat()
            Log.d("★", "*****>>>> circleRadius[$circleRadius]")
        }

        clipPath = Path()

        a.recycle()
    }


    fun animator(startRect: Rect, endRect: Rect): Animator {
        val animatorSet = AnimatorSet()

        val heightAnimator = ValueAnimator.ofInt(startRect.height(), endRect.height())
        val widthAnimator = ValueAnimator.ofInt(startRect.width(), endRect.width())

        heightAnimator.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.height = `val`

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

//        val circularReveal = if(startRect.width() < endRect.width()) {
//            val centerX = startRect.centerX()
//            val centerY = startRect.centerY()
//            val finalRadius = Math.hypot(endRect.width().toDouble(), endRect.height().toDouble()).toFloat()
//
//            ViewAnimationUtils.createCircularReveal(this, centerX, centerY,
//                    (startRect.width() / 2).toFloat(), finalRadius)
//        } else {
//            val centerX = endRect.centerX()
//            val centerY = endRect.centerY()
//            val initialRadius = Math.hypot(startRect.width().toDouble(), endRect.height().toDouble()).toFloat()
//
//            ViewAnimationUtils.createCircularReveal(this, centerX, centerY,
//                    initialRadius, (endRect.width() / 2).toFloat())
//        }

        animatorSet.playTogether(heightAnimator, widthAnimator)

        return animatorSet
    }

    /**
     * this needed because of that somehow [.onSizeChanged] NOT CALLED when requestLayout while activity transition end is running
     */
    private fun requestLayoutSupport() {
        val parent = parent as View
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.EXACTLY)
        parent.measure(widthSpec, heightSpec)
        parent.layout(parent.left, parent.top, parent.right, parent.bottom)
    }

    public override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //This event-method provides the real dimensions of this custom view.

        //        Log.d("size changed", "w = " + w + " h = " + h);

        bitmapRect = RectF(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {

        val drawable = drawable ?: return

        if (width == 0 || height == 0) {
            return
        }

        clipPath!!.reset()
        clipPath!!.addRoundRect(bitmapRect, cornerRadius, cornerRadius, Path.Direction.CW)

        canvas.clipPath(clipPath!!)
        super.onDraw(canvas)
    }

    companion object {
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
    }

}
