package com.playground.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Half.toFloat
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.playground.R

class CircleRectView : AppCompatImageView {

    var cornerRadius: Float = 0f
    var cornerRadiusTopLeft: Float = 0f
    var cornerRadiusTopRight: Float = 0f
    var cornerRadiusBottomLeft: Float = 0f
    var cornerRadiusBottomRight: Float = 0f
    private var isSquared: Boolean = false
    private var isOval: Boolean = false

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

        cornerRadius = a.getDimensionPixelSize(R.styleable.CircleRectView_circleRadius, -1).toFloat()
        cornerRadiusTopLeft = a.getDimensionPixelSize(R.styleable.CircleRectView_circleRadius, 0).toFloat()
        cornerRadiusTopRight = a.getDimensionPixelSize(R.styleable.CircleRectView_circleRadius, 0).toFloat()
        cornerRadiusBottomRight = a.getDimensionPixelSize(R.styleable.CircleRectView_circleRadius, 0).toFloat()
        cornerRadiusBottomLeft = a.getDimensionPixelSize(R.styleable.CircleRectView_circleRadius, 0).toFloat()

        if (cornerRadius >= 0f) {
            cornerRadiusTopLeft = cornerRadius
            cornerRadiusTopRight = cornerRadius
            cornerRadiusBottomRight = cornerRadius
            cornerRadiusBottomLeft = cornerRadius
        } else {
            cornerRadius = 0f
        }

        isSquared = a.getBoolean(R.styleable.CircleRectView_circleIsSquared, false)
        isOval = a.getBoolean(R.styleable.CircleRectView_isOval, false)
        clipPath = Path()

        Log.d("★", "** CircleRectView init cornerRadiusTopLeft[" + cornerRadiusTopLeft + "] cornerRadiusTopRight[" + cornerRadiusTopRight + "] cornerRadiusBottomRight[" + cornerRadiusBottomRight + "] cornerRadiusBottomLeft[" + cornerRadiusBottomLeft + "] isSquared[" + isSquared +"] isOval[" + isOval + "]")

        a.recycle()
    }

    fun scaleAnimator(startRect: Rect, endRect: Rect): Animator {
        val animatorSet = AnimatorSet()

        val heightAnimator = ValueAnimator.ofInt(startRect.height(), endRect.height())
        val widthAnimator = ValueAnimator.ofInt(startRect.width(), endRect.width())

        heightAnimator.addUpdateListener { valueAnimator ->
            val height = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.height = height

            setLayoutParams(layoutParams)
            requestLayoutSupport()
        }

        widthAnimator.addUpdateListener { valueAnimator ->
            val width = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.width = width

            setLayoutParams(layoutParams)
            requestLayoutSupport()
        }
        animatorSet.playTogether(heightAnimator, widthAnimator)

        return animatorSet
    }

    /**
     * this needed because of that somehow [.onSizeChanged] NOT CALLED when requestLayout while activity transition end is running
     */
    fun requestLayoutSupport() {
        val parent = parent as View
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.EXACTLY)
        parent.measure(widthSpec, heightSpec)
        parent.layout(parent.left, parent.top, parent.right, parent.bottom)
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isSquared) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    public override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (isSquared) {
            super.onSizeChanged(w, w, oldw, oldh)
            bitmapRect = RectF(0f, 0f, w.toFloat(), w.toFloat())
        } else {
            super.onSizeChanged(w, h, oldw, oldh)
            bitmapRect = RectF(0f, 0f, w.toFloat(), h.toFloat())
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null || clipPath == null) {
            return
        }
        if (width == 0 || height == 0) {
            return
        }

        clipPath!!.reset()

        Log.d("★", "== CircleRectView cornerRadius[" + cornerRadius + "] cornerRadiusTopLeft[" + cornerRadiusTopLeft + "] cornerRadiusTopRight[" + cornerRadiusTopRight + "] cornerRadiusBottomRight[" + cornerRadiusBottomRight + "] cornerRadiusBottomLeft[" + cornerRadiusBottomLeft + "]")
        if (isOval) {
            val radius = Math.min(width, height) / 2f
            clipPath!!.addRoundRect(bitmapRect, radius, radius, Path.Direction.CW)
        } else {
            //val rect = RectF(0f, 0f, height.toFloat() / 2 , width.toFloat() / 2)
            //clipPath!!.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
//            clipPath!!.addRoundRect(bitmapRect, cornerRadius, cornerRadius, Path.Direction.CW)

            clipPath!!.addRoundRect(bitmapRect,
                    floatArrayOf(cornerRadiusTopLeft, cornerRadiusTopLeft,
                            cornerRadiusTopRight, cornerRadiusTopRight,
                            cornerRadiusBottomRight, cornerRadiusBottomRight,
                            cornerRadiusBottomLeft, cornerRadiusBottomLeft
                    ), Path.Direction.CW)
        }

        canvas.clipPath(clipPath!!)
        super.onDraw(canvas)
    }
}










//    private var circleRadius: Int = 0
//    var cornerRadius: Float = 0.toFloat()
//    private val eps = 0.001f
//
//    private var bitmapRect: RectF? = null
//    private var clipPath: Path? = null
//
//    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
//
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr) {
//        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CircleRectView, defStyleAttr, defStyleRes)
//        init(a)
//    }
//
//    private fun init(a: TypedArray) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//        }
//
//        if (a.hasValue(R.styleable.CircleRectView_circleRadius)) {
//            circleRadius = a.getDimensionPixelSize(R.styleable.CircleRectView_circleRadius, 0)
//            cornerRadius = circleRadius.toFloat()
//            Log.d("★", "*****>>>> circleRadius[$circleRadius]")
//        }
//
//        clipPath = Path()
//
//        a.recycle()
//    }
//
//
//    fun animator(startRect: Rect, endRect: Rect): Animator {
//        val animatorSet = AnimatorSet()
//
//        val heightAnimator = ValueAnimator.ofInt(startRect.height(), endRect.height())
//        val widthAnimator = ValueAnimator.ofInt(startRect.width(), endRect.width())
//
//        heightAnimator.addUpdateListener { valueAnimator ->
//            val `val` = valueAnimator.animatedValue as Int
//            val layoutParams = layoutParams
//            layoutParams.height = `val`
//
//            setLayoutParams(layoutParams)
//            requestLayoutSupport()
//        }
//
//        widthAnimator.addUpdateListener { valueAnimator ->
//            val `val` = valueAnimator.animatedValue as Int
//            val layoutParams = layoutParams
//            layoutParams.width = `val`
//
//            setLayoutParams(layoutParams)
//            requestLayoutSupport()
//        }
//
//        val radiusAnimator = ValueAnimator.ofFloat(30f, 8f)
//        radiusAnimator.interpolator = AccelerateInterpolator()
//
//        radiusAnimator.addUpdateListener { animator -> cornerRadius = animator.animatedValue as Float }
//
////        val circularReveal = if(startRect.width() < endRect.width()) {
////            val centerX = startRect.centerX()
////            val centerY = startRect.centerY()
////            val finalRadius = Math.hypot(endRect.width().toDouble(), endRect.height().toDouble()).toFloat()
////
////            ViewAnimationUtils.createCircularReveal(this, centerX, centerY,
////                    (startRect.width() / 2).toFloat(), finalRadius)
////        } else {
////            val centerX = endRect.centerX()
////            val centerY = endRect.centerY()
////            val initialRadius = Math.hypot(startRect.width().toDouble(), endRect.height().toDouble()).toFloat()
////
////            ViewAnimationUtils.createCircularReveal(this, centerX, centerY,
////                    initialRadius, (endRect.width() / 2).toFloat())
////        }
//
//        animatorSet.playTogether(heightAnimator, widthAnimator)
//
//        return animatorSet
//    }
//
//    /**
//     * this needed because of that somehow [.onSizeChanged] NOT CALLED when requestLayout while activity transition end is running
//     */
//    private fun requestLayoutSupport() {
//        val parent = parent as View
//        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
//        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.EXACTLY)
//        parent.measure(widthSpec, heightSpec)
//        parent.layout(parent.left, parent.top, parent.right, parent.bottom)
//    }
//
//    public override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        bitmapRect = RectF(0f, 0f, w.toFloat(), h.toFloat())
//    }
//
//    override fun onDraw(canvas: Canvas) {
//
//        val drawable = drawable ?: return
//
//        if (width == 0 || height == 0) {
//            return
//        }
//
//        clipPath!!.reset()
//        clipPath!!.addRoundRect(bitmapRect, cornerRadius, cornerRadius, Path.Direction.CW)
//
//        canvas.clipPath(clipPath!!)
//        super.onDraw(canvas)
//    }
//
//    companion object {
//        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
//    }

//}
