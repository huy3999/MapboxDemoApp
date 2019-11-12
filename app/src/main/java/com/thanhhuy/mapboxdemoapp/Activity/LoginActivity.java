package com.thanhhuy.mapboxdemoapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thanhhuy.mapboxdemoapp.R;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText txtName;
    TextView txtNhap;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtName = findViewById(R.id.txtName);
        txtNhap = findViewById(R.id.txtNhap);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtName.getText().toString();
                Log.d("login",""+name);
                //txtNhap.setText(name);
                if(name.equals("user1") ||name.equals("user2")||name.equals("User1")||name.equals("User2")) {
                    sendMessage(view);

                }else{
                    Toast.makeText(LoginActivity.this,"Không có user này, mời nhập lại",Toast.LENGTH_LONG).show();
                    //name = "";
                }
            }
        });
    }
    public void sendMessage(View view) {
        Intent intent1 = new Intent(this, MainActivity.class);

        intent1.putExtra("name", name);
        startActivity(intent1);
    }
}
