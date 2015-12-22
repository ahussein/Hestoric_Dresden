package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

/**
 * Created by abdelrahman on 22/12/15.
 */
import android.util.Log;

public class LogUtils {

    public static String tag = "Historic_Dresden";

    public static void info(String message) {
        /**
         * Log a message to the android system
         */
        Log.i(tag, message);
    }

    public static void verbose(String message) {
        /**
         * Log a message to the android system
         */
        Log.v(tag, message);
    }

    public static void debug(String message) {
        /**
         * Log a message to the android system
         */
        Log.d(tag, message);
    }

    public static void warning(String message) {
        /**
         * Log a message to the android system
         */
        Log.w(tag, message);
    }

    public static void error(String message) {
        /**
         * Log a message to the android system
         */
        Log.e(tag, message);
    }
}
