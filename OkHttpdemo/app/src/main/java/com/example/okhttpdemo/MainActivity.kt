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

        val okHttpClient = OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .build()

        sendButton.setOnClickListener { sendRequestWrapper(okHttpClient) }
    }

    private fun sendRequestWrapper(okHttpClient: OkHttpClient) = runBlocking {
        launch {
            val response = withContext(Dispatchers.IO) { sendRequest(okHttpClient) }
            val toast = Toast.makeText(this@MainActivity, response, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun sendRequest(okHttpClient: OkHttpClient): String? {
        val request: Request = Request.Builder()
            .url(getString(R.string.url))
            .build()
        val response = try {
            okHttpClient.newCall(request).execute().body()?.string()
        } catch(e: Throwable) {
            e.toString()
        }

        return response
    }
}