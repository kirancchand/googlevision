package com.example.kannan.google_vision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText pen, cug_no;
    Button Login, register, chumma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pen = (EditText) findViewById(R.id.pen);
        cug_no = (EditText) findViewById(R.id.dob);
        Login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        chumma = (Button) findViewById(R.id.chumma);

        chumma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), First_Page.class);
                startActivity(intent);

            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pen_number = pen.getText().toString();
                String cug_number = cug_no.getText().toString();

                if (!pen_number.isEmpty() & !cug_number.isEmpty())
                    new Login_async().execute(pen_number, cug_number);
                else
                    Toast.makeText(getApplicationContext(), "Enter the details", Toast.LENGTH_LONG).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);

            }
        });


    }


    class Login_async extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            String url = "http://192.168.43.158/directory_appv1/vd_controller/VD_Controller/vd_loginaction?password=" + strings[0] + "&username=" + strings[1];
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Integer result = jsonObj.getInt("status");
                    final String message=jsonObj.getString("message");
                    String reason=jsonObj.getString("reason");
                    if (result == 1) {

                        Intent intent = new Intent(getApplicationContext(), First_Page.class);
                        startActivity(intent);
                        finish();
                    } else
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "" +message , Toast.LENGTH_LONG).show();

                            }
                        });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }
    }
}
