package com.example.kannan.google_vision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {


    TextView dialog;
    EditText uname,pword;
    Button Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uname=(EditText)findViewById(R.id.Username);
        pword=(EditText) findViewById(R.id.Password);
        Login=(Button)findViewById(R.id.login);
        dialog=(TextView) findViewById(R.id.Dialog);
        final String u,p ,t1,t2;
        u="Admin";
        p="Password";
        t1=uname.getText().toString();
        t2=pword.getText().toString();




        dialog.setVisibility(View.GONE);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((uname.getText().toString().equals("Admin"))&&(pword.getText().toString().equals("Password")))
                {



                    Intent i = new Intent(Login.this, First_Page.class);
                    startActivity(i);
                    uname.getText().clear();
                    pword.getText().clear();

                }

                else
                {

                    dialog.setVisibility(View.VISIBLE);
                    uname.getText().clear();
                    pword.getText().clear();
                }


                 dialog.setVisibility(View.INVISIBLE);
                 dialog.setVisibility(View.GONE);

            }
        });


    }

    }

