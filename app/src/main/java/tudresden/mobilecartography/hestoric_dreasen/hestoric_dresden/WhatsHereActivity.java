package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WhatsHereActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private DatabaseHelper db_helper = new DatabaseHelper(this);
    private SQLiteDatabase db_connection;
    private double radius = 20000.0; // 500 meters radius

    ListView list;
    List<AttractionResult> attractions;
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
        setTitle("What is here ?");
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
            // get list of nearby point of interests based on the current location
            Iterator<AttractionResult> nearby_attractions = GeoUtils.get_nearby_attractions(current_lat, current_lng, radius, db_connection).iterator();
            int n = 0;

            attractions = new ArrayList<>();

            while (nearby_attractions.hasNext() && n < 3) {
                AttractionResult attraction_info = nearby_attractions.next();
                attractions.add(attraction_info);
                n = n + 1;
            }
            if (n > 0){
                Iterator<AttractionResult> attractions_iter = attractions.iterator();
                String result[] = new String[n];
                String imageurl[] = new String[n];
                int m = 0;
                while (attractions_iter.hasNext()) {
                    AttractionResult attr_result = attractions_iter.next();
                    if(attr_result.getAttr().getImages().size() > 0)
                    {
                        imageurl[m] = attr_result.getAttr().getImages().get(0).getImage_url() ;
                    }
                    else{
                        imageurl[m] =  "https://tradingnav.com/Content/image/noimage.jpg" ;
                    }
                    result[m] = (attr_result.getAttr().getName() + " \n " + (long) attr_result.getDistance()+ " mt");
                    m = m + 1;
                }
                CustomListAdapter adapter=new CustomListAdapter(this, result, imageurl);
                list=(ListView)findViewById(R.id.list);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new ListView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int i, long l) {


                        Intent intent = new Intent(WhatsHereActivity.this, ImageViewer.class);
                        intent.putExtra("attraction", attractions.get(i).getAttr());
                        startActivity(intent);
                    }
                });


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



