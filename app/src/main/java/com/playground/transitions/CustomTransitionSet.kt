package com.playground.transitions

import android.transition.*
import com.playground.R

class CustomTransitionSet : TransitionSet() {
    init {
        addTransition(ChangeBounds())
        addTransition(ChangeTransform())
        addTransition(ChangeClipBounds())
        addTransition(ChangeImageTransform())
        addTransition(CustomTransition().addTarget(R.id.card_image))
    }
}
