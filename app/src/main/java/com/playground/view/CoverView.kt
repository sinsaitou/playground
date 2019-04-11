package com.playground.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.os.Build
import android.transition.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ImageView
import androidx.annotation.IntDef
import com.playground.R
import com.playground.transitions.CoverViewTransition
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class CoverView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {

    private val mCircleToRectTransition: Transition
    private val mRectToCircleTransition: Transition

    private val mClipPath = Path()
    private val mRectPath = Path()

    private var mIsMorphing: Boolean = false
    private var mRadius = 0f

    private var mCallbacks: Callbacks? = null
    private var mShape: Int = 0

    var shape: Int
        get() = mShape
        set(@Shape shape) {
            if (shape != mShape) {
                mShape = shape
                setScaleType()
                if (!isInLayout && !isLayoutRequested) {
                    calculateRadius()
                    resetPaths()
                }
            }
        }


    var transitionRadius: Float
        get() = mRadius
        set(radius) {
            if (radius != mRadius) {
                mRadius = radius
                resetPaths()
                invalidate()
            }
        }

    val minRadius: Float
        get() {
            val w = width
            val h = height
            return Math.min(w, h) / 2f
        }

    val maxRadius: Float
        get() {
            val w = width
            val h = height
            return Math.hypot((w / 2f).toDouble(), (h / 2f).toDouble()).toFloat()
        }

    init {
        // TODO: Canvas.clipPath works wrong when running with hardware acceleration on Android N
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
        }

        mRectToCircleTransition = MorphTransition(SHAPE_RECTANGLE)
        mRectToCircleTransition.addTarget(this)
        mRectToCircleTransition.addListener(object : TransitionAdapter() {
            override fun onTransitionStart(transition: Transition) {
                mIsMorphing = true
            }

            override fun onTransitionEnd(transition: Transition) {
                mIsMorphing = false
                mShape = SHAPE_CIRCLE
                if (mCallbacks != null) {
                    mCallbacks!!.onMorphEnd(this@CoverView)
                }
            }

            override fun onTransitionCancel(transition: Transition) {
                super.onTransitionCancel(transition)
            }

            override fun onTransitionPause(transition: Transition) {
                super.onTransitionPause(transition)
            }

            override fun onTransitionResume(transition: Transition) {
                super.onTransitionResume(transition)
            }
        })

        mCircleToRectTransition = MorphTransition(SHAPE_CIRCLE)
        mCircleToRectTransition.addTarget(this)
        mCircleToRectTransition.addListener(object : TransitionAdapter() {
            override fun onTransitionStart(transition: Transition) {
                mIsMorphing = true
            }

            override fun onTransitionEnd(transition: Transition) {
                mIsMorphing = false
                mShape = SHAPE_RECTANGLE
                if (mCallbacks != null) {
                    mCallbacks!!.onMorphEnd(this@CoverView)
                }
            }

            override fun onTransitionCancel(transition: Transition) {
                super.onTransitionCancel(transition)
            }

            override fun onTransitionPause(transition: Transition) {
                super.onTransitionPause(transition)
            }

            override fun onTransitionResume(transition: Transition) {
                super.onTransitionResume(transition)
            }
        })

        val a = context.obtainStyledAttributes(attrs, R.styleable.CoverView)
        @Shape var shape = a.getInt(R.styleable.CoverView_shape, SHAPE_RECTANGLE)
        a.recycle()

        shape = shape
        setScaleType()
    }

    fun setCallbacks(callbacks: Callbacks) {
        mCallbacks = callbacks
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateRadius()
        resetPaths()
    }

    private fun calculateRadius() {
        if (SHAPE_CIRCLE == mShape) {
            mRadius = minRadius
        } else {
            mRadius = maxRadius
        }
    }

    private fun setScaleType() {
        if (SHAPE_CIRCLE == mShape) {
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        } else {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun resetPaths() {

        val w = width
        val h = height
        val centerX = w / 2f
        val centerY = h / 2f

        mClipPath.reset()
        mClipPath.addCircle(centerX, centerY, mRadius, Path.Direction.CW)

        mRectPath.reset()
        mRectPath.addRect(0f, 0f, w.toFloat(), h.toFloat(), Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(mClipPath)
        super.onDraw(canvas)
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        // Don't need to consume the system window insets
        return insets
    }

    fun morph() {
        if (SHAPE_CIRCLE == mShape) {
            morphToRect()
        } else {
            morphToCircle()
        }
    }

    private fun morphToCircle() {
        if (mIsMorphing) {
            return
        }
        TransitionManager.beginDelayedTransition(parent as ViewGroup, mRectToCircleTransition)
        scaleType = ImageView.ScaleType.CENTER_INSIDE
    }

    private fun morphToRect() {
        if (mIsMorphing) {
            return
        }
        TransitionManager.beginDelayedTransition(parent as ViewGroup, mCircleToRectTransition)
        scaleType = ImageView.ScaleType.CENTER_CROP
    }


    @IntDef(SHAPE_CIRCLE, SHAPE_RECTANGLE)
    @Retention(RetentionPolicy.SOURCE)
    annotation class Shape

    interface Callbacks {
        fun onMorphEnd(coverView: CoverView)

        fun onRotateEnd(coverView: CoverView)
    }

    class MorphTransition constructor(shape: Int) : TransitionSet() {
        init {
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(CoverViewTransition(shape))
            addTransition(ChangeImageTransform())
            addTransition(ChangeTransform())
        }
    }

    /**
     * [SavedState] methods
     */


    open class TransitionAdapter : Transition.TransitionListener {

        override fun onTransitionStart(transition: Transition) {}

        override fun onTransitionEnd(transition: Transition) {}

        override fun onTransitionCancel(transition: Transition) {}

        override fun onTransitionPause(transition: Transition) {}

        override fun onTransitionResume(transition: Transition) {}
    }

    companion object {

        const val SHAPE_RECTANGLE = 0
        const val SHAPE_CIRCLE = 1

        const val ALPHA_TRANSPARENT = 0L
        const val ALPHA_OPAQUE = 255L

        private const val FULL_ANGLE = 360f
        private const val HALF_ANGLE = FULL_ANGLE / 2
        private const val DURATION = 2500
        private const val DURATION_PER_DEGREES = DURATION / FULL_ANGLE
    }

}
