package khcy3lhe.seizuredetection;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private Long mRowId;
    private DB_Medication mDbHelper;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DB_Medication(this);
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(DB_Medication.KEY_ROWID)
                : null;

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDbHelper.open();
        setRowIdFromIntent();
        getData();
    }

    private void setRowIdFromIntent() {
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(DB_Medication.KEY_ROWID)
                    : null;

        }
    }

    private void getData(){
        if (mRowId != null) {
            Cursor reminder = mDbHelper.fetchMedication(mRowId);
            startManagingCursor(reminder);

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

    private void saveState() {
        new ReminderManagerMedication(this).setReminder(mRowId, mCalendar);
    }

    public void InfoPage(View view) {
        Intent intent = new Intent(HomeActivity.this, InfoPage.class);
        startActivity(intent);
    }

    public void AddSeizure(View view) {
        Intent intent = new Intent(HomeActivity.this, AddSeizure.class);
        startActivity(intent);
    }

    public void ManagePage(View view) {
        Intent intent = new Intent(HomeActivity.this, ManagePage.class);
        startActivity(intent);
    }

    public void ManageHistory(View view) {
        Intent intent = new Intent(HomeActivity.this, ManageHistory.class);
        startActivity(intent);
    }

    public void ScanActivity (View view) {
        Intent intent = new Intent(HomeActivity.this, ScanActivity.class);
        startActivity(intent);
    }
}
