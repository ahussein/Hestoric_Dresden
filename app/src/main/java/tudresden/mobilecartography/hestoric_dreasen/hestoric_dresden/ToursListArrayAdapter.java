package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Joó Kata on 2016.01.20..
 */
public class ToursListArrayAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] titles;
    List<List<String>> toursInfo;

    public ToursListArrayAdapter(Activity context, String[] titles, List<List<String>> toursInfo) {
        super(context, R.layout.tourslistlist, titles);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.titles=titles;
        this.toursInfo = toursInfo;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.tourslistlist, null, true);

        TextView title = (TextView) rowView.findViewById(R.id.title);
        title.setText(this.titles[position]);

        TextView duration = (TextView) rowView.findViewById(R.id.duration);
        duration.setText(this.toursInfo.get(position).get(1));

        TextView description = (TextView) rowView.findViewById(R.id.description);
        description.setText(this.toursInfo.get(position).get(2));
        return rowView;

    };
}
