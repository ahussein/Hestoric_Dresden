package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;


import android.content.Intent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ToursActivity extends AppCompatActivity {

    private MapView mapView = null;
    String title;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Guided Tours");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        filename = intent.getStringExtra("filename");
        IconFactory mIconFactory = IconFactory.getInstance(this);
        Drawable mStartIconDrawable = ContextCompat.getDrawable(this, R.drawable.start);
        Icon startIcon = mIconFactory.fromDrawable(mStartIconDrawable);
        Drawable mStopIconDrawable = ContextCompat.getDrawable(this, R.drawable.stop);
        Icon stopIcon = mIconFactory.fromDrawable(mStopIconDrawable);
        Drawable mMarkerIconDrawable = ContextCompat.getDrawable(this, R.drawable.mapboxmarker);
        Icon markerIcon = mIconFactory.fromDrawable(mMarkerIconDrawable);


        /** Create a mapView and give it some properties */
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setStyleUrl("https://www.mapbox.com/android-sdk/files/mapbox-raster-v8.json");
        mapView.setCenterCoordinate(new LatLng(51.052132, 13.739489));
        mapView.setZoomLevel(14);
        Map<String, List <LatLng>> tour_markers_map = new HashMap<>();
        Map<String, List<String>> tour_titles_map = new HashMap<>();

        List <LatLng> general_tour_points = new ArrayList ();
        general_tour_points.add(new LatLng (51.0531004, 13.7340542) ) ;
        general_tour_points.add(new LatLng (51.0542293, 13.7356479) ) ;
        general_tour_points.add(new LatLng (51.0538089, 13.7370684) ) ;
        general_tour_points.add(new LatLng (51.0533363, 13.7436782) ) ;
        general_tour_points.add(new LatLng (51.0527318, 13.7451036) ) ;
        general_tour_points.add(new LatLng (51.0518214, 13.7439931) ) ;
        general_tour_points.add(new LatLng(51.051626, 13.741618)) ;
        general_tour_points.add(new LatLng(51.0489821, 13.7391163)) ;
        general_tour_points.add(new LatLng(51.0520859, 13.736437)) ;
        List<String> general_tour_titles =  Arrays.asList("Zwinge", "Semperoper", "Katholische Hofkirche",
                "Brühl's Terrace","Brühlschen Garten","Albertinum","Frauenkirche","Holy Cross Church", "Dresden Castle");

        tour_markers_map.put("General Tour", general_tour_points);
        tour_titles_map.put("General Tour", general_tour_titles);

        List <LatLng> church_tour_points = new ArrayList ();
        general_tour_points.add(new LatLng (51.0522831, 13.7450394) ) ;
        general_tour_points.add(new LatLng (51.05195270000001, 13.7467555) ) ;
        general_tour_points.add(new LatLng (51.0489821, 13.7391163) ) ;
        general_tour_points.add(new LatLng (51.051182, 13.7351195) ) ;
        general_tour_points.add(new LatLng (51.0538099, 13.7370709) ) ;
        general_tour_points.add(new LatLng(51.051626, 13.741618)) ;
        List<String> church_tour_titles =  Arrays.asList("Evangelisch- Reformierte Gemeinde zu Dresden", "Neue Synagoge", "Holy Cross Church",
                "Busmannkapelle", "Dresden Cathedral", "Frauenkirche");

        tour_markers_map.put("Church Tour", church_tour_points);
        tour_titles_map.put("Church Tour", church_tour_titles);

        List <LatLng> museum_tour_points = new ArrayList ();
        general_tour_points.add(new LatLng (51.050241, 13.7431273) ) ;
        general_tour_points.add(new LatLng(51.052216900000005, 13.7448015)) ;
        general_tour_points.add(new LatLng(51.0526457, 13.7438185)) ;
        general_tour_points.add(new LatLng (51.0521963, 13.7398583) ) ;
        general_tour_points.add(new LatLng (51.0523371, 13.7377039) ) ;
        general_tour_points.add(new LatLng (51.0521531, 13.7361236)) ;
        general_tour_points.add(new LatLng (51.0531531, 13.7369033) ) ;
        general_tour_points.add(new LatLng (51.053367, 13.7347288) ) ;
        general_tour_points.add(new LatLng (51.052513, 13.7337691)) ;
        general_tour_points.add(new LatLng (51.0531757, 13.7330282)) ;
        List<String> museum_tour_titles =  Arrays.asList("Dresden City Museum", "Albertinum", "Museum Festung Dresden", "Dresden Transport Museum",
                "Kupferstich-Kabinett", "Türckische Cammer", "Neues Grünes Gewölbe", "Old Masters Picture Gallery", "Porzellansammlung", "Mathematisch-Physikalischer Salon"  );

        tour_markers_map.put("Museum Tour", museum_tour_points);
        tour_titles_map.put("Museum Tour", museum_tour_titles);

        List<LatLng> points_simpleArray = tour_markers_map.get(this.title);
        List<String> titles = tour_titles_map.get(this.title);
        for(int i = 0; i< points_simpleArray.size(); i++)
        {
            if(i>0 && i<(points_simpleArray.size() - 1)) {
                mapView.addMarker(new MarkerOptions()
                        .position(points_simpleArray.get(i))
                        .title(titles.get(i))
                        .icon(markerIcon)
                );
            }
            if(i == 0){
                mapView.addMarker(new MarkerOptions()
                                .position(points_simpleArray.get(i))
                                .title(titles.get(i))
                                 .icon(startIcon)
                );
            }
            if(i== (points_simpleArray.size()-1)){
                mapView.addMarker(new MarkerOptions()
                        .position(points_simpleArray.get(i))
                        .title(titles.get(i))
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

    String getFileName(){
        return this.filename;
    }
    private class DrawGeoJSON extends AsyncTask<Void, Void, List<LatLng>> {
        private final String TAG = "";

        @Override
        protected List<LatLng> doInBackground(Void... voids) {

            ArrayList<LatLng> points = new ArrayList<LatLng>();

            try {
                // Load GeoJSON file
                InputStream inputStream = getAssets().open(getFileName());
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