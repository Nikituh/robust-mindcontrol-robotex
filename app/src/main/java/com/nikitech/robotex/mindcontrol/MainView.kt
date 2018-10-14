package com.nikitech.robotex.mindcontrol

import android.content.Context
import android.graphics.Color
import android.view.View
import com.jjoe64.graphview.GraphView
import com.nikitech.robotex.mindcontrol.subviews.*
import mobile.ecofleet.com.common.base.BaseView

class MainView(context: Context) : BaseView(context) {

    companion object {
        @JvmStatic val DEFAULT_X_BOUNDS = 30000.0
    }

    val tabBar = TabBar(context)

    val accelerometer = GraphContainer(context)
    val EEG = GraphContainer(context)

    val buttons = ButtonContainer(context)

    val refresh = ImageButton(context, R.drawable.ic_refresh_black_24dp)

    val accelGraphs = mutableListOf<GraphView>()
    val EEGGraphs = mutableListOf<GraphView>()

    init {

        addView(tabBar)

        accelGraphs.add(createGraph("X"))
        accelGraphs.add(createGraph("Y"))
        accelGraphs.add(createGraph("Z"))

        accelerometer.add(accelGraphs)
        addView(accelerometer)

        EEGGraphs.add(createGraph("EEG1"))
        EEGGraphs.add(createGraph("EEG2"))
        EEGGraphs.add(createGraph("EEG3"))
        EEGGraphs.add(createGraph("EEG4"))
        EEGGraphs.add(createGraph("LEFT AUX"))
        EEGGraphs.add(createGraph("RIGHT AUX"))

        EEG.add(EEGGraphs)
        addView(EEG)

        addView(buttons)

        refresh.setBackgroundColor(Color.GREEN)
        addView(refresh)

        setMainViewFrame(false)

        setConnectActive()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val padding = (10 * getDensity()).toInt()

        var x = 0
        var y = 0
        var w = frame.width
        var h = w / 6

        tabBar.setFrame(x, y, w, h)

        x = padding
        y += h + padding
        w = frame.width - 2 * padding
        h = frame.height - (tabBar.frame.height + 2 * padding)

        accelerometer.setFrame(x, y, w, h)
        EEG.setFrame(x, y, w, h)
        buttons.setFrame(x, y, w, h)

//        val leftPadding = (50 * getDensity()).toInt()
//        val size = frame.width - 2 * leftPadding
//
//        y += h + padding
//
//        buttons.setFrame(leftPadding, y, size, size)

        val buttonSize = (60 * getDensity()).toInt()

        x = frame.width - (buttonSize + padding)
        y = frame.height - (buttonSize + padding)

        refresh.setFrame(x, y, buttonSize, buttonSize)
        refresh.setCornerRadius((buttonSize / 2).toFloat())
    }

    private fun createGraph(title: String) : GraphView {

        val graph = GraphView(context)

        graph.title = title
        graph.viewport.setMaxX(DEFAULT_X_BOUNDS)
        graph.viewport.isXAxisBoundsManual = true
        graph.gridLabelRenderer.textSize = 18f
        graph.gridLabelRenderer.reloadStyles()

        return graph
    }

    fun setActiveItem(item: TabView) {
        when {
            item.isConnectTab() -> {
                setConnectActive()
            }
            item.isControlTab() -> {
                setControlActive()
            }
            item.isAccelTab() -> {
                setAccelerometerActive()
            }
            item.isEEGTab() -> {
                setEEGActive()
            }
        }
    }
    private fun setConnectActive() {
        tabBar.setActiveItem(0)
        EEG.visibility = View.GONE
        accelerometer.visibility = View.GONE
        buttons.visibility = View.GONE

        refresh.visibility = View.VISIBLE
    }

    private fun setControlActive() {
        tabBar.setActiveItem(1)

        EEG.visibility = View.GONE
        accelerometer.visibility = View.GONE
        refresh.visibility = View.GONE

        buttons.visibility = View.VISIBLE
    }

    private fun setAccelerometerActive() {
        tabBar.setActiveItem(2)

        EEG.visibility = View.GONE
        buttons.visibility = View.GONE
        refresh.visibility = View.GONE

        accelerometer.visibility = View.VISIBLE
    }

    private fun setEEGActive() {
        tabBar.setActiveItem(3)

        accelerometer.visibility = View.GONE
        buttons.visibility = View.GONE
        refresh.visibility = View.GONE

        EEG.visibility = View.VISIBLE
    }

}