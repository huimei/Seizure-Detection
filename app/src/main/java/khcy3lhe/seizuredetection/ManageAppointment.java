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

public class ManageAppointment extends AppCompatActivity {

    private DB_Appointment dbHelper;
    private SimpleCursorAdapter dataAdapter;
    public int ID;
    public String drName;
    public String app_date;
    public String app_time;
    static ListView listView;
    static Button delete;
    static Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Run DB_Medication
        dbHelper = new DB_Appointment(this);
        dbHelper.open();

        //Hardcoded to display data (Should be remove before launching)
        dbHelper.deleteAll();
        dbHelper.insertAppoinment("Dr Tim", 20160601, 1600, "Bring remainding medicine");
        dbHelper.insertAppoinment("Dr Ling", 20160605, 1700, "Bring remainding medicine");

        //List View Declaration
        cursor = dbHelper.fetchAllAppointment();
        listView = (ListView) findViewById(R.id.appointmentList);
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
        delete = (Button) findViewById(R.id.appointmentDelete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });
    }

    public void AddAppointment(View view) {
        Intent intent = new Intent(ManageAppointment.this, AddAppointment.class);
        startActivity(intent);
    }

    public void deleteData(){

        ID = cursor.getInt(cursor.getColumnIndex(DB_Medication.KEY_ROWID));
        drName = cursor.getString(cursor.getColumnIndex(DB_Appointment.KEY_DRNAME));
        app_date = cursor.getString(cursor.getColumnIndex(DB_Appointment.KEY_DATE));
        app_time = cursor.getString(cursor.getColumnIndex(DB_Appointment.KEY_TIME));

        //Alert Dialog for Delete
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ManageAppointment.this);
        deleteDialog.setTitle("Delete Item");

        TextView dialogTxtConfirm = new TextView(ManageAppointment.this);
        ViewGroup.LayoutParams dialogTxtConfirm_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtConfirm.setLayoutParams(dialogTxtConfirm_LayoutParams);
        dialogTxtConfirm.setText("Do you sure you want to delete this?");

        TextView dialogTxtDoctor = new TextView(ManageAppointment.this);
        ViewGroup.LayoutParams dialogTxtDoctor_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtDoctor.setLayoutParams(dialogTxtDoctor_LayoutParams);
        dialogTxtDoctor.setText("Doctor's Name: " + String.valueOf(drName));

        TextView dialogTxtDate = new TextView(ManageAppointment.this);
        ViewGroup.LayoutParams dialogTxtDate_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtDate.setLayoutParams(dialogTxtDate_LayoutParams);
        dialogTxtDate.setText("Time: " + String.valueOf(app_date));

        TextView dialogTxtTime = new TextView(ManageAppointment.this);
        ViewGroup.LayoutParams dialogTxtTime_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogTxtTime.setLayoutParams(dialogTxtTime_LayoutParams);
        dialogTxtTime.setText("Time: " + String.valueOf(app_time));

        LinearLayout layout = new LinearLayout(ManageAppointment.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(dialogTxtConfirm);
        layout.addView(dialogTxtDoctor);
        layout.addView(dialogTxtDate);
        layout.addView(dialogTxtTime);
        deleteDialog.setView(layout);

        deleteDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                dbHelper.deleteAppointment(ID);
                updateList();
                //Notify ListView About Change of Data
                listView.deferNotifyDataSetChanged();

                //Change data in Adapter
                dataAdapter.swapCursor(dbHelper.fetchAllAppointment());
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
                DB_Appointment.KEY_DRNAME,
                DB_Appointment.KEY_DATE,
                DB_Appointment.KEY_TIME,
                DB_Appointment.KEY_COMMENT};

        //The XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.doctorName,
                R.id.appointmentDate,
                R.id.appointmentTime,
                R.id.appointmentComment
        };

        //Create the adapter using the cursor pointing to the desired data as well as the layout information
        dataAdapter = new SimpleCursorAdapter(this, R.layout.add_appointment, cursor, columns, to,0);

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
