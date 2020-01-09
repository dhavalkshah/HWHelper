var exec = require('cordova/exec');

function HWHelper() { 
	console.log("HWHelper.js: is created");
}
HWHelper.prototype.onDynamicLinkReceived = function(payload){
	console.log("Received push notification")
	console.log(payload)
}
HWHelper.prototype.onDynamicLink = function( callback, success, error ){
	HWHelper.prototype.onDynamicLinkReceived = callback;
	exec(success, error, "HWHelper", 'registerDynamicLink',[]);
}
// HWHelper.prototype.onDynamicLink = function(success, error, callback){
//     exports.onDynamicLinkReceived = callback;
//     exec(success, error, 'HWHelper', 'onDynamicLink');
// };

HWHelper.prototype.getDeviceInfo = function(arg0, success, error){
    exec(success, error, 'HWHelper', 'getDeviceInfo', [arg0]);
};
HWHelper.prototype.pluginInitialize = function(success, error){
    exec(success, error, 'HWHelper', 'pluginInitialize');
};
HWHelper.prototype.logEvent = function(name, params, success, error){
    exec(success, error, 'HWHelper', 'logEvent',  [name, params || {}] );
};
HWHelper.prototype.setUserId = function(userId, success, error){
    exec(success, error, 'HWHelper', 'setUserId',  [userId] );
};
HWHelper.prototype.setUserProperty = function(name, params, success, error){
    exec(success, error, 'HWHelper', 'setUserProperty',  [name, params || {}] );
};
HWHelper.prototype.resetAnalyticsData = function(success, error){
    exec(success, error, 'HWHelper', 'resetAnalyticsData');
};
HWHelper.prototype.setEnabled = function(enabled, success, error){
    exec(success, error, 'HWHelper', 'setEnabled',  [enabled] );
};
HWHelper.prototype.setCurrentScreen = function(screenName, success, error){
    exec(success, error, 'HWHelper', 'setCurrentScreen',  [screenName] );
};
HWHelper.prototype.crashlyticsInit = function(success, error){
    exec(success, error, 'HWHelper', 'crashlyticsInit', []);
};
HWHelper.prototype.crash = function(success, error){
    exec(success, error, 'HWHelper', 'crash', []);
};
HWHelper.prototype.logPriority = function(priority, tag, message, success, error){
    exec(success, error, 'HWHelper', 'logPriority', [priority, tag, message]);
};
HWHelper.prototype.log = function(message, success, error){
    exec(success, error, 'HWHelper', 'log', [message]);
};
HWHelper.prototype.setString = function(key, value, success, error){
    exec(success, error, 'HWHelper', 'setString', [key, value]);
};
HWHelper.prototype.setBool = function(key, value, success, error){
    exec(success, error, 'HWHelper', 'setBool', [key, value]);
};
HWHelper.prototype.setDouble = function(key, value, success, error){
    exec(success, error, 'HWHelper', 'setDouble', [key, value]);
};
HWHelper.prototype.setFloat = function(key, value, success, error){
    exec(success, error, 'HWHelper', 'setFloat', [key, value]);
};
HWHelper.prototype.setInt = function(key, value, success, error){
    exec(success, error, 'HWHelper', 'setInt', [key, value]);
};
HWHelper.prototype.setUserIdentifier = function(identifier, success, error){
    exec(success, error, 'HWHelper', 'setUserIdentifier', [identifier]);
};
HWHelper.prototype.logException = function(message, success, error){
    exec(success, error, 'HWHelper', 'logException', [message]);
};
HWHelper.prototype.fbDynamicLinkInit = function(success, error){
    exec(success, error, 'HWHelper', 'fbDynamicLinkInit', []);
};
HWHelper.prototype.createShortDynamicLink = function(params, success, error){
    exec(success, error, 'HWHelper', 'createDynamicLink', [params, cordova.platformId === "ios" ? 1 : 2]);
};
HWHelper.prototype.createDynamicLink = function(params, success, error){
    exec(success, error, 'HWHelper', 'createDynamicLink', [params, 0]);
};

exec(function(result){ console.log("HWHelper Ready OK") }, function(result){ console.log("HWHelper Ready ERROR") }, "HWHelper",'ready',[]);

var hwHelperPlugin = new HWHelper();
module.exports = hwHelperPlugin;