package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void overviewactivity(View view) {

        Intent overview = new Intent(this, ButtonNewActivity.class);
        startActivity(overview);
    }
    public void whatshereactivity(View view) {

        Intent whatshere = new Intent(this, WhatsHereActivity.class);
        startActivity(whatshere);
    }
    public void mapactivity(View view) {

        Intent map = new Intent(this, MapActivity.class);
        startActivity(map);
    }
    public void toursactivity(View view) {

        Intent tours = new Intent(this, ToursActivity.class);
        startActivity(tours);
    }
    public void aboutactivity(View view) {

        Intent about = new Intent(this, AboutActivity.class);
        startActivity(about);
    }
}
