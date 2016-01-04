package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.annotation.TargetApi;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.SearchView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
//    private MarkerOptions currentLocationMarker;
    private DatabaseHelper db_helper = new DatabaseHelper(this);
    private SQLiteDatabase db_connection;
//    private double radius = -1.0;
    private Iterator<Attraction> all_attractions;
    private HashMap<Marker, Attraction> attraction_marker_map = new HashMap();
    private static float default_zoom_level = 15;
    private static float zoom_level_threshold = (float) 16.5;
    private static float current_zoom_level = -1;

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        // create the database connection
        try{
            db_helper.createDataBase();
            db_connection = db_helper.getDataBase();
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
        handle_search_view();
        mapFragment.getMapAsync(this);
    }

    /**
     * Handle the search view on the map
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void handle_search_view()
    {
        final SearchView search_view = (SearchView) findViewById(R.id.searchView);
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter_map_after_search(query);
                search_view.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String new_text) {
                return true;
            }
        });
    }

    /**
     * Filters the map markers based on the search query
     * @param query
     */
    private void filter_map_after_search(String query){
        String pattern = "(?i).*" + query + ".*"; // match the query category if its anywhere in the category field in the database
        Iterator iterator = attraction_marker_map.entrySet().iterator();
        while(iterator.hasNext()){
            HashMap.Entry item = (HashMap.Entry) iterator.next();
            if (query.equals("all") || query.equals("everything")){
                ((Marker)item.getKey()).setVisible(true);
                continue;
            }
            if (!((Attraction)item.getValue()).getCategory().matches(pattern)){
                ((Marker)item.getKey()).setVisible(false);
            }else{
                ((Marker)item.getKey()).setVisible(true);
            }
        }
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
     * Updates the visible markers on the map whether by adding an icon to the marker or removing it
     * @param operation: if the value is remove then it will remove the icon from the visible markers, if the value is add then it will add an icon to the visible markers
     */
    private void update_labled_icons(String operation){
        Iterator iterator = attraction_marker_map.entrySet().iterator();
        while(iterator.hasNext()){
            HashMap.Entry<Marker, Attraction> item = (HashMap.Entry) iterator.next();
            if (item.getKey().isVisible()){
                if (operation.equals("remove")) {
                    item.getKey().setIcon(BitmapDescriptorFactory.defaultMarker());
                }
                else if (operation.equals("add")){
                    IconGenerator icon_generator= new IconGenerator(this);
                    icon_generator.setStyle(IconGenerator.STYLE_GREEN);
                    String name = null;
                    try {
                        name = URLDecoder.decode(item.getValue().getName(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = icon_generator.makeIcon(name);
                    item.getKey().setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                }
            }
        }
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
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        // add zoom level callback handler
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (current_zoom_level != cameraPosition.zoom){
                    current_zoom_level = cameraPosition.zoom;
                    LogUtils.debug("Current zoom level is:" + cameraPosition.zoom);
                    if (cameraPosition.zoom >= zoom_level_threshold){
                        update_labled_icons("add");
                    }else{
                        update_labled_icons("remove");
                    }
                }
            }
        });

        // add marker onclick callback
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Attraction attraction = attraction_marker_map.get(marker);
                LogUtils.debug("Attraction " + attraction.getName() + " has " + attraction.getImages());
                return true;
            }
        });
        // Add a marker in current location and move the camera
        updateCurrentMarkerOnMap();
    }

    private void updateCurrentMarkerOnMap(){
        if (mLastLocation != null && mMap != null) {
//            Double current_lat = mLastLocation.getLatitude();
//            Double current_lng = mLastLocation.getLongitude();
//            LatLng currentLocation = new LatLng(current_lat, current_lng);
            // get list of nearby point of interests based on the current location
            all_attractions = GeoUtils.get_all_attractions(db_connection).iterator();
            // go over the result and create markers, create a function for this later
            while (all_attractions.hasNext()){
                Attraction attraction_info = all_attractions.next();
                IconGenerator icon_generator= new IconGenerator(this);
                icon_generator.setStyle(IconGenerator.STYLE_GREEN);
                Bitmap bitmap = icon_generator.makeIcon(attraction_info.getName());
                MarkerOptions marker_options = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .position(new LatLng(attraction_info.getLat(), attraction_info.getLng()))
                        .title(attraction_info.getName());
                attraction_marker_map.put(mMap.addMarker(marker_options), attraction_info);
            }
//            currentLocationMarker = new MarkerOptions().position(currentLocation).title("You are here!");
//            mMap.addMarker(currentLocationMarker);
            // move camera to the church of our lady location which will be the center of the view
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.051895, 13.741579), default_zoom_level));
        }
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
