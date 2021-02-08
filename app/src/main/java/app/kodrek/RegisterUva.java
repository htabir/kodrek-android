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

public class RegisterUva extends AppCompatActivity {

    EditText editText_uva;
    TextView textView_uvaLabel;
    Button button_kontinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_uva);

        editText_uva = findViewById(R.id.uva);
        textView_uvaLabel = findViewById(R.id.uvaLabel);
        button_kontinue = findViewById(R.id.kontinue);
    }

    public void goForPassword(View v){
        if(TextUtils.isEmpty(editText_uva.getText().toString())){
            editText_uva.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8ae3e")));
            textView_uvaLabel.setText("Please Enter Valid\nUVA ID");
        }else{
            checkUva();
        }
    }

    private void checkUva(){
        button_kontinue.setText("CHECKING");
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUva(editText_uva.getText().toString());

        Call<CheckingResponse> checkUvaResponseCall = ApiClient.getUserService().checkUva(registrationRequest);
        checkUvaResponseCall.enqueue(new Callback<CheckingResponse>() {
            @Override
            public void onResponse(Call<CheckingResponse> call, Response<CheckingResponse> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(RegisterUva.this, RegisterPassword.class);
                    i.putExtra("name", getIntent().getStringExtra("name"));
                    i.putExtra("email", getIntent().getStringExtra("email"));
                    i.putExtra("username", getIntent().getStringExtra("username"));
                    i.putExtra("codeforces", getIntent().getStringExtra("codeforces"));
                    i.putExtra("uva", editText_uva.getText().toString());
                    startActivity(i);
                }else{
                    button_kontinue.setText("CONTINUE");
                    editText_uva.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f5616f")));
                    textView_uvaLabel.setText("Invalid UVA ID!\nEnter A Valid One");
                }
            }

            @Override
            public void onFailure(Call<CheckingResponse> call, Throwable t) {

            }
        });

    }

    public void backToCf(View v){
        Intent i = new Intent(this, RegisterCf.class);
        i.putExtra("name", getIntent().getStringExtra("name"));
        i.putExtra("email", getIntent().getStringExtra("email"));
        i.putExtra("username", getIntent().getStringExtra("email"));
        startActivity(i);
    }
}