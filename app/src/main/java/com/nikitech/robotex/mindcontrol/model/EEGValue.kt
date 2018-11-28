package com.nikitech.robotex.mindcontrol.model

import com.choosemuse.libmuse.Eeg
import com.choosemuse.libmuse.MuseDataPacket
import org.json.JSONArray
import org.json.JSONObject

class EEGValue(val one: Double, val two: Double, val three: Double, val four: Double, val auxLeft: Double, val auxRight: Double) {

    /**
     * Intended command for the robot, used when uploading data
     */
    var command: String? = null

    fun getValue(enum: Eeg) : Double {
        return when (enum) {
            Eeg.EEG1 -> one
            Eeg.EEG2 -> two
            Eeg.EEG3 -> three
            Eeg.EEG4 -> four
            Eeg.AUX_LEFT -> auxLeft
            else -> auxRight
        }
    }

    companion object {
        @JvmStatic fun fromMuseDataPacket(packet: MuseDataPacket) : EEGValue {
            return EEGValue(
                packet.getEegChannelValue(Eeg.EEG1),
                packet.getEegChannelValue(Eeg.EEG2),
                packet.getEegChannelValue(Eeg.EEG3),
                packet.getEegChannelValue(Eeg.EEG4),
                packet.getEegChannelValue(Eeg.AUX_LEFT),
                packet.getEegChannelValue(Eeg.AUX_RIGHT)
            )
        }

        @JvmStatic fun toJson(list: List<EEGValue>) : JSONArray {
            val array = JSONArray()

            for (item in list) {
                array.put(item.toJson())
            }

            return array
        }

        @JvmStatic fun toPointList(list: List<EEGValue>, type: Eeg) : List<Pair<Double, Double>> {
            val result = mutableListOf<Pair<Double, Double>>()

            var index = 0.0
            for (item in list) {
                result.add(Pair(index, item.getValue(type)))
                index++
            }

            return result
        }
    }

    private fun toJson(): JSONObject {
        val item = JSONObject()

        item.put("eeg1", this.one)
        item.put("eeg2", this.two)
        item.put("eeg3", this.three)
        item.put("eeg4", this.four)
        item.put("command", this.command)

        return item
    }
}