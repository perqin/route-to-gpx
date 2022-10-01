package com.perqin.routetogpx.views.main.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.perqin.routetogpx.business.map.MapInitializer

class MainMapViewModel(application: Application, private val mapInitializer: MapInitializer) : AndroidViewModel(application) {
    private lateinit var locationClient: LocationClient

    private val _myLocation = MutableLiveData<BDLocation>()
    val myLocation: LiveData<BDLocation> = _myLocation

    fun initMapSdk() {
        mapInitializer.init()
        setupLocationClient()
    }

    fun resumeLocation() {
        locationClient.start()
    }

    fun pauseLocation() {
        locationClient.stop()
    }

    private fun setupLocationClient() {
        locationClient = LocationClient(getApplication()).apply {
            locOption = LocationClientOption().apply {
                openGps = true
                scanSpan = 1000
            }
            registerLocationListener(object : BDAbstractLocationListener() {
                override fun onReceiveLocation(location: BDLocation?) {
                    if (location == null) {
                        return
                    }
                    _myLocation.value = location
                }
            })
        }
    }
}
