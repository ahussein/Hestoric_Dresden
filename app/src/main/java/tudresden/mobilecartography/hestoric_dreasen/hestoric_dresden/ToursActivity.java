package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;


import android.graphics.drawable.Drawable;


import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.graphics.Color;
import android.os.AsyncTask;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;

import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;






public class ToursActivity extends AppCompatActivity {

    private MapView mapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Guided Tours");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);

        IconFactory mIconFactory = IconFactory.getInstance(this);
        Drawable mStartIconDrawable = ContextCompat.getDrawable(this, R.drawable.start);
        Icon startIcon = mIconFactory.fromDrawable(mStartIconDrawable);
        Drawable mStopIconDrawable = ContextCompat.getDrawable(this, R.drawable.stop);
        Icon stopIcon = mIconFactory.fromDrawable(mStopIconDrawable);


        /** Create a mapView and give it some properties */
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setStyleUrl("https://www.mapbox.com/android-sdk/files/mapbox-raster-v8.json");
        mapView.setCenterCoordinate(new LatLng(51.052132, 13.739489));
        mapView.setZoomLevel(14);
        List <LatLng> points_simpleArray = new ArrayList ();
        points_simpleArray.add(new LatLng (51.0531004, 13.7340542) ) ;
        points_simpleArray.add(new LatLng (51.0542293, 13.7356479) ) ;
        points_simpleArray.add(new LatLng (51.0538089, 13.7370684) ) ;
        points_simpleArray.add(new LatLng (51.0533363, 13.7436782) ) ;
        points_simpleArray.add(new LatLng (51.0527318, 13.7451036) ) ;
        points_simpleArray.add(new LatLng (51.0518214, 13.7439931) ) ;
        points_simpleArray.add(new LatLng (51.051626, 13.741618) ) ;
        points_simpleArray.add(new LatLng (51.0489821, 13.7391163) ) ;
        points_simpleArray.add(new LatLng (51.0520859, 13.736437) ) ;


        List<String> title =  Arrays.asList("A", "B", "C", "D","E","F","G");
        for(int i = 0; i< points_simpleArray.size(); i++)
        {
            if(i>0 && i<(points_simpleArray.size() - 1)) {
                mapView.addMarker(new MarkerOptions()
                        .position(points_simpleArray.get(i))
                        .title(title.get(i-1)));
            }
            if(i == 0){
                mapView.addMarker(new MarkerOptions()
                                .position(points_simpleArray.get(i))
                                .title("Start")
                                 .icon(startIcon)
                );
            }
            if(i== (points_simpleArray.size()-1)){
                mapView.addMarker(new MarkerOptions()
                        .position(points_simpleArray.get(i))
                        .title("End")
                        .icon(stopIcon)
                );
            }
        }
        mapView.onCreate(savedInstanceState);
        // Load and Draw the GeoJSON
        new DrawGeoJSON().execute();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private class DrawGeoJSON extends AsyncTask<Void, Void, List<LatLng>> {
        private final String TAG = "";

        @Override
        protected List<LatLng> doInBackground(Void... voids) {

            ArrayList<LatLng> points = new ArrayList<LatLng>();

            try {
                // Load GeoJSON file
                InputStream inputStream = getAssets().open("route.geojson");
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }

                inputStream.close();

                // Parse JSON
                JSONObject json = new JSONObject(sb.toString());
                JSONArray features = json.getJSONArray("features");
                JSONObject feature = features.getJSONObject(0);
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry != null) {
                    String type = geometry.getString("type");

                    // Our GeoJSON only has one feature: a line string
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {

                        // Get the Coordinates
                        JSONArray coords = geometry.getJSONArray("coordinates");
                        for (int lc = 0; lc < coords.length(); lc++) {
                            JSONArray coord = coords.getJSONArray(lc);
                            LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                            points.add(latLng);
                        }
                    }

                }
            } catch (Exception e) {
                Log.e(TAG, "Exception Loading GeoJSON: " + e.toString());
            }

            return points;

        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            super.onPostExecute(points);

            if (points.size() > 0) {
                LatLng[] pointsArray = points.toArray(new LatLng[points.size()]);

                // Draw Points on MapView
                mapView.addPolyline(new PolylineOptions()
                        .add(pointsArray)
                        .color(Color.parseColor("#562c10"))
                        .width(3));




            }
        }

    }
}