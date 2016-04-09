package khcy3lhe.seizuredetection;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ManageMedicatonAlert extends AppCompatActivity {

    private DB_Medication dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicaton_alert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DB_Medication(this);
        dbHelper.open();

        displayListView();
    }

    private void displayListView(){
        Cursor cursor = dbHelper.fetchAllMedication();

        // The desired columns to be bound
        String[] columns = new String[] {
                DB_Medication.KEY_MEDICATION,
                DB_Medication.KEY_TIME};

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.medicationName,
                R.id.medicationTime,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(this, R.layout.add_medication, cursor, columns, to, 0);
        ListView listView = (ListView) findViewById(R.id.medicationList);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

}
