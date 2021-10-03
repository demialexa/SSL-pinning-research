Java.perform(function() {
    var ArrayList = Java.use("java.util.ArrayList");
    var TrustManagerImpl = Java.use('com.android.org.conscrypt.TrustManagerImpl');

    TrustManagerImpl.verifyChain.implementation = function(a1, a2, a3, a4, a5, a6) {
        console.log('Bypassing SSL Pinning...');

        // do nothing, most importantly throw nothing
        var k = ArrayList.$new();
        return k;
    }
});
