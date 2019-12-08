package cordova.plugin.hwhelper;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Iterator;
import android.os.Bundle;
public class HWHelper extends CordovaPlugin {
    public static final String TAG = "HWHelper";
    public static String platform;                            // Device OS
    public static String uuid;                                // Device UUID

    private static final String ANDROID_PLATFORM = "Android";
    private static final String AMAZON_PLATFORM = "amazon-fireos";
    private static final String AMAZON_DEVICE = "Amazon";

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("getDeviceInfo")) {
            Log.d(TAG, "getDeviceInfo");
            String message = args.getString(0);
            this.getDeviceInfo(message, callbackContext);
            return true;
        }
        else if(action.equals("pluginInitialize")){
            Log.d(TAG, "pluginInitialize");
            this.pluginInitialize(callbackContext);
            return true;
        }
        else if(action.equals("logEvent")){
            Log.d(TAG, "logEvent");
            String name = args.getString(0);
            JSONObject params = args.getJSONObject(1);
            this.logEvent(name, params, callbackContext);
            return true;
        }
        else if(action.equals("setUserId")){
            Log.d(TAG, "setUserId");
            String userId = args.getString(0);
            this.setUserId(userId, callbackContext);
            return true;
        }
        else if(action.equals("setUserProperty")){
            Log.d(TAG, "setUserProperty");
            String name = args.getString(0);
            String value = args.getString(1);
            this.setUserProperty(name, value, callbackContext);
            return true;
        }
        else if(action.equals("resetAnalyticsData")){
            Log.d(TAG, "resetAnalyticsData");
            this.resetAnalyticsData(callbackContext);
            return true;
        }
        else if(action.equals("setEnabled")){
            Log.d(TAG, "setEnabled");
            boolean enabled = args.getBoolean(0);
            this.setEnabled(enabled, callbackContext);
            return true;
        }
        else if(action.equals("setCurrentScreen")){
            Log.d(TAG, "setCurrentScreen");
            String screenName = args.getString(0);
            this.setCurrentScreen(screenName, callbackContext);
            return true;
        }
        return false;
    }

    private void pluginInitialize(CallbackContext callbackContext) {
        Log.d(TAG, "Starting Firebase Analytics plugin");
        Context context = this.cordova.getActivity().getApplicationContext();
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        callbackContext.success("init success");
    }
    private void logEvent(String name, JSONObject params, CallbackContext callbackContext) {
        Bundle bundle = new Bundle();
        Iterator<String> it = params.keys();
        try{
            while (it.hasNext()) {
                String key = it.next();
                Object value = params.get(key);
                if (value instanceof String) {
                    bundle.putString(key, (String)value);
                } else if (value instanceof Integer) {
                    bundle.putInt(key, (Integer)value);
                } else if (value instanceof Double) {
                    bundle.putDouble(key, (Double)value);
                } else if (value instanceof Long) {
                    bundle.putLong(key, (Long)value);
                } else {
                    Log.w(TAG, "Value for key " + key + " not one of (String, Integer, Double, Long)");
                }
            }
            this.firebaseAnalytics.logEvent(name, bundle);
            callbackContext.success();
        }
        catch(Exception e){
            Log.d(TAG, Log.getStackTraceString(e));
            callbackContext.error(e.getMessage());
        }
    }
    private void setUserId(String userId, CallbackContext callbackContext) {
        this.firebaseAnalytics.setUserId(userId);
        callbackContext.success();
    }
    private void setUserProperty(String name, String value, CallbackContext callbackContext) {
        this.firebaseAnalytics.setUserProperty(name, value);
        callbackContext.success();
    }
    private void resetAnalyticsData(CallbackContext callbackContext) {
        this.firebaseAnalytics.resetAnalyticsData();
        callbackContext.success();
    }
    private void setEnabled(boolean enabled, CallbackContext callbackContext) {
        this.firebaseAnalytics.setAnalyticsCollectionEnabled(enabled);
        callbackContext.success();
    }
    private void setCurrentScreen(String screenName, CallbackContext callbackContext) {
        firebaseAnalytics.setCurrentScreen(
            cordova.getActivity(),
            screenName,
            null
        );
        callbackContext.success();
    }

    private void getDeviceInfo(String message, CallbackContext callbackContext){
        Log.d(TAG, "Starting to get the device info");
        try{
            JSONObject r = new JSONObject();
            r.put("uuid", this.getUuid());
            r.put("version", this.getOSVersion());
            r.put("platform", this.getPlatform());
            r.put("model", this.getModel());
            r.put("manufacturer", this.getManufacturer());
            r.put("isVirtual", this.isVirtual());
            r.put("serial", this.getSerialNumber());
            callbackContext.success(r);
        }
        catch(Exception e){
            Log.d(TAG, Log.getStackTraceString(e));
            callbackContext.error(e.getMessage());
        }
    }
    public String getUuid() {
        String uuid = Settings.Secure.getString(this.cordova.getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return uuid;
    }
    public String getOSVersion() {
        String osversion = android.os.Build.VERSION.RELEASE;
        return osversion;
    }
    public String getPlatform() {
        String platform;
        if (isAmazonDevice()) {
            platform = AMAZON_PLATFORM;
        } else {
            platform = ANDROID_PLATFORM;
        }
        return platform;
    }
    public String getModel() {
        String model = android.os.Build.MODEL;
        return model;
    }
    public String getProductName() {
        String productname = android.os.Build.PRODUCT;
        return productname;
    }
    public String getManufacturer() {
        String manufacturer = android.os.Build.MANUFACTURER;
        return manufacturer;
    }
    public String getSerialNumber() {
        String serial = android.os.Build.SERIAL;
        return serial;
    }
    public String getSDKVersion() {
        @SuppressWarnings("deprecation")
        String sdkversion = android.os.Build.VERSION.SDK;
        return sdkversion;
    }

    public String getTimeZoneID() {
        TimeZone tz = TimeZone.getDefault();
        return (tz.getID());
    }
    public boolean isVirtual() {
        return android.os.Build.FINGERPRINT.contains("generic") || android.os.Build.PRODUCT.contains("sdk");
    }
    public boolean isAmazonDevice() {
        if (android.os.Build.MANUFACTURER.equals(AMAZON_DEVICE)) {
            return true;
        }
        return false;
    }
}
