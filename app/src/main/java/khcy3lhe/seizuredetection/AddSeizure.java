package khcy3lhe.seizuredetection;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddSeizure extends AppCompatActivity{

    private DB_Seizure dbHelper;
    static int numberofRow;
    private DB_Personal dbPersonal;
    static Cursor cursor;

    static Spinner spinnerSeizureType;
    static EditText DateEdit;
    static EditText TimeEdit;
    static EditText DurationEdit;
    static EditText CommentsEdit;
    static Spinner spinnerPreictal;
    static Spinner spinnerPostictal;
    static Spinner spinnerTrigger;
    static Spinner spinnerSleep;
    static Switch buttonMedicated;
    static Button saveButton;

    static String itemSeizure;
    static String itemDate;
    static String itemTime;
    static String itemDuration;
    static String itemPreictal;
    static String itemPostictal;
    static String itemTrigger;
    static String itemSleep;
    static int itemMedicated;
    static String itemComments;

    private static Calendar c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seizure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Run DB_Medication
        dbHelper = new DB_Seizure(this);
        dbHelper.open();
        numberofRow = dbHelper.numberOfRows();

        //Run DB_Persoanal
        dbPersonal = new DB_Personal(this);
        dbPersonal.open();
        int rows = dbPersonal.numberOfRows();
        if (rows > 0){
            cursor = dbPersonal.fetchAll();
            if (cursor!=null && cursor.moveToFirst()){
                //Assign selected data to each item
                itemSeizure = cursor.getString(cursor.getColumnIndex(DB_Personal.KEY_SEIZUREP));
            }
        }

        //Seizure Type
        spinnerSeizureType = (Spinner) findViewById(R.id.fill_seizureType);
        List<String> seizureType = Arrays.asList(getResources().getStringArray(R.array.seizureTypeList));
        ArrayAdapter<String> dataAdapterSeizure = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,seizureType);
        dataAdapterSeizure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeizureType.setAdapter(dataAdapterSeizure);
        spinnerSeizureType.setSelection(dataAdapterSeizure.getPosition(itemSeizure));
        spinnerSeizureType.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                itemSeizure = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Date
        DateEdit = (EditText) findViewById(R.id.fill_date);
        DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        //Time
        TimeEdit = (EditText) findViewById(R.id.fill_time);
        TimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        //Duration
        DurationEdit = (EditText) findViewById(R.id.fill_duration);

        //Preictal Symptoms
        spinnerPreictal = (Spinner) findViewById(R.id.fill_preictal);
        List<String> preictalSymptoms = Arrays.asList(getResources().getStringArray(R.array.preictalList));
        ArrayAdapter<String> dataAdapterPreictal = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,preictalSymptoms);
        dataAdapterPreictal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPreictal.setAdapter(dataAdapterPreictal);
        spinnerPreictal.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                itemPreictal = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Postictal Symptoms
        spinnerPostictal = (Spinner) findViewById(R.id.fill_postictal);
        List<String> postictalSymptoms = Arrays.asList(getResources().getStringArray(R.array.postictalList));
        ArrayAdapter<String> dataAdapterPostictal = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,postictalSymptoms);
        dataAdapterPreictal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPostictal.setAdapter(dataAdapterPostictal);
        spinnerPostictal.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                itemPostictal = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Trigger
        spinnerTrigger = (Spinner) findViewById(R.id.fill_trigger);
        List<String> trigger = Arrays.asList(getResources().getStringArray(R.array.triggerList));
        ArrayAdapter<String> dataAdapterTrigger = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,trigger);
        dataAdapterPreictal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrigger.setAdapter(dataAdapterTrigger);
        spinnerTrigger.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                itemTrigger = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Asleep/Awake
        spinnerSleep = (Spinner) findViewById(R.id.fill_sleep);
        List<String> sleep = Arrays.asList(getResources().getStringArray(R.array.sleepList));
        ArrayAdapter<String> dataAdapterSleep = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,sleep);
        dataAdapterPreictal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSleep.setAdapter(dataAdapterSleep);
        spinnerSleep.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                itemSleep = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Medicated
        buttonMedicated = (Switch) findViewById(R.id.fill_medicated);
        buttonMedicated.setChecked(false);
        buttonMedicated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   itemMedicated = 1;
                }else{
                    itemMedicated = 0;
                }
            }
        });

        //Comments
        CommentsEdit = (EditText) findViewById(R.id.fill_comment);

        saveButton = (Button) findViewById(R.id.addNew_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });
    }

    //Return to Parent Page
    public void HomeActivity(View view)
    {
        Intent intent = new Intent(AddSeizure.this, HomeActivity.class);
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
            dialog.getDatePicker().setMaxDate(new Date().getTime());

            // Create a new instance of DatePickerDialog and return it
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            // Do something with the time chosen by the user
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

            // Do something with the time chosen by the user
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy");
            itemDate = dateTimeFormat.format(c.getTime());

            DateEdit.setText(itemDate);
        }
    }

    //Fragment for Time Picker
    public static class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,DateFormat.is24HourFormat(getActivity()));
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

        itemDuration = DurationEdit.getText().toString();
        itemComments = CommentsEdit.getText().toString();

        if (itemDate==null || itemTime==null){
            //Alert Dialog for Warning
            AlertDialog.Builder warningDialog = new AlertDialog.Builder(AddSeizure.this);
            warningDialog.setTitle("Warning!");

            TextView dialogTxtWarning = new TextView(AddSeizure.this);
            ViewGroup.LayoutParams dialogTxtWarning_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtWarning.setLayoutParams(dialogTxtWarning_LayoutParams);
            dialogTxtWarning.setText("Please at least fill in Date and Start Time");

            LinearLayout layoutWarning = new LinearLayout(AddSeizure.this);
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
            AlertDialog.Builder addDialog = new AlertDialog.Builder(AddSeizure.this);
            addDialog.setTitle("Add Item");

            TextView dialogTxtConfirm = new TextView(AddSeizure.this);
            ViewGroup.LayoutParams dialogTxtConfirm_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtConfirm.setLayoutParams(dialogTxtConfirm_LayoutParams);
            dialogTxtConfirm.setText("Do you sure you want to save this?");

            LinearLayout layout = new LinearLayout(AddSeizure.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(dialogTxtConfirm);
            addDialog.setView(layout);

            addDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                // do something when the button is clicked
                public void onClick(DialogInterface arg0, int arg1) {

                    dbHelper.insertSeizure(itemSeizure, itemDate, itemTime, itemDuration,
                            itemPreictal, itemPostictal, itemTrigger, itemSleep, itemMedicated, null, itemComments);

                    //Insert data to seizure record
                    DB_SeizureRecord db_seizureRecord = new DB_SeizureRecord(AddSeizure.this);
                    int number;

                    db_seizureRecord.open();
                    number = db_seizureRecord.numberOfRows();
                    db_seizureRecord.insertSeizureRecord(itemDate, itemTime, itemDuration, 0, 0, null, 0, 0, 0, 0, null, 1);

                    if (dbHelper.numberOfRows()>numberofRow && db_seizureRecord.numberOfRows() > number){
                        //Show Text on Screen
                        Context context = getApplicationContext();
                        CharSequence text = "New seizure record saved";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);

                        toast.show();

                        //Close Database
                        dbHelper.close();

                        //Return to Parent Page
                        Intent intent = new Intent(AddSeizure.this, HomeActivity.class);
                        startActivity(intent);

                        finish();
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
        itemSeizure = null;
        itemDate = null;
        itemTime = null;
        itemDuration = null;
        itemPreictal = null;
        itemPostictal = null;
        itemTrigger = null;
        itemSleep = null;
        itemMedicated = 0;
        itemComments  = null;
    }

    @Override
    protected void onStop(){
        super.onStop();
        dbHelper.close();
    }
}
