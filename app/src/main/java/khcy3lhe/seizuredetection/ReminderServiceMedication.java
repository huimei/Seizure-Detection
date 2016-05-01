package khcy3lhe.seizuredetection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;



public class ReminderServiceMedication extends WakeReminderIntentService {

    public ReminderServiceMedication() {
        super("ReminderService");
    }

    @Override
    void doReminderWork(Intent intent) {
        Log.d("ReminderService", "Doing work.");
        Long rowId = intent.getExtras().getLong(DB_Medication.KEY_ROWID);

        NotificationManager mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiverMedicationActivity.class);
        notificationIntent.putExtra(DB_Medication.KEY_ROWID, rowId);

        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder note = new NotificationCompat.Builder(this);
        note.setTicker("Task Reminder!");
        note.setContentTitle(getString(R.string.notify_new_task_title));
        note.setContentText(getString(R.string.notify_new_task_message));
        note.setSmallIcon(android.R.drawable.stat_sys_warning);
        note.setContentIntent(pi);
        note.setOngoing(true);
        note.setDefaults(Notification.DEFAULT_SOUND);

        // An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value).
        // I highly doubt this will ever happen. But is good to note.
        int id = (int)((long)rowId);
        mgr.notify(id, note.build());
    }
}

