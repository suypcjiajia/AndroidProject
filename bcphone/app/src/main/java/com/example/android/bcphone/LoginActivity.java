package com.example.android.bcphone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText password;
    Button login;
    LoginActivity mysel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = findViewById(R.id.password);
        login = findViewById(R.id.btnLogin);

        login.setOnClickListener(onBtnLoginClick);

        mysel = this;
    }


    /**
     * 验证用户名和密码
     * @param v
     */
    View.OnClickListener onBtnLoginClick = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {

            System.out.println("onBtnLoginClick");

            if(password.getText().toString().equals( "1") ){
                Intent intent = new Intent(LoginActivity.this, DiscoverActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(mysel,"口令错误",Toast.LENGTH_LONG).show();
            }

        }
    };

    public static void main(String args[]) {
        System.out.println("Hello World!");
    }
}
