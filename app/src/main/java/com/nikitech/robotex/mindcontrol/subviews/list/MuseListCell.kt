package com.nikitech.robotex.mindcontrol.subviews.list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.choosemuse.libmuse.Muse
import mobile.ecofleet.com.common.base.BaseView
import mobile.ecofleet.com.common.base.setFrame
import mobile.ecofleet.com.common.base.toBold

class MuseListCell(context: Context) : BaseView(context) {

    var muse: Muse? = null

    val name = TextView(context)
    val rssi = TextView(context)
    val address = TextView(context)

    init {

        setBackgroundColor(Color.rgb(255, 255, 255))

        name.toBold()
        addView(name)

        addView(rssi)

        addView(address)
    }

    override fun layoutSubviews() {

        val padding = (10 * getDensity()).toInt()

        var x = padding
        var y = padding
        var w = frame.width - 2 * padding - 20
        var h = (20 * getDensity()).toInt()

        name.setFrame(x, y, w, h)

        y += h

        rssi.setFrame(x, y, w, h)

        y += h

        address.setFrame(x, y, w, h)
    }

    @SuppressLint("SetTextI18n")
    fun update(muse: Muse, isSelected: Boolean) {

        this.muse = muse

        if (isSelected) {
            setBorderColor(2, Color.rgb((2 * getDensity()).toInt(), 150, 0))
        } else {
            removeBorder()
        }

        name.text = muse.name

        if (muse.rssi == 0.0) {
            rssi.text = "RSSI Unavailable"
        } else {
            rssi.text = "RSSI: " + muse.rssi.toString()
        }

        address.text = "Address: " + muse.macAddress.toString()
    }
}