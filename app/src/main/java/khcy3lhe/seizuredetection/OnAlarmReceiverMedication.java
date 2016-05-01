package khcy3lhe.seizuredetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.util.Log;
import android.widget.Toast;

public class OnAlarmReceiverMedication extends BroadcastReceiver {

    private static final String TAG = ComponentInfo.class.getCanonicalName();


    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context.getApplicationContext(), "received",
                Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Received wake up from alarm manager.");

        long rowid = intent.getExtras().getLong(DB_Medication.KEY_ROWID);

        WakeReminderIntentService.acquireStaticLock(context);

        Intent i = new Intent(context, ReminderServiceMedication.class);
        i.putExtra(DB_Medication.KEY_ROWID, rowid);
        context.startService(i);

    }
}
