package com.example.okhttpdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.security.ProviderInstaller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ProviderInstaller.installIfNeeded(this)

        val sendButton: Button = findViewById(R.id.button)
        val certificatePinner = CertificatePinner.Builder()
            .add(
                getString(R.string.hostname),
                getString(R.string.pin)
            ).build()

        val client = OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .build()

        sendButton.setOnClickListener { sendRequestWrapper(client) }
    }

    private fun sendRequestWrapper(client: OkHttpClient) = runBlocking {
        launch {
            val response = withContext(Dispatchers.IO) { sendRequest(client) }
            val toast = Toast.makeText(this@MainActivity, response, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun sendRequest(client: OkHttpClient): String? {
        val request = Request.Builder()
            .url(getString(R.string.proto) + getString(R.string.hostname))
            .build()
        val response = try {
            client.newCall(request).execute().body()?.string()
        } catch(e: Throwable) {
            e.toString()
        }

        return response
    }
}