package com.nikitech.robotex.mindcontrol.subviews

import android.content.Context
import android.view.MotionEvent
import com.nikitech.robotex.mindcontrol.base.BaseButton

class CommandButton(context: Context, imageResource: Int, val command: String) : ImageButton(context, imageResource) {

    var isPressedDown = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val action = event?.action ?: return super.onTouchEvent(event)

        if (action == MotionEvent.ACTION_DOWN) {
            isPressedDown = true
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            isPressedDown = false
        }

        return super.onTouchEvent(event)
    }

}