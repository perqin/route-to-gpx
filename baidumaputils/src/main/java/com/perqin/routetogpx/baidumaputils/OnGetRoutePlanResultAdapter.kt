package com.perqin.routetogpx.baidumaputils

import com.baidu.mapapi.search.route.BikingRouteResult
import com.baidu.mapapi.search.route.DrivingRouteResult
import com.baidu.mapapi.search.route.IndoorRouteResult
import com.baidu.mapapi.search.route.MassTransitRouteResult
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener
import com.baidu.mapapi.search.route.TransitRouteResult
import com.baidu.mapapi.search.route.WalkingRouteResult

open class OnGetRoutePlanResultAdapter : OnGetRoutePlanResultListener {
    override fun onGetWalkingRouteResult(p0: WalkingRouteResult) {
    }

    override fun onGetTransitRouteResult(p0: TransitRouteResult) {
    }

    override fun onGetMassTransitRouteResult(p0: MassTransitRouteResult) {
    }

    override fun onGetDrivingRouteResult(p0: DrivingRouteResult) {
    }

    override fun onGetIndoorRouteResult(p0: IndoorRouteResult) {
    }

    override fun onGetBikingRouteResult(bikingRouteResult: BikingRouteResult) {
    }
}
