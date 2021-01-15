package com.example.firebaseproductlist;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button mStoreListBtn;
    FirebaseFirestore db;



    List<StoreModel> storeModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            StoreModel storeModel = new StoreModel(
                                    documentSnapshot.getString("sId"),
                                    documentSnapshot.getString("sName"),
                                    documentSnapshot.getString("sDesc"),
                                    documentSnapshot.getString("sRadius"),
                                    documentSnapshot.getString("sLat"),
                                    documentSnapshot.getString("sLong"));
                            storeModelList.add(storeModel);

                            // Add a marker in Sydney and move the camera
                            LatLng store = new LatLng(Double.parseDouble(documentSnapshot.getString("sLat")),Double.parseDouble(documentSnapshot.getString("sLong")));
                            mMap.addMarker(new MarkerOptions().position(store).title(documentSnapshot.getString("sName")));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(store));
                        }

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
    }

}