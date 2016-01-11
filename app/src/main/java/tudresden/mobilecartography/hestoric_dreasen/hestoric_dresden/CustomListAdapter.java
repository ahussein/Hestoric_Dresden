package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;


        import android.app.Activity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] imageurl;
    ImageUtils imageresult ;

    public CustomListAdapter(Activity context, String[] itemname, String[] imageurl) {
        super(context, R.layout.whatsherelist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imageurl=imageurl;
        this.imageresult = new ImageUtils(context);
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.whatsherelist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.listtext);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);


        txtTitle.setText(itemname[position]);
        this.imageresult.draw_image_from_url(this.imageurl[position],imageView );

        return rowView;

    };
}