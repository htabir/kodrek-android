package app.kodrek;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubmissionsTable extends AppCompatActivity {

    TableLayout tableLayout_subsTable;
    TextView textView_header;
    TextView textView_timeline;
    Button button_load;
    ConstraintLayout constraintLayout_empty;

    OjData codeforce = new OjData();
    OjData uva = new OjData();
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

        mergeData();
        update();
    }

    private void update(){
        if(timeline=="today"){
            textView_header.setText("Problem Tried");
            textView_timeline.setText("Today");
        }else if(timeline=="overall"){
            textView_timeline.setText("Overall");
        }
        initTable();
        if(c==0){
            constraintLayout_empty.setVisibility(View.VISIBLE);
        }
    }

    private void initTable(){
        if(f!=-1){
            for(int i=f; i<(f+20); i++, c++){
                Map.Entry<String, Integer> data= subs.entrySet().iterator().next();
                if(timeline.equals("today")){
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
        if(f==-1){
            button_load.setVisibility(View.GONE);
        }
    }

    private void mergeData(){
        subs = new HashMap<>();
        subs.putAll(codeforce.getSolvedSet());
        subs.putAll(codeforce.getUnsolvedSet());
        subs.putAll(uva.getSolvedSet());
        subs.putAll(uva.getUnsolvedSet());

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

}