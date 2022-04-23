package com.sameer.signal

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var textView: TextView

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var wifiManager: WifiManager

    private lateinit var accPosX: String
    private lateinit var accPosY: String
    private lateinit var accPosZ: String

    private var wifiStrength = ""
    private var linkSpeed = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        // Initialise sensor object
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        wifiManager = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        updateWifiStrength()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                accPosX = event.values[0].roundToInt().toString()
                accPosY = event.values[1].roundToInt().toString()
                accPosZ = event.values[2].roundToInt().toString()
                updateTextView()
            }
        }
    }

    private fun updateWifiStrength() {
        val numberOfLevels = 5
        val wifiInfo = wifiManager.connectionInfo
        val level = WifiManager.calculateSignalLevel(wifiInfo.rssi, numberOfLevels)
        var speed = wifiInfo.linkSpeed
        wifiStrength = "$level out of $numberOfLevels"
        linkSpeed = "$speed"
    }

    private fun updateTextView() {
        textView.text = """
            Accelerometer
            X = $accPosX
            Y = $accPosY
            Z = $accPosZ
            
            Wifi Strength = $wifiStrength
            Link Speed = $linkSpeed
        """.trimIndent()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) { return }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}