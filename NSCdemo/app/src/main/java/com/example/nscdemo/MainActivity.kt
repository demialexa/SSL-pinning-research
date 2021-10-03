package com.example.nscdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.security.ProviderInstaller
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ProviderInstaller.installIfNeeded(this)

        val sendButton: Button = findViewById(R.id.button)
        sendButton.setOnClickListener { sendRequestWrapper() }
    }

    private fun sendRequestWrapper() = runBlocking {
        launch {
            val response = withContext(Dispatchers.IO) { sendRequest() }
            val toast = Toast.makeText(this@MainActivity, response, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun sendRequest(): String {
        val url = URL(getString(R.string.url))
        val urlConnection = url.openConnection() as HttpsURLConnection

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