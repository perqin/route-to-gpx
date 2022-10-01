package com.perqin.routetogpx.views.main.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.baidu.location.BDLocation
import com.baidu.location.LocationClientOption
import com.perqin.routetogpx.business.map.MapLocationClient

class MainMapViewModel(application: Application, private val locationClient: MapLocationClient) : AndroidViewModel(application) {
    val myLocation: LiveData<BDLocation> = locationClient.location

    fun initMapSdk() {
        setupLocationClient()
    }

    fun resumeLocation() {
        locationClient.start()
    }

    fun pauseLocation() {
        locationClient.stop()
    }

    private fun setupLocationClient() {
        locationClient.configureLocOption(LocationClientOption().apply {
            openGps = true
            scanSpan = 1000
        })
    }
}
