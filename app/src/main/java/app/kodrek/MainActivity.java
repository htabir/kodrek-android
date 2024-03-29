package app.kodrek;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        String json = prefs.getString("loginResponse", "");

//        if(!internetIsConnected()){
//            Toast.makeText(this, "NO INTERNET", Toast.LENGTH_LONG).show();
//        }

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

//    public boolean internetIsConnected() {
//        try {
//            String command = "ping -c 1 google.com";
//            return (Runtime.getRuntime().exec(command).waitFor() == 0);
//        } catch (Exception e) {
//            return false;
//        }
//    }
}