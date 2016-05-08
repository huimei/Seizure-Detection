/*
 * Copyright (c) 2015, Seraphim Sense Ltd.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written
 *    permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/*
 * Modified by Luen Hui Mei for G53IDS project only
 */

package khcy3lhe.seizuredetection;

import com.angel.sdk.BleCharacteristic;
import com.angel.sdk.BleDevice;
import com.angel.sdk.ChAccelerationEnergyMagnitude;
import com.angel.sdk.ChAccelerationWaveform;
import com.angel.sdk.ChBatteryLevel;
import com.angel.sdk.ChHeartRateMeasurement;
import com.angel.sdk.ChOpticalWaveform;
import com.angel.sdk.ChStepCount;
import com.angel.sdk.ChTemperatureMeasurement;
import com.angel.sdk.SrvActivityMonitoring;
import com.angel.sdk.SrvBattery;
import com.angel.sdk.SrvHealthThermometer;
import com.angel.sdk.SrvHeartRate;
import com.angel.sdk.SrvWaveformSignal;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import junit.framework.Assert;
import java.text.SimpleDateFormat;
import java.util.*;

public class AngelHome extends AppCompatActivity {

    private static final int RSSI_UPDATE_INTERVAL = 1000; // Milliseconds
    private static final int ANIMATION_DURATION = 500; // Milliseconds

    private int orientation;

    private GraphView mAccelerationWaveformView, mBlueOpticalWaveformView, mGreenOpticalWaveformView;

    private BleDevice mBleDevice;
    private String mBleDeviceAddress;

    private Handler mHandler;
    private Runnable mPeriodicReader;
    private ChAccelerationEnergyMagnitude mChAccelerationEnergyMagnitude = null;

    //My own implementation
    //Threshold
    static int accThreshold = 3000000;
    static int durationThreshold = 10;
    static double heartRateThresholdMedian;
    static double heartRateThresholdMean;

    //Database
    private DB_SeizureRecord dbHelper;
    private DB_Personal dbPersonal;
    static Cursor cursor;
    static int numberofRow;
    static String pname;
    static String contact;
    static  String seizure;

    //Detection record
    static int magnitude;
    static int magnitudeStart = 0;
    static int magnitudeEnd = 0;
    static int heartRate;
    static int heartRateStart = 0;
    static int heartRateEnd = 0;
    static int startFlag = 0;
    static int fiveMinsFlag = 0;
    static long startTime;
    static long endTime;
    static int duration;
    static String dateStart;
    static String timeStart;
    static String durationRecorded;
    static LinkedList<Integer> heartRecord = new LinkedList<>();
    static LinkedList<Integer> accelerometerRecord = new LinkedList<>();

    //Detection
    static LinkedList<Integer> t1 = new LinkedList<>();
    static LinkedList<Integer> t2 = new LinkedList<>();
    int size1 = 20;
    int size2 = 5;
    int firstt2 = 0;
    static int t1point = 0;
    static int t2point = 0;
    static int median1 = 0, median2 = 0;
    static long mean1 = 0, mean2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Run DB_Medication
        dbHelper = new DB_SeizureRecord(this);
        dbHelper.open();
        numberofRow = dbHelper.numberOfRows();

        //Run DB_Persoanal
        dbPersonal = new DB_Personal(this);
        dbPersonal.open();
        int rows = dbPersonal.numberOfRows();
        if (rows > 0){
            cursor = dbPersonal.fetchAll();
            if (cursor!=null && cursor.moveToFirst()){
                //Assign selected data to each item
                pname = cursor.getString(cursor.getColumnIndex(DB_Personal.KEY_NAME));
                contact = cursor.getString(cursor.getColumnIndex(DB_Personal.KEY_CONTACT));
                seizure = cursor.getString(cursor.getColumnIndex(DB_Personal.KEY_SEIZUREP));
            }
        } else{
            pname = " ";
            contact = "+60168516176";
        }

        setContentView(R.layout.activity_measurements);
        orientation = getResources().getConfiguration().orientation;

        mHandler = new Handler(this.getMainLooper());

        mPeriodicReader = new Runnable() {
            @Override
            public void run() {
                mBleDevice.readRemoteRssi();
                if (mChAccelerationEnergyMagnitude != null) {
                    mChAccelerationEnergyMagnitude.readValue(mAccelerationEnergyMagnitudeListener);
                }

                mHandler.postDelayed(mPeriodicReader, RSSI_UPDATE_INTERVAL);
            }
        };

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGreenOpticalWaveformView = (GraphView) findViewById(R.id.graph_green);
            mGreenOpticalWaveformView.setStrokeColor(0xffffffff);
            mBlueOpticalWaveformView = (GraphView) findViewById(R.id.graph_blue);
            mBlueOpticalWaveformView.setStrokeColor(0xffffffff);
            mAccelerationWaveformView = (GraphView) findViewById(R.id.graph_acceleration);
            mAccelerationWaveformView.setStrokeColor(0xfff7a300);
        }
    }

    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        assert(extras != null);
        //mBleDeviceAddress = extras.getString("ble_device_address");
        mBleDeviceAddress = "00:07:80:78:EA:EB";

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            connectGraphs(mBleDeviceAddress);
        } else {
            connect(mBleDeviceAddress);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            displaySignalStrength(0);
        }
        unscheduleUpdaters();
        mBleDevice.disconnect();
    }

    private void connectGraphs(String deviceAddress) {

        if (mBleDevice != null) {
            mBleDevice.disconnect();
        }
        mBleDevice = new BleDevice(this, mDeviceGraphLifecycleCallback, mHandler);

        try {
            mBleDevice.registerServiceClass(SrvWaveformSignal.class);

        } catch (NoSuchMethodException e) {
            throw new AssertionError();
        } catch (IllegalAccessException e) {
            throw new AssertionError();
        } catch (InstantiationException e) {
            throw new AssertionError();
        }

        mBleDevice.connect(deviceAddress);

    }

    private void connect(String deviceAddress) {
        // A device has been chosen from the list. Create an instance of BleDevice,
        // populate it with interesting services and then connect

        if (mBleDevice != null) {
            mBleDevice.disconnect();
        }
        mBleDevice = new BleDevice(this, mDeviceLifecycleCallback, mHandler);

        try {
            mBleDevice.registerServiceClass(SrvHeartRate.class);
            mBleDevice.registerServiceClass(SrvHealthThermometer.class);
            mBleDevice.registerServiceClass(SrvBattery.class);
            mBleDevice.registerServiceClass(SrvActivityMonitoring.class);

        } catch (NoSuchMethodException e) {
            throw new AssertionError();
        } catch (IllegalAccessException e) {
            throw new AssertionError();
        } catch (InstantiationException e) {
            throw new AssertionError();
        }

        mBleDevice.connect(deviceAddress);

        scheduleUpdaters();
        displayOnDisconnect();
    }

    private final BleDevice.LifecycleCallback mDeviceGraphLifecycleCallback = new BleDevice.LifecycleCallback() {
        @Override
        public void onBluetoothServicesDiscovered(BleDevice bleDevice) {
            bleDevice.getService(SrvWaveformSignal.class).getAccelerationWaveform().enableNotifications(mAccelerationWaveformListener);
            bleDevice.getService(SrvWaveformSignal.class).getOpticalWaveform().enableNotifications(mOpticalWaveformListener);
        }

        @Override
        public void onBluetoothDeviceDisconnected() {
            unscheduleUpdaters();
            connectGraphs(mBleDeviceAddress);
        }

        @Override
        public void onReadRemoteRssi(int i) {

        }
    };


    /**
     * Upon Heart Rate Service discovery starts listening to incoming heart rate
     * notifications. {@code onBluetoothServicesDiscovered} is triggered after
     * {@link BleDevice#connect(String)} is called.
     */
    private final BleDevice.LifecycleCallback mDeviceLifecycleCallback = new BleDevice.LifecycleCallback() {
        @Override
        public void onBluetoothServicesDiscovered(BleDevice device) {
            device.getService(SrvHeartRate.class).getHeartRateMeasurement().enableNotifications(mHeartRateListener);
            device.getService(SrvHealthThermometer.class).getTemperatureMeasurement().enableNotifications(mTemperatureListener);
            device.getService(SrvBattery.class).getBatteryLevel().enableNotifications(mBatteryLevelListener);
            device.getService(SrvActivityMonitoring.class).getStepCount().enableNotifications(mStepCountListener);
            mChAccelerationEnergyMagnitude = device.getService(SrvActivityMonitoring.class).getChAccelerationEnergyMagnitude();
            Assert.assertNotNull(mChAccelerationEnergyMagnitude);
        }

        @Override
        public void onBluetoothDeviceDisconnected() {
            displayOnDisconnect();
            unscheduleUpdaters();

            // Re-connect immediately
            connect(mBleDeviceAddress);
        }

        @Override
        public void onReadRemoteRssi(final int rssi) {
            displaySignalStrength(rssi);
        }
    };

    private final BleCharacteristic.ValueReadyCallback<ChAccelerationWaveform.AccelerationWaveformValue> mAccelerationWaveformListener = new BleCharacteristic.ValueReadyCallback<ChAccelerationWaveform.AccelerationWaveformValue>() {
        @Override
        public void onValueReady(ChAccelerationWaveform.AccelerationWaveformValue accelerationWaveformValue) {
            if (accelerationWaveformValue != null && accelerationWaveformValue.wave != null && mAccelerationWaveformView != null)
                for (Integer item : accelerationWaveformValue.wave) {
                    mAccelerationWaveformView.addValue(item);
                }

        }
    };

    private final BleCharacteristic.ValueReadyCallback<ChOpticalWaveform.OpticalWaveformValue> mOpticalWaveformListener = new BleCharacteristic.ValueReadyCallback<ChOpticalWaveform.OpticalWaveformValue>() {
        @Override
        public void onValueReady(ChOpticalWaveform.OpticalWaveformValue opticalWaveformValue) {
            if (opticalWaveformValue != null && opticalWaveformValue.wave != null)
                for (ChOpticalWaveform.OpticalSample item : opticalWaveformValue.wave) {
                    mGreenOpticalWaveformView.addValue(item.green);
                    mBlueOpticalWaveformView.addValue(item.blue);
                }
        }
    };

    private final BleCharacteristic.ValueReadyCallback<ChHeartRateMeasurement.HeartRateMeasurementValue> mHeartRateListener = new BleCharacteristic.ValueReadyCallback<ChHeartRateMeasurement.HeartRateMeasurementValue>() {
        @Override
        public void onValueReady(final ChHeartRateMeasurement.HeartRateMeasurementValue hrMeasurement) {
            displayHeartRate(hrMeasurement.getHeartRateMeasurement());
        }
    };

    private final BleCharacteristic.ValueReadyCallback<ChBatteryLevel.BatteryLevelValue> mBatteryLevelListener =
            new BleCharacteristic.ValueReadyCallback<ChBatteryLevel.BatteryLevelValue>() {
                @Override
                public void onValueReady(final ChBatteryLevel.BatteryLevelValue batteryLevel) {
                    displayBatteryLevel(batteryLevel.value);
                }
            };

    private final BleCharacteristic.ValueReadyCallback<ChTemperatureMeasurement.TemperatureMeasurementValue> mTemperatureListener =
            new BleCharacteristic.ValueReadyCallback<ChTemperatureMeasurement.TemperatureMeasurementValue>() {
                @Override
                public void onValueReady(final ChTemperatureMeasurement.TemperatureMeasurementValue temperature) {
                    displayTemperature(temperature.getTemperatureMeasurement());
                }
            };

    private final BleCharacteristic.ValueReadyCallback<ChStepCount.StepCountValue> mStepCountListener =
            new BleCharacteristic.ValueReadyCallback<ChStepCount.StepCountValue>() {
                @Override
                public void onValueReady(final ChStepCount.StepCountValue stepCountValue) {
                    displayStepCount(stepCountValue.value);
                }
            };

    private final BleCharacteristic.ValueReadyCallback<ChAccelerationEnergyMagnitude.AccelerationEnergyMagnitudeValue> mAccelerationEnergyMagnitudeListener =
            new BleCharacteristic.ValueReadyCallback<ChAccelerationEnergyMagnitude.AccelerationEnergyMagnitudeValue>() {
                @Override
                public void onValueReady(final ChAccelerationEnergyMagnitude.AccelerationEnergyMagnitudeValue accelerationEnergyMagnitudeValue) {
                    displayAccelerationEnergyMagnitude(accelerationEnergyMagnitudeValue.value);
                }
            };

    private void displayHeartRate(final int bpm) {

        TextView textView = (TextView)findViewById(R.id.textview_heart_rate);
        textView.setText(bpm + " bpm");

        //Assign heart rate to value
        heartRate = bpm;

        //My own implementation starts here

        //int interval = 0;
        //int sampling = 100;
        //BPM = (sampling rate / peak interval) * 60
        //Hence peak interval = (sampling rate / BPM) * 60
        //Given sampling rate is 100HZ
        //interval = (sampling / heartRate) * 60;

        if (t2point < size2){
            t2.add(heartRate);
            t2point++;
        }else{
            int v1 = t2.get(0);
            int v2 = t2.get(1);
            int v3 = t2.get(2);
            int v4 = t2.get(3);
            int v5 = t2.get(4);

            //Calculation for median
            median2 = v3;

            //Calculation for mean
            mean2 = (v1 + v2 + v3 + v4 + v5) / size2;

            firstt2 = v1;

            t2.removeFirst();
            t2.add(heartRate);
        }

        if (t1point < size1){
            t1.add(firstt2);
            t1point++;
        }else{
            int v1 = t1.get(0);
            int v2 = t1.get(1);
            int v3 = t1.get(2);
            int v4 = t1.get(3);
            int v5 = t1.get(4);
            int v6 = t1.get(5);
            int v7 = t1.get(6);
            int v8 = t1.get(7);
            int v9 = t1.get(8);
            int v10 = t1.get(9);
            int v11 = t1.get(10);
            int v12 = t1.get(11);
            int v13 = t1.get(12);
            int v14 = t1.get(13);
            int v15 = t1.get(14);
            int v16 = t1.get(15);
            int v17 = t1.get(16);
            int v18 = t1.get(17);
            int v19 = t1.get(18);
            int v20 = t1.get(19);

            //Calculation for median
            median1 = (v10 + v11) / 2;

            //Calculation for mean
            mean1 = (v1 + v2 + v3 + v4 + v5 + v6 + v7 + v8 + v9 + v10 +
                    v11 + v12 + v13 + v14 + v15 + v16 + v17 + v18 + v19 + v20) / size1;

            t1.removeFirst();
            t1.add(firstt2);
        }

        double comparisonMedian;
        double comparisonMean;
        comparisonMedian = (median2 - median1) / median1;
        comparisonMean = (mean2 - mean1) / mean1;

        if (comparisonMedian > 0.15){
            heartRateThresholdMedian = comparisonMedian;
        }
        if (comparisonMean > 0.15){
            heartRateThresholdMean = comparisonMean;
        }
        //My implementation ends here

        ScaleAnimation effect =  new ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        effect.setDuration(ANIMATION_DURATION);
        effect.setRepeatMode(Animation.REVERSE);
        effect.setRepeatCount(1);

        View heartView = findViewById(R.id.imageview_heart);
        heartView.startAnimation(effect);
    }


    private void displaySignalStrength(int db) {
        int iconId;
        if (db > -70) {
            iconId = R.drawable.ic_signal_4;
        } else if (db > - 80) {
            iconId = R.drawable.ic_signal_3;
        } else if (db > - 85) {
            iconId = R.drawable.ic_signal_2;
        } else if (db > - 87) {
            iconId = R.drawable.ic_signal_1;
        } else {
            iconId = R.drawable.ic_signal_0;
        }
        ImageView imageView = (ImageView)findViewById(R.id.imageview_signal);
        imageView.setImageResource(iconId);
        TextView textView = (TextView)findViewById(R.id.textview_signal);
        textView.setText(db + "db");
    }

    private void displayBatteryLevel(int percents) {
        int iconId;
        if (percents < 20) {
            iconId = R.drawable.ic_battery_0;
        } else if (percents < 40) {
            iconId = R.drawable.ic_battery_1;
        } else if (percents < 60) {
            iconId = R.drawable.ic_battery_2;
        } else if (percents < 80) {
            iconId = R.drawable.ic_battery_3;
        } else {
            iconId = R.drawable.ic_battery_4;
        }

        ImageView imageView = (ImageView)findViewById(R.id.imageview_battery);
        imageView.setImageResource(iconId);
        TextView textView = (TextView)findViewById(R.id.textview_battery);
        textView.setText(percents + "%");
    }

    private void displayTemperature(final float degreesCelsius) {
        TextView textView = (TextView)findViewById(R.id.textview_temperature);
        textView.setText(degreesCelsius + "\u00b0C");

        ScaleAnimation effect =  new ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        effect.setDuration(ANIMATION_DURATION);
        effect.setRepeatMode(Animation.REVERSE);
        effect.setRepeatCount(1);
        View thermometerTop = findViewById(R.id.imageview_thermometer_top);
        thermometerTop.startAnimation(effect);
    }

    private void displayStepCount(final int stepCount) {
        TextView textView = (TextView)findViewById(R.id.textview_step_count);
        Assert.assertNotNull(textView);
        textView.setText(stepCount + " steps");

        TranslateAnimation moveDown = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_PARENT, 0.25f);
        moveDown.setDuration(ANIMATION_DURATION);
        moveDown.setRepeatMode(Animation.REVERSE);
        moveDown.setRepeatCount(1);
        View stepLeft = findViewById(R.id.imageview_step_left);
        stepLeft.startAnimation(moveDown);

        TranslateAnimation moveUp = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_PARENT, -0.25f);
        moveUp.setDuration(ANIMATION_DURATION);
        moveUp.setRepeatMode(Animation.REVERSE);
        moveUp.setRepeatCount(1);
        View stepRight = findViewById(R.id.imageview_step_right);
        stepRight.startAnimation(moveUp);
    }

    private void displayAccelerationEnergyMagnitude(final int accelerationEnergyMagnitude) {
        TextView textView = (TextView) findViewById(R.id.textview_acceleration);
        Assert.assertNotNull(textView);
        textView.setText(accelerationEnergyMagnitude + "g");

        //Assign acceleration energy magnitude to value
        magnitude = accelerationEnergyMagnitude;

        //My own implementation
        if (magnitude > accThreshold && startFlag == 0){
            if (duration < 3){
                Toast.makeText(this, "Potential seizure detected on " + pname, Toast.LENGTH_SHORT).show();
                duration ++;
            }else{
                startDetection();
            }
        }else if(magnitude > accThreshold && startFlag == 1){
            detection();
        }
        if (magnitude < accThreshold && startFlag == 1){
           endDetection();
        }
        //My own implementation ends here

        ScaleAnimation effect =  new ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        effect.setDuration(ANIMATION_DURATION);
        effect.setRepeatMode(Animation.REVERSE);
        effect.setRepeatCount(1);

        View imageView = findViewById(R.id.imageview_acceleration);
        imageView.startAnimation(effect);
    }

    private void displayOnDisconnect() {
        displaySignalStrength(-99);
        displayBatteryLevel(0);
    }

    private void scheduleUpdaters() {
        mHandler.post(mPeriodicReader);
    }

    private void unscheduleUpdaters() {
        mHandler.removeCallbacks(mPeriodicReader);
    }

    public void startDetection(){
        //This is to check if heart rate increases, currently not available yet
        /*if (heartRateThresholdMedian != 0.0 || heartRateThresholdMean != 0.0){
            Toast.makeText(this, "Seizure Detected.", Toast.LENGTH_SHORT).show();

            //Get date and time
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
            dateStart = df.format(c.getTime());
            timeStart = tf.format(c.getTime());
            startTime = System.currentTimeMillis();

            //Record starting magnitude
            magnitudeStart = magnitude;
            accelerometerRecord.add(magnitudeStart);

            //Record starting heart rate
            heartRateStart = heartRate;
            heartRecord.add(heartRateStart);

            //Set flag to true
            startFlag = 1;
        }*/
        Toast.makeText(this, "Seizure Detected.", Toast.LENGTH_SHORT).show();

        sendSMSMessage("Potential seizure detected on " + pname);

        //Get date and time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        dateStart = df.format(c.getTime());
        timeStart = tf.format(c.getTime());
        startTime = System.currentTimeMillis();

        //Record starting magnitude
        magnitudeStart = magnitude;
        accelerometerRecord.add(magnitudeStart);

        //Record starting heart rate
        heartRateStart = heartRate;
        heartRecord.add(heartRateStart);

        //Set flag to true
        startFlag = 1;
    }
    public void detection() {
        duration ++;
        accelerometerRecord.add(magnitude);
        heartRecord.add(heartRate);

        //Detect if seizure is longer than 5 minutes (used 5sec for testing purpose
        if (duration >= durationThreshold && fiveMinsFlag == 0){
            Toast.makeText(this, "Seizure longer than 5mins.", Toast.LENGTH_SHORT).show();
            sendSMSMessage("Seizure on " + pname + " happened more than 5mins. Please call ambulance service.");
            fiveMinsFlag = 1;
        }
    }
    public void endDetection(){
        Toast.makeText(this, "Seizure Ended.", Toast.LENGTH_SHORT).show();

        //Get time
        endTime = System.currentTimeMillis();

        //Record ending magnitude
        magnitudeEnd = magnitude;
        accelerometerRecord.add(magnitudeEnd);

        //Record ending hear rate
        heartRateEnd = heartRate;
        heartRecord.add(heartRateEnd);

        //Calculate duration
        long diff = (endTime - startTime)/1000;
        long minute = diff / 60;
        long second = diff % 60;
        durationRecorded = Long.toString(minute)+"."+Long.toString(second);

        //Convert recorded magnitude into String
        int size = accelerometerRecord.size();
        String acc = null;

        for (int i = 0; i < size; i++){
            acc = Integer.toString(accelerometerRecord.get(i)) + " ";
        }

        //Convert recorded heart rate into String
        int size1 = accelerometerRecord.size();
        String heart = null;

        for (int i = 0; i < size1; i++){
            heart = Integer.toString(heartRecord.get(i)) + " ";
        }

        //Insert data to seizure record
        dbHelper.insertSeizureRecord(dateStart, timeStart, durationRecorded, heartRateStart, heartRateEnd, heart,
                                    heartRateThresholdMedian, heartRateThresholdMean, magnitudeStart, magnitudeEnd, acc, 0);

        //Insert data to seizure for user to fill in details
        DB_Seizure dbSeizure = new DB_Seizure(this);
        int number;

        dbSeizure.open();
        number = dbSeizure.numberOfRows();
        dbSeizure.insertSeizure(seizure, dateStart, timeStart, durationRecorded, null, null, null, null, 0, null, null);

        //Check if database updated correctly
        if (dbHelper.numberOfRows()>numberofRow && dbSeizure.numberOfRows()>number){
            //Show Text on Screen
            Toast.makeText(this, "Seizure Recorded", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Seizure Record Fail", Toast.LENGTH_SHORT).show();
        }

        //Set all back to default
        startFlag = 0;
        fiveMinsFlag = 0;
        magnitudeStart = 0;
        magnitudeEnd = 0;
        heartRateStart = 0;
        heartRateEnd = 0;
        duration = 0;
        heartRecord.clear();
        accelerometerRecord.clear();
    }

    protected void sendSMSMessage(String message) {
        Log.i("Send SMS", "");
        String phoneNo = contact;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed to send", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}

