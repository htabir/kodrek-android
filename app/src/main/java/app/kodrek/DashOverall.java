package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    Preset userPreset;

    TextView textView_totalSub;
    TextView textView_totalAc;
    TextView textView_username;
    ProgressBar progressBar_totalBar;

    ConstraintLayout constraintLayout_ladderBox;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_overall);

        textView_totalSub = findViewById(R.id.totalSub);
        textView_totalAc = findViewById(R.id.totalAc);
        textView_username = findViewById(R.id.username);
        progressBar_totalBar = findViewById(R.id.totalBar);
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

    private void update(){
        textView_username.setText(loginResponse.getUsername());
        textView_totalSub.setText((codeforce.getTotalSub()+uva.getTotalSub())+"");
        textView_totalAc.setText((codeforce.getTotalAc()+uva.getTotalAc())+"");

        Integer ac = codeforce.getTotalAc()+uva.getTotalAc();
        Integer ot = codeforce.getTotalOt()+uva.getTotalOt();
        progressBar_totalBar.setMax((codeforce.getTotalSub()+uva.getTotalSub()));
        progressBar_totalBar.setProgress(ac+ot);
        progressBar_totalBar.setSecondaryProgress(ac);

        Gson gson = new Gson();
        String json = prefs.getString("userPreset", "");

        if(json==""){
            constraintLayout_ladderBox.setVisibility(View.GONE);
            ConstraintLayout constraintLayout_messageBox = findViewById(R.id.messageBox);
            constraintLayout_messageBox.setVisibility(View.VISIBLE);
        }else{
            userPreset = gson.fromJson(json, Preset.class);
            TextView textView_ladderTotal = findViewById(R.id.ladderTotal);
            textView_ladderTotal.setText(userPreset.getTotal()+"");
            ProgressBar progressBar_ladderProgress = findViewById(R.id.ladderProgress);
            progressBar_ladderProgress.setMax(userPreset.getTotal());
            int solved = userPreset.countSolved(codeforce, "cf") + userPreset.countSolved(uva, "uva");
            int unsolved = userPreset.countUnsolved(codeforce, "cf") + userPreset.countUnsolved(uva, "uva");
            progressBar_ladderProgress.setProgress(solved);
            TextView textView_ladderInfo = findViewById(R.id.ladderInfo);
            textView_ladderInfo.setText("You have solved " + solved + ",\n tried " + (solved+unsolved) + " problem(s).\n " + (userPreset.getTotal() - solved) + " more to finish this ladder.");
        }
        setHistory();
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

    public void gotoPresets(View v){
        Intent i = new Intent(this, LadderPreset.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
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

    private void setHistory(){
        int[] textId = new int[] {R.id.textHistory0, R.id.textHistory1, R.id.textHistory2, R.id.textHistory3, R.id.textHistory4, R.id.textHistory5, R.id.textHistory6};
        int[] barId = new int[] {R.id.barHistory0, R.id.barHistory1, R.id.barHistory2, R.id.barHistory3, R.id.barHistory4, R.id.barHistory5, R.id.barHistory6};
        int mx=0;
        for(int i=0; i<7; i++){
            TextView textView = findViewById(textId[i]);
            int val = codeforce.getDayWiseAc(i) + uva.getDayWiseAc(i);
            textView.setText(val+"");
            ProgressBar progressBar = findViewById(barId[i]);
            progressBar.setProgress(val);
            if(val>mx){
                mx = val;
            }
        }
        for(int i=0; i<7; i++){
            ProgressBar progressBar = findViewById(barId[i]);
            progressBar.setMax(mx);
        }
    }
}