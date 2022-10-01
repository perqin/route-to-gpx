package com.perqin.routetogpx.views.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener
import com.baidu.mapapi.search.poi.PoiCitySearchOption
import com.baidu.mapapi.search.poi.PoiDetailResult
import com.baidu.mapapi.search.poi.PoiDetailSearchResult
import com.baidu.mapapi.search.poi.PoiIndoorResult
import com.baidu.mapapi.search.poi.PoiResult
import com.baidu.mapapi.search.poi.PoiSearch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val poiSearch = PoiSearch.newInstance()

    private val _searchPoiList = MutableLiveData<List<PoiInfo>>(emptyList())
    val searchPoiList: LiveData<List<PoiInfo>> = _searchPoiList

    private val _routeSearchStart = MutableLiveData<PoiInfo?>()
    val routeSearchStart: LiveData<PoiInfo?> = _routeSearchStart

    private val _routeSearchDest = MutableLiveData<PoiInfo?>()
    val routeSearchDest: LiveData<PoiInfo?> = _routeSearchDest

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
    }

    override fun onCleared() {
        super.onCleared()
        poiSearch.destroy()
    }

    fun searchPoi(query: String) {
        poiSearch.searchInCity(PoiCitySearchOption().city("深圳").keyword(query))
    }

    fun onSelectSearchPoi(poiInfo: PoiInfo) {
        _searchPoiList.value = emptyList()
        _routeSearchDest.value = poiInfo
    }
}
