package com.perqin.routetogpx.views.main.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baidu.mapapi.map.MyLocationData
import com.perqin.routetogpx.databinding.MainMapFragmentBinding
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

        vm.myLocation.observe(viewLifecycleOwner) { location ->
            map.setMyLocationData(MyLocationData.Builder()
                .accuracy(location.radius)
                .direction(location.direction)
                .latitude(location.latitude)
                .longitude(location.longitude)
                .build())
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