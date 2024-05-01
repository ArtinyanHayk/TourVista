package com.example.destination.Location;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.example.destination.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class LocationForUser extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private SearchView mapSearchView;
    private LatLng location;

    /////////////
    private Double latitude;
    private Double longitude;

    ////////////


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_for_user);

        mapSearchView = findViewById(R.id.mapSearch);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;

                if(location!= null){
                    Geocoder geocoder = new Geocoder(LocationForUser.this);
                    try{
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                        //??????????
                        throw new RuntimeException(e);
                    }
                    if (addressList!= null &&!addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        /////

                        ////
                        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());

                        myMap.clear();
                        myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    } else {
                        Toast.makeText(LocationForUser.this, "Такого места не существует", Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location!= null){
                currentLocation = location;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        if (currentLocation!= null) {
            LatLng myLocation = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
            MarkerOptions userMarkerOptions = new MarkerOptions().position(myLocation).title("My Location");
            userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            myMap.addMarker(userMarkerOptions);
            //myMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        }

        location = getIntent().getParcelableExtra("Location");
        Toast.makeText(this, Double.toString(location.latitude) + "  " + Double.toString(location.longitude), Toast.LENGTH_SHORT).show();
        MarkerOptions postMarkerOptions = new MarkerOptions().position(location).title("Posted location");
        postMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        myMap.addMarker(postMarkerOptions);
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }else{
                Toast.makeText(this, "Location permission is denied,please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}