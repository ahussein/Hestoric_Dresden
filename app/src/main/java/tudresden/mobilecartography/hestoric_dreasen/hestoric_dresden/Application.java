package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

/**
 * Created by Ally on 16.12.2015.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontOverride.setDefaultFont(this, "DEFAULT", "fonts/simplifica_typeface.ttf");
    }
}
