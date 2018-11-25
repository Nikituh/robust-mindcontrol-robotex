package com.nikitech.robotex.mindcontrol.networking

import org.jetbrains.anko.doAsync


class Networking {

    companion object {
        @JvmStatic
        val INSTANCE = Networking()

        private var ip = "192.168.43.129"
        private var base = "http://$ip:5000/"

        const val forward = "forward"
        const val stop = "stop"
        const val left = "left"
        const val right = "right"
        const val reverse = "reverse"
    }

    var delegate: NetworkingDelegate? = null

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
            try {
                val response = khttp.get(base + command, timeout=3.0)
                print(response)
            } catch (exception: Exception) {

                var message = exception.message

                if (message == null) {
                    message = "Unknown error"
                }
                delegate?.onError(message)
            }
        }
    }

    fun updateIpAddress(address: String) {
        ip = address
    }

    fun getIpAddress(): String {
        return ip
    }
}

interface NetworkingDelegate {
    fun onError(message: String)
}