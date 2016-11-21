package ru.boomik.flashlight;

import android.app.Application;
import android.util.Log;

/**
 * Created by boomv on 22.05.2016.
 */
public class FlashlightApp extends Application {
    @Override
    public void onTerminate() {
        Log.e("Flash", "Terminate");
        FlashlightController.destroy();
        super.onTerminate();
    }
}
