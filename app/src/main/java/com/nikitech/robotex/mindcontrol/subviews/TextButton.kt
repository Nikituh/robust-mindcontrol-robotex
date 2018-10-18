package com.nikitech.robotex.mindcontrol.subviews

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import mobile.ecofleet.com.common.base.BaseView
import mobile.ecofleet.com.common.base.setFrame

class TextButton(context: Context, title: String) : BaseView(context) {

    val text = TextView(context)

    init {
        text.text = title
        text.gravity = Gravity.CENTER
        addView(text)
    }

    override fun layoutSubviews() {
        text.setFrame(0, 0, frame.width, frame.height)
    }
}