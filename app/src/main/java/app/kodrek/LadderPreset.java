package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LadderPreset extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladder_preset);
    }

    public void gotoDash(View v){
        Intent i = new Intent(this, DashToday.class);
        startActivity(i);
    }

    public void gotoLadderCurrent(View v){
        Intent i = new Intent(this, LadderCurrent.class);
        startActivity(i);
    }
}