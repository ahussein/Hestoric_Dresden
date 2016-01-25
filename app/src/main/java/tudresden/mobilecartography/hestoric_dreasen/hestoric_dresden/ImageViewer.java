package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.viewpagerindicator.CirclePageIndicator;

public class ImageViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Attraction attraction = intent.getParcelableExtra("attraction");
        setTitle(attraction.getName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);


        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(this, attraction);
        viewPager.setAdapter(adapter);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        // Set circle indicator radius
        indicator.setRadius(5 * density);
        indicator.setFillColor(R.color.colorPrimaryDark);
        indicator.setStrokeColor(R.color.text_color);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
