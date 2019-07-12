 package com.example.kannan.google_vision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

 public class Register extends AppCompatActivity {

    TextView user_name,pen_number,cug;
    Button register,chumma;
    Database_register db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user_name=(TextView)findViewById(R.id.user_name);
        pen_number=(TextView)findViewById(R.id.pen_number);
        cug=(TextView)findViewById(R.id.dob);
        register=(Button)findViewById(R.id.user_register);

        register.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                String name=user_name.getText().toString().trim();
                String pen=pen_number.getText().toString().trim();
                String cug_number=cug.getText().toString().trim();

                if(!name.isEmpty() & !pen.isEmpty() & !cug_number.isEmpty()) {
                    new Register_async().execute(name,pen,cug_number);

                }
                else
                    Toast.makeText(getApplicationContext(),"Enter all details",Toast.LENGTH_LONG).show();
            }
        });




    }

//    public void Process_Register(String name,String pen_no, String cug_no){
//
//        HttpHandler sh = new HttpHandler();
//
//        String url="http://192.168.137.87/directory_appv1/vd_controller/VD_Controller/vd_registeraction?name="+name+"&pen_no="+pen_no+"&cug_no="+cug_no;
//        String jsonStr = sh.makeServiceCall(url);
//
//        if (jsonStr != null) {
//            try {
//                JSONObject jsonObj = new JSONObject(jsonStr);
//
//               Integer result=jsonObj.getInt("status");
//               String message=jsonObj.getString("message");
//                if (result==1)
//                {
//
//                    db.insertData(user_name.getText().toString(),pen_number.getText().toString(),cug.getText().toString());
//                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
//
//                    Intent intent = new Intent(getApplicationContext(),Login.class);
//                    startActivity(intent);
//                    finish();
//                }else
//                    Toast.makeText(getApplicationContext(),"Registration not completed",Toast.LENGTH_LONG).show();
//
//
//            }catch (JSONException e) {
//                e.printStackTrace();
//            } ;
//
//
//
//        }
//
//
//    }
//

    class Register_async extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();

            //  String jsonStr = sh.makeServiceCall(strings[0]);

            String url = "http://192.168.43.158/directory_appv1/vd_controller/VD_Controller/vd_registeraction?name=" + strings[0] + "&pen_no=" + strings[1] + "&cug_no=" + strings[2];
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    Integer result = jsonObj.getInt("status");
                    final String message = jsonObj.getString("message");
                    final String reason=jsonObj.getString("reason");
                    if (result == 1) {

                        db.insertData(user_name.getText().toString(), pen_number.getText().toString(), cug.getText().toString());


                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "" +message , Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "" +reason, Toast.LENGTH_LONG).show();
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
            return null;
        }

    }}