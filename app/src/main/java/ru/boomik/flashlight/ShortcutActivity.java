package ru.boomik.flashlight;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class ShortcutActivity extends Activity {

    private CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);
        checkbox = (CheckBox) findViewById(R.id.white);
    }

    public void Select (View v) {
        returnShortcut(v.getId()==R.id.dimming);
    }

    private void returnShortcut(boolean dimming) {
        Intent shortcutIntent = new Intent(this, StartFlashActivity.class);
        if (dimming) shortcutIntent.setAction("ScreenOff");
        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        boolean white = checkbox.isChecked();
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, white ? R.drawable.ic_notify : R.mipmap.ic_launcher));
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(this.getApplicationInfo().labelRes));
        setResult(RESULT_OK, intent);
        finish();
    }
}
