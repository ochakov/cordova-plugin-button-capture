package com.ochakov.plugins;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class ButtonCaptureService extends AccessibilityService {

    private boolean volumeUpButtonPressed = false;
    private boolean volumeDownButtonPressed = false;
    private boolean bothActivated = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        // Log.e("TAG", "onAccessibilityEvent");
    }

    @Override
    public void onInterrupt() {
        Log.e("TAG", "onInterrupt");
    }

    @Override
    public void onServiceConnected() {
        Log.e("TAG", "onServiceConnected");
        super.onServiceConnected();
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        Log.e("TAG", "onKeyEvent" + event.getKeyCode());
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_UP: {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN: {
                        if (!volumeUpButtonPressed) {
                            volumeUpButtonPressed = true;
                            ButtonCapture.volumeUpButtonPressed();
                        }
                        break;
                    }
                    case KeyEvent.ACTION_UP: {
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                volumeUpButtonPressed = false;
                            }
                        }, 300);
                        break;
                    }
                }
                break;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN: {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN: {
                        if (!volumeDownButtonPressed) {
                            volumeDownButtonPressed = true;
                            ButtonCapture.volumeDownButtonPressed();
                        }
                        break;
                    }
                    case KeyEvent.ACTION_UP: {
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                volumeDownButtonPressed = false;
                            }
                        }, 300);
                        break;
                    }
                }
                break;
            }
        }

        if (volumeUpButtonPressed && volumeDownButtonPressed && !bothActivated) {
            bothActivated = true;
            onBothVolumeKeysPressed();
        }
        if (!volumeUpButtonPressed || !volumeDownButtonPressed) {
            bothActivated = false;
        }

        return super.onKeyEvent(event);
    }

    private void onBothVolumeKeysPressed() {
        Log.e("TAG", "onBothVolumeKeysPressed: BOTH BUTTONS PRESSED");
		ButtonCapture.twoButtonsPressed();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}