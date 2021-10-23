package com.example.lightsaber

import android.content.DialogInterface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import java.lang.Math.abs
import java.lang.Thread.sleep
import kotlin.collections.indexOf as indexOf

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var img: ImageView
    lateinit var hilt: ImageView
    lateinit var color: ImageView

    lateinit var onSound: MediaPlayer
    lateinit var startSound: MediaPlayer
    lateinit var sensorManager: SensorManager
    lateinit var leftSwingPlayer: MediaPlayer
    lateinit var rightSwingPlayer: MediaPlayer
    lateinit var endSound: MediaPlayer

    lateinit var stb: Animation
    lateinit var light_on: Animation
    lateinit var light_off: Animation
    lateinit var color_on: Animation
    lateinit var color_off: Animation



    var colors = arrayOf("blue","green","purple","yellow","red")
    var choosen_color = "blue"
    var visible = 0


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hilt = findViewById(R.id.hilt)
        color = findViewById(R.id.color)

        stb = AnimationUtils.loadAnimation(this,R.anim.stb)
        light_on = AnimationUtils.loadAnimation(this,R.anim.light_on)
        light_off = AnimationUtils.loadAnimation(this,R.anim.light_off)
        color_on = AnimationUtils.loadAnimation(this,R.anim.color_on)
        color_off = AnimationUtils.loadAnimation(this,R.anim.color_off)

        onSound = MediaPlayer.create(this,R.raw.on_sound)
        startSound = MediaPlayer.create(this,R.raw.start_sound)
        leftSwingPlayer = MediaPlayer.create(this, R.raw.left_swing)
        rightSwingPlayer = MediaPlayer.create(this, R.raw.right_swing)
        endSound = MediaPlayer.create(this,R.raw.end_sound)

        hilt.startAnimation(stb)
        color.startAnimation(stb)

        setUpSensors()
    }

    fun setUpSensors()
    {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also{
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST )
        }
    }

    fun hiltPushed(view: View) {
        print(choosen_color)
        img = findViewById(R.id.lightBlade)
        if(choosen_color == "blue")
        {
            img.setImageResource(R.drawable.blue)
        }
        else if(choosen_color == "green")
        {
            img.setImageResource(R.drawable.green)
        }
        else if(choosen_color == "yellow")
        {
            img.setImageResource(R.drawable.yellow)
        }
        else if(choosen_color == "red")
        {
            img.setImageResource(R.drawable.red)
        }
        else if(choosen_color == "purple")
        {
            img.setImageResource(R.drawable.purple)
        }

        if (visible == 0)
        {
            img.startAnimation(light_on)
            color.startAnimation(color_off)
            startSound.start()
            onSound.start()
            onSound.isLooping = true
            visible = 1
            img.visibility = View.VISIBLE
            color.visibility = View.INVISIBLE
        }
        else
        {
            img.startAnimation(light_off)
            color.startAnimation(color_on)
            endSound.start()
            onSound.pause()
            visible = 0
            img.visibility = View.INVISIBLE
            color.visibility = View.VISIBLE
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = event.values[0]
            val upDown = event.values[1]
            if(sides>20 && visible == 1)
            {
                leftSwingPlayer.start()
            }
            else if(sides<-20 && visible == 1)
            {
                rightSwingPlayer.start()
            }

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    fun onColorClicked(view: View) {
        var i = colors.indexOf(choosen_color)
        var alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setTitle("Choose color of the light")
        alertDialog.setSingleChoiceItems(colors,i){dialogInterface: DialogInterface, position : Int ->
            choosen_color=colors[position]
        }
        alertDialog.setCancelable(true)
        alertDialog.show()
        print("\n\n\nbaszkikam:"+choosen_color+"\n\n\n")
    }


}
