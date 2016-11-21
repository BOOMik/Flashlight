package ru.boomik.flashlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class StartFlashActivity extends Activity implements FlashlightController.TorchStateChanged {

    private FlashlightController flash;
    private CircleBackgroundView backgroundView;
    private SwitchView switchButton;
    private boolean startApp;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        startApp = true;
        boolean needLock = false;
        Intent intent = getIntent();
        if (intent!=null) {
            String action = getIntent().getAction();
            needLock = action != null && action.equals("ScreenOff");

            if (!needLock) {
                Bundle extras = intent.getExtras();
                needLock = needLock || (extras != null && extras.containsKey("com.android.systemui.camera_launch_source") && extras.get("com.android.systemui.camera_launch_source").equals("lockscreen_affordance"));
            }
        }

        if (needLock) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            params.screenBrightness = 0;
            getWindow().setAttributes(params);
        }

        switchButton = (SwitchView) findViewById(R.id.switch_button);

        findViewById(R.id.frame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchButton.setChecked(!switchButton.isChecked(),true);
            }
        });

        backgroundView = (CircleBackgroundView) findViewById(R.id.reveal);

        if (switchButton != null && backgroundView != null) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary, getResources().newTheme()));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, getResources().newTheme()));
            backgroundView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            switchButton.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View view, boolean isChecked) {
                    startFlash(isChecked);
                }
            });
            switchButton.setChecked(true,true);
        }

        flash = FlashlightController.getInstance(this);
        flash.addTorchStateChangedListener(this);
        startFlash(true);

    }

    private void changeState (boolean enable) {
        switchButton.setChecked(enable,true);
        backgroundView.setState(enable);
        if (enable) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary, getResources().newTheme()));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, getResources().newTheme()));
			backgroundView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
			backgroundView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent, getResources().newTheme()));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent, getResources().newTheme()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (startApp) {
            backgroundView.show();
            startApp = false;
        }
    }

    private void startFlash(boolean flashIsOn) {
        if (flash==null) return;
        if (flashIsOn) {
            flash.turnOnFlashLight();
        } else {
            flash.turnOffFlashLight();
        }
    }

    private void stop() {
        if (flash==null) return;
        flash.close();
        flash=null;
    }

    @Override
    public void onBackPressed() {
        stop();
        if (backgroundView !=null && backgroundView.isShowed()) {
            backgroundView.hide();
            switchButton.setChecked(false,true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishThis();
                }
            }, 200);
        } else finishThis();
    }

    private void finishThis() {
        finish();
        overridePendingTransition(0,R.anim.close_activity);
    }

    @Override
    protected void onDestroy() {
        FlashlightController.destroy();
        super.onDestroy();
    }

    @Override
    public void torchStateChanged(boolean state) {
        changeState(state);
    }
}
