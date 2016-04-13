package khcy3lhe.seizuredetection;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class ViewSeizure extends AppCompatActivity {

    private DB_Seizure dbHelper;

    static EditText DateEdit;
    static EditText TimeEdit;
    static int itemDate;
    static int itemTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_seizure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Run DB_Medication
        dbHelper = new DB_Seizure(this);
        dbHelper.open();


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
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());

            // Create a new instance of DatePickerDialog and return it
            return dialog;
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

            String tmp = Integer.toString(year) + stringMonth + stringDay;
            itemDate = Integer.parseInt(tmp);
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
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
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

            String tmp = stringHour + stringMinute;
            itemTime = Integer.parseInt(tmp);
        }
    }


}
