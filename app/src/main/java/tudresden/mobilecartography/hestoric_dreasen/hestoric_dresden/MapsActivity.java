package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private MarkerOptions currentLocationMarker;
    private DatabaseHelper db_helper = new DatabaseHelper(this);
    private SQLiteDatabase db_connection;
    private Cursor db_cursor;
    private int radius = 500; // 500 meters radius

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        // create the database connection
        try{
            db_helper.createDataBase();
            db_connection = db_helper.getDataBase();
            db_cursor = db_connection.rawQuery("SELECT name FROM sqlite_master WHERE type='table';", null);
            db_cursor.moveToFirst();
            while (! db_cursor.isAfterLast()){
                LogUtils.debug("Table names:");
                LogUtils.debug(db_cursor.getString(db_cursor.getColumnIndex("name")));
                db_cursor.moveToNext();
            }

        }catch(IOException ioe){
            LogUtils.error("Failed to create database");
        }
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        // destroy the connection to the db
        db_helper.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null){
            updateCurrentMarkerOnMap();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

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

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        // Add a marker in current location and move the camera
        updateCurrentMarkerOnMap();
    }

    private void updateCurrentMarkerOnMap(){
        if (mLastLocation != null && mMap != null) {
            Double current_lat = mLastLocation.getLatitude();
            Double current_lng = mLastLocation.getLongitude();
            LatLng currentLocation = new LatLng(current_lat, current_lng);
            // get list of nearby point of interests based on the current location
            Iterator<ArrayList> nearby_attractions = get_nearby_attractions(current_lat, current_lng, radius).iterator();
            // go over the result and create markers, create a function for this later
            while (nearby_attractions.hasNext()){
                ArrayList attraction_info = nearby_attractions.next();
                MarkerOptions marker = new MarkerOptions().position(new LatLng((double)attraction_info.get(1), (double) attraction_info.get(2)))
                        .title((String)attraction_info.get(0));
                mMap.addMarker(marker);
            }
            currentLocationMarker = new MarkerOptions().position(currentLocation).title("You are here!");
//            mMap.addMarker(currentLocationMarker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
        }
    }

    private ArrayList<ArrayList> get_nearby_attractions(Double current_lat, Double current_lng, int radius){
        /**
         * Get the nearby attraction according to the current location and the radius
         */
        ArrayList result = new ArrayList();
        try {
            db_cursor = db_connection.rawQuery("select * from main_data limit 10;", null);
            db_cursor.moveToFirst();
            while (!db_cursor.isAfterLast()) {
                ArrayList attraction_info = new ArrayList();
                String name = db_cursor.getString(db_cursor.getColumnIndex("name"));
                Double lat = db_cursor.getDouble(db_cursor.getColumnIndex("lat"));
                Double lng = db_cursor.getDouble(db_cursor.getColumnIndex("lng"));
                attraction_info.add(name);
                attraction_info.add(lat);
                attraction_info.add(lng);
                result.add(attraction_info);
                db_cursor.moveToNext();
            }
        }catch(Exception e){
            LogUtils.error("Failed to get nearby attractions. Reason: " + e.getMessage());
        }

        return result;

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
