package com.nikitech.robotex.mindcontrol.subviews

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.widget.ImageView
import com.nikitech.robotex.mindcontrol.base.BaseButton
import mobile.ecofleet.com.common.base.setFrame

class ImageButton(context: Context, imageResource: Int) : BaseButton(context) {

    private val imageView = ImageView(context)

    init {

        setBackgroundColor(Color.WHITE)

        setImageResource(imageResource)

        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageView.adjustViewBounds = true

        setBackgroundColor(Color.rgb(220, 220, 220))
        setBorderColor((2 * getDensity()).toInt(), Color.rgb(180, 180, 180))

        addView(imageView)
    }


    fun setImageResource(resource: Int) {
        imageView.setImageResource(resource)
        imageView.tag = resource
    }

    override fun layoutSubviews() {

        setCornerRadius((frame.width / 2).toFloat())

        val padding: Int = (15 * context.resources.displayMetrics.density).toInt()
        val imageSize = frame.width - 2 * padding

        imageView.setFrame(padding, padding, imageSize, imageSize)
        setCornerRadius(5.0f)

    }

}