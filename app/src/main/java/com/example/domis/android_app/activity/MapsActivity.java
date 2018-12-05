package com.example.domis.android_app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.domis.android_app.R;
import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.repository.FirebaseRepository;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportPlaceAutocompleteFragment pafSrc;
    private SupportPlaceAutocompleteFragment pafDest;
    private LatLng src;
    private LatLng dest;
    private Booking booking;
    private Marker srcMarker;
    private Marker destMarker;
    private TextView incorrectCountryLabel;
    private boolean srcExists;
    private boolean destExists;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private final LatLng mDefaultLocation = new LatLng(51.8860477, -8.533566);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private Location mLastKnownLocation;

    private GeoApiContext geoContext;
    private FirebaseRepository rep;

    private Button bookButton;
    private Button menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        booking = new Booking();
        booking.setUserID(FirebaseAuth.getInstance().getUid());

        rep = new FirebaseRepository();

        srcExists = false;
        destExists = false;

        geoContext = getGeoContext();

        bookButton = findViewById(R.id.bookButton);
        menuButton = findViewById(R.id.menuButton);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (!mLocationPermissionGranted)
            getLocationPermission();

        incorrectCountryLabel = findViewById(R.id.incorrectCountryLabel);

        pafSrc = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_src);
        pafSrc.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (place != null) {
                    if(place.getAddress().toString().endsWith("Ireland"))
                    {
                        Log.e("Andress: ", place.getAddress().toString());
                        src = place.getLatLng();
                        incorrectCountryLabel.setText("");
                        setSourceMarker(src, place.getName().toString());
                    }
                    else
                    {
                        Log.e("Andress: ", place.getAddress().toString());
                        incorrectCountryLabel.setText("Please choose a location in Ireland");
                    }
                }
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });

        pafDest = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_dest);
        pafDest.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (place != null) {
                    if(place.getAddress().toString().endsWith("Ireland"))
                    {
                        Log.e("Andress: ", place.getAddress().toString());
                        dest = place.getLatLng();
                        incorrectCountryLabel.setText("");
                        setDestMarker(place);
                    }
                    else
                    {
                        Log.e("Andress: ", place.getAddress().toString());
                        incorrectCountryLabel.setText("Please choose a location in Ireland");
                    }
                }
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(src != null && dest != null)
                {
                    try {
                        booking.setSource(src);
                        booking.setDestination(dest);
                        DirectionsResult directions = getDirections();
                        addPolyline(directions);
                        float totalDist = calculateDistance(directions);
                        float totalDuration = calculateDuration(directions);
                        totalDist /= 1000;
                        totalDuration /= 60;
                        float cost = 3 + (totalDist * 1.33333f);
                        Log.e("Dist: ", totalDist + "");
                        Log.e("Dur: ", ((int)totalDuration / 60) + "h " + ((int)totalDuration % 60) + "min");
                        Log.e("Cost: ", "€" + cost);
                        Thread.sleep(1000);
                        final AlertDialog ad = new AlertDialog.Builder(MapsActivity.this).create();
                        ad.setMessage("Distance: " + totalDist + "km\n" +
                        "Duration: " + ((int)totalDuration / 60) + "h " + ((int)totalDuration % 60) + "min\n" +
                        "Cost: €" + cost + "\n" +
                        "Do you require wheelchair access?");
                        ad.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        booking.setWheelchair(true);
                                        rep.booking(booking);
                                    }
                                });
                        ad.setButton(DialogInterface.BUTTON_NEGATIVE, "No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        booking.setWheelchair(false);
                                        rep.booking(booking);
                                    }
                                });
                        ad.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ad.cancel();
                                    }
                                });
                        ad.show();
                        incorrectCountryLabel.setText("");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ApiException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    incorrectCountryLabel.setText("INCORRECT SOURCE/DESTINATION");
                }
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incorrectCountryLabel.setText("");
                startMenuActivity();
            }
        });
    }

    private void startMenuActivity() {
        startActivity(new Intent(this, MenuActivity.class));
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in CIT and move the camera
        //51.8860477, -8.533566
        getDeviceLocation();
        //mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title("Marker in CIT"));
    }

    public void setSourceMarker(LatLng p, String n) {
        if (p != null) {
            if (!srcExists) {
                srcMarker = mMap.addMarker(new MarkerOptions().position(p).title(n));
                srcExists = true;
            } else {
                srcMarker.setPosition(p);
                srcMarker.setTitle(n);
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(p));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(6));
        }

    }

    public void setDestMarker(Place p) {
        if (p != null) {
            if (!destExists) {
                destMarker = mMap.addMarker(new MarkerOptions().position(p.getLatLng()).title(p.getName().toString()));
                destExists = true;
            } else {
                destMarker.setPosition(p.getLatLng());
                destMarker.setTitle(p.getName().toString());
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(p.getLatLng()));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(6));
        }

    }

    private float calculateDuration(DirectionsResult directions) {
        float total = 0;
        for(int i = 0; i < directions.routes[0].legs.length; i++)
        {
            total += directions.routes[0].legs[i].duration.inSeconds;
        }
        return total;
    }

    private float calculateDistance(DirectionsResult directions) {
        float total = 0;
        for(int i = 0; i < directions.routes[0].legs.length; i++)
        {
            total += directions.routes[0].legs[i].distance.inMeters;
        }
        return total;
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            setSourceMarker(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), "Current Location");
                        } else {
                            Log.d("Er", "Current location is null. Using defaults.");
                            Log.e("Er", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude())).title("Last Known Location"));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_api_key)).build();
        return geoApiContext;
    }

    private DirectionsResult getDirections() throws InterruptedException, ApiException, IOException {
        DirectionsResult result = DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.DRIVING).origin("" + src.latitude + "," + src.longitude).destination("" + dest.latitude + "," + dest.longitude).units(Unit.METRIC).await();
        return result;
    }

    private void addPolyline(DirectionsResult results) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath).geodesic(true).color(Color.BLUE));
    }
}
