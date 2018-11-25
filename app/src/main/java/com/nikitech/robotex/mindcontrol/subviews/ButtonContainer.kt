package com.nikitech.robotex.mindcontrol.subviews

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.Switch
import android.widget.TextView
import com.nikitech.robotex.mindcontrol.R
import com.nikitech.robotex.mindcontrol.model.Command
import mobile.ecofleet.com.common.base.BaseView
import mobile.ecofleet.com.common.base.setFrame
import org.jetbrains.anko.backgroundColor

class ButtonContainer(context: Context) : BaseView(context) {

    private val container = BaseView(context)

    val left = CommandButton(context, R.drawable.ic_arrow_back_black_24dp, Command.LEFT.string)
    val forward = CommandButton(context, R.drawable.ic_arrow_upward_black_24dp, Command.FORWARD.string)
    val right = CommandButton(context, R.drawable.ic_arrow_forward_black_24dp, Command.RIGHT.string)
    val reverse = CommandButton(context, R.drawable.ic_arrow_downward_black_24dp, Command.REVERSE.string)
    val stop = CommandButton(context, R.drawable.ic_stop_black_24dp, Command.STOP.string)

    val list = mutableListOf<CommandButton>()

    val eeg = Switch(context)
    private val switchText = TextView(context)

    init {

        addView(container)

        container.addView(left)
        container.addView(forward)
        container.addView(right)
        container.addView(reverse)
        container.addView(stop)

        list.add(left)
        list.add(forward)
        list.add(right)
        list.add(reverse)
        list.add(stop)

        addView(eeg)

        switchText.text = "Listen to eeg events"
        switchText.gravity = Gravity.CENTER
        addView(switchText)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()


        var w = frame.width / 6 * 5
        var h = w
        var x = frame.width / 2 - w / 2
        var y = x

        container.setFrame(x, y, w, h)

        val padding = container.frame.width / 10
        val buttonWidth = (container.frame.width - 2 * padding) / 3

        x = buttonWidth + padding
        y = 0
        w = buttonWidth
        h = buttonWidth

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

        y = container.frame.y + container.frame.height + 3 * padding
        x = padding
        w = frame.width / 8
        h = w

        eeg.setFrame(x, y, w, h)

        x += w
        w = frame.width / 3

        switchText.setFrame(x, y, w, h)
    }

}