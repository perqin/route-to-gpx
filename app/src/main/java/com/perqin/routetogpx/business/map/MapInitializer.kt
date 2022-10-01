package com.perqin.routetogpx.business.map

import android.content.Context
import android.os.Looper
import com.baidu.mapapi.SDKInitializer

class MapInitializer(private val context: Context, private val locationClient: MapLocationClient) {
    private var isInitialized = false

    fun init() {
        if (!Looper.getMainLooper().isCurrentThread) {
            throw RuntimeException("init must be called on main thread")
        }
        if (isInitialized) {
            return
        }
        SDKInitializer.initialize(context)

        // Init LocationClient
        locationClient.init(context)

        isInitialized = true
    }
}
