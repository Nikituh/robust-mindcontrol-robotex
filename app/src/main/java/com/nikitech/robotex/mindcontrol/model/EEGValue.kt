package com.nikitech.robotex.mindcontrol.model

import com.choosemuse.libmuse.Eeg
import com.choosemuse.libmuse.MuseDataPacket

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
            Eeg.EEG4 -> three
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
    }
}