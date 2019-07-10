package edu.hope.cs.treesap2.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.csv.CSVRecord;

import java.util.List;

import edu.hope.cs.treesap2.R;
import edu.hope.cs.treesap2.control.PrefManager;
import edu.hope.cs.treesap2.control.Transform;
import edu.hope.cs.treesap2.datasource.CityOfHollandDataSource;
import edu.hope.cs.treesap2.datasource.DataSource;
import edu.hope.cs.treesap2.datasource.HopeCollegeDataSource;
import edu.hope.cs.treesap2.datasource.ITreeDataSource;
import edu.hope.cs.treesap2.model.Tree;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private static final String[] PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    private static final int REQUEST_ID = 6;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Context parent;
    float personalMarker = BitmapDescriptorFactory.HUE_VIOLET;
    float treeMarker = BitmapDescriptorFactory.HUE_GREEN;
    float zoom = 18-(2*PrefManager.getFloat("zoom", (float)1));
    boolean whichSource = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL).tiltGesturesEnabled(false);
        MapFragment.newInstance(options);
        mapFragment.getMapAsync(this);
        parent = (Context) getIntent().getSerializableExtra("parent");
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Bundle bundle = new Bundle();
                if(!marker.getTitle().equals("My Position")) {
                    bundle.putDouble("longitude", marker.getPosition().longitude);
                    bundle.putDouble("latitude", marker.getPosition().latitude);
                    Intent mapsIntent = new Intent();
                    mapsIntent.putExtras(bundle);
                    setResult(RESULT_OK, mapsIntent);
                    finish();
                } else {
                    android.support.v7.app.AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new android.support.v7.app.AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                    } else {
                        builder = new android.support.v7.app.AlertDialog.Builder(MapsActivity.this);
                    }
                    builder.setTitle("No Tree Identified")
                            .setMessage("You must select a tree to receive the information!")
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        if(MainActivity.getSelectedDataSources().isEmpty()) {
            android.support.v7.app.AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new android.support.v7.app.AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new android.support.v7.app.AlertDialog.Builder(MapsActivity.this);
            }
            builder.setTitle("No Data Sources")
                    .setMessage("You must select a data source to view trees near you!")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MapsActivity.this, MainActivity.class));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        for (DataSource ds : MainActivity.getSelectedDataSources()) {
            ds.initialize(MapsActivity.this, null);
            Iterable<CSVRecord> stuff = null;
            int treeField = 0;
            String location = "";
            if (ds instanceof CityOfHollandDataSource) {
                stuff = ((CityOfHollandDataSource) ds).getCoordinates(MapsActivity.this, "/data/user/0/edu.hope.cs.treesap2/files/COHTreeData.csv");
                treeField = 1;
                whichSource = false;
            } else if (ds instanceof HopeCollegeDataSource) {
                stuff = ds.getCoordinates(MapsActivity.this, "/data/user/0/edu.hope.cs.treesap2/files/HCTreeData.csv");
                treeField = 2;
                location = "Hope College Pine Grove";
                whichSource = true;
            } else if (ds instanceof ITreeDataSource) {
                stuff = ((ITreeDataSource) ds).getCoordinates(MapsActivity.this, "/data/user/0/edu.hope.cs.treesap2/files/iTreeTreeData.csv");
                treeField = 2;
                location = "Hope College Pine Grove";
                whichSource = true;
            }
            if (stuff == null) {
                continue;
            }
            int count =-1;
            for (CSVRecord record : stuff) {
                count++;
                if(count>2278){
                    break;
                }
                String latitude;
                String longitude;
                if(!whichSource) {
                    latitude = record.get("Latitude");
                    longitude = record.get("Longitude");
                }
                else{
                    latitude = record.get(Tree.LATITUDE);
                    longitude = record.get(Tree.LONGITUDE);
                }
                if (latitude != null && longitude != null) {
                    if (!latitude.equals("")) {
                        if (latitude.matches("^-?[0-9]\\d*(\\.\\d+)?$")) {

                            if (!longitude.equals("")) {
                                if (longitude.matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                    LatLng coords = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                                    if (ds.getClass().equals(CityOfHollandDataSource.class)) {
                                        location = record.get("Park");
                                    }
                                    try {
                                        String name = Transform.ChangeName(record.get(treeField));
                                        mMap.addMarker(new MarkerOptions().position(coords).title(name).snippet(location).icon(BitmapDescriptorFactory.defaultMarker(treeMarker)));
                                    }catch(ArrayIndexOutOfBoundsException e) {

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        LatLng hope = new LatLng(42.788002, -86.105971);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMS, REQUEST_ID);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hope, zoom));
            return;
        } else {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = null;
            Location defaultLocation = new Location("");
            defaultLocation.setLatitude(42.788002);
            defaultLocation.setLongitude(-86.105971);
            List<String> provs = locationManager.getAllProviders();
            for(String prov : provs) {
                if (locationManager.getLastKnownLocation(prov) != null) {
                    if (!prov.equals("gps")) {
                        defaultLocation = locationManager.getLastKnownLocation(prov);
                    } else {
                        location = locationManager.getLastKnownLocation(prov);
                    }
                    if (location != null) {
                        break;
                    }
                }
            }
            if(location==null){
                location = defaultLocation;
            }
            onLocationChanged(location);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng currentLocation = new LatLng(latitude, longitude);
            if (PrefManager.getBoolean("marker", true)) {
                mMap.addMarker(new MarkerOptions().position(currentLocation)
                        .title("My Position").snippet("You are here.")
                        .icon(BitmapDescriptorFactory.defaultMarker(personalMarker)));
            }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoom));
        }

        }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(PrefManager.getBoolean("marker", false)) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(personalMarker));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        }

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}

