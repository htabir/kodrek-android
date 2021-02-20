package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Menu extends AppCompatActivity {

    LoginResponse loginResponse;
    SharedPreferences prefs;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("loginResponse", "");
        loginResponse = gson.fromJson(json, LoginResponse.class);
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

    public void power(View v){
        finishAffinity();
        System.exit(0);
    }

    public void changeDailyGoal(View v){
        Intent i = new Intent(this, GoalSetter.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        i.putExtra("title", "daily");
        startActivity(i);
    }

    public void changePresetGoal(View v){
        Intent i = new Intent(this, GoalSetter.class);
        i.putExtra("codeforce", getIntent().getStringExtra("codeforce"));
        i.putExtra("uva", getIntent().getStringExtra("uva"));
        i.putExtra("presetList", getIntent().getStringExtra("presetList"));
        i.putExtra("title", "preset");
        startActivity(i);
    }

    public void logOut(View v){
        Call<CheckingResponse> logout = ApiClient.getUserService().logout("Bearer "+loginResponse.getToken());
        logout.enqueue(new Callback<CheckingResponse>() {
            @Override
            public void onResponse(Call<CheckingResponse> call, Response<CheckingResponse> response) {
                if(response.isSuccessful()){
                    prefs.edit().remove("loginResponse").commit();
                    prefs.edit().remove("userPreset").commit();
                    Intent i = new Intent(Menu.this, LoginSignupOption.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CheckingResponse> call, Throwable t) {

            }
        });
    }
}