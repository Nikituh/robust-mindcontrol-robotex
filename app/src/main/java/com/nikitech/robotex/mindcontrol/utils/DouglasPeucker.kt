package com.nikitech.robotex.mindcontrol.utils

import com.choosemuse.libmuse.Eeg
import com.nikitech.robotex.mindcontrol.model.EEGValue


class DouglasPeucker {

    private fun perpendicularDistance(pt: Pair<Double, Double>, lineStart: Pair<Double, Double>, lineEnd: Pair<Double, Double>): Double {
        var dx = lineEnd.first - lineStart.first
        var dy = lineEnd.second - lineStart.second

        // Normalize
        val mag = Math.hypot(dx, dy)
        if (mag > 0.0) { dx /= mag; dy /= mag }
        val pvx = pt.first - lineStart.first
        val pvy = pt.second - lineStart.second

        // Get dot product (project pv onto normalized direction)
        val pvdot = dx * pvx + dy * pvy

        // Scale line direction vector and substract it from pv
        val ax = pvx - pvdot * dx
        val ay = pvy - pvdot * dy

        return Math.hypot(ax, ay)
    }

    private fun calculateRamerDouglasPeucker(pointList: List<Pair<Double, Double>>, epsilon: Double, out: MutableList<Pair<Double, Double>>) {
        if (pointList.size < 2) throw IllegalArgumentException("Not enough points to simplify")

        // Find the point with the maximum distance from line between start and end
        var dmax = 0.0
        var index = 0
        val end = pointList.size - 1
        for (i in 1 until end) {
            val d = perpendicularDistance(pointList[i], pointList[0], pointList[end])
            if (d > dmax) { index = i; dmax = d }
        }

        // If max distance is greater than epsilon, recursively simplify
        if (dmax > epsilon) {
            val recResults1 = mutableListOf<Pair<Double, Double>>()
            val recResults2 = mutableListOf<Pair<Double, Double>>()
            val firstLine = pointList.take(index + 1)
            val lastLine  = pointList.drop(index)
            calculateRamerDouglasPeucker(firstLine, epsilon, recResults1)
            calculateRamerDouglasPeucker(lastLine, epsilon, recResults2)

            // build the result list
            out.addAll(recResults1.take(recResults1.size - 1))
            out.addAll(recResults2)
            if (out.size < 2) throw RuntimeException("Problem assembling output")
        }
        else {
            // Just return start and end points
            out.clear()
            out.add(pointList.first())
            out.add(pointList.last())
        }
    }

    fun apply(values: List<EEGValue>) : List<EEGValue> {

        if (values.isEmpty()) {
//            println("No values to simplify")
            return values
        }

        // Several commands aren't supported,
        // store YOUR SINGLE command here and add it to the result list
        val command = values[0].command

//        println("EEG Points before simplification: " + values.size)

        val result = mutableListOf<EEGValue>()

        val eeg1 = calculateOutPoints(values, Eeg.EEG1)
        val eeg2 = calculateOutPoints(values, Eeg.EEG2)
        val eeg3 = calculateOutPoints(values, Eeg.EEG3)
        val eeg4 = calculateOutPoints(values, Eeg.EEG4)

        val lowest = findLowestCount(eeg1, eeg2, eeg3, eeg4)

        if (lowest == 2) {
            return mutableListOf()
        }

        for (i in 0 until lowest) {
            val value = EEGValue(eeg1[i].second, eeg2[i].second, eeg3[i].second, eeg4[i].second, -1.0, -1.0)
            value.command = command
            result.add(value)
        }

        return result
    }

    private fun findLowestCount(list1: List<Pair<Double, Double>>, list2: List<Pair<Double, Double>>,
                                list3: List<Pair<Double, Double>>, list4: List<Pair<Double, Double>>) : Int {
        var lowest = list1.size

        if (list2.size < lowest) {
            lowest = list2.size
        }

        if (list3.size < lowest) {
            lowest = list3.size
        }

        if (list4.size < lowest) {
            lowest = list4.size
        }

        return lowest
    }

    private fun calculateOutPoints(list: List<EEGValue>, type: Eeg) : List<Pair<Double, Double>> {
        val pointList = EEGValue.toPointList(list, type)
        val outList = mutableListOf<Pair<Double, Double>>()
        calculateRamerDouglasPeucker(pointList, 2.0, outList)
//        println(type.toString() + " Points remaining after simplification: " + outList.size)
        return outList
    }
}