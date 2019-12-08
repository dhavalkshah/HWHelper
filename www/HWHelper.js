var exec = require('cordova/exec');

exports.getDeviceInfo = function(arg0, success, error){
    exec(success, error, 'HWHelper', 'getDeviceInfo', [arg0]);
};
exports.pluginInitialize = function(success, error){
    exec(success, error, 'HWHelper', 'pluginInitialize');
};
exports.logEvent = function(name, params, success, error){
    exec(success, error, 'HWHelper', 'logEvent',  [name, params || {}] );
};
exports.setUserId = function(userId, success, error){
    exec(success, error, 'HWHelper', 'setUserId',  [userId] );
};
exports.setUserProperty = function(name, params, success, error){
    exec(success, error, 'HWHelper', 'setUserProperty',  [name, params || {}] );
};
exports.resetAnalyticsData = function(success, error){
    exec(success, error, 'HWHelper', 'resetAnalyticsData');
};
exports.setEnabled = function(enabled, success, error){
    exec(success, error, 'HWHelper', 'setEnabled',  [enabled] );
};
exports.setCurrentScreen = function(screenName, success, error){
    exec(success, error, 'HWHelper', 'setCurrentScreen',  [screenName] );
};