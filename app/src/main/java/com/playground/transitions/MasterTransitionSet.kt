package com.playground.transitions

import android.transition.*
import com.playground.R

class MasterTransitionSet : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        addTransition(CustomTransition(12f).addTarget(R.id.item_image))
        addTransition(ChangeBounds().addTarget(R.id.detail_image))
        addTransition(ChangeTransform().addTarget(R.id.detail_image))
        addTransition(ChangeClipBounds().addTarget(R.id.detail_image))
        addTransition(ChangeImageTransform().addTarget(R.id.detail_image))
    }
}
