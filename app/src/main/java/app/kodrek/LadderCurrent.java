package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LadderCurrent extends AppCompatActivity {

    PresetList presetList;
    LoginResponse loginResponse;
    OjData codeforce = new OjData();
    OjData uva = new OjData();
    Preset userPreset;

    TextView textView_presetName;
    TextView textView_presetTotal;
    TextView textView_presetDays;
    TextView textView_presetOwner;
    TextView textView_totalCount;
    TextView textView_solvedCount;
    TextView textView_message;

    ProgressBar progressBar_solvedProgress;
    ProgressBar progressBar_ojStats;

    ScrollView holder;

    ImageView imageView_actionBtn;

    SharedPreferences prefs;

    int tgl = 0;

    int presetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladder_current);

        textView_presetName = findViewById(R.id.presetName);
        textView_presetTotal = findViewById(R.id.presetTotal);
        textView_presetDays = findViewById(R.id.presetDays);
        textView_presetOwner = findViewById(R.id.presetOwner);
        textView_totalCount = findViewById(R.id.totalCount);
        textView_solvedCount = findViewById(R.id.solvedCount);
        textView_message = findViewById(R.id.message);

        holder = findViewById(R.id.holder);

        progressBar_solvedProgress = findViewById(R.id.solvedProgress);
        progressBar_ojStats = findViewById(R.id.ojStats);

        imageView_actionBtn = findViewById(R.id.actionButton);

        prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = getIntent().getStringExtra("presetList");
        presetList = gson.fromJson(json, PresetList.class);

        json = prefs.getString("loginResponse", "");
        loginResponse = gson.fromJson(json, LoginResponse.class);

        json = getIntent().getStringExtra("codeforce");
        codeforce = gson.fromJson(json, OjData.class);
        json = getIntent().getStringExtra("uva");
        uva = gson.fromJson(json, OjData.class);

        if(getIntent().hasExtra("presetId")){
            presetId = getIntent().getIntExtra("presetId", 0);
            updateById();
        }else{
            json = prefs.getString("userPreset", "");
            if(json == ""){

            }else{
                userPreset = gson.fromJson(json, Preset.class);
//                Toast.makeText(LadderCurrent.this, "Here", Toast.LENGTH_LONG).show();
                updateByPrefs();
            }
        }
    }

    private void updateById(){
        textView_message = findViewById(R.id.message);
        textView_message.setText("Fetching Data..");
        textView_message.setVisibility(View.VISIBLE);
        Button button_currentButton = findViewById(R.id.currentButton);
        button_currentButton.setVisibility(View.GONE);
        TextView textView_currentText = findViewById(R.id.currentText);
        textView_currentText.setVisibility(View.VISIBLE);
        textView_presetName.setText(presetList.getPresetName(presetId));
        textView_presetTotal.setText(presetList.getPresetTotal(presetId) + " Problems");
        textView_presetDays.setText(((int)(presetList.getPresetDays(presetId)) / 86400) + " Days");

        Call<Preset> getPresetDetails = ApiClient.getUserService().getPresetDetails(presetId, "Bearer "+loginResponse.getToken());
        getPresetDetails.enqueue(new Callback<Preset>() {
            @Override
            public void onResponse(Call<Preset> call, Response<Preset> response) {
                if(response.isSuccessful()){
                    textView_message.setVisibility(View.GONE);
                    Preset preset = response.body();
                    textView_presetOwner.setText(preset.getOwner());
                    LinearLayout linearLayout = findViewById(R.id.submissionBox);
                    linearLayout.setVisibility(View.GONE);
                    linearLayout = findViewById(R.id.tableBox);
                    linearLayout.setVisibility(View.GONE);ScrollView holder = findViewById(R.id.holder);
                    holder.setVisibility(View.VISIBLE);
                    ConstraintLayout constraintLayout_activateHolder = findViewById(R.id.activateHolder);
                    constraintLayout_activateHolder.setVisibility(View.VISIBLE);
                    textView_totalCount.setText(presetList.getPresetTotal(presetId)+"");
                    int solved = preset.countSolved(codeforce, "cf") + preset.countSolved(uva, "uva");
                    textView_solvedCount.setText(solved+"");
                    progressBar_solvedProgress.setMax(presetList.getPresetTotal(presetId));
                    progressBar_solvedProgress.setProgress(solved);
                    progressBar_ojStats.setMax(presetList.getPresetTotal(presetId));
                    progressBar_ojStats.setProgress(preset.cfTotal());

//                    Toast.makeText(LadderCurrent.this, preset.uvaTotal()+"", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Preset> call, Throwable t) {

            }
        });
    }

    private void updateByPrefs(){
        textView_message.setVisibility(View.GONE);
        textView_presetName.setText(userPreset.getName());
        textView_presetTotal.setText(userPreset.getTotal() + " Problems");
        int days = (int) (((System.currentTimeMillis()/1000L) - userPreset.getCheckpoint() + userPreset.getDays()) / 86400);
        textView_presetDays.setText(days + " Days");
        textView_presetOwner.setText(userPreset.getOwner());
        if(userPreset.getLike()==0){
            imageView_actionBtn.setImageResource(R.drawable.heartl);
        }else{
            imageView_actionBtn.setImageResource(R.drawable.hearts);
        }
        holder.setVisibility(View.VISIBLE);
        textView_totalCount.setText(userPreset.getTotal()+"");
        int solved = userPreset.countSolved(codeforce, "cf") + userPreset.countSolved(uva, "uva");
        textView_solvedCount.setText(solved+"");
        progressBar_solvedProgress.setMax(userPreset.getTotal());
        progressBar_solvedProgress.setProgress(solved);
        progressBar_ojStats.setMax(userPreset.getTotal());
        progressBar_ojStats.setProgress(userPreset.cfTotal());
        int unsolved = userPreset.countUnsolved(codeforce, "cf") + userPreset.countUnsolved(uva, "uva");
        TextView textView_countTries = findViewById(R.id.countTries);
        textView_countTries.setText((solved + unsolved) + "");
        ProgressBar progressBar_progressTries = findViewById(R.id.progressTries);
        progressBar_progressTries.setMax((solved + unsolved));
        progressBar_progressTries.setProgress(solved);

        makeUnsolvedTable();
        makeAllTable();
    }

    public void gotoDash(View v){
        Intent i = new Intent(this, DashToday.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
    }

    public void gotoLadderPreset(View v){
        Intent i = new Intent(this, LadderPreset.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
    }

    public void refresh(View v){
        if(getIntent().hasExtra("presetId")){
            finish();
        }else{
            Intent i = new Intent(this, FetchData.class).putExtra("activity", "app.kodrek.LadderCurrent");
            startActivity(i);
        }
    }

    public void gotoLadderCurrent(View v){
        Intent i = new Intent(this, LadderCurrent.class);
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

    public void activePreset(View v){
        Button button_activateButton = findViewById(R.id.activateButton);
        button_activateButton.setText("ACTIVATING..");
        Call<Preset> setPreset = ApiClient.getUserService().setPreset(presetId, "Bearer "+loginResponse.getToken());
        setPreset.enqueue(new Callback<Preset>() {
            @Override
            public void onResponse(Call<Preset> call, Response<Preset> response) {
                if(response.isSuccessful()){
                    userPreset = response.body();
                    SharedPreferences.Editor prefsEditor = getSharedPreferences("K0DR3K", MODE_PRIVATE).edit();
                    Gson gson = new Gson();
                    String jsonT = gson.toJson(userPreset);
                    prefsEditor.putString("userPreset", jsonT);
                    prefsEditor.commit();

                    Intent i = new Intent(LadderCurrent.this, LadderCurrent.class);
                    i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
                    i.putExtra("uva", getIntent().getStringExtra("uva"));
                    i.putExtra("presetList", getIntent().getStringExtra("presetList"));
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<Preset> call, Throwable t) {

            }
        });
    }

    public void seeLadderOverall(View v){
        Intent i = new Intent(this, SubmissionsTable.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        i.putExtra("timeline", "ladderOverall");
        startActivity(i);
    }

    public void seeLadderSolved(View v){
        Intent i = new Intent(this, SubmissionsTable.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        i.putExtra("timeline", "ladderSolved");
        startActivity(i);
    }

    private void makeUnsolvedTable(){
        List<String> probs = userPreset.getCf();
        probs.addAll(userPreset.getUva());
        Collections.shuffle(probs);
        TableLayout tableLayout_tableUnsolved = findViewById(R.id.tableUnsolved);
        int i = 0;
        for(String prob : probs){
            if(codeforce.getSolvedSet().get(prob) != null || uva.getSolvedSet().get(prob) != null){
                continue;
            }
            String probId = "";
            if(userPreset.getUva().contains(prob)){
                probId = "UVA-"+prob;
            }else{
                probId = "CF-"+prob;
            }
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(3);
            if(i%2==0){
                tableRow.setBackgroundColor(getResources().getColor(R.color.base));
            }else{
                tableRow.setBackgroundColor(getResources().getColor(R.color.box));
            }
            tableRow.setPadding(18, 6, 18, 6);
            TextView textView_probId  = new TextView(this);
            textView_probId.setText(probId);
            textView_probId.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            textView_probId.setTextColor(getResources().getColor(R.color.white));
            textView_probId.setLayoutParams(new TableRow.LayoutParams(5, ViewGroup.LayoutParams.MATCH_PARENT, 2f));
            tableRow.addView(textView_probId);
            TextView textView_Status = new TextView(this);
            if(codeforce.getUnsolvedSet().get(prob) != null || uva.getUnsolvedSet().get(prob) != null){
                textView_Status.setText("Tried");
                textView_Status.setTextColor(getResources().getColor(R.color.danger));
            }else{
                textView_Status.setText("Unsolved");
                textView_Status.setTextColor(getResources().getColor(R.color.warning));
            }
            textView_Status.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            textView_Status.setLayoutParams(new TableRow.LayoutParams(5, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            tableRow.addView(textView_Status);
            tableLayout_tableUnsolved.addView(tableRow);
            i++;
        }
//        Toast.makeText(this, probs.size()+"", Toast.LENGTH_LONG).show();
    }

    private void makeAllTable(){
        List<String> probs = userPreset.getCf();
        probs.addAll(userPreset.getUva());
        Collections.shuffle(probs);
        TableLayout tableLayout_tableAll = findViewById(R.id.tableAll);
        int i = 0;
        for(String prob : probs){
            String probId = "";
            if(userPreset.getUva().contains(prob)){
                probId = "UVA-"+prob;
            }else{
                probId = "CF-"+prob;
            }
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(3);
            if(i%2==0){
                tableRow.setBackgroundColor(getResources().getColor(R.color.base));
            }else{
                tableRow.setBackgroundColor(getResources().getColor(R.color.box));
            }
            tableRow.setPadding(18, 6, 18, 6);
            TextView textView_probId  = new TextView(this);
            textView_probId.setText(probId);
            textView_probId.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            textView_probId.setTextColor(getResources().getColor(R.color.white));
            textView_probId.setLayoutParams(new TableRow.LayoutParams(5, ViewGroup.LayoutParams.MATCH_PARENT, 2f));
            tableRow.addView(textView_probId);
            TextView textView_Status = new TextView(this);
            if(codeforce.getSolvedSet().get(prob) != null || uva.getSolvedSet().get(prob) != null){
                textView_Status.setText("Solved");
                textView_Status.setTextColor(getResources().getColor(R.color.success));
            }else if(codeforce.getUnsolvedSet().get(prob) != null || uva.getUnsolvedSet().get(prob) != null){
                textView_Status.setText("Tried");
                textView_Status.setTextColor(getResources().getColor(R.color.danger));
            }else{
                textView_Status.setText("Unsolved");
                textView_Status.setTextColor(getResources().getColor(R.color.warning));
            }
            textView_Status.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            textView_Status.setLayoutParams(new TableRow.LayoutParams(5, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            tableRow.addView(textView_Status);
            tableLayout_tableAll.addView(tableRow);
            i++;
        }
//        Toast.makeText(this, probs.size()+"", Toast.LENGTH_LONG).show();
    }

    public void toggleSolvedUnsolved(View v){
        if(tgl ==0){
            tgl = 1;
            TextView textView_slvUnslv = findViewById(R.id.slvUnslv);
            textView_slvUnslv.setText("All");
            TableLayout tableLayout = findViewById(R.id.tableUnsolved);
            tableLayout.setVisibility(View.GONE);
            tableLayout = findViewById(R.id.tableAll);
            tableLayout.setVisibility(View.VISIBLE);
        }else{
            tgl = 0;
            TextView textView_slvUnslv = findViewById(R.id.slvUnslv);
            textView_slvUnslv.setText("Unsolved");
            TableLayout tableLayout = findViewById(R.id.tableAll);
            tableLayout.setVisibility(View.GONE);
            tableLayout = findViewById(R.id.tableUnsolved);
            tableLayout.setVisibility(View.VISIBLE);
        }
    }

    public void likePreset(View v){
        String json = prefs.getString("userPreset", "");
        if(json == ""){
            return;
        }
        ImageView imageView_actionButton = findViewById(R.id.actionButton);
        Call<CheckingResponse> likePreset = ApiClient.getUserService().likePreset("Bearer "+loginResponse.getToken());
        likePreset.enqueue(new Callback<CheckingResponse>() {
            @Override
            public void onResponse(Call<CheckingResponse> call, Response<CheckingResponse> response) {
                if(response.isSuccessful()){
                    if(userPreset.getLike()==1){
                        userPreset.setLike(0);
                        imageView_actionButton.setImageResource(R.drawable.heartl);
                    }else{
                        userPreset.setLike(1);
                        imageView_actionButton.setImageResource(R.drawable.hearts);
                    }
                }
                SharedPreferences.Editor prefsEditor = getSharedPreferences("K0DR3K", MODE_PRIVATE).edit();
                Gson gson = new Gson();
                String jsonT = gson.toJson(userPreset);
                prefsEditor.putString("userPreset", jsonT);
                prefsEditor.commit();
            }

            @Override
            public void onFailure(Call<CheckingResponse> call, Throwable t) {

            }
        });
    }

}