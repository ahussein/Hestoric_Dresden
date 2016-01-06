package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WhatsHereActivity extends ListActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private DatabaseHelper db_helper = new DatabaseHelper(this);
    private SQLiteDatabase db_connection;
    private Cursor db_cursor;
    private double radius = 20.0; // 500 meters radius
    TextView latitudeField;
    TextView longitudeField;

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        // create the database connection
        try {
            db_helper.createDataBase();
            db_connection = db_helper.getDataBase();
        } catch (IOException ioe) {
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
        setContentView(R.layout.activity_whats_here);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Double current_lat = mLastLocation.getLatitude();
            Double current_lng = mLastLocation.getLongitude();
            LatLng currentLocation = new LatLng(current_lat, current_lng);
            // get list of nearby point of interests based on the current location
            Iterator<AttractionResult> nearby_attractions = GeoUtils.get_nearby_attractions(current_lat, current_lng, radius, db_connection).iterator();
            int n = 0;
            List<AttractionResult> attractions = new ArrayList();

            while (nearby_attractions.hasNext() && n < 3) {
                AttractionResult attraction_info = nearby_attractions.next();
                attractions.add(attraction_info);
                n = n + 1;
            }
            if (n > 0){
                Iterator<AttractionResult> attractions_iter = attractions.iterator();
                String result[] = new String[n];
                int m = 0;
                while (attractions_iter.hasNext()) {
                    AttractionResult attr_result = attractions_iter.next();
                    result[m] = ("Attraction Name: " + attr_result.getAttr().getName() + "  \nDistance: " + (long) attr_result.getDistance() + " metres");
                    m = m + 1;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        R.layout.whatsherelist, result);
                setListAdapter(adapter);
            }else{


                AlertDialog alertDialog = new AlertDialog.Builder(
                        WhatsHereActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Sorry !");

                // Setting Dialog Message
                alertDialog.setMessage("No nearby attractions..");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.sorry);



                // Showing Alert Message
                alertDialog.show();

            }


        }


    }



    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}



