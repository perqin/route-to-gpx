package com.perqin.routetogpx.koin

import com.perqin.routetogpx.business.map.MapInitializer
import com.perqin.routetogpx.business.map.MapLocationClient
import com.perqin.routetogpx.views.main.MainViewModel
import com.perqin.routetogpx.views.main.map.MainMapViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::MapInitializer)
    singleOf(::MapLocationClient)
    viewModelOf(::MainViewModel)
    viewModelOf(::MainMapViewModel)
}
