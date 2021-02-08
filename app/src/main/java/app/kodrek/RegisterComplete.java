package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterComplete extends AppCompatActivity {

    ImageView imageView_icon;
    TextView textView_message;
    TextView textView_messageSmall;
    Button button_goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete);

        imageView_icon = findViewById(R.id.icon);
        textView_message = findViewById(R.id.message);
        textView_messageSmall = findViewById(R.id.messageSmall);
        button_goButton = findViewById(R.id.goButton);

        if(getIntent().hasExtra("status")){
            imageView_icon.setImageResource(R.drawable.times);
            textView_message.setText("Registration Failed");
            textView_messageSmall.setText("Try Registering Again!");
            button_goButton.setText("Register");
        }
    }

    public void gotoLogin(View v){
        Intent i;
        if(getIntent().hasExtra("status")){
            i = new Intent(this, RegisterName.class);
        }else{
            i = new Intent(this, LoginEmail.class);
        }
        startActivity(i);
    }

    public void onBackPressed(){
        Intent i = new Intent(this, LoginSignupOption.class);
        startActivity(i);
    }
}