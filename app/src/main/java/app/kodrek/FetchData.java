package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchData extends AppCompatActivity {

    LoginResponse loginResponse;
    Preset userPreset;
    OjData codeforce = new OjData();
    OjData uva = new OjData();
    TextView textView_loginMessage;
    ProgressBar progressBar_fetchingBar;
    PresetList pList = new PresetList();

    String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar_fetchingBar = findViewById(R.id.fetchingProgress);
        progressBar_fetchingBar.setMax(400);

        textView_loginMessage = findViewById(R.id.loadingMessage);
        String[] msgs = getResources().getStringArray(R.array.loadingMessages);
        Random random = new Random();
        int rand = random.nextInt(msgs.length-1);
        textView_loginMessage.setText(msgs[rand]);

        SharedPreferences prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("loginResponse", "");
        loginResponse = gson.fromJson(json, LoginResponse.class);

        getCf();
    }

    private void getBack(){
        if(getIntent().hasExtra("activity")){
            activity = getIntent().getStringExtra("activity");
        }else{
            activity = "com.kodrek.DashToday";
        }
        try {
            Class<?> c = Class.forName(activity);
            Intent intent = new Intent(this, c);
            Gson gson = new Gson();
            String json;
            json = gson.toJson(codeforce);
            intent.putExtra("codeforce", json);
            json = gson.toJson(uva);
            intent.putExtra("uva", json);
            json = gson.toJson(pList);
            intent.putExtra("presetList", json);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            Intent intent = new Intent(this, DashToday.class);
            Gson gson = new Gson();
            String json;
            json = gson.toJson(codeforce);
            intent.putExtra("codeforce", json);
            json = gson.toJson(uva);
            intent.putExtra("uva", json);
            json = gson.toJson(pList);
            intent.putExtra("presetList", json);
            startActivity(intent);
        }

    }

    private void getCf(){
        Call<OjData> getCfStats = ApiClient.getUserService().getCf(loginResponse.getUsername(), "Bearer "+loginResponse.getToken());
        getCfStats.enqueue(new Callback<OjData>() {
            @Override
            public void onResponse(Call<OjData> call, Response<OjData> response) {
                if(response.isSuccessful()){
                    codeforce = response.body();
                    setProgressAnimate(1);
                    getUva();
                }else{
                    logout();
                }
            }

            @Override
            public void onFailure(Call<OjData> call, Throwable t) {

            }
        });
    }

    private void getUva(){
        Call<OjData> getUvaStats = ApiClient.getUserService().getUva(loginResponse.getUsername(), "Bearer "+loginResponse.getToken());
        getUvaStats.enqueue(new Callback<OjData>() {
            @Override
            public void onResponse(Call<OjData> call, Response<OjData> response) {
                if(response.isSuccessful()){
                    uva = response.body();
                    setProgressAnimate(2);
                    getPresetList();
                }else{
                    logout();
                }
            }

            @Override
            public void onFailure(Call<OjData> call, Throwable t) {

            }
        });
    }


    private void getPresetList(){
        Call<PresetList> getPresets = ApiClient.getUserService().getPresetList("Bearer "+loginResponse.getToken());
        getPresets.enqueue(new Callback<PresetList>() {
            @Override
            public void onResponse(Call<PresetList> call, Response<PresetList> response) {
                if(response.isSuccessful()){
                    pList = response.body();
                    setProgressAnimate(3);
                    getPresetStats();
                }else{
                    logout();
                }

            }

            @Override
            public void onFailure(Call<PresetList> call, Throwable t) {

            }
        });
    }

    private void getPresetStats(){
        Call<Preset> getUserPreset = ApiClient.getUserService().getPresetStats(loginResponse.getUsername(), "Bearer "+loginResponse.getToken());
        getUserPreset.enqueue(new Callback<Preset>() {
            @Override
            public void onResponse(Call<Preset> call, Response<Preset> response) {
                if(response.isSuccessful()){
                    userPreset = response.body();
                    SharedPreferences.Editor prefsEditor = getSharedPreferences("K0DR3K", MODE_PRIVATE).edit();
                    Gson gson = new Gson();
                    String jsonT = gson.toJson(userPreset);
                    prefsEditor.putString("userPreset", jsonT);
                    prefsEditor.commit();

                }else{

                }
                setProgressAnimate(4);
                getBack();
            }

            @Override
            public void onFailure(Call<Preset> call, Throwable t) {

            }
        });
    }

    private void setProgressAnimate(int progressTo){
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar_fetchingBar, "progress", progressBar_fetchingBar.getProgress(), progressTo*100);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private void logout(){
        SharedPreferences prefs = getSharedPreferences("K0DR3K", MODE_PRIVATE);
        prefs.edit().remove("loginResponse").commit();
        prefs.edit().remove("userPreset").commit();
        Intent i = new Intent(this, LoginSignupOption.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        
    }
}