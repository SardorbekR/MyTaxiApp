package r.sardorbek.mytaxiapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    boolean locationPermissionGranted = false;
    private ConstraintLayout constraintLayout;
    private Button expandButton, backBtn;
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getLocationPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment = getSupportFragmentManager().getFragments().get(0);

        expandButton = findViewById(R.id.detailsBtn);
        backBtn = findViewById(R.id.backBtn);
        constraintLayout = findViewById(R.id.trip_details);

        bottomSheet();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*
         *   Here I defined marker properties such as position, icon and made it draggable.
         *   It will zoom to the marker's position while opening the map
         *
         * */

        LatLng pinPosition = new LatLng(41.33861, 69.3342);

        googleMap.addMarker(new MarkerOptions()
                .position(pinPosition).draggable(true))
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_start));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pinPosition, (float) 18.5), 2000, null);

        if (locationPermissionGranted) {
            googleMap.setMyLocationEnabled(true);
        }
    }


    // Implementation of BottomSheet Behaviour and defining its properties
    private void bottomSheet() {
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset != 0) {
                    backBtn.setVisibility(View.GONE);
                    if (fragment.getView() != null)
                        fragment.getView().setAlpha(1.2f - slideOffset);
                } else {
                    backBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        bottomSheetBehavior.setPeekHeight(275);
        bottomSheetBehavior.setHideable(false);

        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    //Asking permission for getting users location
    private void getLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(MapsActivity.this, "Location permission required to show your current location", Toast.LENGTH_SHORT).show();
            }
        }
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }


    //Result of users choice regarding location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            } else {
                Toast.makeText(MapsActivity.this, "Permission didn't granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //OnClick function of Back button which takes user back to Main screen
    public void back(View view) {
        finish();
    }
}
