package com.nikitech.robotex.mindcontrol.subviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.choosemuse.libmuse.Muse
import com.nikitech.robotex.mindcontrol.R
import com.nikitech.robotex.mindcontrol.subviews.list.MuseListAdapter
import mobile.ecofleet.com.common.base.BaseScrollView
import mobile.ecofleet.com.common.base.BaseView
import mobile.ecofleet.com.common.base.setFrame
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.runOnUiThread

class ConnectContainer(context: Context) : BaseScrollView(context) {

    private val header = TextView(context)
    private val subheader = TextView(context)

    val list = ListView(context)
    val adapter = MuseListAdapter(context)

    val connect = TextButton(context, "Connect")

    val refresh = ImageButton(context, R.drawable.ic_refresh_black_24dp)

    private val robotHeader = TextView(context)

    private val addressHeader = TextView(context)
    val addressField = EditText(context)

    init {

        header.text =
                "If you don't see a list of Brain scanners, " +
                "tap the button on the top right of the screen. " +
                "\nTo connect to a device, click on one."
        header.gravity = Gravity.CENTER
        addView(header)

        subheader.text =
                "Remember to have both bluetooth and location enabled in your settings." +
                "\nThere are on extra hard checks for this"
        subheader.gravity = Gravity.CENTER
        addView(subheader)

        list.setBackgroundColor(Color.rgb(245, 245, 245))
        addView(list)

        connect.hide()
        connect.setBackgroundColor(Color.rgb(0, 180, 180))
        connect.text.setTextColor(Color.WHITE)

        addView(connect)

        refresh.setBackgroundColor(Color.rgb(0, 180, 0))
        addView(refresh)

        list.adapter = adapter

        robotHeader.text =
                "Go Tethering & portable hotspot in your Settings. " +
                "Enable Portable hotspot with the SSID 'musework' and password 'matahaninternetti. " +
                "Then restart your Raspberry Pi'"
        robotHeader.gravity = Gravity.CENTER
        addView(robotHeader)

        addressHeader.text = "Change the ip address of your robot:"
        addView(addressHeader)

        addView(addressField)

        isFocusableInTouchMode = true

        addressField.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_NUMBER_FLAG_DECIMAL

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            refresh.elevation = 5.0F
        }
    }

    override fun layoutSubviews() {

        val padding = (10 * getDensity()).toInt()

        var x = 0
        var y = 0
        var w = frame.width
        var h = frame.width / 7

        header.setFrame(x, y, w, h)

        y += h + padding

        subheader.setFrame(x, y, w, h)

        y += h + padding
        h = (frame.width / 3.5).toInt()

        list.setFrame(x, y, w, h)
        adapter.width = w

        y += h + padding
        w = (frame.width / 3).toInt()
        h = (w / 3).toInt()

        connect.setFrame(x, y, w, h)
        connect.setBorderColor(2, Color.rgb(0, 100, 0))
        connect.setCornerRadius(5.0f)

        y += h + padding
        w = frame.width
        h = (frame.height / 6.5).toInt()

        robotHeader.setFrame(x, y, w, h)

        y += h + padding
        h = w / 12

        addressHeader.setFrame(x, y, w, h)

        y += h
        h = w / 8
        addressField.setFrame(x, y, w, h)

        val buttonSize = (60 * getDensity()).toInt()

        x = frame.width - (buttonSize + padding)
        y = frame.height - (buttonSize + padding)

        refresh.setFrame(x, y, buttonSize, buttonSize)
        refresh.setCornerRadius((buttonSize / 2).toFloat())
    }

    fun refreshList(muses: MutableList<Muse>) {
        context.runOnUiThread {
            adapter.items = muses
            adapter.notifyDataSetChanged()
        }

    }
}