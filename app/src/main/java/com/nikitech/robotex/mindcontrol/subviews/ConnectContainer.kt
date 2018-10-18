package com.nikitech.robotex.mindcontrol.subviews

import android.content.Context
import android.graphics.Color
import android.widget.ListView
import com.choosemuse.libmuse.Muse
import com.nikitech.robotex.mindcontrol.R
import com.nikitech.robotex.mindcontrol.subviews.list.MuseListAdapter
import mobile.ecofleet.com.common.base.BaseView
import mobile.ecofleet.com.common.base.setFrame
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.runOnUiThread

class ConnectContainer(context: Context) : BaseView(context) {

    val list = ListView(context)
    val adapter = MuseListAdapter(context)

    val refresh = ImageButton(context, R.drawable.ic_refresh_black_24dp)

    init {

        list.backgroundColor = Color.rgb(245, 245, 245)
        addView(list)

        refresh.setBackgroundColor(Color.rgb(0, 150, 0))
        addView(refresh)

        list.adapter = adapter
    }

    override fun layoutSubviews() {

        val padding = (10 * getDensity()).toInt()

        var x = 0
        var y = 0
        var w = frame.width / 2 * 3
        var h = frame.width

        list.setFrame(x, y, w, h)
        adapter.width = w

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