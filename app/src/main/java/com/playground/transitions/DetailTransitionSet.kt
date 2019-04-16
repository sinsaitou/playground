package com.playground.transitions

import android.transition.TransitionSet
import com.playground.R

class DetailTransitionSet : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        addTransition(CustomTransition().addTarget(R.id.detail_image))
    }
}
