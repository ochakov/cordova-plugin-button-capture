package com.ochakov.plugins;

import static android.content.Context.DISPLAY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static android.content.Intent.FLAG_FROM_BACKGROUND;
import static android.content.Intent.FLAG_RECEIVER_FOREGROUND;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.content.pm.ServiceInfo;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class ButtonCapture extends CordovaPlugin {

    protected static int holdTime = 0;
    protected static int switchTime = 300;
    protected static int vibrateTime = 100;
    private static boolean isServiceEnabled = false;
	private static String intentName = null;

    @Override
    protected void pluginInitialize() {
        loadSettings(this.cordova.getContext());
    }

    public static void loadSettings(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        intentName = sharedPreferences.getString("intentName", null);
        isServiceEnabled = sharedPreferences.getBoolean("isServiceEnabled", false);
        holdTime = sharedPreferences.getInt("holdTime", 0);
        switchTime = sharedPreferences.getInt("switchTime", 300);
    }

    public static void volumeDownButtonPressed(Context context) {
    }

    public static void volumeUpButtonPressed(Context context) {
    }

    public static void twoButtonsPressed(Context context) {
        if (isServiceEnabled && intentName != null) {
            vibrate(context);
            Uri uri = Uri.parse(intentName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP | FLAG_FROM_BACKGROUND | FLAG_RECEIVER_FOREGROUND);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

    private static void vibrate(Context context) {
        if (vibrateTime > 0) {
            Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
            v.vibrate(VibrationEffect.createOneShot(vibrateTime, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        final Context context = this.cordova.getActivity().getApplicationContext();

        if (action.equals("isServiceEnabled")) {
            if (isAccessibilityServiceEnabled(context)) {
                // service can be activated
                callbackContext.success();
                return true;
            } else {
                // service can NOT be activated
                callbackContext.error("");
                return false;
            }
        }
        if (action.equals("enableService")) {
			isServiceEnabled = true;
			intentName = args.getString(0);
            saveSettings(context);
		}
        if (action.equals("disableService")) {
			isServiceEnabled = false;
            saveSettings(context);
		}
        if (action.equals("openAccessibilitySettings")) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            this.cordova.getActivity().startActivity(intent);
        }
        if (action.equals("setHoldTime")) {
            holdTime = args.getInt(0);
            saveSettings(context);
        }
        if (action.equals("setButtonSwitchTime")) {
            switchTime = args.getInt(0);
            saveSettings(context);
        }
        if (action.equals("setVibrateTime")) {
            vibrateTime = args.getInt(0);
            saveSettings(context);
        }
        return false;
    }

    private void saveSettings(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save the settings to the shared preferences
        editor.putString("intentName", intentName);
        editor.putBoolean("isServiceEnabled", isServiceEnabled);
        editor.apply();
    }

    private Boolean isAccessibilityServiceEnabled(Context context) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(ButtonCaptureService.class.getName()))
                return true;
        }
        return false;
    }
}