package com.playground.transitions

import android.transition.TransitionSet
import com.playground.R

class CardTransitionSet : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        addTransition(CustomTransition(8f).addTarget(R.id.card_image))
    }
}
