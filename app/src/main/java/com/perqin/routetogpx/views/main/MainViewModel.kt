package com.perqin.routetogpx.views.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.poi.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val poiSearch = PoiSearch.newInstance()

    private val _searchPoiList = MutableLiveData<List<PoiInfo>>(emptyList())
    val searchPoiList: LiveData<List<PoiInfo>> = _searchPoiList

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
}
