/**
 * Created by Fabio Ciravegna, The University of Sheffield on 25/05/15.
 * f.ciravegna@shef.ac.uk
 *
 * code modified from https://github.com/theelfismike/android-step-counter
 * to check if with Wake
 */


package uk.ac.shef.oak.simplepedometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener {

    public static final String TAG = MainActivity.class.getName();

    private SensorManager sensorManager;
    private TextView count;
//    boolean activityRunning;
    int mBaseSteps=0;
    boolean mFoundStepBase =false;
    static PowerManager.WakeLock mWakeLock;
    private Intent mService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count = (TextView) findViewById(R.id.text_step);
        acquireWakeLock();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        registerSensor();

        // starting the service that checks if the wakeLock is active
        mService=new Intent(this, TestBackground.class);
        startService(mService);
    }

    /**
     * it registers the step counter sensor
     */
    private void registerSensor() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            Log.i(TAG, "YES!! Count sensor available!");
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else{
            Log.i(TAG, "Count sensor not available!");
        }
    }


    /**
     * it acquires the wakelock.
     */
    private void acquireWakeLock() {
        try {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm == null) {
                Log.e(TAG, "Power manager not found!");
                return;
            }
            if (mWakeLock == null) {
                mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getPackageName());
                if (mWakeLock == null) {
                    Log.e(TAG, "Could not create wake lock (null).");
                    return;
                }
            }
            if (!mWakeLock.isHeld()) {
                mWakeLock.acquire();
                if (!mWakeLock.isHeld()) {
                    Log.e(TAG, "Could not acquire wake lock.");
                }
            }
        } catch (RuntimeException e) {
            Log.e(TAG, "Caught unexpected exception: " + e.getMessage(), e);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
         // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this); 
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String str = String.valueOf(event.values[0]);
        int currSteps = (int) Float.parseFloat(str);
        if (!mFoundStepBase) {
            mBaseSteps = currSteps;
            mFoundStepBase = true;
        }
        Log.i("STEPS:", "Steps currently: " + (currSteps - mBaseSteps) + "/" + currSteps);
        count.setText("Steps currently: " + (currSteps - mBaseSteps) + "/" + currSteps);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}