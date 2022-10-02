package com.perqin.routetogpx.views.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.baidu.location.BDLocation
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener
import com.baidu.mapapi.search.poi.PoiCitySearchOption
import com.baidu.mapapi.search.poi.PoiDetailResult
import com.baidu.mapapi.search.poi.PoiDetailSearchResult
import com.baidu.mapapi.search.poi.PoiIndoorResult
import com.baidu.mapapi.search.poi.PoiResult
import com.baidu.mapapi.search.poi.PoiSearch
import com.baidu.mapapi.search.route.BikingRoutePlanOption
import com.baidu.mapapi.search.route.BikingRouteResult
import com.baidu.mapapi.search.route.PlanNode
import com.baidu.mapapi.search.route.RoutePlanSearch
import com.perqin.routetogpx.baidumaputils.OnGetRoutePlanResultAdapter
import com.perqin.routetogpx.business.map.MapLocationClient
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainViewModel(application: Application, private val locationClient: MapLocationClient) : AndroidViewModel(application) {
    private val location = locationClient.location
    private val locationObserver = Observer<BDLocation> {
        // Just for requesting latest my location value
    }

    private val poiSearch = PoiSearch.newInstance()

    private val _searchPoiList = MutableLiveData<List<PoiInfo>>(emptyList())
    val searchPoiList: LiveData<List<PoiInfo>> = _searchPoiList

    private val _routeSearchStart = MutableLiveData<PoiInfo?>()
    val routeSearchStart: LiveData<PoiInfo?> = _routeSearchStart

    private val _routeSearchDest = MutableLiveData<PoiInfo?>()
    val routeSearchDest: LiveData<PoiInfo?> = _routeSearchDest

    private val routeSearch = RoutePlanSearch.newInstance()

    private val _bikingRouteResult = MutableLiveData<BikingRouteResult>()
    val bikingRouteResult: LiveData<BikingRouteResult> = _bikingRouteResult

    val isRouteSearchActive = object : MediatorLiveData<Boolean>() {
        private var start: PoiInfo? = null
        private var dest: PoiInfo? = null

        init {
            value = false
            addSource(_routeSearchStart) {
                start = it
                update()
            }
            addSource(_routeSearchDest) {
                dest = it
                update()
            }
        }

        private fun update() {
            value = start != null || dest != null
        }
    }

    init {
        location.observeForever(locationObserver)
        poiSearch.setOnGetPoiSearchResultListener(object : OnGetPoiSearchResultListener {
            override fun onGetPoiResult(result: PoiResult) {
                _searchPoiList.value = result.allPoi
            }

            override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {
            }

            override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {
            }

            override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
                // Deprecated
            }
        })
        routeSearch.setOnGetRoutePlanResultListener(object : OnGetRoutePlanResultAdapter() {
            override fun onGetBikingRouteResult(bikingRouteResult: BikingRouteResult) {
                _bikingRouteResult.value = bikingRouteResult
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        poiSearch.destroy()
        routeSearch.destroy()
        location.removeObserver(locationObserver)
    }

    fun searchPoi(query: String) {
        poiSearch.searchInCity(PoiCitySearchOption().city("深圳").keyword(query))
    }

    fun onSelectSearchPoi(poiInfo: PoiInfo) {
        _searchPoiList.value = emptyList()
        _routeSearchDest.value = poiInfo
        if (_routeSearchStart.value == null) {
            viewModelScope.launch {
                setStartToMyLocationAndSearch()
            }
        }
    }

    private suspend fun setStartToMyLocationAndSearch() {
        _routeSearchStart.value = PoiInfo().apply {
            name = "Finding my location"
        }
        val location = if (location.value == null) {
            val result = locationClient.requestLocation()
            if (result != 0) {
                Log.w(TAG, "Failed to request location: $result")
                return
            }
            suspendCoroutine { cont ->
                location.observeForever(object : Observer<BDLocation> {
                    override fun onChanged(t: BDLocation?) {
                        if (t != null) {
                            location.removeObserver(this)
                            cont.resume(t)
                        }
                    }
                })
            }
        } else {
            location.value!!
        }
        _routeSearchStart.value = PoiInfo().apply {
            setName("My location")
            setLocation(LatLng(location.latitude, location.longitude))
        }
        routeSearch.bikingSearch(BikingRoutePlanOption()
            .from(PlanNode.withLocation(_routeSearchStart.value!!.location))
            .to(PlanNode.withLocation(_routeSearchDest.value!!.location)))
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
