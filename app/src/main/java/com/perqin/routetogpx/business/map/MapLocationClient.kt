package com.perqin.routetogpx.business.map

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

class MapLocationClient {
    private lateinit var client: LocationClient
    private val _location = MutableLiveData<BDLocation>()
    val location: LiveData<BDLocation> = _location

    internal fun init(context: Context) {
        client = LocationClient(context)
        client.registerLocationListener(object : BDAbstractLocationListener() {
            override fun onReceiveLocation(location: BDLocation) {
                _location.value = location
            }
        })
    }

    fun start() {
        client.start()
    }

    fun stop() {
        client.stop()
    }

    fun requestLocation() =client.requestLocation()

    fun configureLocOption(locOption: LocationClientOption) {
        client.locOption = locOption
    }
}
