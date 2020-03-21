package r.sardorbek.mytaxiapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.bottom_sheet_trip_details.*


class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private var locationPermissionGranted = false
    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        locationPermission

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        fragment = supportFragmentManager.fragments[0]
        bottomSheet()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment!!.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        /*
         *   Here I defined marker properties such as position, icon and made it draggable.
         *   It will zoom to the marker's position while opening the map
         */
        val pinPosition = LatLng(41.33861, 69.3342)
        googleMap.addMarker(MarkerOptions()
                        .position(pinPosition).draggable(true))
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_start))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pinPosition, 18.5F), 2000, null)

        if (locationPermissionGranted) {
            googleMap.isMyLocationEnabled = true
            val locationManager: LocationManager = baseContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = locationManager.getProviders(true)

            var location: Location? = null
            for (i in providers.indices) {
                location = locationManager.getLastKnownLocation(providers[i])
                if (location != null) break
            }
            if (location != null) {
                //The code inside of this if condition draws the path between two points
                val path: MutableList<LatLng> = ArrayList()

                val context: GeoApiContext = GeoApiContext.Builder().apiKey("AIzaSyB7ZiJe5WB7XEP0HPjet_w_F2wZqVm37L8").build()
                val req: DirectionsApiRequest = DirectionsApi.getDirections(context, "${location.latitude
                },${location.longitude}", "41.33861,69.3342")
                try {
                    val res: DirectionsResult = req.await()

                    if (res.routes != null && res.routes.isNotEmpty()) {
                        val route: DirectionsRoute = res.routes[0]
                        if (route.legs != null) {
                            for (i in route.legs.indices) {
                                val leg: DirectionsLeg = route.legs[i]
                                if (leg.steps != null) {
                                    for (j in leg.steps.indices) {
                                        val step: DirectionsStep = leg.steps[j]
                                        if (step.steps != null && step.steps.isNotEmpty()) {
                                            for (k in step.steps.indices) {
                                                val step1: DirectionsStep = step.steps[k]
                                                val points1: EncodedPolyline = step1.polyline
                                                if (points1 != null) {
                                                    //Decode polyline and add points to list of route coordinates
                                                    val coords1: MutableList<com.google.maps.model.LatLng>? = points1.decodePath()
                                                    for (coord1 in coords1!!) {
                                                        path.add(LatLng(coord1.lat, coord1.lng))
                                                    }
                                                }
                                            }
                                        } else {
                                            val points: EncodedPolyline = step.polyline
                                            //Decode polyline and add points to list of route coordinates
                                            val coords: MutableList<com.google.maps.model.LatLng>? = points.decodePath()
                                            for (coord in coords!!) {
                                                path.add(LatLng(coord.lat, coord.lng))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (ex: Exception) {
                    Log.e("TAG", ex.localizedMessage)
                }

                //Draw the polyline
                if (path.size > 0) {
                    val opts = PolylineOptions().addAll(path).color(Color.BLUE).width(5f)
                    googleMap.addPolyline(opts)
                }


            }
        }
    }

    // Implementation of BottomSheet Behaviour and defining its properties
    private fun bottomSheet() {
        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(trip_details)

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset != 0f) {
                    backBtn.visibility = View.GONE
                    if (fragment!!.view != null) fragment!!.view!!.alpha = 1.2f - slideOffset
                } else {
                    backBtn.visibility = View.VISIBLE
                }
            }
        })
        bottomSheetBehavior.peekHeight = 275
        bottomSheetBehavior.isHideable = false

        expandBtn.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
    }

    //Asking permission for getting users location
    private val locationPermission: Unit
        get() {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this@MapsActivity, "Location permission required to show your current location", Toast.LENGTH_SHORT).show()
                }
            }
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }

    //Result of users choice regarding location permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true
            } else {
                Toast.makeText(this@MapsActivity, "Permission didn't granted!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //OnClick function of Back button which takes user back to Main screen
    fun back(view: View) {
        finish()
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2
    }

    fun call(view: View) {
        intent = Intent(Intent.ACTION_DIAL)
        startActivity(intent);
    }


}