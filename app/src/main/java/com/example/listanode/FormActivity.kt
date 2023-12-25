package com.example.listanode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.example.listanode.ui.theme.ListaNodeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.Dispatchers.Main
import androidx.compose.ui.platform.LocalContext




class FormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ListaNodeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormContent() {
    var textFieldValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("Enter text") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val textToSend = textFieldValue
                        println(textToSend)
                        sendDataToEndpoint(textToSend)


                    } catch (e: Exception) {
                        // Handle error (e.g., show a toast or log an error message)
                        e.printStackTrace()
                    }

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Submit")
        }
    }
}


fun sendDataToEndpoint(textToSend: String) {
    try {
        val url =  "http://192.168.254.48:4000/"
        val connection = URL(url).openConnection() as HttpURLConnection

        // Set the request method to POST
        connection.requestMethod = "POST"

        // Set the content type to "application/x-www-form-urlencoded"
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        // Enable output stream to send data to the server
        connection.doOutput = true

        // Construct the data to be sent in the request body
        val data = "nome=${URLEncoder.encode(textToSend, "UTF-8")}"

        // Write the data to the output stream
        val outputStream: OutputStream = connection.outputStream
        outputStream.write(data.toByteArray(Charsets.UTF_8))
        outputStream.flush()
        outputStream.close()

        // Get the response code from the server
        val responseCode = connection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Request was successful
            // You may want to read the response from the server if needed

        } else {
            // Handle unsuccessful response
            // You may want to throw an exception or handle the error accordingly
        }
    } catch (e: Exception) {
        // Handle exceptions, e.g., IOException
        e.printStackTrace()
    }
}

@Preview(showBackground = true)
@Composable
fun FormContentPreview() {
    ListaNodeTheme {
        FormContent()
    }
}
