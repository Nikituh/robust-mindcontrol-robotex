package com.nikitech.robotex.mindcontrol.model

import com.choosemuse.libmuse.Muse
import com.choosemuse.libmuse.MuseArtifactPacket
import com.choosemuse.libmuse.MuseDataListener
import com.choosemuse.libmuse.MuseDataPacket
import com.nikitech.robotex.mindcontrol.MainActivity
import java.lang.ref.WeakReference

class DataListener(private val context: MainActivity) : MuseDataListener() {

    override fun receiveMuseDataPacket(p: MuseDataPacket, muse: Muse) {
        context.receiveMuseDataPacket(p, muse)
    }

    override fun receiveMuseArtifactPacket(p: MuseArtifactPacket, muse: Muse) {
        context.receiveMuseArtifactPacket(p, muse)
    }
}
