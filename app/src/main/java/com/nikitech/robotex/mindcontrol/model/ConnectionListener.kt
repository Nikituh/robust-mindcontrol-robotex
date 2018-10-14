package com.nikitech.robotex.mindcontrol.model

import com.choosemuse.libmuse.Muse
import com.choosemuse.libmuse.MuseConnectionListener
import com.choosemuse.libmuse.MuseConnectionPacket
import com.nikitech.robotex.mindcontrol.MainActivity

class ConnectionListener(private val context: MainActivity) : MuseConnectionListener() {

    override fun receiveMuseConnectionPacket(p: MuseConnectionPacket, muse: Muse) {
        context.receiveMuseConnectionPacket(p, muse)
    }
}