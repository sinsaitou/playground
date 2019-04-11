package com.playground.transitions

import android.transition.TransitionSet
import com.playground.R

class CustomTransitionSet : TransitionSet() {
    init {
        ordering = ORDERING_SEQUENTIAL
        addTransition(CustomTransition().addTarget(R.id.card_image))
        //addTransition(CoverViewTransition(1).addTarget(R.id.card_image))
        //addTransition(ChangeBounds().addTarget(R.id.card_image))
        //addTransition(ChangeTransform().addTarget(R.id.card_image))
        //addTransition(ChangeClipBounds().addTarget(R.id.card_image))
        //addTransition(ChangeImageTransform().addTarget(R.id.card_image))
    }
}
