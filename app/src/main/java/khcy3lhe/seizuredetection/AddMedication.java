package khcy3lhe.seizuredetection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddMedication extends AppCompatActivity {

    private DB_Medication dbHelper;
    static EditText TimeEdit;
    static Spinner spinnerMedicationName;
    static Button save;
    static int TIME;
    static String NAME;
    static int numberofRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Run DB_Medication
        dbHelper = new DB_Medication(this);
        dbHelper.open();
        numberofRow = dbHelper.numberOfRows();

        //Seizure Type
        spinnerMedicationName = (Spinner) findViewById(R.id.medicationNameEdit);
        List<String> seizureType = Arrays.asList(getResources().getStringArray(R.array.medicationName));
        ArrayAdapter<String> dataAdapterSeizure = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,seizureType);
        dataAdapterSeizure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicationName.setAdapter(dataAdapterSeizure);

        //Time
        TimeEdit = (EditText) findViewById(R.id.medicationTimeEdit);
        TimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        //Get Selected Medication Name
        spinnerMedicationName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                NAME = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //No action
            }
        });

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
    public void ManageMedicationAlert(View view)
    {
        Intent intent = new Intent(AddMedication.this, ManageMedicatonAlert.class);
        startActivity(intent);
    }

    //Show Fragment
    public void showTimePickerDialog(View v) {
        DialogFragment TimePickerFragment = new AddMedication.TimeFragment();
        TimePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //Fragment for Time Picker
    public static class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            String tmp = Integer.toString(hour) + Integer.toString(minute);
            TIME = Integer.parseInt(tmp);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            TimeEdit.setText(hourOfDay + ":" + minute);
        }
    }

    public void addData() {

        if (NAME==null || TIME==0){
            //Alert Dialog for Warning
            AlertDialog.Builder warningDialog = new AlertDialog.Builder(AddMedication.this);
            warningDialog.setTitle("Warning!");

            TextView dialogTxtWarning = new TextView(AddMedication.this);
            ViewGroup.LayoutParams dialogTxtWarning_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtWarning.setLayoutParams(dialogTxtWarning_LayoutParams);
            dialogTxtWarning.setText("Please select a medication or time");

            LinearLayout layoutWarning = new LinearLayout(AddMedication.this);
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
            AlertDialog.Builder addDialog = new AlertDialog.Builder(AddMedication.this);
            addDialog.setTitle("Add Item");

            TextView dialogTxtConfirm = new TextView(AddMedication.this);
            ViewGroup.LayoutParams dialogTxtConfirm_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtConfirm.setLayoutParams(dialogTxtConfirm_LayoutParams);
            dialogTxtConfirm.setText("Do you sure you want to add this?");

            TextView dialogTxtMedication = new TextView(AddMedication.this);
            ViewGroup.LayoutParams dialogTxtMedication_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtMedication.setLayoutParams(dialogTxtMedication_LayoutParams);
            dialogTxtMedication.setText("Medicine Name: " + NAME);

            TextView dialogTxtTime = new TextView(AddMedication.this);
            ViewGroup.LayoutParams dialogTxtTime_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtTime.setLayoutParams(dialogTxtTime_LayoutParams);
            dialogTxtTime.setText("Time: " + Integer.toString(TIME));

            LinearLayout layout = new LinearLayout(AddMedication.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(dialogTxtConfirm);
            layout.addView(dialogTxtMedication);
            layout.addView(dialogTxtTime);
            addDialog.setView(layout);

            addDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                // do something when the button is clicked
                public void onClick(DialogInterface arg0, int arg1) {
                    dbHelper.insertMedication(NAME, TIME);
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
                        Intent intent = new Intent(AddMedication.this, ManageMedicatonAlert.class);
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
}
