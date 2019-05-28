package com.example.kannan.google_vision;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String cameraPermission[];
    String storagePermission[];


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_GALLERY_CODE = 1000;
    private static final int IMAGE_CAMERA_CODE = 1001;

   // TextView content;
    EditText tv_result;
    private ImageView imageHolder;
    Button gallery;
    Button capturedImageButton;
    private Uri image_uri;
    private String imageFileName;
    Button message;
    ListView lv;
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gallery = (Button) findViewById(R.id.button3);
        tv_result = (EditText) findViewById(R.id.recognizeResult);
        imageHolder = (ImageView) findViewById(R.id.captured_photo);
        Button capturedImageButton = (Button) findViewById(R.id.photo_button);
        message=(Button)findViewById(R.id.save) ;
        //content=(TextView)findViewById(R.id.textView);
       lv = (ListView) findViewById(R.id.list);

        contactList = new ArrayList<>();
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
       // new GetContacts().execute();


        message.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v)
            {
                try{

                    // CALL GetText method to make post method call
                    GetText();
                }
                catch(Exception ex)
                {
                  // content.setText(" url exeption! " );
                }
            }
       });



        capturedImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickCamera();
                }

            }
        });

        gallery.setOnClickListener(new View.OnClickListener()
        {
        @Override
        public void onClick (View v){

        if (!checkStoragePermission()) {
            requestStoragePermission();
        } else {
            pickGallery();
        }
    }
    });
    }




    public void GetText()
    {

        // Create data variable for sent values to server
        new GetContacts().execute();
    }


  private class GetContacts extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }




        @Override
        protected String doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
           // String url="http://localhost/androidapi/api.php";

            String url = "https://api.androidhive.info/contacts/";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(MainActivity.class.getSimpleName(), "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                  //  Toast.makeText(getApplicationContext(), "Json parsing error: try" , Toast.LENGTH_LONG).show();
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                   //  looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");
                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(MainActivity.class.getSimpleName(), "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(MainActivity.class.getSimpleName(), "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           // content.setText(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList, R.layout.list_item, new String[]{ "email","mobile"}, new int[]{R.id.email, R.id.mobile});
            lv.setAdapter(adapter);
        }


}






//    public  void  GetText()
//    {
//
//        // Create data variable for sent values to server
//        new HttpAsync().execute();
//    }
//
//
//
//
//    class HttpAsync extends AsyncTask<Void, Integer, String>
//    {
//
//        @Override
//        protected String doInBackground(Void... strings) {
//            String data = null;
//
//            try {
//                data = URLEncoder.encode("details", "UTF-8")
//                        + "=" + URLEncoder.encode(tv_result.getText().toString(), "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//            String text = "";
//            BufferedReader reader=null;
//
//            // Send data
//            try
//            {
//
//                // Defined URL  where to send data
//                URL url = new URL("http://localhost/androidapi/api.php");
//
//                // Send POST data request
//
//                URLConnection conn = url.openConnection();
//                conn.setDoOutput(true);
//                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//                wr.write( data );
//                wr.flush();
//
//                // Get the server response
//
//
//                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//
//
//                // Read Server Response
//                while((line = reader.readLine()) != null)
//                {
//                    // Append server response in string
//                    sb.append(line + "\n");
//                }
//
//
//                text = sb.toString();
//            }
//            catch(Exception ex)
//            {
//                System.out.print(ex);
//            }
//            finally
//            {
//                try
//                {
//
//                    reader.close();
//                }
//
//                catch(Exception ex) {}
//            }
//
//            // Show response on activity
//            return text;
//
//        }
//
//
//
//
//    protected void onPostExecute(String result) {
//        // this is executed on the main thread after the process is over
//        // update your UI here
//       content.setText(result);
//    }
//    }
//






    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }




    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_GALLERY_CODE);

    }

    private void pickCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = createUniqueFileName();

        //for handling uri exposing exception in api > 24
        image_uri = FileProvider.getUriForFile(MainActivity.this,
                BuildConfig.APPLICATION_ID + ".provider", file);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, image_uri);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, IMAGE_CAMERA_CODE);


    }


    private File createUniqueFileName() {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "IMG_"+ timeStamp + ".jpg";
        return new File(mediaStorageDir.getPath() + File.separator + imageFileName);
    }



    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);

    }






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAcceped = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && writeStorageAcceped) {
                        pickCamera();
                    }
                    else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_REQUEST_CODE:
                if(grantResults.length > 0) {
                    boolean writeStorageAcceped = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    if(writeStorageAcceped) {
                        pickGallery();
                    }
                    else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

      //  lv_floating_buttons.setVisibility(View.GONE);
      //  floatingActionButton.show();

        if(resultCode == RESULT_OK)
        {
            if(requestCode == IMAGE_GALLERY_CODE){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            }
            if(requestCode == IMAGE_CAMERA_CODE){
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                System.out.println(resultUri.toString());
                imageHolder.setImageURI(resultUri);

                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageHolder.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItems = items.valueAt(i);
                        sb.append(myItems.getValue());
                        sb.append("\n");
                    }

                    tv_result.setText(sb.toString());


                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }


        }





}
}






