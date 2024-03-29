package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

public class DashToday extends AppCompatActivity {

    TextView textView_username;
    LoginResponse loginResponse;
    OjData codeforce = new OjData();
    OjData uva = new OjData();
    Preset userPreset;
    TextView textView_totalAc;
    TextView textView_totalTr;
    TextView textView_dailyGoal;
    ProgressBar progressBar_goalBar;

    ConstraintLayout constraintLayout_ladderBox;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_today);
        textView_username = findViewById(R.id.username);
        textView_totalAc = findViewById(R.id.totalAc);
        textView_totalTr = findViewById(R.id.totalTr);
        textView_dailyGoal = findViewById(R.id.dailyGoal);
        progressBar_goalBar = findViewById(R.id.goalBar);
        constraintLayout_ladderBox = findViewById(R.id.ladderBox);

        prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("loginResponse", "");
        loginResponse = gson.fromJson(json, LoginResponse.class);

        json = getIntent().getStringExtra("codeforce");
        codeforce = gson.fromJson(json, OjData.class);
        json = getIntent().getStringExtra("uva");
        uva = gson.fromJson(json, OjData.class);

        update();
    }

    private void update() {
        textView_username.setText(loginResponse.getUsername());
        int ac = (codeforce.getDayWiseAc(0) + uva.getDayWiseAc(0));
        textView_totalAc.setText(ac+"");
        int total = codeforce.getDayWiseAc(0) + codeforce.getDayWiseUn(0) + uva.getDayWiseAc(0) + uva.getDayWiseUn(0);
        textView_totalTr.setText(total+"");
        progressBar_goalBar.setMax(loginResponse.getDailyGoal());
        progressBar_goalBar.setProgress(ac);
        textView_dailyGoal.setText("Today you solved "+ac+"/"+loginResponse.getDailyGoal()+" problems.");

        Gson gson = new Gson();
        String json = prefs.getString("userPreset", "");


        if(json == ""){
            constraintLayout_ladderBox.setVisibility(View.GONE);
            ConstraintLayout constraintLayout_messageBox = findViewById(R.id.messageBox);
            constraintLayout_messageBox.setVisibility(View.VISIBLE);
        }else{
            userPreset = gson.fromJson(json, Preset.class);
            TextView textView_presetGoal = findViewById(R.id.presetGoal);
            textView_presetGoal.setText(loginResponse.getPresetDailyGoal()+"");
            ProgressBar progressBar_ladderProgress = findViewById(R.id.ladderProgress);
            progressBar_ladderProgress.setMax(loginResponse.getPresetDailyGoal());
//            Toast.makeText(this, userPreset.cfTotal()+"", Toast.LENGTH_LONG).show();
            int solved = userPreset.dailySolved(codeforce, "cf") + userPreset.dailySolved(uva, "uva");
            progressBar_ladderProgress.setProgress(solved);
            TextView textView_ladderInfo = findViewById(R.id.ladderInfo);
            int r = (loginResponse.getPresetDailyGoal() - solved);
            String s;
            if(r>0){
                s = r + " more to complete\ntoday's goal";
            }else{
                s = "You have completed\ntoday's goal.";
            }
            textView_ladderInfo.setText("You have solved\n" + solved + " problems today.\n" + s);
        }
    }

    public void gotoOverall(View v) {
        Intent i = new Intent(this, DashOverall.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
    }

    public void gotoLadder(View v) {
        Intent i = new Intent(this, LadderCurrent.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
    }

    public void gotoPresets(View v){
        Intent i = new Intent(this, LadderPreset.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
    }

    public void refresh(View v) {
        Intent i = new Intent(this, FetchData.class).putExtra("activity", "app.kodrek.DashToday");
        startActivity(i);
    }

    public void gotoMenu(View v){
        Intent i = new Intent(this, Menu.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
    }

    public void comingSoon(View v){
        Intent i = new Intent(this, ComingSoon.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
    }

    public void seeTried(View v) {
        Intent i = new Intent(this, SubmissionsTable.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        i.putExtra("timeline", "today");
        startActivity(i);
    }

    public void seeLadderTries(View v){
        Intent i = new Intent(this, SubmissionsTable.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        i.putExtra("timeline", "ladderToday");
        startActivity(i);
    }

//    private boolean isNetAvl(){
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
//    }


    public void onBackPressed(){

    }
}