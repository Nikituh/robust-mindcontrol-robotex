package com.nikitech.robotex.mindcontrol.subviews.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import com.choosemuse.libmuse.Muse

class MuseListAdapter(context: Context)  : ArrayAdapter<Muse>(context, -1) {

    var items: MutableList<Muse> = mutableListOf()

    var width: Int = 0

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Muse {
        return items[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val cell: MuseListCell?

        val item = items[position]

        if (convertView == null) {
            cell = MuseListCell(context)

            val height = (40 * context.resources.displayMetrics.density).toInt()
            cell.layoutParams = AbsListView.LayoutParams(width, height)
            cell.setFrame(0, 0, width, height)
        } else {
            cell = convertView as MuseListCell
        }

        cell.update(item)
        cell.layoutSubviews()

        return cell
    }
}