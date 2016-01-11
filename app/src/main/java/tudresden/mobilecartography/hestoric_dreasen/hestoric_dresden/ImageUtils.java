package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Utilities for images retrieval and manipulation
 */
public class ImageUtils {
    Context context;
    Picasso picasso;
    ImageUtils(Context context){
        this.context = context;
    }

    public void draw_image_from_url(String url_string, ImageView target_image_view){
        Picasso p = (picasso != null) ? picasso : Picasso.with(context);
        RequestCreator rq = null;
        if(url_string!=null){
            rq = p.load(url_string);
        }else{
            return;
        }

        if(rq == null){
            return;
        }

//        if(getEmpty() != 0){
//            rq.placeholder(getEmpty());
//        }
//
//        if(getError() != 0){
//            rq.error(getError());
//        }

//        switch (scale_type){
//            case Fit:
//                rq.fit();
//                break;
//            case CenterCrop:
//                rq.fit().centerCrop();
//                break;
//            case CenterInside:
//                rq.fit().centerInside();
//                break;
//            case FitCenterCrop:
//                rq.fit().centerInside();
//        }

        rq.into(target_image_view,new Callback() {
            @Override
            public void onSuccess() {
//                if(v.findViewById(R.id.loading_bar) != null){
//                    v.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
//                }
            }

            @Override
            public void onError() {
//                if(mLoadListener != null){
//                    mLoadListener.onEnd(false,me);
//                }
//                if(v.findViewById(R.id.loading_bar) != null){
//                    v.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
//                }
            }
        });
    }


}
