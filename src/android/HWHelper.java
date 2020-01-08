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
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

import org.apache.cordova.CordovaInterface;
import com.crashlytics.android.Crashlytics;

import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters;
import com.google.firebase.dynamiclinks.DynamicLink.GoogleAnalyticsParameters;
import com.google.firebase.dynamiclinks.DynamicLink.IosParameters;
import com.google.firebase.dynamiclinks.DynamicLink.ItunesConnectAnalyticsParameters;
import com.google.firebase.dynamiclinks.DynamicLink.NavigationInfoParameters;
import com.google.firebase.dynamiclinks.DynamicLink.SocialMetaTagParameters;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaWebView;

public class HWHelper extends CordovaPlugin {
    public static final String TAG = "HWHelper";
    public static String platform;                            // Device OS
    public static String uuid;                                // Device UUID

    private static final String ANDROID_PLATFORM = "Android";
    private static final String AMAZON_PLATFORM = "amazon-fireos";
    private static final String AMAZON_DEVICE = "Amazon";

    private FirebaseAnalytics firebaseAnalytics;

    private FirebaseDynamicLinks firebaseDynamicLinks;
    private String domainUriPrefix;
    public static CallbackContext dynamicLinkCallback;

    public static CordovaWebView gWebView;
    public static String notificationCallBack = "HWHelper.onDynamicLinkReceived";
    public static Boolean notificationCallBackReady = false;
    public static Intent lastPush = null;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
        gWebView = webView;
        this.firebaseDynamicLinks = FirebaseDynamicLinks.getInstance();
		Log.d(TAG, "==> HWHelper initialize");
	}

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("ready")) {
            callbackContext.success();
        }
        else if (action.equals("registerDynamicLink")) {
            notificationCallBackReady = true;
            final CallbackContext localcallbackContext = callbackContext;
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if(lastPush != null) HWHelper.sendPushPayload1( lastPush );
                    lastPush = null;
                    localcallbackContext.success();
                }
            });
        }
        else if(action.equals("getDeviceInfo")) {
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
        else if(action.equals("crashlyticsInit")){
            Log.d(TAG, "crashlyticsInit");
            this.crashlyticsInit(callbackContext);
            return true;
        }
        else if(action.equals("crash")){
            Log.d(TAG, "crash");
            this.crash(callbackContext);
            return true;
        }
        else if(action.equals("logPriority")){
            Log.d(TAG, "logPriority");
            this.logPriority(args, callbackContext);
            return true;
        }
        else if(action.equals("log")){
            Log.d(TAG, "log");
            this.log(args, callbackContext);
            return true;
        }
        else if(action.equals("setString")){
            Log.d(TAG, "setString");
            this.setString(args, callbackContext);
            return true;
        }
        else if(action.equals("setBool")){
            Log.d(TAG, "setBool");
            this.setString(args, callbackContext);
            return true;
        }
        else if(action.equals("setDouble")){
            Log.d(TAG, "setDouble");
            this.setDouble(args, callbackContext);
            return true;
        }
        else if(action.equals("setFloat")){
            Log.d(TAG, "setFloat");
            this.setFloat(args, callbackContext);
            return true;
        }
        else if(action.equals("setInt")){
            Log.d(TAG, "setInt");
            this.setInt(args, callbackContext);
            return true;
        }
        else if(action.equals("logException")){
            Log.d(TAG, "logException");
            this.logException(args, callbackContext);
            return true;
        }
        else if(action.equals("setUserIdentifier")){
            Log.d(TAG, "setUserIdentifier");
            this.setUserIdentifier(args, callbackContext);
            return true;
        }
        else if(action.equals("fbDynamicLinkInit")){
            Log.d(TAG, "fbDynamicLinkInit");
            this.fbDynamicLinkInit(callbackContext);
            return true;
        }
        else if(action.equals("onDynamicLink")){
            Log.d(TAG, "onDynamicLink");
            this.onDynamicLink(callbackContext);
            return true;
        }
        return false;
    }

    public static void sendPushPayload1(final Intent intent) {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
                .continueWith(new Continuation<PendingDynamicLinkData, JSONObject>() {
                    @Override
                    public JSONObject then(Task<PendingDynamicLinkData> task) throws JSONException {
                        PendingDynamicLinkData data = task.getResult();

                        JSONObject result = new JSONObject();
                        result.put("deepLink", data.getLink());
                        result.put("clickTimestamp", data.getClickTimestamp());
                        result.put("minimumAppVersion", data.getMinimumAppVersion());
                        String callBack = "javascript:" + notificationCallBack + "(" + result.toString() + ")";

                        if(notificationCallBackReady && gWebView != null){
                            Log.d(TAG, "\tSent PUSH to view: " + callBack);
                            gWebView.sendJavascript(callBack);
                        }else {
                            if(notificationCallBackReady){
                                Log.d(TAG, "notificationCallBackReady is true");
                            }
                            if(gWebView == null){
                                Log.d(TAG, "web view is null");
                            }
                            Log.d(TAG, "\tView not ready. SAVED NOTIFICATION: " + callBack);
                            lastPush = intent;
                        }
                        // if (dynamicLinkCallback != null) {
                        //     PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, result);
                        //     pluginResult.setKeepCallback(true);
                        //     dynamicLinkCallback.sendPluginResult(pluginResult);
                        // }

                        return result;
                    }
                });
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "ON New Intent");
        if (this.dynamicLinkCallback != null) {
            respondWithDynamicLink(intent);
        }
    }

    

    private void crashlyticsInit(CallbackContext callbackContext){

        Log.d(TAG, "Initializing FBCrashlyticsPlugin");

        Fabric.with(this.cordova.getActivity(), new Crashlytics());
        callbackContext.success("init success");
    }
    private void crash(CallbackContext callbackContext){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                Crashlytics.getInstance().crash();
            }
        });
        callbackContext.success("init success");
    }
    private void logPriority(final JSONArray args, CallbackContext callbackContext){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try {
                    final Integer priority = args.getInt(0);
                    final String tag = args.getString(1);
                    final String msg = args.getString(2);

                    Crashlytics.log(priority, tag, msg);
                } catch (JSONException e) {
                    Log.e(TAG, "Error logging with priority", e);
                }
            }
        });
        callbackContext.success("logPriority success");
    }
    private void log(final JSONArray args, CallbackContext callbackContext){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try {
                    final String msg = args.getString(0);
                    Crashlytics.log(msg);
                } catch (JSONException e) {
                    Log.e(TAG, "Error logging", e);
                }
            }
        });
        callbackContext.success("log success");
    }
    private void setString(final JSONArray args, CallbackContext callbackContext){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try {
                    final String key = args.getString(0);
                    final String value = args.getString(1);
        
                    Crashlytics.setString(key, value);
                } catch (JSONException e) {
                    Log.e(TAG, "Error setting string", e);
                }
            }
        });
        callbackContext.success("setString success");
    }
    private void setBool(final JSONArray args, CallbackContext callbackContext){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try {
                    final String key = args.getString(0);
                    final Boolean value = args.getBoolean(1);
        
                    Crashlytics.setBool(key, value);
                } catch (JSONException e) {
                    Log.e(TAG, "Error setting string", e);
                }
            }
        });
        callbackContext.success("setBool success");
    }
    private void setDouble(final JSONArray args, CallbackContext callbackContext){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try {
                    final String key = args.getString(0);
                    final Double value = args.getDouble(1);
        
                    Crashlytics.setDouble(key, value);
                } catch (JSONException e) {
                    Log.e(TAG, "Error setting string", e);
                }
            }
        });
        callbackContext.success("setBool success");
    }
    private void setFloat(final JSONArray args, CallbackContext callbackContext){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try {
                    final String key = args.getString(0);
                    final Double value = args.getDouble(1);

                    Crashlytics.setFloat(key, value.floatValue());
                } catch (JSONException e) {
                    Log.e(TAG, "Error setting string", e);
                }
            }
        });
        callbackContext.success("setFloats success");
    }
    private void setInt(final JSONArray args, CallbackContext callbackContext){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try {
                    final String key = args.getString(0);
                    final Integer value = args.getInt(1);

                    Crashlytics.setInt(key, value);
                } catch (JSONException e) {
                    Log.e(TAG, "Error setting int", e);
                }
            }
        });
        callbackContext.success("setInt success");
    }
    private void logException(final JSONArray args, CallbackContext callbackContext){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try {
                    final String msg = args.getString(0);

                    Exception exception = new Exception(msg);

                    Crashlytics.logException(exception);
                } catch (JSONException e) {
                    Log.e(TAG, "Error setting logException", e);
                }
            }
        });
        callbackContext.success("logException success");
    }
    private void setUserIdentifier(final JSONArray args, CallbackContext callbackContext){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try {
                    final String identifier = args.getString(0);

                    Crashlytics.setUserIdentifier(identifier);
                } catch (JSONException e) {
                    Log.e(TAG, "Error setting setUserIdentifier", e);
                }
            }
        });
        callbackContext.success("setUserIdentifier success");
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
    private void setCurrentScreen(final String screenName, CallbackContext callbackContext) {
        this.cordova.getActivity().runOnUiThread(new Runnable() {            
            private String locScreenName = screenName;
            @Override
            public void run(){
                FirebaseAnalytics.getInstance(cordova.getActivity().getApplicationContext()).setCurrentScreen(
                    cordova.getActivity(),
                    locScreenName,
                    null
                );
            }
        });
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
    public void fbDynamicLinkInit(CallbackContext callbackContext) {
        Log.d(TAG, "Starting Firebase Dynamic Link plugin");
        // this.firebaseDynamicLinks = FirebaseDynamicLinks.getInstance();
        callbackContext.success("init success");
    }
    public void onDynamicLink(CallbackContext callbackContext) {
        this.dynamicLinkCallback = callbackContext;
        respondWithDynamicLink(cordova.getActivity().getIntent());
    }
    private void respondWithDynamicLink(Intent intent) {
        this.firebaseDynamicLinks.getDynamicLink(intent)
                .continueWith(new Continuation<PendingDynamicLinkData, JSONObject>() {
                    @Override
                    public JSONObject then(Task<PendingDynamicLinkData> task) throws JSONException {
                        PendingDynamicLinkData data = task.getResult();

                        JSONObject result = new JSONObject();
                        result.put("deepLink", data.getLink());
                        result.put("clickTimestamp", data.getClickTimestamp());
                        result.put("minimumAppVersion", data.getMinimumAppVersion());

                        if (dynamicLinkCallback != null) {
                            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, result);
                            pluginResult.setKeepCallback(true);
                            dynamicLinkCallback.sendPluginResult(pluginResult);
                        }

                        return result;
                    }
                });
    }
}
