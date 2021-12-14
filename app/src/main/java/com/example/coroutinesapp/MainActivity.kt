package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var viewText: TextView
    lateinit var buttonAdvice: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewText = findViewById(R.id.viewText)
        buttonAdvice = findViewById(R.id.buttonAdvice)
        buttonAdvice.setOnClickListener {getAdvice()}

    }

    fun getAdvice() {

        var advice = ""
        CoroutineScope(IO).launch {

            var data = async { fetchData() }.await()

            if(!data.isEmpty()) {

                var jsonOfAdvice = JSONObject(data)
                advice = jsonOfAdvice.getJSONObject("slip").getString("advice")

                withContext(Main){
                    viewText.text = advice
                }

            }
        }
    }


    fun fetchData(): String{

        var response = ""
        try{
            response = URL("https://api.adviceslip.com/advice").readText()
        }catch(e: Exception){
            Log.d("MAIN", "ISSUE: $e")
        }
        // our response is saved as a string and returned
        return response
    }


}