package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class ImageViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        Intent intent = getIntent();
        Attraction attraction = intent.getParcelableExtra("attraction");
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(this, attraction);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
