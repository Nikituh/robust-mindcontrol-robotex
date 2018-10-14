package com.nikitech.robotex.mindcontrol.utils

import android.os.Build

class DeviceUtils {

    companion object {

        private const val MARSHMALLOW = 23

        fun isMarshmallow(): Boolean {
            return Build.VERSION.SDK_INT >= MARSHMALLOW
        }

    }
}