var exec = require('cordova/exec');

exports.isServiceEnabled = function (success, error) {
    exec(success, error, 'ButtonCapture', 'isServiceEnabled', null);
};

exports.enableService = function (intent, success, error) {
    exec(success, error, 'ButtonCapture', 'enableService', [intent]);
};

exports.disableService = function (success, error) {
    exec(success, error, 'ButtonCapture', 'enableService', null);
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
