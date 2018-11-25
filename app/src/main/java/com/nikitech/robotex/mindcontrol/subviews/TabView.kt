package com.nikitech.robotex.mindcontrol.subviews

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.MotionEvent
import android.widget.TextView
import com.nikitech.robotex.mindcontrol.base.BaseButton
import com.nikitech.robotex.mindcontrol.utils.Colors
import mobile.ecofleet.com.common.base.setFrame
import org.jetbrains.anko.textColor

class TabView(context: Context, text : String) : BaseButton(context) {

    companion object {
        @JvmStatic val TAB_CONNECT = "CONNECT"
        @JvmStatic val TAB_CONTROL = "CONTROL"
        @JvmStatic val TAB_ACCELEROMETER = "ACCEL."
        @JvmStatic val TAB_EEG = "EEG"
    }

    val title = TextView(context)

    init {
        title.text = text
        title.textSize = 11.0f
        title.textColor = Color.WHITE
        title.gravity = Gravity.CENTER
        addView(title)
    }

    override fun layoutSubviews() {
        val padding = (5 * getDensity()).toInt()
        title.setFrame(padding, padding, frame.width - 2 * padding, frame.height - 2 * padding)
    }

    fun normalize() {
        title.setBackgroundColor(Colors.main)
        isHighlighted = false
    }

    private var isHighlighted = false

    fun highlight() {
        title.setBackgroundColor(Colors.mainDarker)
        isHighlighted = true
    }

    fun isConnectTab(): Boolean {
        return title.text == TAB_CONNECT
    }

    fun isControlTab(): Boolean {
        return title.text == TAB_CONTROL
    }

    fun isAccelTab(): Boolean {
        return title.text == TAB_ACCELEROMETER
    }

    fun isEEGTab(): Boolean {
        return title.text == TAB_EEG
    }

    fun isHighlighted(): Boolean {
        return  isHighlighted
    }
}