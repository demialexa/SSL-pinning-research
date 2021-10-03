Java.perform(function() {
    var NetworkSecurityTrustManager = Java.use('android.security.net.config.NetworkSecurityTrustManager');

    NetworkSecurityTrustManager.checkPins.implementation = function(chain) {
        console.log('Bypassing SSL Pinning...');

        // do nothing, most importantly throw nothing
    }
});
