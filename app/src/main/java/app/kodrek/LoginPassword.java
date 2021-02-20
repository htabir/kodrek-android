package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginPassword extends AppCompatActivity {

    EditText editText_password;
    TextView textView_passwordLabel;
    Button button_login;
    String email;
    Boolean hidden;
    ImageView imageView_eye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);
        email = getIntent().getStringExtra("email");

        editText_password = findViewById(R.id.password);
        textView_passwordLabel = findViewById(R.id.passwordLabel);
        button_login = findViewById(R.id.loginButton);
        imageView_eye = findViewById(R.id.eye);
        hidden = true;
    }

    public void gotoToday(View v){
        if(TextUtils.isEmpty(editText_password.getText().toString()) || editText_password.getText().toString().length()<6){
            editText_password.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8ae3e")));
            textView_passwordLabel.setText("Please Enter\nValid Password");
        }else{
            button_login.setText("LOGGING IN");
            login();
        }
    }

    public void login(){
        String password = editText_password.getText().toString();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        Call<LoginResponse> loginResponseCall = ApiClient.getUserService().userLogin(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    LoginResponse loginResponse = response.body();
                    // storing data
                    SharedPreferences.Editor prefsEditor = getSharedPreferences("K0DR3K", MODE_PRIVATE).edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(loginResponse);
                    prefsEditor.putString("loginResponse", json);
                    prefsEditor.commit();

                    Intent i = new Intent(LoginPassword.this, FetchData.class);
                    startActivity(i);
                }else{
                    button_login.setText("LOGIN");
                    editText_password.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f5616f")));
                    textView_passwordLabel.setText("Invalid Credentials!\nEnter Your\nPassword Again");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void changeInputType(View v){
        if(hidden){
            editText_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageView_eye.setImageResource(R.drawable.eye);
            hidden=false;
        }else{
            editText_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageView_eye.setImageResource(R.drawable.eyev);
            hidden=true;
        }
    }

    public void backToEmail(View v){
        Intent i = new Intent(this, LoginEmail.class).putExtra("email", email);
        startActivity(i);
    }
}