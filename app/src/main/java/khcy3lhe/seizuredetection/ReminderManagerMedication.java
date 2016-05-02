package khcy3lhe.seizuredetection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.database.Cursor;

public class ReminderManagerMedication implements Runnable{

    private Context mContext;
    private AlarmManager mAlarmManager;
    private Long mRowId;
    private DB_Medication mDbHelper;
    private Calendar mCalendar;

    public ReminderManagerMedication(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void run() {
        Intent i = new Intent(mContext, OnAlarmReceiverMedication.class);
        i.putExtra(DB_Medication.KEY_ROWID, (long)mRowId);

        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, PendingIntent.FLAG_ONE_SHOT);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
    }

    public void setReminder() {
        mDbHelper = new DB_Medication(mContext);
        mDbHelper.open();
        getData();
    }


    private void getData(){
        if (mRowId != null) {
            Cursor reminder = mDbHelper.fetchMedication(mRowId);

            // Get the date from the database and format it for our use.
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm");
            Date date;
            try {
                String dateString = reminder.getString(reminder.getColumnIndexOrThrow(DB_Medication.KEY_TIME));
                date = dateTimeFormat.parse(dateString);
                mCalendar.setTime(date);
            } catch (ParseException e) {
                Log.e("ReminderEditActivity", e.getMessage(), e);
            }
        }
    }


}
