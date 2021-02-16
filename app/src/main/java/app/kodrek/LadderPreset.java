package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;


public class LadderPreset extends AppCompatActivity {

    PresetList presetList;
    LinearLayout presetHolder, presetBox;
    Preset userPreset;
    String jsonT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladder_preset);

        presetHolder = findViewById(R.id.presetHolder);

        SharedPreferences prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = getIntent().getStringExtra("presetList");
        presetList = gson.fromJson(json, PresetList.class);
        jsonT = prefs.getString("userPreset", "");
        userPreset = gson.fromJson(jsonT, Preset.class);

        makeList();

    }

    private void makeList(){

        if(jsonT != ""){
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.preset_active, presetHolder, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LadderPreset.this, LadderCurrent.class);
                    i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
                    i.putExtra("uva", getIntent().getStringExtra("uva"));
                    i.putExtra("presetList", getIntent().getStringExtra("presetList"));
                    startActivity(i);
                }
            });
            TextView textView = (TextView) view.findViewById(R.id.presetName);
            textView.setText(userPreset.getName());
            textView = (TextView) view.findViewById(R.id.presetTotal);
            textView.setText(userPreset.getTotal()+"");
            textView = (TextView) view.findViewById(R.id.presetTouch);
            textView.setText(presetList.getPresetTouch(userPreset.getPresetId())+" People");
            textView = (TextView) view.findViewById(R.id.presetLike);
            textView.setText(presetList.getPresetLike(userPreset.getPresetId())+" Likes");
            int days = (int) (((System.currentTimeMillis()/1000L) - userPreset.getCheckpoint() + userPreset.getDays()) / 86400);
            textView = (TextView) view.findViewById(R.id.presetDays);
            textView.setText(days+" Days");
            presetHolder.addView(view);
        }

        for(int id: presetList.getId()){
            if(jsonT!="" && id==userPreset.getPresetId()){
                continue;
            }
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.preset_regular, presetHolder, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LadderPreset.this, LadderCurrent.class);
                    i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
                    i.putExtra("uva", getIntent().getStringExtra("uva"));
                    i.putExtra("presetList", getIntent().getStringExtra("presetList"));
                    i.putExtra("presetId", id);
                    startActivity(i);
                }
            });
            TextView textView = (TextView) view.findViewById(R.id.presetName);
            textView.setText(presetList.getPresetName(id));
            textView = (TextView) view.findViewById(R.id.presetTotal);
            textView.setText(presetList.getPresetTotal(id)+"");
            textView = (TextView) view.findViewById(R.id.presetTouch);
            textView.setText(presetList.getPresetTouch(id)+" People");
            textView = (TextView) view.findViewById(R.id.presetLike);
            textView.setText(presetList.getPresetLike(id)+" Likes");
            textView = (TextView) view.findViewById(R.id.presetDays);
            textView.setText(((int)(presetList.getPresetDays(id) / 86400))+" Days");
            presetHolder.addView(view);
        }
    }

    public void presetDetails(View v, int id){

    }

    public void gotoDash(View v){
        Intent i = new Intent(this, DashToday.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
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
}