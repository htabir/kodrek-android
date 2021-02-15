package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        String json = prefs.getString("loginResponse", "");

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(json!=""){
                    Intent i = new Intent(MainActivity.this, FetchData.class).putExtra("activity", "app.kodrek.DashToday");
                    startActivity(i);
                }else{
                    prefs.edit().remove("loginResponse").commit();
                    Intent i = new Intent(MainActivity.this, LoginSignupOption.class);
                    startActivity(i);
                }
            }
        }, 2000);
    }
}