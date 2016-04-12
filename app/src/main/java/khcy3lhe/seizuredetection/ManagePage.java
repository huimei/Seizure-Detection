package khcy3lhe.seizuredetection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ManagePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void ManageMedicationAlert(View view)
    {
        Intent intent = new Intent(ManagePage.this, ManageMedicatonAlert.class);
        startActivity(intent);
    }

    public void ManageAppointment(View view)
    {
        Intent intent = new Intent(ManagePage.this, ManageAppointment.class);
        startActivity(intent);
    }

}
