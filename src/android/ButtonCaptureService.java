package com.ochakov.plugins;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class ButtonCaptureService extends AccessibilityService {

    private boolean volumeUpButtonPressed = false;
    private boolean volumeDownButtonPressed = false;
    private long volumeUpButtonPendingId = 1;
    private long volumeDownButtonPendingId = 1;
    private boolean bothActivated = false;
    private static final String TAG = "ButtonCaptureService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        // Log.i(TAG, "onAccessibilityEvent");
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt");
    }

    @Override
    public void onServiceConnected() {
        Log.i(TAG, "onServiceConnected");
        super.onServiceConnected();

        ButtonCapture.loadSettings(this.getApplicationContext());
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        Log.d(TAG, "onKeyEvent" + event.getKeyCode());
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_UP: {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN: {
                        synchronized (ButtonCaptureService.this) {
                            if (!volumeUpButtonPressed) {
                                volumeUpButtonPressed = true;
                                Log.d(TAG, "volumeUpButtonPressed=true");
                                ButtonCapture.volumeUpButtonPressed(getApplicationContext());
                            }
                            volumeUpButtonPendingId++;
                            Log.d(TAG, "volumeUpButtonPendingId="+volumeUpButtonPendingId);
                        }
                        break;
                    }
                    case KeyEvent.ACTION_UP: {
                        final Handler handler;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                            handler = Handler.createAsync(Looper.getMainLooper());
                        } else {
                            handler = new Handler(Looper.getMainLooper());
                        }
                        volumeUpButtonPendingId++;
                        Log.d(TAG, "volumeUpButtonPendingId="+volumeUpButtonPendingId);
                        handler.postDelayed(new Runnable() {
                            private final long pendingId = volumeUpButtonPendingId;

                            @Override
                            public void run() {
                                synchronized (ButtonCaptureService.this) {
                                    if (pendingId == volumeUpButtonPendingId) {
                                        volumeUpButtonPressed = false;
                                        Log.d(TAG, "volumeUpButtonPressed=false");
                                        if (!volumeDownButtonPressed) {
                                            bothActivated = false;
                                        }
                                    }
                                }
                            }
                        }, ButtonCapture.switchTime);
                        break;
                    }
                }
                break;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN: {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN: {
                        synchronized (ButtonCaptureService.this) {
                            if (!volumeDownButtonPressed) {
                                volumeDownButtonPressed = true;
                                Log.d(TAG, "volumeDownButtonPressed=true");
                                ButtonCapture.volumeDownButtonPressed(getApplicationContext());
                            }
                            volumeDownButtonPendingId++;
                            Log.d(TAG, "volumeDownButtonPendingId=" + volumeDownButtonPendingId);
                        }
                        break;
                    }
                    case KeyEvent.ACTION_UP: {
                        final Handler handler = new Handler(Looper.getMainLooper());
                        volumeDownButtonPendingId++;
                        Log.d(TAG, "volumeDownButtonPendingId=" + volumeDownButtonPendingId);
                        handler.postDelayed(new Runnable() {
                            private final long pendingId = volumeDownButtonPendingId;

                            @Override
                            public void run() {
                                synchronized (ButtonCaptureService.this) {
                                    if (pendingId == volumeDownButtonPendingId) {
                                        volumeDownButtonPressed = false;
                                        Log.d(TAG, "volumeDownButtonPressed=false");
                                        if (!volumeUpButtonPressed) {
                                            bothActivated = false;
                                        }
                                    }
                                }
                            }
                        }, ButtonCapture.switchTime);
                        break;
                    }
                }
                break;
            }
        }

        if (volumeUpButtonPressed && volumeDownButtonPressed && !bothActivated) {
            bothActivated = true;
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (bothActivated) {
                        onBothVolumeKeysPressed();
                    }
                }
            }, ButtonCapture.holdTime);
        }
        if (!volumeUpButtonPressed && !volumeDownButtonPressed) {
            bothActivated = false;
        }

        if (volumeUpButtonPressed && volumeDownButtonPressed) {
            return true;
        }

        return super.onKeyEvent(event);
    }

    private void onBothVolumeKeysPressed() {
        Log.i(TAG, "onBothVolumeKeysPressed: BOTH BUTTONS PRESSED");
		ButtonCapture.twoButtonsPressed(getApplicationContext());
    }
}