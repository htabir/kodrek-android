package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterEmail extends AppCompatActivity {

    EditText editText_email;
    TextView textView_emailLabel;
    Button button_kontinue;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);

        name = getIntent().getStringExtra("name");

        editText_email = findViewById(R.id.email);
        textView_emailLabel = findViewById(R.id.emailLabel);
        button_kontinue = findViewById(R.id.kontinue);
    }

    public void goForUsername(View v){
        if(TextUtils.isEmpty(editText_email.getText().toString()) || !validateEmail(editText_email.getText().toString())){
            editText_email.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8ae3e")));
            textView_emailLabel.setText("Please Enter\nValid Email");
        }else{
            checkEmail();
        }
    }

    private void checkEmail(){
        button_kontinue.setText("CHECKING");
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(editText_email.getText().toString());

        Call<CheckingResponse> checkEmailResponseCall = ApiClient.getUserService().checkEmail(registrationRequest);
        checkEmailResponseCall.enqueue(new Callback<CheckingResponse>() {
            @Override
            public void onResponse(Call<CheckingResponse> call, Response<CheckingResponse> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(RegisterEmail.this, RegisterUsername.class);
                    i.putExtra("name", name);
                    i.putExtra("email", editText_email.getText().toString());
                    startActivity(i);
                }else{
                    button_kontinue.setText("CONTINUE");
                    editText_email.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f5616f")));
                    textView_emailLabel.setText("Email Already Exist!!\nTry Logging In");
                }
            }

            @Override
            public void onFailure(Call<CheckingResponse> call, Throwable t) {
            }
        });
    }

    public void backToName(View v){
        Intent i = new Intent(this, RegisterName.class);
        startActivity(i);
    }

    private boolean validateEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}