package com.playground.transitions

import android.transition.*
import com.playground.R

class CustomTransitionSet : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        addTransition(CustomTransition().addTarget(R.id.card_image))
        addTransition(ChangeTransform())
        addTransition(ChangeImageTransform())
        addTransition(ChangeBounds())
        addTransition(ChangeClipBounds())
    }
}
