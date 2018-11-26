package com.nikitech.robotex.mindcontrol.networking

import com.nikitech.robotex.mindcontrol.model.Command
import com.nikitech.robotex.mindcontrol.model.EEGValue
import org.jetbrains.anko.doAsync


class Networking {

    companion object {
        @JvmStatic
        val INSTANCE = Networking()

        private var ip = "192.168.43.129"
        private var base = "http://$ip:5000/"

        const val storageUrl = "http://prototypes.nikitech.eu/mindcontrol/store_command.php"
    }

    var delegate: NetworkingDelegate? = null

    fun forward() {
        get(Command.FORWARD.string)
    }

    fun stop() {
        get(Command.STOP.string)
    }

    fun left() {
        get(Command.LEFT.string)
    }

    fun right() {
        get(Command.RIGHT.string)
    }

    fun reverse() {
        get(Command.REVERSE.string)
    }

    fun get(command: String) {
        doAsync {
            try {
                val response = khttp.get(base + command, timeout=3.0)
                print(response)
            } catch (exception: Exception) {
                callException(exception)
            }
        }
    }

    fun post(data: List<EEGValue>) {
        doAsync {
            try {
                val json = EEGValue.toJson(data)
                val response = khttp.post(storageUrl, data = json)
                val string = response.text
                val array = response.jsonArray
                println(response)
            } catch (exception: Exception) {
                callException(exception)
            }
        }
    }

    private fun callException(exception: Exception) {

        var message = exception.message

        if (message == null) {
            message = "Unknown error"
        }
        delegate?.onError(message)
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