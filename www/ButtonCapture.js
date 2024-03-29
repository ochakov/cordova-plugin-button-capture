var exec = require('cordova/exec');

exports.isServiceEnabled = function (success, error) {
    exec(success, error, 'ButtonCapture', 'isServiceEnabled', null);
};

exports.enableService = function (intent, success, error) {
    exec(success, error, 'ButtonCapture', 'enableService', [intent]);
};

exports.disableService = function (success, error) {
    exec(success, error, 'ButtonCapture', 'disableService', null);
};

exports.openAccessibilitySettings = function (success, error) {
    exec(success, error, 'ButtonCapture', 'openAccessibilitySettings', null);
};

exports.setHoldTime = function (holdTime, success, error) {
    exec(success, error, 'ButtonCapture', 'setHoldTime', [holdTime]);
};

exports.setButtonSwitchTime = function (buttonSwitchTime, success, error) {
    exec(success, error, 'ButtonCapture', 'setButtonSwitchTime', [buttonSwitchTime]);
};

exports.setVibrateTime = function (vibrateTime, success, error) {
    exec(success, error, 'ButtonCapture', 'setVibrateTime', [vibrateTime]);
};

exports.setScreenOffVibrateTime = function (vibrateTime, success, error) {
    exec(success, error, 'ButtonCapture', 'setScreenOffVibrateTime', [vibrateTime]);
};

exports.setSkipFirstPressTime = function (skipFirstPressTime, success, error) {
    exec(success, error, 'ButtonCapture', 'setSkipFirstPressTime', [skipFirstPressTime]);
};
