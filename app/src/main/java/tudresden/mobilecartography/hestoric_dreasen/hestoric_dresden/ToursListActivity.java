package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToursListActivity extends AppCompatActivity{

    ListView list;

    List<List<String>> toursInfo = new ArrayList<>();
    String[] titles = new String[3];
    List<String> generalTour = new ArrayList<>();
    List<String> churchTour = new ArrayList<>();
    List<String> museumTour = new ArrayList<>();
    Map<String, String> title_filename = new HashMap<>();



    void onListItemClick(AdapterView<?> a, View v, int i, long l) {
        Intent intent = new Intent(this, ToursActivity.class);
        TextView title = (TextView) v.findViewById(R.id.title);
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("filename", title_filename.get(title.getText().toString()));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Tours");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours_list);
        title_filename.put("General Tour", "route.geojson");
        title_filename.put("Tour of the churches", "church.geojson");
        title_filename.put("Tour of the museums", "museums.geojson");
        generalTour.add("General Tour");
        titles[0] = "General Tour";
        generalTour.add("Time: 40 min, Length: 3 km");
        generalTour.add("Zwinger, Semperoper, Katolische Hofkirche, Brühl's Terrace, Brühlschen Garten, Albertinum, Frauenkirche, " +
                "Holy Cross Church, Altmarkt, Castle");
        toursInfo.add(generalTour);
        churchTour.add("Tour of the churches");
        titles[1] = "Tour of the churches";
        churchTour.add("Time: 30 min, Length: 2,3 km");
        churchTour.add("Neue Synagoge," +
                " the Holy Cross Church, Busmannkapelle, the Dresden Cathedral, finishing at the most symbolic church of Dresden, the Frauenkirche.");
        toursInfo.add(churchTour);
        museumTour.add("Tour of the museums");
        titles[2] = "Tour of the museums";
        museumTour.add("Time: 25 min, Length: 2 km");
        museumTour.add("City Museum, " +
                "Albertinum, the Museum Festung, Türkische Cammer, Kupferstich Kabinett, Neues Grünes Gewölbe, " +
                "three exibitions in Zwinger (Old Masters Picture Gallery, Porzellansammlung, Mathematisch-Physikalischer Salon).");
        toursInfo.add(museumTour);

        ToursListArrayAdapter adapter=new ToursListArrayAdapter(this, titles, toursInfo);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                onListItemClick(a, v, i, l);
            }
        });
    }

}