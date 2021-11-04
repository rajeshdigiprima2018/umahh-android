package app.com.ummah;

import android.app.Application;
import android.location.Location;

import net.time4j.android.ApplicationStarter;

public class MyApps extends Application {
    public static Location LOCATION;
    public static String PLACE="";

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationStarter.initialize(this, true);
    }
}
