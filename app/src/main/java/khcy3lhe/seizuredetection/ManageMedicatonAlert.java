package khcy3lhe.seizuredetection;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ManageMedicatonAlert extends AppCompatActivity {

    private DB_Medication dbHelper;
    private SimpleCursorAdapter dataAdapter;
    public int ID;
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

        //Hardcoded to display data (Should be remove before launching)
        dbHelper.deleteAll();
        dbHelper.insertMedication("MedA", 1500);
        dbHelper.insertMedication("MedB", 1845);

        //List View Declaration
        cursor = dbHelper.fetchAllMedication();
        listView = (ListView) findViewById(R.id.medicationList);
        listView.setScrollContainer(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //Display List View
        displayListView();

        //List View Select Item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                listView.setSelected(true);

                // Get the cursor, positioned to the corresponding row in the result set
                cursor = (Cursor) listView.getItemAtPosition(position);
            }
        });

        //Delete Button Action
        delete = (Button) findViewById(R.id.medicationDelete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });
    }

    public void AddMedication (View view)
    {
        Intent intent = new Intent(ManageMedicatonAlert.this, AddMedication.class);
        startActivity(intent);
    }

    public void deleteData(){

        ID = cursor.getInt(cursor.getColumnIndex(DB_Medication.KEY_ROWID));
        medicationName = cursor.getString(cursor.getColumnIndex(DB_Medication.KEY_MEDICATION));
        medicationTime = cursor.getString(cursor.getColumnIndex(DB_Medication.KEY_TIME));

        //Alert Dialog for Delete
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ManageMedicatonAlert.this);
        deleteDialog.setTitle("Delete Item");

        TextView dialogTxtConfirm = new TextView(ManageMedicatonAlert.this);
        ViewGroup.LayoutParams dialogTxtConfirm_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtConfirm.setLayoutParams(dialogTxtConfirm_LayoutParams);
        dialogTxtConfirm.setText("Do you sure you want to delete this?");

        TextView dialogTxtMedication = new TextView(ManageMedicatonAlert.this);
        ViewGroup.LayoutParams dialogTxtMedication_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtMedication.setLayoutParams(dialogTxtMedication_LayoutParams);
        dialogTxtMedication.setText("Medicine Name: " + String.valueOf(medicationName));

        TextView dialogTxtTime = new TextView(ManageMedicatonAlert.this);
        ViewGroup.LayoutParams dialogTxtTime_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtTime.setLayoutParams(dialogTxtTime_LayoutParams);
        dialogTxtTime.setText("Time: " + String.valueOf(medicationTime));

        LinearLayout layout = new LinearLayout(ManageMedicatonAlert.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(dialogTxtConfirm);
        layout.addView(dialogTxtMedication);
        layout.addView(dialogTxtTime);
        deleteDialog.setView(layout);

        deleteDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                dbHelper.deleteMedication(ID);
                updateList();
                //Notify ListView About Change of Data
                listView.deferNotifyDataSetChanged();

                //Change data in Adapter
                dataAdapter.swapCursor(dbHelper.fetchAllMedication());
            }
        });

        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        deleteDialog.show();
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    protected void onStop(){
        super.onStop();
        dbHelper.close();
    }

    private void updateList(){
        cursor.requery();
    }

}
