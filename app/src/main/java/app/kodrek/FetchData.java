package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchData extends AppCompatActivity {

    LoginResponse loginResponse;
    OjData codeforce = new OjData();
    OjData uva = new OjData();
    TextView textView_loginMessage;

    String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);

        activity = getIntent().getStringExtra("activity");

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
        try {
            Class<?> c = Class.forName(activity);
            Intent intent = new Intent(this, c);
            Gson gson = new Gson();
            String json;
            json = gson.toJson(codeforce);
            intent.putExtra("codeforce", json);
            json = gson.toJson(uva);
            intent.putExtra("uva", json);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void getCf(){
        Call<OjData> getCfStats = ApiClient.getUserService().getCf(loginResponse.getUsername(), "Bearer "+loginResponse.getToken());
        getCfStats.enqueue(new Callback<OjData>() {
            @Override
            public void onResponse(Call<OjData> call, Response<OjData> response) {
                if(response.isSuccessful()){
                    codeforce = response.body();
                    getCfSolved();
                }
            }

            @Override
            public void onFailure(Call<OjData> call, Throwable t) {

            }
        });
    }

    public void getCfSolved(){
        Call<Map<String, Integer>> getCfSolvedSet = ApiClient.getUserService().getCfSolved(loginResponse.getUsername(), "Bearer "+loginResponse.getToken());
        getCfSolvedSet.enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                codeforce.setSolvedSet(response.body());
                getCfUnsolved();
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {

            }
        });
    }

    public void getCfUnsolved(){
        Call<Map<String, Integer>> getCfUnsolvedSet = ApiClient.getUserService().getCfUnsolved(loginResponse.getUsername(), "Bearer "+loginResponse.getToken());
        getCfUnsolvedSet.enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                codeforce.setUnsolvedSet(response.body());
                getUva();
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {

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
                    getUvaSolved();
                }
            }

            @Override
            public void onFailure(Call<OjData> call, Throwable t) {

            }
        });
    }

    public void getUvaSolved(){
        Call<Map<String, Integer>> getUvaSolvedSet = ApiClient.getUserService().getUvaSolved(loginResponse.getUsername(), "Bearer "+loginResponse.getToken());
        getUvaSolvedSet.enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                uva.setSolvedSet(response.body());
                getUvaUnsolved();
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {

            }
        });
    }

    public void getUvaUnsolved(){
        Call<Map<String, Integer>> getUvaUnsolvedSet = ApiClient.getUserService().getUvaUnsolved(loginResponse.getUsername(), "Bearer "+loginResponse.getToken());
        getUvaUnsolvedSet.enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                uva.setUnsolvedSet(response.body());
                getBack();
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {

            }
        });
    }
}