package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void OverviewActivity(View view) {
        startActivity(new Intent(this, OverviewActivity.class));
    }

    //the function that gets the user location
    public void WhatsHereActivity(View view) {
        startActivity(new Intent(this, WhatsHereActivity.class));
    }

    public void MapActivity(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void ToursActivity(View view) {
        startActivity(new Intent(this, ToursListActivity.class));
    }

    public void AboutActivity(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }
}
