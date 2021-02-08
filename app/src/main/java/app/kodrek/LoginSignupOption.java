package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginSignupOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup_option);
    }

    public void gotoLogin(View v){
        Intent i = new Intent(this, LoginEmail.class);
        startActivity(i);
    }

    public void test(View v){
        Intent i = new Intent(this, RegisterName.class);
        startActivity(i);
    }
}