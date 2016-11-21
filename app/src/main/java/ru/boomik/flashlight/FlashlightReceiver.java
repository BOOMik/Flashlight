package ru.boomik.flashlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FlashlightReceiver extends BroadcastReceiver {

    public static final String ACTION_SWITCH = "ru.boomik.flashlight.SWITCH";
    public static final String ACTION_ON = "ru.boomik.flashlight.ENABLE";
    public static final String ACTION_OFF = "ru.boomik.flashlight.DISABLE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent==null) return;
        String action = intent.getAction();
        Log.e("Flash", action);
        FlashlightController controller = FlashlightController.getInstance();
        switch (action) {
            case ACTION_SWITCH:
                if (controller==null) controller = FlashlightController.getInstance(context);
                controller.flashSwitch();
                break;
            case ACTION_ON:
                if (controller==null) controller = FlashlightController.getInstance(context);
                controller.turnOnFlashLight();
                break;
            case ACTION_OFF:
                if (controller!=null) controller.turnOffFlashLight();
                break;
        }
    }
}
