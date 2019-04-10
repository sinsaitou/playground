package com.playground.transitions

//class ShapeViewTransition : Transition {
//
//    private val mStartShape: Int
//    val SHAPE_RECTANGLE = 0
//    val SHAPE_CIRCLE = 1
//
////    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
////        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
////        val shape = a.getInt(R.styleable.MusicCoverView_shape, CircleView.SHAPE_RECTANGLE)
////        a.recycle()
////        mStartShape = shape
////    }
//
//    constructor(shape: Int) {
//        mStartShape = shape
//    }
//
//    override fun getTransitionProperties(): Array<String> {
//        return sTransitionProperties
//    }
//
//    override fun captureStartValues(transitionValues: TransitionValues) {
//        // Add fake value to force calling of createAnimator method
//        captureValues(transitionValues, "start")
//    }
//
//    override fun captureEndValues(transitionValues: TransitionValues) {
//        // Add fake value to force calling of createAnimator method
//        captureValues(transitionValues, "end")
//    }
//
//    private fun captureValues(transitionValues: TransitionValues, value: Any) {
//        if (transitionValues.view is ImageView) {
//            transitionValues.values[PROPNAME_RADIUS] = value
//            transitionValues.values[PROPNAME_ALPHA] = value
//        }
//    }
//
//    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues, endValues: TransitionValues?): Animator? {
//
//        if (endValues == null || endValues.view !is ImageView) {
//            return null
//        }
//
//        val coverView = endValues.view as ImageView
//        val minRadius = coverView.getMinRadius()
//        val maxRadius = coverView.getMaxRadius()
//
//        val startRadius: Float
//        val endRadius: Float
//        val startTrackAlpha: Int
//        val endTrackAlpha: Int
//
//        if (mStartShape == MusicCoverView.SHAPE_RECTANGLE) {
//            startRadius = maxRadius
//            endRadius = minRadius
//            startTrackAlpha = MusicCoverView.ALPHA_TRANSPARENT
//            endTrackAlpha = MusicCoverView.ALPHA_OPAQUE
//        } else {
//            startRadius = minRadius
//            endRadius = maxRadius
//            startTrackAlpha = MusicCoverView.ALPHA_OPAQUE
//            endTrackAlpha = MusicCoverView.ALPHA_TRANSPARENT
//        }
//
//        val animatorList = ArrayList<Animator>()
//
//        coverView.setTransitionRadius(startRadius)
//        animatorList.add(ObjectAnimator.ofFloat<MusicCoverView>(coverView, RADIUS_PROPERTY, startRadius, endRadius))
//
//        coverView.setTransitionAlpha(startTrackAlpha)
//        animatorList.add(ObjectAnimator.ofInt<MusicCoverView>(coverView, ALPHA_PROPERTY, startTrackAlpha, endTrackAlpha))
//
//        val animator = AnimatorSet()
//        animator.playTogether(animatorList)
//        return animator
//    }
//
//    companion object {
//
//        private val PROPNAME_RADIUS = ShapeViewTransition::class.java.name + ":radius"
//        private val PROPNAME_ALPHA = ShapeViewTransition::class.java.name + ":alpha"
//        private val sTransitionProperties = arrayOf(PROPNAME_RADIUS, PROPNAME_ALPHA)
//
//        private val RADIUS_PROPERTY = object : Property<ImageView, Float>(Float::class.java, "radius") {
//            override fun set(view: ImageView, radius: Float?) {
//                view.setTransitionRadius(radius)
//            }
//
//            override fun get(view: ImageView): Float? {
//                return view.getTransitionRadius()
//            }
//        }
//
//        private val ALPHA_PROPERTY = object : Property<ImageView, Int>(Int::class.java, "alpha") {
//            override fun set(view: ImageView, alpha: Int?) {
//                view.setTransitionAlpha(alpha)
//            }
//
//            override fun get(view: ImageView): Int? {
//                return view.getTransitionAlpha()
//            }
//        }
//    }
//
//}
