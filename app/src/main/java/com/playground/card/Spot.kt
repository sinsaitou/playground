package com.playground.card

import java.io.Serializable

data class Spot(
        val id: Long = counter++,
        val name: String,
        val city: String,
        val url: String
): Serializable {
    companion object {
        private var counter = 0L
    }
}
