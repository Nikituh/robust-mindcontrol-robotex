package com.nikitech.robotex.mindcontrol.model

import com.choosemuse.libmuse.Eeg
import com.choosemuse.libmuse.MuseDataPacket

class EEGValue(val one: Double, val two: Double, val three: Double, val four: Double, val auxLeft: Double, val auxRight: Double) {

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