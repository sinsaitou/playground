package com.playground.transitions

import android.transition.TransitionSet
import com.playground.R

class MasterTransitionSet : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        addTransition(CustomTransition(30f).addTarget(R.id.item_image))
    }
}
