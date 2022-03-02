package com.example.oeientation.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.nfc.Tag
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.oeientation.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.type.LatLng


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    var oriLocation:Location? =null
    var flag = false
    val locationList= mutableListOf<String>()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        val gps_location: Button = binding.gps
        val stop_location:Button = binding.gps2
        gps_location.setOnClickListener {
            checkGPSState()
            oriLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            Toast.makeText(activity,"按鈕緯度:${oriLocation!!.latitude} 按鈕經度:${oriLocation!!.longitude}",Toast.LENGTH_SHORT).show()
            locationList.add("{${oriLocation!!.latitude},${oriLocation!!.longitude}}")
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000L, 5f,locationListener)
        }
        stop_location.setOnClickListener{
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users")
            myRef.setValue(locationList)
            locationManager.removeUpdates(locationListener);

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { activityResult ->
        // 判斷使用者是否給予權限
        if (activityResult.resultCode == Activity.RESULT_OK) {
            Toast.makeText(activity,"訂位已開啟", Toast.LENGTH_SHORT).show()
        } else {
            checkGPSState()
        }
    }

    private fun checkGPSState() {
        val locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder(activity)
                .setTitle("GPS 尚未開啟")
                .setMessage("使用此功能需要開啟 GSP 定位功能")
                .setPositiveButton("前往開啟"){_, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    requestPermissionLauncher.launch(intent)
                    }
                .setNeutralButton("取消", null)
                .show()
        } else {
            //todo getDeviceLocation()
            Toast.makeText(activity, "已獲取到位置權限且GPS已開啟，可以準備開始獲取經緯度", Toast.LENGTH_SHORT).show()
        }
    }

    val locationListener = object : LocationListener{
        override fun onLocationChanged(location: Location) {
            if(location != null) {
                Toast.makeText(activity,"緯度:${location.latitude} 經度:${location.longitude}",Toast.LENGTH_SHORT).show()
                locationList.add("{${location.latitude},${location.longitude}}")
            }

        }

    }

}