package com.nikitech.robotex.mindcontrol.subviews

import android.content.Context
import android.graphics.Color
import com.nikitech.robotex.mindcontrol.R
import mobile.ecofleet.com.common.base.BaseView
import org.jetbrains.anko.backgroundColor

class ButtonContainer(context: Context) : BaseView(context) {

    val left = ImageButton(context, R.drawable.ic_arrow_back_black_24dp)
    val forward = ImageButton(context, R.drawable.ic_arrow_upward_black_24dp)
    val right = ImageButton(context, R.drawable.ic_arrow_forward_black_24dp)
    val reverse = ImageButton(context, R.drawable.ic_arrow_downward_black_24dp)
    val stop = ImageButton(context, R.drawable.ic_stop_black_24dp)

    init {

        addView(left)
        addView(forward)
        addView(right)
        addView(reverse)
        addView(stop)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()


        val padding = frame.width / 10
        val buttonWidth = (frame.width - 2 * padding) / 3


        var x = buttonWidth + padding
        var y = 0
        val w = buttonWidth
        val h = buttonWidth

        forward.setFrame(x, y, w, h)

        x += buttonWidth + padding
        y += buttonWidth + padding

        right.setFrame(x, y, w, h)

        x -= buttonWidth + padding
        y += buttonWidth + padding

        reverse.setFrame(x, y, w, h)

        x -= buttonWidth + padding
        y -= buttonWidth + padding

        left.setFrame(x, y, w, h)

        x += buttonWidth + padding

        stop.setFrame(x, y, w, h)
    }
}