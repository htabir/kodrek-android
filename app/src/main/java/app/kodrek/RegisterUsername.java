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

public class RegisterUsername extends AppCompatActivity {

    EditText editText_username;
    TextView textView_usernameLabel;
    Button button_kontinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_username);

        editText_username = findViewById(R.id.username);
        textView_usernameLabel = findViewById(R.id.usernameLabel);
        button_kontinue = findViewById(R.id.kontinue);
    }

    public void goForCf(View v){
        if(TextUtils.isEmpty(editText_username.getText().toString())){
            editText_username.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8ae3e")));
            textView_usernameLabel.setText("Please Enter\nValid Username");
        }else{
            checkUsername();
        }
    }

    private void checkUsername(){
        button_kontinue.setText("CHECKING");
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername(editText_username.getText().toString());

        Call<CheckingResponse> checkUsernameResponseCall = ApiClient.getUserService().checkUsername(registrationRequest);
        checkUsernameResponseCall.enqueue(new Callback<CheckingResponse>() {
            @Override
            public void onResponse(Call<CheckingResponse> call, Response<CheckingResponse> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(RegisterUsername.this, RegisterCf.class);
                    i.putExtra("name", getIntent().getStringExtra("name"));
                    i.putExtra("email", getIntent().getStringExtra("email"));
                    i.putExtra("username", editText_username.getText().toString());
                    startActivity(i);
                }else{
                    button_kontinue.setText("CONTINUE");
                    editText_username.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f5616f")));
                    textView_usernameLabel.setText("Duplicate Found!\nTry A New One");
                }
            }

            @Override
            public void onFailure(Call<CheckingResponse> call, Throwable t) {

            }
        });
    }

    public void backToEmail(View v){
        Intent i = new Intent(this, RegisterEmail.class).putExtra("name", getIntent().getStringExtra("name"));
        startActivity(i);
    }
}