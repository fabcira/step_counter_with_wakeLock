/**
 * Created by Fabio Ciravegna, The University of Sheffield on 25/05/15.
 * f.ciravegna@shef.ac.uk
 */

package uk.ac.shef.oak.simplepedometer;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class TestBackground extends IntentService {
    public TestBackground(String name) {
        super(name);

    }
    public TestBackground(){
        super("TestBackground");
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("Is WakeLock held? ", MainActivity.mWakeLock.isHeld() + "");
            }
        }, 1, 1000);
        return ret;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
