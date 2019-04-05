package com.playground.transitions

import android.transition.*

class CustomTransitionSet : TransitionSet() {
    init {
        addTransition(ChangeBounds())
        addTransition(ChangeTransform())
        addTransition(ChangeClipBounds())
        addTransition(ChangeImageTransform())
        addTransition(CustomTransition())
    }
}
