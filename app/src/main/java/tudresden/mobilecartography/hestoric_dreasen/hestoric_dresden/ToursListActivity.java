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

public class ToursListActivity extends AppCompatActivity{

    ListView list;

    List<List<String>> toursInfo = new ArrayList<>();
    String[] titles = new String[3];
    List<String> generalTour = new ArrayList<>();
    List<String> churchTour = new ArrayList<>();
    List<String> museumTour = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Tours");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours_list);
        generalTour.add("General Tour");
        titles[0] = "General Tour";
        generalTour.add("Time: 40 min, Length: 3 km");
        generalTour.add("This 40 minute round tour guides you through the most important landmarks in the old town of Dresden. Starting from Zwinger, passing by the opera, Katolische Hofkirche, Brühl's Terrace, Brühlschen Garten, Albertinum, Frauenkirche, Holy Cross Church, Altmarkt and finally arriving to the Castle near the starting point.");
        toursInfo.add(generalTour);
        churchTour.add("Tour of the churches");
        titles[1] = "Tour of the churches";
        churchTour.add("Time: 30 min, Length: 2,3 km");
        churchTour.add("The tour of the churches guides you through the most important churches in Altstadt starting at the Neue Synagoge and passing by the Holy Cross Church, Busmannkapelle, the Dresden Cathedral, finishing at the most symbolic church of Dresden, the Frauenkirche.");
        toursInfo.add(churchTour);
        museumTour.add("Tour of the museums");
        titles[2] = "Tour of the museums";
        museumTour.add("Time: 25 min, Length: 2 km");
        museumTour.add("In this short walking path, you get to know the museums in the most historic part of Dresden and either visit them or just admire the magnificent pieces of architecture from outside. The tour starts from the City Museum, and leads you along Albertinum, the Museum Festung, Türkische Cammer, Kupferstich Kabinett, Neues Grünes Gewölbe, finishing with the three exibitions in Zwinger (Old Masters Picture Gallery, Porzellansammlung, Mathematisch-Physikalischer Salon).");
        toursInfo.add(museumTour);

        ToursListArrayAdapter adapter=new ToursListArrayAdapter(this, titles, toursInfo);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                //Intent intent = new Intent(OverviewActivity.this);
                //intent.putExtra("attraction", attractions.get(i).getAttr());
                //startActivity(intent);
            }
        });
    }

}