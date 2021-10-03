package com.example.trustmanagerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.security.ProviderInstaller
import java.net.URL
import java.security.KeyStore
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ProviderInstaller.installIfNeeded(this@MainActivity)

        // Load KeyStore with the Certificate file from resources (as InputStream).
        val resourceStream = resources.openRawResource(R.raw.github)
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)

        keyStore.load(resourceStream, null)

        // Get TrustManagerFactory and init it with KeyStore.
        val trustManagerAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val trustManagerFactory = TrustManagerFactory.getInstance(trustManagerAlgorithm)

        trustManagerFactory.init(keyStore)

        // Get an instance of SSLContext, bind it with TrustManager,
        // and create an sslContext with a URL connection.
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagerFactory.trustManagers, null)

        val sendButton: Button = findViewById(R.id.button)
        sendButton.setOnClickListener { sendRequestWrapper(sslContext) }
    }

    private fun sendRequestWrapper(sslContext: SSLContext) = runBlocking {
        launch {
            val response = withContext(Dispatchers.IO) { sendRequest(sslContext) }
            val toast = Toast.makeText(this@MainActivity, response, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun sendRequest(sslContext: SSLContext): String {
        val url = URL(getString(R.string.url))
        val urlConnection= url.openConnection() as HttpsURLConnection
        urlConnection.sslSocketFactory = sslContext.socketFactory

        val response = try {
            urlConnection.inputStream.bufferedReader().readText()
        } catch(e: Throwable) {
            e.toString()
        } finally {
            urlConnection.disconnect()
        }

        return response
    }
}