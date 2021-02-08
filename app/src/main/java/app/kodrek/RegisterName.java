package app.kodrek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterName extends AppCompatActivity {

    EditText editText_name;
    TextView textView_nameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);

        editText_name = findViewById(R.id.name);
        textView_nameLabel = findViewById(R.id.nameLabel);
    }

    public void goForEmail(View v){
        if(TextUtils.isEmpty(editText_name.getText().toString())){
            editText_name.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f8ae3e")));
            textView_nameLabel.setText("Please Enter\nValid Name");
        }else{
            Intent i = new Intent(this, RegisterEmail.class).putExtra("name", editText_name.getText().toString());
            startActivity(i);
        }
    }

    public void backToOptions(View v){
        Intent i = new Intent(this, LoginSignupOption.class);
        startActivity(i);
    }
}