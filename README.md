# cordova-plugin-button-capture
Capture both volume buttons in accessibility service

Use this plugin to create an Android accessibility service to open your application and call for action when both volume buttons are pressed.
Volume buttons can be pressed outside the application, anywhere, anytime.

This plugin requires accessibility service to be enabled. Service name and description can be set by plugin variables.
On every application start, the service must be initialized by setting the intent name that will be sent when both volume buttons are pressed.

<i>In order to call for action, use custom url scheme plugin https://github.com/EddyVerbruggen/Custom-URL-scheme</i>

<b>INSTALLATION</b>

In your config.xml add
```
    <plugin name="cordova-plugin-button-capture" spec="https://github.com/ochakov/cordova-plugin-button-capture.git">
        <variable name="SERVICE_NAME" value="My volume buttons service" />
        <variable name="SERVICE_DESC" value="Capture both volume buttons to generate panic alert" />
    </plugin>
```

<b>USAGE</b>

```javascript
cordova.plugin.isServiceEnabled(function(success) {
   console.log("Service enabled")
}, function(error) {
   console.log("Open accessibility settings to enable the service")
});

// Start listening for both volume buttons and triggen a custom url intent when pressed
cordova.plugin.enableService("mycustomurl://detected");

// Stop listening for buttons
cordova.plugin.disableService();

// Open accessibility settings page to enable the service
cordova.plugin.openAccessibilitySettings();

// Set the requried time in msec to hold volume buttons before triggering the intent
// For devices where both buttons cannot be pressed simultaneously, the last button must be held instead.
// Default 0 msec (instantly, no hold needed)
cordova.plugin.setHoldTime(holdTime);

// Set the requried time in msec between pressing volume up and down buttons separately.
// Useful for devices where both buttons cannot be pressed simultaneously.
// In this case, it is possible to press one button and then the other, within buttonSwitchTime.
// Default 300 msec
cordova.plugin.setButtonSwitchTime(buttonSwitchTime);
```
