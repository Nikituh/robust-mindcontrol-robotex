package com.nikitech.robotex.mindcontrol.subviews

import android.content.Context
import android.graphics.Color
import android.os.Build
import com.nikitech.robotex.mindcontrol.utils.Colors
import mobile.ecofleet.com.common.base.BaseView

class TabBar(context: Context) : BaseView(context) {

    private val connect = TabView(context, "CONNECT")
    private val control = TabView(context, "CONTROL")
    private val accelerometer = TabView(context, "ACCEL.")
    private val EEG = TabView(context, "EEG")

    private val separators = mutableListOf<BaseView>()
    val list = mutableListOf<TabView>()

    init {
        list.add(connect)
        list.add(control)
        list.add(accelerometer)
        list.add(EEG)

        for ((counter, item) in list.withIndex()) {
            addView(item)
            if (counter < list.size - 1) {
                val separator = BaseView(context)
                separator.setBackgroundColor(Color.WHITE)
                addView(separator)
                separators.add(separator)
            }
        }

        setBackgroundColor(Colors.main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 5.0f
        }
    }

    override fun layoutSubviews() {

        val separatorPadding = (10 * getDensity()).toInt()
        val separatorHeight = frame.height - 2 * separatorPadding
        val separatorWidth = (1 * getDensity()).toInt()
        var x = 0
        val y = 0
        val w = frame.width / list.size
        val h = frame.height

        for ((counter, item) in list.withIndex()) {
            item.setFrame(x, y, w, h)
            x += w
            if (counter < list.size - 1) {
                separators[counter].setFrame(x, separatorPadding, separatorWidth, separatorHeight)
            }
        }
    }

    fun isGraphTabActive() : Boolean {
        return EEG.isHighlighted() || accelerometer.isHighlighted()
    }

    fun setActiveItem(index: Int) {
        this.setActiveItem(list[index])
    }

    private fun setActiveItem(tab: TabView) {
        for (item in list) {
            item.normalize()
        }
        tab.highlight()
    }

    fun isActiveTabAccelerometer(): Boolean {

        for (item in list) {
            if (item.isHighlighted() && item.isAccelTab()) {
                return true
            }
        }
        return false
    }

    fun isActiveTabEeg(): Boolean {
        for (item in list) {
            if (item.isHighlighted() && item.isEEGTab()) {
                return true
            }
        }
        return false
    }
}