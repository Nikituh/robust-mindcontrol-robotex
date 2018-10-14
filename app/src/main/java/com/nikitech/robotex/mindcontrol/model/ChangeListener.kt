package com.nikitech.robotex.mindcontrol.model

import com.choosemuse.libmuse.MuseListener
import com.nikitech.robotex.mindcontrol.MainActivity

internal class ChangeListener(private val context: MainActivity) : MuseListener() {

    override fun museListChanged() {
        context.museListChanged()
    }
}
