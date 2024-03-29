package khcy3lhe.seizuredetection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void InfoPage(View view) {
        Intent intent = new Intent(HomeActivity.this, InfoPage.class);
        startActivity(intent);
    }

    public void AddSeizure(View view) {
        Intent intent = new Intent(HomeActivity.this, AddSeizure.class);
        startActivity(intent);
    }

    public void ManagePage(View view) {
        Intent intent = new Intent(HomeActivity.this, ManagePage.class);
        startActivity(intent);
    }

    public void ManageHistory(View view) {
        Intent intent = new Intent(HomeActivity.this, ManageHistory.class);
        startActivity(intent);
    }

    public void ScanActivity (View view) {
        Intent intent = new Intent(HomeActivity.this, ScanActivity.class);
        startActivity(intent);
    }
}
