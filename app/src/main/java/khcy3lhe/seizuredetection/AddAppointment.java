package khcy3lhe.seizuredetection;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAppointment extends AppCompatActivity {

    private DB_Appointment dbHelper;
    static EditText DrName;
    static EditText DateEdit;
    static EditText TimeEdit;
    static EditText CommentEdit;
    static Button save;
    static String itemDrName;
    static String itemDate;
    static String itemTime;
    static String itemComment;

    static int numberofRow;
    private static Calendar c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Run DB_Medication
        dbHelper = new DB_Appointment(this);
        dbHelper.open();
        numberofRow = dbHelper.numberOfRows();

        //Doctor's name
        DrName = (EditText) findViewById(R.id.fillDrName);

        //Date
        DateEdit = (EditText) findViewById(R.id.fillAppDate);
        DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        //Time
        TimeEdit = (EditText) findViewById(R.id.fillAppTime);
        TimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        //Comment
        CommentEdit = (EditText) findViewById(R.id.fillAppComment);

        //Save Button Action
        save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });
    }

    //Return to Parent Page
    public void ManageAppointment(View view) {
        Intent intent = new Intent(AddAppointment.this, ManageAppointment.class);
        startActivity(intent);
    }

    //Show fragments
    public void showDatePickerDialog(View v) {
        DialogFragment DatePickerFragment = new CalenderFragment();
        DatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment TimePickerFragment = new TimeFragment();
        TimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }


    //Fragment for Date Picker
    public static class CalenderFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());

            // Create a new instance of DatePickerDialog and return it
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            // Do something with the time chosen by the user
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

            // Do something with the time chosen by the user
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
            itemDate = dateTimeFormat.format(c.getTime());

            DateEdit.setText(itemDate);
        }
    }

    //Fragment for Time Picker
    public static class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {

            // Do something with the time chosen by the user
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);

            // Do something with the time chosen by the user
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm");
            itemTime = dateTimeFormat.format(c.getTime());

            TimeEdit.setText(itemTime);
        }
    }

    public void addData() {

        itemDrName = DrName.getText().toString();
        itemComment = CommentEdit.getText().toString();

        if (itemDrName==null || itemDate==null || itemTime==null){
            //Alert Dialog for Warning
            AlertDialog.Builder warningDialog = new AlertDialog.Builder(AddAppointment.this);
            warningDialog.setTitle("Warning!");

            TextView dialogTxtWarning = new TextView(AddAppointment.this);
            ViewGroup.LayoutParams dialogTxtWarning_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtWarning.setLayoutParams(dialogTxtWarning_LayoutParams);
            dialogTxtWarning.setText("Please fill in the name, date and time");

            LinearLayout layoutWarning = new LinearLayout(AddAppointment.this);
            layoutWarning.setOrientation(LinearLayout.VERTICAL);
            layoutWarning.addView(dialogTxtWarning);
            warningDialog.setView(layoutWarning);

            warningDialog.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                // do something when the button is clicked
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            warningDialog.show();
        } else {
            //Alert Dialog for Add
            AlertDialog.Builder addDialog = new AlertDialog.Builder(AddAppointment.this);
            addDialog.setTitle("Add Item");

            TextView dialogTxtConfirm = new TextView(AddAppointment.this);
            ViewGroup.LayoutParams dialogTxtConfirm_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtConfirm.setLayoutParams(dialogTxtConfirm_LayoutParams);
            dialogTxtConfirm.setText("Do you sure you want to add this?");

            TextView dialogTxtDrName = new TextView(AddAppointment.this);
            ViewGroup.LayoutParams dialogTxtDrName_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtDrName.setLayoutParams(dialogTxtDrName_LayoutParams);
            dialogTxtDrName.setText("Doctor's Name: " + itemDrName);

            TextView dialogTxtDate = new TextView(AddAppointment.this);
            ViewGroup.LayoutParams dialogTxtDate_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtDate.setLayoutParams(dialogTxtDate_LayoutParams);
            dialogTxtDate.setText("Date: " + itemDate);

            TextView dialogTxtTime = new TextView(AddAppointment.this);
            ViewGroup.LayoutParams dialogTxtTime_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtTime.setLayoutParams(dialogTxtTime_LayoutParams);
            dialogTxtTime.setText("Time: " + itemTime);

            TextView dialogTxtComment = new TextView(AddAppointment.this);
            ViewGroup.LayoutParams dialogTxtComment_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtComment.setLayoutParams(dialogTxtComment_LayoutParams);
            dialogTxtComment.setText("Comments: " + itemComment);

            LinearLayout layout = new LinearLayout(AddAppointment.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(dialogTxtConfirm);
            layout.addView(dialogTxtDrName);
            layout.addView(dialogTxtDate);
            layout.addView(dialogTxtTime);
            layout.addView(dialogTxtComment);
            addDialog.setView(layout);

            addDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                // do something when the button is clicked
                public void onClick(DialogInterface arg0, int arg1) {
                    dbHelper.insertAppointment(itemDrName, itemDate, itemTime, itemComment);
                    if (dbHelper.numberOfRows()>numberofRow){
                        //Show Text on Screen
                        Context context = getApplicationContext();
                        CharSequence text = "New medication reminder saved";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);

                        toast.show();

                        //Close Database
                        dbHelper.close();

                        //Return to Parent Page
                        Intent intent = new Intent(AddAppointment.this, ManageAppointment.class);
                        startActivity(intent);
                    }else{
                        Context context1 = getApplicationContext();
                        CharSequence text1 = "Data couldn't be save";
                        int duration1 = Toast.LENGTH_SHORT;
                        Toast toast1 = Toast.makeText(context1, text1, duration1);
                        toast1.show();
                    }
                }
            });

            addDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                // do something when the button is clicked
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

            addDialog.show();
        }
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


}
