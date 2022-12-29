package com.ochakov.plugins;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.content.pm.ServiceInfo;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class ButtonCapture extends CordovaPlugin {

    private static ButtonCapture instance = null;
    private boolean isServiceEnabled = false;
	private String intentName = null;

    @Override
    protected void pluginInitialize() {
        instance = this;
    }

    public static void twoButtonsPressed() {
        if (instance != null) {
            instance.handleButtonPressed();
        }
    }

    public static void volumeDownButtonPressed() {
    }

    public static void volumeUpButtonPressed() {
    }

    private void handleButtonPressed() {
        if (isServiceEnabled && intentName != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);

            Uri uri = Uri.parse(intentName);
            sendIntent.setData(uri);
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            try {
                instance.cordova.getActivity().startActivity(shareIntent);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("isServiceEnabled")) {
            Context context = this.cordova.getActivity().getApplicationContext();
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
		}
        if (action.equals("disableService")) {
			isServiceEnabled = false;
		}
        if (action.equals("openAccessibilitySettings")) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            this.cordova.getActivity().startActivity(intent);
        }
        return false;
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