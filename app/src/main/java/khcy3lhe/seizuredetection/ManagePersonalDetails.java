package khcy3lhe.seizuredetection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class ManagePersonalDetails extends AppCompatActivity {

    private DB_Personal dbHelper;
    static Cursor cursor;
    static int numberofRows;

    static EditText pname;
    static Spinner gender;
    static Spinner spinnerSeizureType;
    static EditText contact;
    static Button saveButton;

    static String patientName;
    static String patientGender;
    static String patientSeizure;
    static String patientContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_personal_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Run DB_Medication
        dbHelper = new DB_Personal(this);
        dbHelper.open();
        numberofRows = dbHelper.numberOfRows();

        if (numberofRows > 0){
            cursor = dbHelper.fetchAll();
            if (cursor!=null && cursor.moveToFirst()){
                //Assign selected data to each item
                patientName = cursor.getString(cursor.getColumnIndex(DB_Personal.KEY_NAME));
                patientGender = cursor.getString(cursor.getColumnIndex(DB_Personal.KEY_GENDER));
                patientSeizure = cursor.getString(cursor.getColumnIndex(DB_Personal.KEY_SEIZUREP));
                patientContact = cursor.getString(cursor.getColumnIndex(DB_Personal.KEY_CONTACT));
            }
        }

        //Patient name
        pname = (EditText) findViewById(R.id.nameText);
        if (numberofRows > 0){
            pname.setText(patientName);
        }


        //Patient gender
        gender = (Spinner) findViewById(R.id.genderPersonal);
        List<String> genderList = Arrays.asList(getResources().getStringArray(R.array.gender));
        ArrayAdapter<String> dataAdapterGender = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,genderList);
        dataAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(dataAdapterGender);
        if (numberofRows > 0){
            gender.setSelection(dataAdapterGender.getPosition(patientGender));
        }
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                patientGender = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Seizure Type
        spinnerSeizureType = (Spinner) findViewById(R.id.seizurePersonal);
        List<String> seizureType = Arrays.asList(getResources().getStringArray(R.array.seizureTypeList));
        ArrayAdapter<String> dataAdapterSeizure = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,seizureType);
        dataAdapterSeizure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeizureType.setAdapter(dataAdapterSeizure);
        if (numberofRows > 0){
            spinnerSeizureType.setSelection(dataAdapterSeizure.getPosition(patientSeizure));
        }
        spinnerSeizureType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                patientSeizure = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Patient name
        contact = (EditText) findViewById(R.id.phonePersonal);
        if (numberofRows > 0){
            contact.setText(patientContact);
        }


        //Save Button
        saveButton = (Button) findViewById(R.id.savePersonal);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });
    }

    public void addData() {

        patientName = pname.getText().toString();
        patientContact = contact.getText().toString();

        if (patientName==null || patientContact==null){
            //Alert Dialog for Warning
            AlertDialog.Builder warningDialog = new AlertDialog.Builder(ManagePersonalDetails.this);
            warningDialog.setTitle("Warning!");

            TextView dialogTxtWarning = new TextView(ManagePersonalDetails.this);
            ViewGroup.LayoutParams dialogTxtWarning_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtWarning.setLayoutParams(dialogTxtWarning_LayoutParams);
            dialogTxtWarning.setText("Please fill in all information");

            LinearLayout layoutWarning = new LinearLayout(ManagePersonalDetails.this);
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
            AlertDialog.Builder addDialog = new AlertDialog.Builder(ManagePersonalDetails.this);
            addDialog.setTitle("Save Changes");

            TextView dialogTxtConfirm = new TextView(ManagePersonalDetails.this);
            ViewGroup.LayoutParams dialogTxtConfirm_LayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogTxtConfirm.setLayoutParams(dialogTxtConfirm_LayoutParams);
            dialogTxtConfirm.setText("Do you sure you want to save these changes?");

            LinearLayout layout = new LinearLayout(ManagePersonalDetails.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(dialogTxtConfirm);
            addDialog.setView(layout);

            addDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                // do something when the button is clicked
                public void onClick(DialogInterface arg0, int arg1) {

                    try{
                        if (numberofRows > 0){
                            dbHelper.updatePersonal(1, patientName, patientGender, patientSeizure, patientContact);
                        }else{
                            dbHelper.insertPersonal(patientName, patientGender, patientSeizure, patientContact);
                        }

                        //Show Text on Screen
                        Context context = getApplicationContext();
                        CharSequence text = "Personal details updated";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);

                        toast.show();

                        //Close Database
                        dbHelper.close();

                        //Return to Parent Page
                        Intent intent = new Intent(ManagePersonalDetails.this, ManagePage.class);
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

    public void ManagePage(View view) {
        Intent intent = new Intent(ManagePersonalDetails.this, ManagePage.class);
        startActivity(intent);

        finish();
    }
}
