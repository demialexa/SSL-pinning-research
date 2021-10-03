Java.perform(function() {
    var CertificatePinner= Java.use('okhttp3.CertificatePinner');

    CertificatePinner.check.overload('java.lang.String', 'java.util.List').implementation = function(host, ssl) {
        console.log('Bypassing SSL Pinning...');

        // do nothing, most importantly throw nothing
    }
});
