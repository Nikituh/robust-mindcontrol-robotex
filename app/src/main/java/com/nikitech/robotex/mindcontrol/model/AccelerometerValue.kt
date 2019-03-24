package com.nikitech.robotex.mindcontrol.model

class AccelerometerValue(val x: Double, val y: Double, val z: Double) {

    fun isOutlier(): Boolean {
        return x > 1.1 || x < -1.1 || y > 1.1 || y < -1.1 || z > 1.1 || z < -1.1
    }

}