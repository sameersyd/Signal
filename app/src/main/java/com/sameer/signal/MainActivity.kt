package com.sameer.signal

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var textView: TextView

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private lateinit var accPosX: String
    private lateinit var accPosY: String
    private lateinit var accPosZ: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        // Initialise sensor object
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                accPosX = event.values[0].toString()
                accPosY = event.values[1].toString()
                accPosZ = event.values[2].toString()
                updateTextView()
            }
        }
    }

    private fun updateTextView() {
        textView.text = """
            Accelerometer
            X = $accPosX
            Y = $accPosY
            Z = $accPosZ
        """.trimIndent()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) { return }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}