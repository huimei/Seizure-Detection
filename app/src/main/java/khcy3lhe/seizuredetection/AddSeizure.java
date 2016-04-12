package khcy3lhe.seizuredetection;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddSeizure extends AppCompatActivity{

    private DB_Seizure dbHelper;
    static int numberofRow;

    static EditText DateEdit;
    static EditText TimeEdit;
    static Spinner spinnerSeizureType;
    static Spinner spinnerPreictal;
    static Spinner spinnerPostictal;
    static Spinner spinnerTrigger;
    static Spinner spinnerSleep;
    static Switch buttonMedicated;
    static String itemSeizure;
    static int itemDate;
    static int itemTime;
    static String itemPreictal;
    static String itemPostictal;
    static String itemTrigger;
    static String itemSleep;
    static int itemMedicated;


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
        boolean exists = dbHelper.isTableExists();

        if (exists==true){
            Context context1 = getApplicationContext();
            CharSequence text1 = "It Exists!";
            int duration1 = Toast.LENGTH_SHORT;
            Toast toast1 = Toast.makeText(context1, text1, duration1);
            toast1.show();
        }else {
            Context context2 = getApplicationContext();
            CharSequence text2 = "Nope, you are screwed";
            int duration2 = Toast.LENGTH_SHORT;
            Toast toast2 = Toast.makeText(context2, text2, duration2);
            toast2.show();
        }


        //Seizure Type
        spinnerSeizureType = (Spinner) findViewById(R.id.fill_seizureType);
        List<String> seizureType = Arrays.asList(getResources().getStringArray(R.array.seizureTypeList));
        ArrayAdapter<String> dataAdapterSeizure = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,seizureType);
        dataAdapterSeizure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeizureType.setAdapter(dataAdapterSeizure);
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
        spinnerPostictal.setAdapter(dataAdapterTrigger);
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
        spinnerPostictal.setAdapter(dataAdapterSleep);
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

            String stringMonth;
            String stringDay;
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            if (month>=10){
                stringMonth = Integer.toString(month+1);
            } else{
                stringMonth = "0" + Integer.toString(month+1);
            }

            if (day>=10){
                stringDay = Integer.toString(day);
            } else{
                stringDay = "0" + Integer.toString(day);
            }
            String tmp = Integer.toString(year) + stringMonth + stringDay;

            itemDate = Integer.parseInt(tmp);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            String stringMonth;
            String stringDay;

            if (month>=10){
                stringMonth = Integer.toString(month+1);
            } else{
                stringMonth = "0" + Integer.toString(month+1);
            }
            if (day>=10){
                stringDay = Integer.toString(day);
            } else{
                stringDay = "0" + Integer.toString(day);
            }
            // Do something with the date chosen by the user
            DateEdit.setText(stringDay + "/" + stringMonth + "/" + year);
        }
    }

    //Fragment for Time Picker
    public static class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String stringHour;
            String stringMinute;

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            if (hour>=10){
                stringHour = Integer.toString(hour);
            }else {
                stringHour = "0" + Integer.toString(hour);
            }
            if (minute>=10){
                stringMinute = Integer.toString(minute);
            }else {
                stringMinute = "0" + Integer.toString(minute);
            }

            String tmp = stringHour + stringMinute;
            itemTime = Integer.parseInt(tmp);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            String stringHour;
            String stringMinute;

            if (hour>=10){
                stringHour = Integer.toString(hour);
            }else {
                stringHour = "0" + Integer.toString(hour);
            }
            if (minute>=10){
                stringMinute = Integer.toString(minute);
            }else {
                stringMinute = "0" + Integer.toString(minute);
            }

            // Do something with the time chosen by the user
            TimeEdit.setText(stringHour + ":" + stringMinute);
        }
    }
}
