package khcy3lhe.seizuredetection;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewSeizure extends AppCompatActivity {

    private DB_Seizure dbHelper;
    static Cursor cursor;
    static int ID;

    static TextView DateEdit;
    static TextView TimeEdit;
    static TextView DurationEdit;
    static EditText CommentsEdit;
    static Spinner spinnerSeizureType;
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
    static String itemVideo;
    static String itemComments;

    private static Calendar c;


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
        ID = ManageHistory.ID;
        cursor = dbHelper.fetchSeizure(ID);

        if (cursor!=null && cursor.moveToFirst()){
            //Assign selected data to each item
            itemSeizure = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_SEIZURE));
            itemDate = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_DATE));
            itemTime = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_STARTTIME));
            itemDuration = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_DURATION));
            itemPreictal = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_PREICTAL));
            itemPostictal = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_POSTICTAL));
            itemTrigger = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_TRIGGER));
            itemSleep = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_SLEEP));
            itemMedicated = cursor.getInt(cursor.getColumnIndex(DB_Seizure.KEY_MEDICATED));
            itemVideo = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_VIDEO));
            itemComments = cursor.getString(cursor.getColumnIndex(DB_Seizure.KEY_COMMENTS));
        }

        //Seizure Type
        spinnerSeizureType = (Spinner) findViewById(R.id.viewfill_seizureType);
        List<String> seizureType = Arrays.asList(getResources().getStringArray(R.array.seizureTypeList));
        ArrayAdapter<String> dataAdapterSeizure = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,seizureType);
        dataAdapterSeizure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeizureType.setAdapter(dataAdapterSeizure);
        spinnerSeizureType.setSelection(dataAdapterSeizure.getPosition(itemSeizure));
        spinnerSeizureType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

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
        DateEdit = (TextView) findViewById(R.id.viewfill_date);
        DateEdit.setText(String.valueOf(itemDate));
        itemDate = DateEdit.getText().toString();

        //Time
        TimeEdit = (TextView) findViewById(R.id.viewfill_time);
        TimeEdit.setText(String.valueOf(itemTime));
        itemTime = TimeEdit.getText().toString();

        //Duration
        DurationEdit = (TextView) findViewById(R.id.viewfill_duration);
        DurationEdit.setText(String.valueOf(itemDuration));
        itemDuration = DurationEdit.getText().toString();

        //Preictal Symptoms
        spinnerPreictal = (Spinner) findViewById(R.id.viewfill_preictal);
        List<String> preictalSymptoms = Arrays.asList(getResources().getStringArray(R.array.preictalList));
        ArrayAdapter<String> dataAdapterPreictal = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,preictalSymptoms);
        dataAdapterPreictal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPreictal.setAdapter(dataAdapterPreictal);
        spinnerPreictal.setSelection(dataAdapterPreictal.getPosition(itemPreictal));
        spinnerPreictal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

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
        spinnerPostictal = (Spinner) findViewById(R.id.viewfill_postictal);
        List<String> postictalSymptoms = Arrays.asList(getResources().getStringArray(R.array.postictalList));
        ArrayAdapter<String> dataAdapterPostictal = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,postictalSymptoms);
        dataAdapterPreictal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPostictal.setAdapter(dataAdapterPostictal);
        spinnerPostictal.setSelection(dataAdapterPostictal.getPosition(itemPostictal));
        spinnerPostictal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

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
        spinnerTrigger = (Spinner) findViewById(R.id.viewfill_trigger);
        List<String> trigger = Arrays.asList(getResources().getStringArray(R.array.triggerList));
        ArrayAdapter<String> dataAdapterTrigger = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,trigger);
        dataAdapterPreictal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrigger.setAdapter(dataAdapterTrigger);
        spinnerTrigger.setSelection(dataAdapterTrigger.getPosition(itemTrigger));
        spinnerTrigger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

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
        spinnerSleep = (Spinner) findViewById(R.id.viewfill_sleep);
        List<String> sleep = Arrays.asList(getResources().getStringArray(R.array.sleepList));
        ArrayAdapter<String> dataAdapterSleep = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,sleep);
        dataAdapterPreictal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSleep.setAdapter(dataAdapterSleep);
        spinnerSleep.setSelection(dataAdapterSleep.getPosition(itemSleep));
        spinnerSleep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

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
        buttonMedicated = (Switch) findViewById(R.id.viewfill_medicated);
        if (itemMedicated == 0){
            buttonMedicated.setChecked(false);
        } else{
            buttonMedicated.setChecked(true);
        }
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
        CommentsEdit = (EditText) findViewById(R.id.viewfill_comment);
        CommentsEdit.setText(String.valueOf(itemComments));
        itemComments = CommentsEdit.getText().toString();

        saveButton = (Button) findViewById(R.id.viewaddNew_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });
    }

    public void addData() {

        if (itemDate==null || itemTime==null){
            //Alert Dialog for Warning
            AlertDialog.Builder warningDialog = new AlertDialog.Builder(ViewSeizure.this);
            warningDialog.setTitle("Warning!");

            TextView dialogTxtWarning = new TextView(ViewSeizure.this);
            ViewGroup.LayoutParams dialogTxtWarning_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtWarning.setLayoutParams(dialogTxtWarning_LayoutParams);
            dialogTxtWarning.setText("Please at least fill in Date and Start Time");

            LinearLayout layoutWarning = new LinearLayout(ViewSeizure.this);
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
            AlertDialog.Builder addDialog = new AlertDialog.Builder(ViewSeizure.this);
            addDialog.setTitle("Change Item");

            TextView dialogTxtConfirm = new TextView(ViewSeizure.this);
            ViewGroup.LayoutParams dialogTxtConfirm_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtConfirm.setLayoutParams(dialogTxtConfirm_LayoutParams);
            dialogTxtConfirm.setText("Do you sure you want to save these changes?");

            LinearLayout layout = new LinearLayout(ViewSeizure.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(dialogTxtConfirm);
            addDialog.setView(layout);

            addDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                // do something when the button is clicked
                public void onClick(DialogInterface arg0, int arg1) {

                    try{
                        //Update seizure record
                        DB_SeizureRecord db_seizureRecord = new DB_SeizureRecord(ViewSeizure.this);
                        db_seizureRecord.open();
                        db_seizureRecord.updateSeizureRecord(itemDate, itemTime, itemDuration, 0);

                        dbHelper.updateSeizure(ID, itemSeizure, itemDate, itemTime, itemDuration,
                                itemPreictal, itemPostictal, itemTrigger, itemSleep, itemMedicated, itemVideo, itemComments);

                        //Show Text on Screen
                        Context context = getApplicationContext();
                        CharSequence text = "Seizure record updated";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);

                        toast.show();

                        //Close Database
                        dbHelper.close();

                        //Return to Parent Page
                        Intent intent = new Intent(ViewSeizure.this, ManageHistory.class);
                        startActivity(intent);

                        finish();

                    } catch (SQLException e){
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
