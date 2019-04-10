package com.playground.view

//import com.playground.transitions.ShapeViewTransition

//class CircleView constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr), Animatable {
//
//    private val mStartRotateAnimator: ValueAnimator
//    private val mEndRotateAnimator: ValueAnimator
//    private val mCircleToRectTransition: Transition
//    private val mRectToCircleTransition: Transition
//
//    private val mTrackSize: Float
//    private val mTrackPaint: Paint
//    private var mTrackAlpha: Int = 0
//
//    private val mClipPath = Path()
//    private val mRectPath = Path()
//    private val mTrackPath = Path()
//
//    private var mIsMorphing: Boolean = false
//    private var mRadius = 0f
//
//    private var mCallbacks: Callbacks? = null
//    private var mShape: Int = 0
//
//    var shape: Int
//        get() = mShape
//        set(@Shape shape) {
//            if (shape != mShape) {
//                mShape = shape
//                setScaleType()
//                if (!isInLayout && !isLayoutRequested) {
//                    calculateRadius()
//                    resetPaths()
//                }
//            }
//        }
//
//    var trackColor: Int
//        get() = mTrackPaint.color
//        set(@ColorInt trackColor) {
//            if (trackColor != trackColor) {
//                val alpha = if (mShape == SHAPE_CIRCLE) ALPHA_OPAQUE else ALPHA_TRANSPARENT
//                mTrackPaint.color = trackColor
//                mTrackAlpha = Color.alpha(trackColor)
//                mTrackPaint.alpha = alpha * mTrackAlpha / ALPHA_OPAQUE
//                invalidate()
//            }
//        }
//
//    internal var transitionRadius: Float
//        get() = mRadius
//        set(radius) {
//            if (radius != mRadius) {
//                mRadius = radius
//                resetPaths()
//                invalidate()
//            }
//        }
//
//    internal var transitionAlpha: Int
//        get() = mTrackPaint.alpha * ALPHA_OPAQUE / mTrackAlpha
//        set(@IntRange(from = ALPHA_TRANSPARENT, to = ALPHA_OPAQUE) alpha) {
//            if (alpha != transitionAlpha) {
//                mTrackPaint.alpha = alpha * mTrackAlpha / ALPHA_OPAQUE
//                invalidate()
//            }
//        }
//
//    internal val minRadius: Float
//        get() {
//            val w = width
//            val h = height
//            return Math.min(w, h) / 2f
//        }
//
//    internal val maxRadius: Float
//        get() {
//            val w = width
//            val h = height
//            return Math.hypot((w / 2f).toDouble(), (h / 2f).toDouble()).toFloat()
//        }
//
//    init {
//
//        // TODO: Canvas.clipPath works wrong when running with hardware acceleration on Android N
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        }
//
//        val density = resources.displayMetrics.density
//        mTrackSize = TRACK_SIZE * density
//        mTrackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//        mTrackPaint.style = Paint.Style.STROKE
//        mTrackPaint.strokeWidth = TRACK_WIDTH * density
//
//        mStartRotateAnimator = ObjectAnimator.ofFloat<View>(this, View.ROTATION, 0, FULL_ANGLE)
//        mStartRotateAnimator.interpolator = LinearInterpolator()
//        mStartRotateAnimator.repeatCount = Animation.INFINITE
//        mStartRotateAnimator.duration = DURATION.toLong()
//        mStartRotateAnimator.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                val current = rotation
//                val target = if (current > HALF_ANGLE) FULL_ANGLE else 0 // Choose the shortest distance to 0 rotation
//                val diff = if (target > 0) FULL_ANGLE - current else current
//                mEndRotateAnimator.setFloatValues(current, target)
//                mEndRotateAnimator.duration = (DURATION_PER_DEGREES * diff).toInt().toLong()
//                mEndRotateAnimator.start()
//            }
//        })
//
//        mEndRotateAnimator = ObjectAnimator.ofFloat<View>(this@CircleView, View.ROTATION, 0)
//        mEndRotateAnimator.interpolator = LinearInterpolator()
//        mEndRotateAnimator.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                rotation = 0f
//                // isRunning method return true if it's called form here.
//                // So we need call from post method to get the right returning.
//                post {
//                    if (mCallbacks != null) {
//                        mCallbacks!!.onRotateEnd(this@CircleView)
//                    }
//                }
//            }
//        })
//
//        mRectToCircleTransition = ShapeViewTransition(SHAPE_RECTANGLE)
//        mRectToCircleTransition.addTarget(this)
//        mRectToCircleTransition.addListener(object : TransitionAdapter() {
//            override fun onTransitionStart(transition: Transition) {
//                mIsMorphing = true
//            }
//
//            override fun onTransitionEnd(transition: Transition) {
//                mIsMorphing = false
//                mShape = SHAPE_CIRCLE
//                if (mCallbacks != null) {
//                    mCallbacks!!.onMorphEnd(this@CircleView)
//                }
//            }
//        })
//
//        mCircleToRectTransition = ShapeViewTransition(SHAPE_CIRCLE)
//        mCircleToRectTransition.addTarget(this)
//        mCircleToRectTransition.addListener(object : TransitionAdapter() {
//            override fun onTransitionStart(transition: Transition) {
//                mIsMorphing = true
//            }
//
//            override fun onTransitionEnd(transition: Transition) {
//                mIsMorphing = false
//                mShape = SHAPE_RECTANGLE
//                if (mCallbacks != null) {
//                    mCallbacks!!.onMorphEnd(this@CircleView)
//                }
//            }
//        })
//
//        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
//        @Shape var shape = a.getInt(R.styleable.CircleView_shape, SHAPE_RECTANGLE)
//        @ColorInt var trackColor = a.getColor(R.styleable.CircleView_trackColor, TRACK_COLOR)
//        a.recycle()
//
//        shape = shape
//        trackColor = trackColor
//        setScaleType()
//    }
//
//    fun setCallbacks(callbacks: Callbacks) {
//        mCallbacks = callbacks
//    }
//
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        calculateRadius()
//        resetPaths()
//    }
//
//    private fun calculateRadius() {
//        if (SHAPE_CIRCLE == mShape) {
//            mRadius = minRadius
//        } else {
//            mRadius = maxRadius
//        }
//    }
//
//    private fun setScaleType() {
//        if (SHAPE_CIRCLE == mShape) {
//            scaleType = ImageView.ScaleType.CENTER_INSIDE
//        } else {
//            scaleType = ImageView.ScaleType.CENTER_CROP
//        }
//    }
//
//    private fun resetPaths() {
//
//        val w = width
//        val h = height
//        val centerX = w / 2f
//        val centerY = h / 2f
//
//        mClipPath.reset()
//        mClipPath.addCircle(centerX, centerY, mRadius, Path.Direction.CW)
//
//        val trackRadius = Math.min(w, h)
//        val trackCount = (trackRadius / mTrackSize).toInt()
//
//        mTrackPath.reset()
//        for (i in 3 until trackCount) {
//            mTrackPath.addCircle(centerX, centerY, trackRadius * (i / trackCount.toFloat()), Path.Direction.CW)
//        }
//
//        mRectPath.reset()
//        mRectPath.addRect(0f, 0f, w.toFloat(), h.toFloat(), Path.Direction.CW)
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        canvas.clipPath(mClipPath)
//        super.onDraw(canvas)
//        canvas.drawPath(mTrackPath, mTrackPaint)
//    }
//
//    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
//        // Don't need to consume the system window insets
//        return insets
//    }
//
//    fun morph() {
//        if (SHAPE_CIRCLE == mShape) {
//            morphToRect()
//        } else {
//            morphToCircle()
//        }
//    }
//
//    private fun morphToCircle() {
//        if (mIsMorphing) {
//            return
//        }
//        TransitionManager.beginDelayedTransition(parent as ViewGroup, mRectToCircleTransition)
//        scaleType = ImageView.ScaleType.CENTER_INSIDE
//    }
//
//    private fun morphToRect() {
//        if (mIsMorphing) {
//            return
//        }
//        TransitionManager.beginDelayedTransition(parent as ViewGroup, mCircleToRectTransition)
//        scaleType = ImageView.ScaleType.CENTER_CROP
//    }
//
//    override fun start() {
//        if (SHAPE_RECTANGLE == mShape) { // Only start rotate when shape is a circle
//            return
//        }
//        if (!isRunning) {
//            mStartRotateAnimator.start()
//        }
//    }
//
//    override fun stop() {
//        if (mStartRotateAnimator.isRunning) {
//            mStartRotateAnimator.cancel()
//        }
//    }
//
//    override fun isRunning(): Boolean {
//        return mStartRotateAnimator.isRunning || mEndRotateAnimator.isRunning || mIsMorphing
//    }
//
//    @IntDef(SHAPE_CIRCLE, SHAPE_RECTANGLE)
//    @Retention(RetentionPolicy.SOURCE)
//    annotation class Shape
//
//    interface Callbacks {
//        fun onMorphEnd(coverView: CircleView)
//
//        fun onRotateEnd(coverView: CircleView)
//    }
//
//    private class MorphTransition private constructor(shape: Int) : TransitionSet() {
//        init {
//            ordering = TransitionSet.ORDERING_TOGETHER
//            addTransition(ShapeViewTransition(shape))
//            addTransition(ChangeImageTransform())
//            addTransition(ChangeTransform())
//        }
//    }
//
//    /**
//     * [SavedState] methods
//     */
//
//    override fun onSaveInstanceState(): Parcelable? {
//        val superState = super.onSaveInstanceState()
//        val ss = SavedState(superState)
//        ss.shape = shape
//        ss.trackColor = trackColor
//        ss.isRotating = mStartRotateAnimator.isRunning
//        return ss
//    }
//
//    override fun onRestoreInstanceState(state: Parcelable) {
//        val ss = state as SavedState
//        super.onRestoreInstanceState(ss.getSuperState())
//        shape = ss.shape
//        trackColor = ss.trackColor
//        if (ss.isRotating) {
//            start()
//        }
//    }
//
//    class SavedState : AbsSavedState {
//
//        private val shape: Int
//        private val trackColor: Int
//        private val isRotating: Boolean
//
//        private constructor(`in`: Parcel, loader: ClassLoader) : super(`in`, loader) {
//            shape = `in`.readInt()
//            trackColor = `in`.readInt()
//            isRotating = `in`.readValue(Boolean::class.java.classLoader) as Boolean
//        }
//
//        private constructor(superState: Parcelable) : super(superState) {}
//
//        fun writeToParcel(@NonNull dest: Parcel, flags: Int) {
//            super.writeToParcel(dest, flags)
//            dest.writeInt(shape)
//            dest.writeInt(trackColor)
//            dest.writeValue(isRotating)
//        }
//
//        fun toString(): String {
//            return (CircleView::class.java.simpleName + "." + SavedState::class.java.simpleName + "{"
//                    + Integer.toHexString(System.identityHashCode(this))
//                    + " shape=" + shape + ", trackColor=" + trackColor + ", isRotating=" + isRotating + "}")
//        }
//
//        companion object {
//
//            val CREATOR = ParcelableCompat.newCreator(object : ParcelableCompatCreatorCallbacks<SavedState>() {
//                fun createFromParcel(parcel: Parcel, loader: ClassLoader): SavedState {
//                    return SavedState(parcel, loader)
//                }
//
//                fun newArray(size: Int): Array<SavedState> {
//                    return arrayOfNulls(size)
//                }
//            })
//        }
//    }
//
//    private open class TransitionAdapter : Transition.TransitionListener {
//
//        override fun onTransitionStart(transition: Transition) {}
//
//        override fun onTransitionEnd(transition: Transition) {}
//
//        override fun onTransitionCancel(transition: Transition) {}
//
//        override fun onTransitionPause(transition: Transition) {}
//
//        override fun onTransitionResume(transition: Transition) {}
//    }
//
//    companion object {
//
//        val SHAPE_RECTANGLE = 0
//        val SHAPE_CIRCLE = 1
//
//        internal val ALPHA_TRANSPARENT = 0
//        internal val ALPHA_OPAQUE = 255
//
//        private val TRACK_SIZE = 10f
//        private val TRACK_WIDTH = 1f
//        private val TRACK_COLOR = Color.parseColor("#56FFFFFF")
//
//        private val FULL_ANGLE = 360f
//        private val HALF_ANGLE = FULL_ANGLE / 2
//        private val DURATION = 2500
//        private val DURATION_PER_DEGREES = DURATION / FULL_ANGLE
//    }
//
//}
