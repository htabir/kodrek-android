package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoalSetter extends AppCompatActivity {

    TextView textView_goalLabel;
    EditText editText_goal;

    LoginResponse loginResponse;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setter);

        textView_goalLabel = findViewById(R.id.goalLabel);
        editText_goal = findViewById(R.id.goal);

        prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("loginResponse", "");
        loginResponse = gson.fromJson(json, LoginResponse.class);

        update();
    }

    private void update(){
        String s = getIntent().getStringExtra("title");
        if(s.equals("daily")){
            textView_goalLabel.setText("Set Your Daily Goal");
            editText_goal.setHint(loginResponse.getDailyGoal()+"");
        }else{
            editText_goal.setHint(loginResponse.getPresetDailyGoal()+"");
        }
        editText_goal.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void gotoToday(View v) {
        Intent i = new Intent(this, DashToday.class);
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

    public void comingSoon(View v) {
        Intent i = new Intent(this, ComingSoon.class);
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

    public void refresh(View v) {
        Intent i = new Intent(this, FetchData.class).putExtra("activity", "app.kodrek.DashToday");
        startActivity(i);
    }

    public void setGoal(View v){
        int val = Integer.parseInt(editText_goal.getText().toString());
        String s = getIntent().getStringExtra("title");
        if(s.equals("daily")){
            Call<CheckingResponse> changeDailyGoal = ApiClient.getUserService().changeDailyGoal(val, "Bearer "+loginResponse.getToken());
            changeDailyGoal.enqueue(new Callback<CheckingResponse>() {
                @Override
                public void onResponse(Call<CheckingResponse> call, Response<CheckingResponse> response) {
                    if(response.isSuccessful()){
                        loginResponse.setDailyGoal(val);
                        SharedPreferences.Editor prefsEditor = getSharedPreferences("K0DR3K", MODE_PRIVATE).edit();
                        Gson gson = new Gson();
                        String jsonT = gson.toJson(loginResponse);
                        prefsEditor.putString("loginResponse", jsonT);
                        prefsEditor.commit();
                    }
                    Intent i = new Intent(GoalSetter.this, DashToday.class);
                    i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
                    i.putExtra("uva", getIntent().getStringExtra("uva"));
                    i.putExtra("presetList", getIntent().getStringExtra("presetList"));
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<CheckingResponse> call, Throwable t) {

                }
            });
        }else{
            Call<CheckingResponse> changePresetGoal = ApiClient.getUserService().changePresetGoal(val, "Bearer "+loginResponse.getToken());
            changePresetGoal.enqueue(new Callback<CheckingResponse>() {
                @Override
                public void onResponse(Call<CheckingResponse> call, Response<CheckingResponse> response) {
                    if(response.isSuccessful()){
                        loginResponse.setPresetDailyGoal(val);
                        SharedPreferences.Editor prefsEditor = getSharedPreferences("K0DR3K", MODE_PRIVATE).edit();
                        Gson gson = new Gson();
                        String jsonT = gson.toJson(loginResponse);
                        prefsEditor.putString("loginResponse", jsonT);
                        prefsEditor.commit();
                    }
                    Intent i = new Intent(GoalSetter.this, DashToday.class);
                    i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
                    i.putExtra("uva", getIntent().getStringExtra("uva"));
                    i.putExtra("presetList", getIntent().getStringExtra("presetList"));
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<CheckingResponse> call, Throwable t) {

                }
            });
        }
    }
}