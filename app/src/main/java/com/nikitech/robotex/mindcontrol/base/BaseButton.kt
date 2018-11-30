package com.nikitech.robotex.mindcontrol.base

import android.content.Context
import android.view.MotionEvent
import mobile.ecofleet.com.common.base.BaseView

open class BaseButton(context: Context) : BaseView(context) {

    var shouldPreventDuplicateClick: Boolean = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (!isEnabled) {
            return true
        }

        if (event?.action == MotionEvent.ACTION_DOWN) {
            alpha = 0.5f
        } else if (event?.action == MotionEvent.ACTION_UP) {
            alpha = 1.0f
            callOnClick()
        } else if (event?.action == MotionEvent.ACTION_CANCEL) {
            alpha = 1.0f
        }

        return super.onTouchEvent(event)
    }

    fun disable() {
        isEnabled = false
        alpha = 0.5f
    }

    fun enable() {
        isEnabled = true
        alpha = 1.0f
    }
}