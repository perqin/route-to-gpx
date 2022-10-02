package com.perqin.routetogpx.views.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.overlayutil.BikingRouteOverlay
import com.perqin.routetogpx.R
import com.perqin.routetogpx.baidumaputils.latLngBounds
import com.perqin.routetogpx.databinding.MainMapFragmentBinding
import com.perqin.routetogpx.views.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * The main map view [Fragment] subclass as the default destination in the navigation.
 */
class MainMapFragment : Fragment() {
    private var _binding: MainMapFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val map get() = binding.mapView.map

    private val vm: MainMapViewModel by viewModel()
    private val activityVm: MainViewModel by sharedViewModel()

    private var isInitialZoomFired = false

    private lateinit var bikingRouteOverlay: BikingRouteOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Must be called before inflating view
        vm.initMapSdk()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = MainMapFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        map.isMyLocationEnabled = true

        bikingRouteOverlay = BikingRouteOverlay(map)

        vm.myLocation.observe(viewLifecycleOwner) { location ->
            map.setMyLocationData(MyLocationData.Builder()
                .accuracy(location.radius)
                .direction(location.direction)
                .latitude(location.latitude)
                .longitude(location.longitude)
                .build())
            if (!isInitialZoomFired) {
                map.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 16F))
                isInitialZoomFired = true
            }
        }
        activityVm.bikingRouteResult.observe(viewLifecycleOwner) {
            val routeLine = it.routeLines.firstOrNull() ?: return@observe
            bikingRouteOverlay.setData(routeLine)
            bikingRouteOverlay.addToMap()
            val padding = resources.getDimensionPixelOffset(R.dimen.map_route_zoom_padding)
            map.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(routeLine.latLngBounds(), padding, padding, padding, padding))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        vm.resumeLocation()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
        vm.pauseLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        map.isMyLocationEnabled = false
        binding.mapView.onDestroy()
        _binding = null
    }
}