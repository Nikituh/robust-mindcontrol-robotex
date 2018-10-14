package com.nikitech.robotex.mindcontrol.networking

import org.jetbrains.anko.doAsync


class Networking {

    companion object {
        @JvmStatic
        val INSTANCE = Networking()

        const val base = "http://192.168.0.168:5000/"

        const val forward = "forward"
        const val stop = "stop"
        const val left = "left"
        const val right = "right"
        const val reverse = "reverse"
    }


    fun forward() {
        get(forward)
    }

    fun stop() {
        get(stop)
    }

    fun left() {
        get(left)
    }

    fun right() {
        get(right)
    }

    fun reverse() {
        get(reverse)
    }

    fun get(command: String) {
        doAsync {
            khttp.get(base + command)
        }
    }
}