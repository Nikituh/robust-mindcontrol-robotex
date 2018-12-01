package com.nikitech.robotex.mindcontrol.model

enum class Command(val string: String) {
    FORWARD("forward"),
    STOP("stop"),
    LEFT("left"),
    RIGHT("right"),
    REVERSE("reverse"),
    NONE("none  ")
}