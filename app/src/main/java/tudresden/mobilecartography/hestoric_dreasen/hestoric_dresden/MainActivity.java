package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void OverviewActivity(View view) {

        Intent overview = new Intent(this, ButtonNewActivity.class);
        startActivity(overview);
    }

    //the function that gets the user location
    public void WhatsHereActivity(View view) {


        Intent whatshere = new Intent(this, WhatsHereActivity.class);
        startActivity(whatshere);
    }

    public void MapActivity(View view) {

        Intent map = new Intent(this, MapsActivity.class);
        startActivity(map);
    }

    public void ToursActivity(View view) {

        Intent tours = new Intent(this, ToursActivity.class);
        startActivity(tours);
    }

    public void AboutActivity(View view) {

        Intent about = new Intent(this, AboutActivity.class);
        startActivity(about);
    }
}
