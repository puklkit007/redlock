package com.pulkit007.sample;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraActivity;
import com.androidhiddencamera.HiddenCameraFragment;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by pulkit on 25-Feb-18.
 */

public class MyServices extends Service {
    public HiddenCameraFragment mHiddenCameraFragment;
    public ShakeEventListener mSensorListener;
    public SensorManager mSensorManager;

    @Override
    public IBinder onBind(Intent arg0) {
        Toast.makeText(this, "background", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restart=new Intent(getApplicationContext(),this.getClass());
        restart.setPackage(getPackageName());
        startService(restart);

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        Toast.makeText(getApplicationContext(),"afaefefa",Toast.LENGTH_SHORT).show();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            @Override
            public void onShake() {
                Toast.makeText(getApplicationContext(),"afaefefa",Toast.LENGTH_SHORT).show();
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service stopped", Toast.LENGTH_LONG).show();
    }
}
