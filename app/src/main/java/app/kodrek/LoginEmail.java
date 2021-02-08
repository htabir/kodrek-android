package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.google.android.material.textfield.TextInputEditText;

public class LoginEmail extends AppCompatActivity {

    EditText editText_email;
    TextView textView_emailLabel;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        editText_email = findViewById(R.id.email);
        textView_emailLabel = findViewById(R.id.emailLabel);

        if(getIntent().hasExtra("email")){
            email = getIntent().getStringExtra("email");
            editText_email.setText(email);
        }

    }
    @SuppressLint("ResourceAsColor")
    public void goForPassword(View v){
        if(TextUtils.isEmpty(editText_email.getText().toString()) || !validateEmail(editText_email.getText().toString())){
            editText_email.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8ae3e")));
            textView_emailLabel.setText("Please Enter\nValid Email");
        }else{
            Intent i = new Intent(this, LoginPassword.class);
            email = editText_email.getText().toString();
            i.putExtra("email", email);
            startActivity(i);
        }
    }

    public void backToOptions(View v){
        Intent i = new Intent(this, LoginSignupOption.class);
        startActivity(i);
    }

    private boolean validateEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}