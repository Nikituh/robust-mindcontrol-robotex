package com.nikitech.robotex.mindcontrol.subviews

import android.content.Context
import com.jjoe64.graphview.GraphView
import mobile.ecofleet.com.common.base.BaseScrollView
import mobile.ecofleet.com.common.base.BaseView
import mobile.ecofleet.com.common.base.setFrame

class GraphContainer(context: Context) : BaseScrollView(context) {

    private val list = mutableListOf<GraphView>()

    init {

    }

    fun add(items: List<GraphView>) {

        for (item in list) {
            removeView(item)
        }
        list.clear()
        list.addAll(items)
        for (item in items) {
            addView(item)
        }
        layoutSubviews()
    }
    override fun layoutSubviews() {

        val padding = (10 * getDensity()).toInt()

        val x = 0
        var y = 0
        val w = frame.width
        val h = w / 3

        for (item in list) {
            item.setFrame(x, y, w, h)
            y += h + padding
        }

    }

    fun get(index: Int) : GraphView {
        return list[index]
    }
}