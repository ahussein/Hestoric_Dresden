package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

/**
 * Overrides the default application to change the default font
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontOverride.setDefaultFont(this, "DEFAULT", "fonts/simplifica_typeface.ttf");
    }
}
