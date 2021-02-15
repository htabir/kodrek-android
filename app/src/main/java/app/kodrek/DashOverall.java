package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

public class DashOverall extends AppCompatActivity {

    LoginResponse loginResponse;
    OjData codeforce = new OjData();
    OjData uva = new OjData();

    TextView textView_totalSub;
    TextView textView_totalAc;
    TextView textView_username;
    ProgressBar progressBar_totalBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_overall);

        textView_totalSub = findViewById(R.id.totalSub);
        textView_totalAc = findViewById(R.id.totalAc);
        textView_username = findViewById(R.id.username);
        progressBar_totalBar = findViewById(R.id.totalBar);

        SharedPreferences prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("loginResponse", "");
        loginResponse = gson.fromJson(json, LoginResponse.class);

        json = getIntent().getStringExtra("codeforce");
        codeforce = gson.fromJson(json, OjData.class);
        json = getIntent().getStringExtra("uva");
        uva = gson.fromJson(json, OjData.class);

        update();
    }

    private void update(){
        textView_username.setText(loginResponse.getUsername());
        textView_totalSub.setText((codeforce.getTotalSub()+uva.getTotalSub())+"");
        textView_totalAc.setText((codeforce.getTotalAc()+uva.getTotalAc())+"");

        Integer ac = codeforce.getTotalAc()+uva.getTotalAc();
        Integer ot = codeforce.getTotalOt()+uva.getTotalOt();
        progressBar_totalBar.setMax((codeforce.getTotalSub()+uva.getTotalSub()));
        progressBar_totalBar.setProgress(ac+ot);
        progressBar_totalBar.setSecondaryProgress(ac);
    }

    public void gotoToday(View v){
        Intent i = new Intent(this, DashToday.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
    }

    public void gotoLadder(View v){
        Intent i = new Intent(this, LadderCurrent.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
    }

    public void refresh(View v) {
        Intent i = new Intent(this, FetchData.class).putExtra("activity", "app.kodrek.DashOverall");
        startActivity(i);
    }

    public void seeSubs(View v){
        Intent i = new Intent(this, SubmissionsTable.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("timeline", "overall");
        startActivity(i);
    }
}