package trees.park.cal.smoke.models;


import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;

public class SpiceManagerSingleton {
    private static SpiceManager INSTANCE = new SpiceManager(JacksonGoogleHttpClientSpiceService.class);

    public static SpiceManager getSpiceManager() {
        return INSTANCE;
    }
}
