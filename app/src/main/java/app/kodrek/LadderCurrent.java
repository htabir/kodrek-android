package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LadderCurrent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladder_current);

    }

    public void gotoDash(View v){
        Intent i = new Intent(this, DashToday.class);
        startActivity(i);
    }

    public void gotoLadderPreset(View v){
        Intent i = new Intent(this, LadderPreset.class);
        startActivity(i);
    }

}