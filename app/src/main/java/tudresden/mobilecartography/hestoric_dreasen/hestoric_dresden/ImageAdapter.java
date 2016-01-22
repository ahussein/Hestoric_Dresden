package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImageAdapter extends PagerAdapter {
    Context context;
    private Attraction attraction;
    private ImageUtils image_utils;
    private List<AttractionImage> attraction_images;
    /**
     * Scale type of the image.
     */
    private ScaleType scale_type = ScaleType.Fit;

    public enum ScaleType{
        CenterCrop, CenterInside, Fit, FitCenterCrop
    }

    ImageAdapter(Context context, Attraction attraction){
        this.context=context;
        this.image_utils = new ImageUtils(this.context);
        this.attraction = attraction;
        this.attraction_images = attraction.getImages();
    }
    @Override
    public int getCount() {
        if (attraction != null)
            return attraction.getImages().size();
        else
            return 0;
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AttractionImage attraction_image = attraction_images.get(position);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view_item = inflater.inflate(R.layout.attraction_image_view, container, false);
        ImageView image_view = (ImageView) view_item.findViewById(R.id.image_item);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        image_view.setPadding(padding, padding, padding, padding);
        image_view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.image_utils.draw_image_from_url(attraction_image.getImage_url(), image_view);
        TextView date = (TextView) view_item.findViewById(R.id.date);
        date.setText(attraction_image.getDate());
        TextView description = (TextView) view_item.findViewById(R.id.description);
        String final_description = (!attraction_image.getDescription().equals("")? (attraction_image.getDescription() + "\n\n"): "") + this.attraction.getDescription();
        description.setText(final_description);
        ((ViewPager) container).addView(view_item, 0);
        return view_item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}