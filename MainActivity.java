package com.pulkit007.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidhiddencamera.HiddenCameraFragment;
import com.microsoft.projectoxford.face.contract.FaceList;

import java.io.ByteArrayOutputStream;
import java.security.spec.ECField;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button retrieve;
    DatabaseHelper databaseHelper;
    // private HiddenCameraFragment mHiddenCameraFragment;
    int TAKE_PHOTO_CODE = 0;
    private HiddenCameraFragment mHiddenCameraFragment;
    //
    public static Bitmap imageBitmap;
    SurfaceView surfaceView;
    Button register;
    SurfaceHolder surfaceHolder;
    private boolean safeToTakePicture = false;
    ImageView imageView2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    SQLiteDatabase db;
    //ShakeDetector mShakeDetector;
    Sensor mAccelerometer;
    int count = 0;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getString(R.string.subscription_key).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }



        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();

        register = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        retrieve = findViewById(R.id.button2);
        imageView2 = findViewById(R.id.imageView2);

//        db = openOrCreateDatabase("image", 0, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS image(photo BLOB);");
//

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Button retrieve= findViewById(R.id.button2);
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // imageView2.setImageBitmap(databaseHelper.getImage());
                  //  demoCamActivity.takePicture();
           //    Toast.makeText(getApplicationContext(),"Kem palti",Toast.LENGTH_LONG).show();
                // verification(databaseHelper.getImage());
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            @Override
            public void onShake() {
                if (mHiddenCameraFragment != null) {    //Remove fragment from container if present
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(mHiddenCameraFragment)
                            .commit();
                    mHiddenCameraFragment = null;
                }
                startActivity(new Intent(MainActivity.this, DemoCamActivity.class));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                databaseHelper.addEntry(byteArray);
                Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                //}
//            try {
//                db.execSQL("INSERT INTO image VALUES (" + imageBitmap + ")");
//                }
//
            }catch (Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mHiddenCameraFragment != null) {    //Remove fragment from container if present
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mHiddenCameraFragment)
                    .commit();
            mHiddenCameraFragment = null;
        } else { //Kill the activity
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public void detection(View view) {
        Intent intent = new Intent(this, DetectionActivity.class);
        startActivity(intent);
    }

    public void verification(Bitmap image) {
        Intent intent = new Intent(this, FaceVerificationActivity.class);
        startActivity(intent);
    }
}
