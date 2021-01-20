package com.example.firebaseproductlist;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button mStoreListBtn;
    FirebaseFirestore db;

    private GeofencingClient geofencingClient;
    PendingIntent geofencePendingIntent;

    List<StoreModel> storeModelList = new ArrayList<>();

    List<Geofence> geofenceListOutside = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        geofencingClient = LocationServices.getGeofencingClient(this);


        System.out.println("dasdasdasdas----------------------" + PackageManager.PERMISSION_GRANTED);
        System.out.println("aaaaaaaaaaaaaaaaaaa-----------------------------" + Manifest.permission.ACCESS_FINE_LOCATION);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mStoreListBtn = findViewById(R.id.show_list_store);
        mStoreListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, ListStoreActivity.class));
                finish();
            }
        });
        db = FirebaseFirestore.getInstance();
        db.collection("Stores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        storeModelList.clear();
                        // called when data is retrived
                        // loop the data
                        List<Geofence> geofenceListInside = new ArrayList<>();

                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            StoreModel storeModel = new StoreModel(
                                    documentSnapshot.getString("sId"),
                                    documentSnapshot.getString("sName"),
                                    documentSnapshot.getString("sDesc"),
                                    documentSnapshot.getString("sRadius"),
                                    documentSnapshot.getString("sLat"),
                                    documentSnapshot.getString("sLong"));
                            storeModelList.add(storeModel);

                            // Add a marker in Sydney and move the camera
                            LatLng store = new LatLng(Double.parseDouble(documentSnapshot.getString("sLat")), Double.parseDouble(documentSnapshot.getString("sLong")));
                            mMap.addMarker(new MarkerOptions().position(store).title(documentSnapshot.getString("sName")));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(store));
                            geofenceListInside.add(new Geofence.Builder()
                                    // Set the request ID of the geofence. This is a string to identify this
                                    // geofence.
                                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                    .setRequestId((documentSnapshot.getString("sId")))
                                    .setCircularRegion(
                                            Double.parseDouble(documentSnapshot.getString("sLat")),
                                            Double.parseDouble(documentSnapshot.getString("sLong")),
                                            Float.parseFloat(documentSnapshot.getString("sRadius"))
                                    )
                                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build());

                        }
//
                        System.out.println(geofenceListInside);
                        geofenceListOutside = geofenceListInside;
                        createGeofence();
                        // adapter
//                        adapter = new StoreAdapter(ListStoreActivity.this,storeModelList);

                        // set adapter to recycler view
//                        mRecyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // called if there is any error
//                pd.dismiss();
                Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


    }

    public void createGeofence() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "I m here", Toast.LENGTH_SHORT).show();
            System.out.println("dasdasdasdas" + PackageManager.PERMISSION_GRANTED);
            System.out.println("aaaaaaaaaaaaaaaaaaa" + Manifest.permission.ACCESS_FINE_LOCATION);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        // ...
                        Toast.makeText(MapsActivity.this, "Geofence ADDED", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...
                        Toast.makeText(MapsActivity.this, "Geofence Not Added", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        System.out.println(geofenceListOutside);
        builder.addGeofences(geofenceListOutside);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.

        if (geofencePendingIntent != null) {
            Toast.makeText(this, "i m here", Toast.LENGTH_SHORT).show();
            return geofencePendingIntent;
        }
        Intent intent = new Intent(MapsActivity.this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.isMyLocationEnabled();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Toast.makeText(MapsActivity.this, "Last location passed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MapsActivity.this, "last location failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

}