package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SubmissionsTable extends AppCompatActivity {

    TableLayout tableLayout_subsTable;
    TextView textView_header;
    TextView textView_timeline;
    Button button_load;
    ConstraintLayout constraintLayout_empty;

    OjData codeforce = new OjData();
    OjData uva = new OjData();
    Preset userPreset;
    Map<String, Integer> subs;
    String timeline;

    int f = 0, c = 0;
    long time;
    LinkedHashMap<String, Integer> sortedSubs = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions_table);

        tableLayout_subsTable = (TableLayout) findViewById(R.id.subsTable);
        textView_header = findViewById(R.id.headerText);
        textView_timeline = findViewById(R.id.timeline);
        button_load = findViewById(R.id.loadBtn);
        constraintLayout_empty = findViewById(R.id.emptyMessage);

        time = System.currentTimeMillis() / 1000L;

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("codeforce");
        codeforce = gson.fromJson(json, OjData.class);
        json = getIntent().getStringExtra("uva");
        uva = gson.fromJson(json, OjData.class);
        timeline = getIntent().getStringExtra("timeline");

        SharedPreferences prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        json = prefs.getString("userPreset", "");
        if(json!=""){
            userPreset = gson.fromJson(json, Preset.class);
        }

        if(timeline.equals("ladderToday") || timeline.equals("ladderOverall") || timeline.equals("ladderSolved")){
            mergeLadderData();
        }else{
            mergeDashData();
        }
        update();
    }

    private void update(){

        if(timeline.equals("today")){
            textView_timeline.setText("Today");
        }else if(timeline.equals("overall")){
            textView_timeline.setText("Overall");
        }else if(timeline.equals("ladderToday")){
            TextView textView_ladderName = findViewById(R.id.ladderName);
            textView_ladderName.setText(userPreset.getName());
            textView_ladderName.setVisibility(View.VISIBLE);
            textView_timeline.setText("Today");
        }else if(timeline.equals("ladderOverall")){
            TextView textView_ladderName = findViewById(R.id.ladderName);
            textView_ladderName.setText(userPreset.getName());
            textView_ladderName.setVisibility(View.VISIBLE);
            textView_timeline.setText("Overall");
        }
        initTable();
        if(c==0){
            constraintLayout_empty.setVisibility(View.VISIBLE);
        }
    }

    private void initTable(){
        if(f!=-1){
            int limit = ((subs.size() / 20) >= 1)? 20 : (subs.size()%20);
            for(int i=f; i<limit; i++, c++){
                if(c==subs.size()){
                    f=-1;
                    break;
                }
                Map.Entry<String, Integer> data= subs.entrySet().iterator().next();
                if(timeline.equals("today") || timeline.equals("ladderToday")){
                    if(data.getValue() < time - (time % 86400)){
                        f = -1;
                        break;
                    }
                }
                String id = new String();
                int sts = 0;
                if(codeforce.getSolvedSet().get(data.getKey()) != null){
                    id = "CF-"+data.getKey();
                    sts = 1;
                }else if(codeforce.getUnsolvedSet().get(data.getKey()) != null){
                    id = "CF-"+data.getKey();
                    sts = 0;
                }else if(uva.getSolvedSet().get(data.getKey()) != null){
                    id = "UVA-"+data.getKey();
                    sts = 1;
                }else if(uva.getUnsolvedSet().get(data.getKey()) != null){
                    id = "UVA-"+data.getKey();
                    sts = 0;
                }

                TableRow tableRow = new TableRow(this);
                if(i%2==0){
                    tableRow.setBackgroundColor(getResources().getColor(R.color.base));
                }else{
                    tableRow.setBackgroundColor(getResources().getColor(R.color.box));
                }
                tableRow.setWeightSum(12);
                tableRow.setPadding(20, 6, 20, 6);

                TextView problemId = new TextView(this);
                problemId.setText(id);
                problemId.setTextColor(getResources().getColor(R.color.white));
                problemId.setWidth(100);
                problemId.setLayoutParams(new TableRow.LayoutParams(5, ViewGroup.LayoutParams.MATCH_PARENT, 5f));
                problemId.setTextSize(16);
                tableRow.addView(problemId);
                TextView status = new TextView(this);
                status.setTextColor(getResources().getColor(R.color.success));
                if(sts==1){
                    status.setText("AC");
                }else{
                    status.setText("!AC");
                    status.setTextColor(getResources().getColor(R.color.danger));
                }
                status.setWidth(60);
                status.setGravity(Gravity.CENTER);
                status.setLayoutParams(new TableRow.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT, 2f));
                status.setTextSize(16);
                tableRow.addView(status);
                TextView actvty = new TextView(this);
                actvty.setText(((time - data.getValue()) / 86400)+"d ago");
                actvty.setTextColor(getResources().getColor(R.color.white));
                actvty.setWidth(100);
                actvty.setGravity(Gravity.RIGHT);
                actvty.setLayoutParams(new TableRow.LayoutParams(5, ViewGroup.LayoutParams.MATCH_PARENT, 5f));
                actvty.setTextSize(16);
                tableRow.addView(actvty);
                tableLayout_subsTable.addView(tableRow);
                subs.remove(data.getKey());

                if(timeline.equals("overall")){
                    if(subs.size()==0){
                        f = -1;
                        break;
                    }
                }

            }
        }
        if(subs.size()==0){
            f=-1;
        }
        if(f==-1){
            button_load.setVisibility(View.GONE);
        }

    }

    private void mergeDashData(){
        subs = new HashMap<>();
        subs.putAll(codeforce.getSolvedSet());
        subs.putAll(codeforce.getUnsolvedSet());
        subs.putAll(uva.getSolvedSet());
        subs.putAll(uva.getUnsolvedSet());

        subs = sortByValue((HashMap<String, Integer>) subs);
    }

    private void mergeLadderData(){
        subs = new HashMap<>();
        subs.putAll(userPreset.getSolved(codeforce, "cf"));
        subs.putAll(userPreset.getSolved(uva, "uva"));
        if(!timeline.equals("ladderSolved")){
            subs.putAll(userPreset.getUnsolved(codeforce, "cf"));
            subs.putAll(userPreset.getUnsolved(uva, "uva"));
        }

        subs = sortByValue((HashMap<String, Integer>) subs);
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public void loadMore(View v){
        initTable();
    }

    public void gotoDash(View v){
        Intent i = new Intent(this, DashToday.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        startActivity(i);
    }

    public void refresh(View v){
        if(timeline.equals("today") || timeline.equals("overall")){
            Intent i = new Intent(this, FetchData.class).putExtra("activity", "app.kodrek.DashToday");
            startActivity(i);
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

}