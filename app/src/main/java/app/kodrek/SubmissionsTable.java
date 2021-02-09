package app.kodrek;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SubmissionsTable extends AppCompatActivity {

    TableLayout tableLayout_subsTable;
//    Typeface baloo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions_table);

        tableLayout_subsTable = (TableLayout) findViewById(R.id.subsTable);
//        baloo = getResources().getFont(R.font.baloo_medium);

        initTable();
    }

    public void initTable(){
        for(int i=0; i<40; i++){
            TableRow tableRow = new TableRow(this);
            if(i%2==0){
                tableRow.setBackgroundColor(getResources().getColor(R.color.base));
            }else{
                tableRow.setBackgroundColor(getResources().getColor(R.color.box));
            }

            tableRow.setWeightSum(12);

            TextView problemId = new TextView(this);
            problemId.setText("CF121A");
//            problemId.setTypeface(baloo);
            problemId.setTextColor(getResources().getColor(R.color.danger));
            problemId.setWidth(100);
            problemId.setLayoutParams(new TableRow.LayoutParams(5, ViewGroup.LayoutParams.MATCH_PARENT, 5f));
            tableRow.addView(problemId);
            TextView status = new TextView(this);
            status.setText("AC");
//            status.setTypeface(baloo);
            status.setTextColor(getResources().getColor(R.color.white));
            status.setWidth(60);
            status.setGravity(Gravity.CENTER);
            status.setLayoutParams(new TableRow.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT, 2f));
            tableRow.addView(status);
            TextView actvty = new TextView(this);
            actvty.setText("7 Days Ago");
//            actvty.setTypeface(baloo);
            actvty.setTextColor(getResources().getColor(R.color.white));
            actvty.setWidth(100);
            actvty.setGravity(Gravity.RIGHT);
            actvty.setLayoutParams(new TableRow.LayoutParams(5, ViewGroup.LayoutParams.MATCH_PARENT, 5f));
            tableRow.addView(actvty);
            tableLayout_subsTable.addView(tableRow);
        }

    }
}