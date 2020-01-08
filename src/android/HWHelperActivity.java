package cordova.plugin.hwhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Map;
import java.util.HashMap;

public class HWHelperActivity extends Activity {
    private static String TAG = "HWHelperActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "==> HWHelperActivity onCreate");
		
		// Map<String, Object> data = new HashMap<String, Object>();
        // if (getIntent().getExtras() != null) {
		// 	Log.d(TAG, "==> USER TAPPED NOTFICATION");
		// 	// data.put("wasTapped", true);
		// 	for (String key : getIntent().getExtras().keySet()) {
        //         String value = getIntent().getExtras().getString(key);
        //         Log.d(TAG, "\tKey: " + key + " Value: " + value);
		// 		data.put(key, value);
        //     }
        // }
		
        // HWHelper.sendPushPayload(data);
        HWHelper.sendPushPayload1(getIntent());

        finish();

        forceMainActivityReload();
    }

    private void forceMainActivityReload() {
        PackageManager pm = getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
        startActivity(launchIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
		Log.d(TAG, "==> HWHelperActivity onResume");
    }
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "==> HWHelperActivity onStart");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "==> HWHelperActivity onStop");
	}
}