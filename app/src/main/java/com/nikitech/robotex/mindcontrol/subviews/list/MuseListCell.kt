package com.nikitech.robotex.mindcontrol.subviews.list

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.choosemuse.libmuse.Muse
import mobile.ecofleet.com.common.base.BaseView
import mobile.ecofleet.com.common.base.setFrame

class MuseListCell(context: Context) : BaseView(context) {

    val name = TextView(context)

    var chosen = false

    init {
        addView(name)
    }

    override fun layoutSubviews() {
        name.setFrame(0, 0, frame.width, frame.height)
    }

    fun update(muse: Muse, isSelected: Boolean) {
        if (isSelected) {
            setBackgroundColor(Color.GREEN)
        } else {
            setBackgroundColor(Color.rgb(245, 245, 245))
        }
        name.text = muse.name
    }
}