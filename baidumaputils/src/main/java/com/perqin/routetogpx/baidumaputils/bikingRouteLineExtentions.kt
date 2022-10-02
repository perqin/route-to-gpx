package com.perqin.routetogpx.baidumaputils

import com.baidu.mapapi.model.LatLngBounds
import com.baidu.mapapi.search.route.BikingRouteLine

fun BikingRouteLine.latLngBounds(): LatLngBounds {
    println("latLngBounds: ${starting.location} -> ${terminal.location}")
    val builder = LatLngBounds.Builder()
    var wayPointsCount = 0
    allStep.forEach {
        builder.include(it.wayPoints)
        wayPointsCount += it.wayPoints.size
    }
    println("latLngBounds: Included $wayPointsCount waypoint(s)")
    return builder.build()
}
