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
exports.crashlyticsInit = function(success, error){
    exec(success, error, 'HWHelper', 'crashlyticsInit', []);
};
exports.crash = function(success, error){
    exec(success, error, 'HWHelper', 'crash', []);
};
exports.logPriority = function(priority, tag, message, success, error){
    exec(success, error, 'HWHelper', 'logPriority', [priority, tag, message]);
};
exports.log = function(message, success, error){
    exec(success, error, 'HWHelper', 'log', [message]);
};
exports.setString = function(key, value, success, error){
    exec(success, error, 'HWHelper', 'setString', [key, value]);
};
exports.setBool = function(key, value, success, error){
    exec(success, error, 'HWHelper', 'setBool', [key, value]);
};
exports.setDouble = function(key, value, success, error){
    exec(success, error, 'HWHelper', 'setDouble', [key, value]);
};
exports.setFloat = function(key, value, success, error){
    exec(success, error, 'HWHelper', 'setFloat', [key, value]);
};
exports.setInt = function(key, value, success, error){
    exec(success, error, 'HWHelper', 'setInt', [key, value]);
};
exports.setUserIdentifier = function(identifier, success, error){
    exec(success, error, 'HWHelper', 'setUserIdentifier', [identifier]);
};
exports.logException = function(message, success, error){
    exec(success, error, 'HWHelper', 'logException', [message]);
};
exports.fbDynamicLinkInit = function(success, error){
    exec(success, error, 'HWHelper', 'fbDynamicLinkInit', []);
};
exports.onDynamicLink = function(success, error){
    exec(success, error, 'HWHelper', 'onDynamicLink');
};