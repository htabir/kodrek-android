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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterCf extends AppCompatActivity {

    EditText editText_codeforces;
    TextView textView_cfLabel;
    Button button_kontinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cf);

        editText_codeforces = findViewById(R.id.codeforces);
        textView_cfLabel = findViewById(R.id.cfLabel);
        button_kontinue = findViewById(R.id.kontinue);
    }

    public void goForUva(View v){
        if(TextUtils.isEmpty(editText_codeforces.getText().toString())){
            editText_codeforces.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8ae3e")));
            textView_cfLabel.setText("Please Enter Valid\nCodeForces ID");
        }else{
            checkCodeForces();
        }
    }

    private void checkCodeForces(){
        button_kontinue.setText("CHECKING");
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setCodeForces(editText_codeforces.getText().toString());

        Call<CheckingResponse> checkCfResponseCall = ApiClient.getUserService().checkCf(registrationRequest);
        checkCfResponseCall.enqueue(new Callback<CheckingResponse>() {
            @Override
            public void onResponse(Call<CheckingResponse> call, Response<CheckingResponse> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(RegisterCf.this, RegisterUva.class);
                    i.putExtra("name", getIntent().getStringExtra("name"));
                    i.putExtra("email", getIntent().getStringExtra("email"));
                    i.putExtra("username", getIntent().getStringExtra("username"));
                    i.putExtra("codeforces", editText_codeforces.getText().toString());
                    startActivity(i);
                }else{
                    button_kontinue.setText("CONTINUE");
                    editText_codeforces.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f5616f")));
                    textView_cfLabel.setText("Invalid CodeForces ID!\nEnter A Valid One");
                }
            }

            @Override
            public void onFailure(Call<CheckingResponse> call, Throwable t) {

            }
        });
    }

    public void backToUsername(View v){
        Intent i = new Intent(this, RegisterUsername.class);
        i.putExtra("name", getIntent().getStringExtra("name"));
        i.putExtra("email", getIntent().getStringExtra("email"));
        startActivity(i);
    }
}