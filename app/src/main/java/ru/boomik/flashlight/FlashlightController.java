package ru.boomik.flashlight;

import android.content.Context;
import android.hardware.camera2.CameraManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by boomv on 22.05.2016.
 */
public class FlashlightController extends CameraManager.TorchCallback {
    private static FlashlightController ourInstance;
    private boolean flashState;

    public static FlashlightController getInstance(Context context) {
        if (ourInstance == null) ourInstance = new FlashlightController(context.getApplicationContext());
        return ourInstance;
    }

    public static void destroy() {
        if (ourInstance!=null) ourInstance.close();
    }

    public static FlashlightController getInstance() {
        return ourInstance;
    }

    private final Context context;

    private CameraManager mCameraManager;
    private ArrayList<TorchStateChanged> listeners = new ArrayList<>();

    public FlashlightController(Context context) {
        this.context = context;
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        mCameraManager.registerTorchCallback(this, null);
    }

    public boolean isEnabled() {
        return flashState;
    }

    public void flashSwitch() {
        if (flashState)turnOffFlashLight(); else turnOnFlashLight();
    }

    public interface TorchStateChanged {
        void torchStateChanged(boolean state);
    }

    public void addTorchStateChangedListener(@NotNull TorchStateChanged torchStateChanged) {
        if (listeners.contains(torchStateChanged)) return;
        listeners.add(torchStateChanged);
    }
    public void removeTorchStateChangedListener(@NotNull TorchStateChanged torchStateChanged) {
        if (!listeners.contains(torchStateChanged)) return;
        listeners.remove(torchStateChanged);
        if (listeners.isEmpty()) {
            close();
        }
    }

    @Override
    public void onTorchModeChanged(@NotNull String cameraId, boolean enabled) {
        super.onTorchModeChanged(cameraId, enabled);
        flashState = enabled;
        for (TorchStateChanged listener : listeners) {
            if (listener!=null) listener.torchStateChanged(enabled);
        }
        NotificationCenter.controlNotification(context, enabled);
        FlashlightWidget.updateAll(context, enabled);
    }

    public void turnOnFlashLight() {
        if (mCameraManager==null) return;
        try {
            mCameraManager.setTorchMode("0", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight() {
        if (mCameraManager==null) return;
        try {
            mCameraManager.setTorchMode("0", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        ourInstance=null;
        NotificationCenter.controlNotification(context, false);
        FlashlightWidget.updateAll(context, false);
        turnOffFlashLight();
        if (mCameraManager==null) return;
        mCameraManager=null;
    }
}
