package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that contains utils functions for geo-spatial operations
 */
public class GeoUtils {
    static double earth_radius = 6371.0; // in kilometers

    /**
     * Calaculates a distance between two points
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double calculate_distance(double lat1, double lng1, double lat2, double lng2) {
        Location source = new Location("source");
        source.setLatitude(lat1);
        source.setLongitude(lng1);
        Location dest  = new Location("dist");
        dest.setLatitude(lat2);
        dest.setLongitude(lng2);
        return source.distanceTo(dest);
    }

    public static ArrayList<AttractionResult> get_nearby_attractions(Double current_lat, Double current_lng, double radius, SQLiteDatabase db_connection){
        /**
         * Get the nearby attraction according to the current location and the radius
         */
        Cursor db_cursor;
        ArrayList result = new ArrayList();
        try {
            db_cursor = db_connection.rawQuery("select * from main_data;", null);
            db_cursor.moveToFirst();
            while (!db_cursor.isAfterLast()) {
                Attraction attraction_info = new Attraction();
                attraction_info.setName(db_cursor.getString(db_cursor.getColumnIndex("name")));
                attraction_info.setLat(db_cursor.getDouble(db_cursor.getColumnIndex("lat")));
                attraction_info.setLng(db_cursor.getDouble(db_cursor.getColumnIndex("lng")));
                attraction_info.setDate_of_creation(db_cursor.getString(db_cursor.getColumnIndex("time_of_creation")));
                attraction_info.setDescription(db_cursor.getString(db_cursor.getColumnIndex("description")));
                double distance = calculate_distance(current_lat, current_lng, attraction_info.getLat(), attraction_info.getLng());
                if (distance <= radius){
                    AttractionResult attr_result = new AttractionResult();
                    attr_result.setAttr(attraction_info);
                    attr_result.setDistance(distance);
                    result.add(attr_result);
                }
                LogUtils.debug("Distance between current location and " + attraction_info.getName() + " is " + distance + " meters");
                db_cursor.moveToNext();
            }
        }catch(Exception e){
            LogUtils.error("Failed to get nearby attractions. Reason: " + e.getMessage());
        }

        Collections.sort(result);
        return result;

    }
}

class Attraction {
    private String name;
    private double lat;
    private double lng;
    private String date_of_creation;
    private String description;

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDate_of_creation() {
        return date_of_creation;
    }

    public void setDate_of_creation(String date_of_creation) {
        this.date_of_creation = date_of_creation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class AttractionResult implements Comparable<AttractionResult>{
    private Attraction attr;
    private double distance;

    @Override
    public int compareTo(AttractionResult attr_result){
        if (distance > attr_result.getDistance()){
            return 1;
        }else if (distance < attr_result.getDistance()){
            return -1;
        }else{
            return 0;
        }
    }

    public Attraction getAttr() {
        return attr;
    }

    public void setAttr(Attraction attr) {
        this.attr = attr;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}