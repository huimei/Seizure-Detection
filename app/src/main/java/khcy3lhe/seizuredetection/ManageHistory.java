package khcy3lhe.seizuredetection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class ManageHistory extends AppCompatActivity {

    private DB_Seizure dbHelper;
    private SimpleCursorAdapter dataAdapter;
    public int ID;
    public String seizureType;
    public String history_date;
    public String history_time;
    static ListView listView;
    static Button delete;
    static Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Run DB_Medication
        dbHelper = new DB_Seizure(this);
        dbHelper.open();

        //Hardcoded to display data (Should be remove before launching)
        dbHelper.deleteAll();
        dbHelper.insertSeizure("Unknown", 20160601, 1600, null, null, null, null, null, 0, null, null);
        dbHelper.insertSeizure("Unknown", 20160701, 1200, null, null, null, null, null, 0, null, null);

        //List View Declaration
        cursor = dbHelper.fetchAllSeizure();
        listView = (ListView) findViewById(R.id.historyList);
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
        delete = (Button) findViewById(R.id.historyDelete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });

    }

    public void AddSeizure(View view) {
        Intent intent = new Intent(ManageHistory.this, AddSeizure.class);
        startActivity(intent);
    }

    public void deleteData(){

        ID = cursor.getInt(cursor.getColumnIndex(DB_Seizure.KEY_ROWID));
        seizureType = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_SEIZURE));
        history_date = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_DATE));
        history_time = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_STARTTIME));

        //Alert Dialog for Delete
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ManageHistory.this);
        deleteDialog.setTitle("Delete Item");

        TextView dialogTxtConfirm = new TextView(ManageHistory.this);
        ViewGroup.LayoutParams dialogTxtConfirm_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtConfirm.setLayoutParams(dialogTxtConfirm_LayoutParams);
        dialogTxtConfirm.setText("Do you sure you want to delete this?");

        TextView dialogTxtSeizure = new TextView(ManageHistory.this);
        ViewGroup.LayoutParams dialogTxtSeizure_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtSeizure.setLayoutParams(dialogTxtSeizure_LayoutParams);
        dialogTxtSeizure.setText("Seizure Type: " + String.valueOf(seizureType));

        TextView dialogTxtDate = new TextView(ManageHistory.this);
        ViewGroup.LayoutParams dialogTxtDate_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtDate.setLayoutParams(dialogTxtDate_LayoutParams);
        dialogTxtDate.setText("Date: " + String.valueOf(history_date));

        TextView dialogTxtTime = new TextView(ManageHistory.this);
        ViewGroup.LayoutParams dialogTxtTime_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtTime.setLayoutParams(dialogTxtTime_LayoutParams);
        dialogTxtTime.setText("Time: " + String.valueOf(history_time));

        LinearLayout layout = new LinearLayout(ManageHistory.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(dialogTxtConfirm);
        layout.addView(dialogTxtSeizure);
        layout.addView(dialogTxtDate);
        layout.addView(dialogTxtTime);
        deleteDialog.setView(layout);

        deleteDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                dbHelper.deleteSeizure(ID);
                updateList();
                //Notify ListView About Change of Data
                listView.deferNotifyDataSetChanged();

                //Change data in Adapter
                dataAdapter.swapCursor(dbHelper.fetchAllSeizure());
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
                DB_Seizure.KEY_SEIZURE,
                DB_Seizure.KEY_DATE,
                DB_Seizure.KEY_STARTTIME};

        //The XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.seizureName,
                R.id.seizureDate,
                R.id.seizureStartTime,
        };

        //Create the adapter using the cursor pointing to the desired data as well as the layout information
        dataAdapter = new SimpleCursorAdapter(this, R.layout.add_seizure, cursor, columns, to,0);

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
