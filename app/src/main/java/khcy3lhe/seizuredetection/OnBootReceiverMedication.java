package khcy3lhe.seizuredetection;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.database.Cursor;
import android.util.Log;

public class OnBootReceiverMedication extends BroadcastReceiver {

    private static final String TAG = ComponentInfo.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {

        ReminderManagerMedication reminderMgr = new ReminderManagerMedication(context);

        DB_Medication dbHelper = new DB_Medication(context);
        dbHelper.open();

        Cursor cursor = dbHelper.fetchAllMedication();

        if(cursor != null) {
            cursor.moveToFirst();

            int rowIdColumnIndex = cursor.getColumnIndex(DB_Medication.KEY_ROWID);
            int timeColumnIndex = cursor.getColumnIndex(DB_Medication.KEY_TIME);

            while(cursor.isAfterLast() == false) {

                Log.d(TAG, "Adding alarm from boot.");
                Log.d(TAG, "Row Id Column Index - " + rowIdColumnIndex);
                Log.d(TAG, "Date Time Column Index - " + timeColumnIndex);

                Long rowId = cursor.getLong(rowIdColumnIndex);
                String time = cursor.getString(timeColumnIndex);

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");

                try {
                    java.util.Date date = timeFormat.parse(time);
                    cal.setTime(date);

                    reminderMgr.setReminder(rowId, cal);
                } catch (java.text.ParseException e) {
                    Log.e("OnBootReceiver", e.getMessage(), e);
                }

                cursor.moveToNext();
            }
            cursor.close() ;
        }

        dbHelper.close();
    }
}
