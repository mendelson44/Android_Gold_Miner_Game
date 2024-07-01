package com.example.gold_miner_game.logic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


public class SensorManager {
    private long timeStemp = 0;
    private android.hardware.SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private CallBack_CharacterMovement callBack_CharacterMovement;


    public SensorManager(Context context, CallBack_CharacterMovement callBack_CharacterMovement) {
        this.callBack_CharacterMovement = callBack_CharacterMovement;
        sensorManager = (android.hardware.SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                calculateStep(x);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    private void calculateStep(float x) {
        //if(System.currentTimeMillis() - timeStemp > 200){
            timeStemp = System.currentTimeMillis();
            if(x > 1.5){
                if(callBack_CharacterMovement != null)
                    callBack_CharacterMovement.CharacterMoveLeft();
            }
            if(x < -1.5){
                if(callBack_CharacterMovement != null)
                    callBack_CharacterMovement.CharacterMoveRight();
            }
       // }
    }

    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                android.hardware.SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }

}
