package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPassword extends AppCompatActivity {

    EditText editText_password;
    EditText editText_confirmPassword;
    TextView textView_passwordLabel;
    TextView textView_confirmPasswordLabel;
    Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);

        editText_password = findViewById(R.id.password);
        editText_confirmPassword = findViewById(R.id.confirmPassword);
        textView_passwordLabel = findViewById(R.id.passwordLabel);
        textView_confirmPasswordLabel = findViewById(R.id.confirmPasswordLabel);
        button_register = findViewById(R.id.register);
    }

    public void registerUser(View v){

        if(TextUtils.isEmpty(editText_password.getText().toString()) || editText_password.getText().toString().length()<6){
            editText_password.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8ae3e")));
            textView_passwordLabel.setText("Invalid Password!\nTry Again");
        }else if(TextUtils.isEmpty(editText_password.getText().toString()) || !editText_password.getText().toString().equals(editText_confirmPassword.getText().toString())){
            editText_confirmPassword.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f5616f")));
            textView_confirmPasswordLabel.setText("Retry Password!");
        }else{
            register();
        }
    }

    private void register(){
        button_register.setText("PROCESSING");
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setName(getIntent().getStringExtra("name"));
        registrationRequest.setEmail(getIntent().getStringExtra("email"));
        registrationRequest.setUsername(getIntent().getStringExtra("username"));
        registrationRequest.setCodeForces(getIntent().getStringExtra("codeforces"));
        registrationRequest.setUva(getIntent().getStringExtra("uva"));
        registrationRequest.setPassword(editText_password.getText().toString());

        Call<CheckingResponse> userRegistration = ApiClient.getUserService().userRegister(registrationRequest);
        userRegistration.enqueue(new Callback<CheckingResponse>() {
            @Override
            public void onResponse(Call<CheckingResponse> call, Response<CheckingResponse> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(RegisterPassword.this, RegisterComplete.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(RegisterPassword.this, RegisterComplete.class).putExtra("status", "failed");
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<CheckingResponse> call, Throwable t) {

            }
        });
    }

    public void backToUva(View v){
        Intent i = new Intent(this, RegisterUva.class);
        i.putExtra("name", getIntent().getStringExtra("name"));
        i.putExtra("email", getIntent().getStringExtra("email"));
        i.putExtra("username", getIntent().getStringExtra("email"));
        i.putExtra("codeforces", getIntent().getStringExtra("codeforces"));
        i.putExtra("codeforces", getIntent().getStringExtra("uva"));
        startActivity(i);
    }
}