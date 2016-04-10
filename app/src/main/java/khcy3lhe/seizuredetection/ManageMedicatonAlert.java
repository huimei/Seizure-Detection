package khcy3lhe.seizuredetection;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ManageMedicatonAlert extends AppCompatActivity {

    private DB_Medication dbHelper;
    private SimpleCursorAdapter dataAdapter;
    public String medicationName;
    public String medicationTime;
    static Button delete;
    static ListView listView;
    static Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicaton_alert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Run DB_Medication
        dbHelper = new DB_Medication(this);
        dbHelper.open();
        dbHelper.insertMedication("MedA", 1300);
        dbHelper.insertMedication("MedB", 1600);

        //List View Declare
        cursor = dbHelper.fetchAllMedication();
        listView = (ListView) findViewById(R.id.medicationList);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //Display List View
        displayListView();

        //List View Select Item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                listView.setSelected(true);

            }
        });

        //Delete Button Action
        delete = (Button) findViewById(R.id.medicationDelete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelper.deleteMedication(medicationName,medicationTime);
            }
        });
    }

    private void displayListView(){
        //The desired columns to be bound
        String[] columns = new String[] {
                DB_Medication.KEY_MEDICATION,
                DB_Medication.KEY_TIME};

        //The XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.medicationName,
                R.id.medicationTime,
        };

        //Create the adapter using the cursor pointing to the desired data as well as the layout information
        dataAdapter = new SimpleCursorAdapter(this, R.layout.add_medication, cursor, columns, to,0);

        //Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

}
