package com.nikitech.robotex.mindcontrol.subviews.list

import android.content.Context
import android.widget.TextView
import com.choosemuse.libmuse.Muse
import mobile.ecofleet.com.common.base.BaseView
import mobile.ecofleet.com.common.base.setFrame

class MuseListCell(context: Context) : BaseView(context) {

    val name = TextView(context)

    init {

    }

    override fun layoutSubviews() {
        name.setFrame(0, 0, frame.width, frame.height)
    }

    fun update(muse: Muse) {
        name.text = muse.name
    }
}