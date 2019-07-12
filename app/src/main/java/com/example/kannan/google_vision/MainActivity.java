package com.example.kannan.google_vision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    EditText tv_result;
    ImageView imageHolder;
    private Uri image_uri;
    private String received_number;
    Button message,offence;
    ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String res = getIntent().getExtras().getString(("uri"));

        if(res != null){
            image_uri =  Uri.parse(res);
        }

        received_number =   getIntent().getExtras().getString(("number"));


        tv_result = (EditText) findViewById(R.id.recognizeResult);
        imageHolder = (ImageView) findViewById(R.id.captured_photo);
        message=(Button)findViewById(R.id.save) ;
       lv = (ListView) findViewById(R.id.list);
       offence=(Button)findViewById(R.id.offence);

       imageHolder.setImageURI(image_uri);
       lv.setVisibility(View.INVISIBLE);

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



        contactList = new ArrayList<>();
                if(received_number != null && !received_number.isEmpty()){
                    lv.setVisibility(View.VISIBLE);
                    tv_result.setVisibility(View.INVISIBLE);
                    message.setVisibility(View.INVISIBLE);
                    GetText(received_number);
                }



        message.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v)
            {
                try{
                    message.setVisibility(View.GONE);
                    tv_result.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    String number=tv_result.getText().toString().trim();
                    if(!number.isEmpty())
                        // CALL GetText method to make post method call
                    GetText(tv_result.getText().toString());
                    else
                        Toast.makeText(getApplicationContext(),"check your number",Toast.LENGTH_LONG).show();


                }
                catch(Exception ex)
                {
                  // content.setText(" url exeption! " );
                }
            }
       });
      offence.setOnClickListener(new Button.OnClickListener() {

                                       public void onClick(View v) {

                                           Intent intent=new Intent(getApplicationContext(),Offence.class);
                                           startActivity(intent);
                                       }
                                   });



//        capturedImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (!checkCameraPermission()) {
//                    requestCameraPermission();
//                } else {
//                    pickCamera();
//                }
//
//            }
//        });

//        gallery.setOnClickListener(new View.OnClickListener()
//        {
//        @Override
//        public void onClick (View v){
//
//        if (!checkStoragePermission()) {
//            requestStoragePermission();
//        } else {
//            pickGallery();
//        }
//    }
//    });
    }




    public void GetText(String received_number)
    {

        // Create data variable for sent values to server
        new GetContacts().execute(received_number);
    }


  public class GetContacts extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }




        @Override
        protected String doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
           String url="http://www.mocky.io/v2/5d0728d63000009c54051dce?NUMBER="+arg0;

           // String url = "https://api.androidhive.info/contacts/";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(MainActivity.class.getSimpleName(), "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                  //  Toast.makeText(getApplicationContext(), "Json parsing error: try" , Toast.LENGTH_LONG).show();
                    // Getting JSON Array node
                   // JSONArray contacts = jsonObj.getJSONArray("contacts");

                     String number=jsonObj.getString("NUMBER");
                     String name=jsonObj.getString("NAME");
                     String address=jsonObj.getString("ADDRESS");
                     String model=jsonObj.getString("MODEL");
                     String engine=jsonObj.getString("ENGINE");
                     String chasis=jsonObj.getString("CHASIS");
                     String mobile=jsonObj.getString("MOBILE");
                     String dated=jsonObj.getString("DATED");

                     HashMap<String,String> contacts =new HashMap<>();

                     contacts.put("NUMBER",number);
                     contacts.put("NAME",name);
                     contacts.put("ADDRESS",address);
                     contacts.put("MODEL",model);
                     contacts.put("ENGINE",engine);
                     contacts.put("CHASIS",chasis);
                     contacts.put("MOBILE",mobile);
                     contacts.put("DATED",dated);

                     contactList.add(contacts);

                   //  looping through All Contacts
//                    for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);
//                        String id = c.getString("id");
//                        String name = c.getString("name");
//                        String email = c.getString("email");
//                        String address = c.getString("address");
//                        String gender = c.getString("gender");

                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");
//                        // tmp hash map for single contact
//                        HashMap<String, String> contact = new HashMap<>();
//
//                        // adding each child node to HashMap key => value
//                        contact.put("id", id);
//                        contact.put("name", name);
//                        contact.put("email", email);
//                        contact.put("mobile", mobile);

                        // adding contact to contact list
                      //  contactList.add(contact);
                   // }
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

            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList, R.layout.list_item, new String[]{ "NUMBER","NAME","ADDRESS","MODEL","ENGINE","CHASIS","MOBILE","DATED"}, new int[]{R.id.number, R.id.name,R.id.address,R.id.model,R.id.engine,R.id.chasis,R.id.mobile,R.id.dated});
            lv.setAdapter(adapter);
        }


}












//    public boolean checkStoragePermission() {
//        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
//        return result;
//    }
//
//    public boolean checkCameraPermission() {
//
//        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
//        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
//
//        return result && result1;
//    }
//
//    public void requestStoragePermission() {
//        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
//    }
//
//
//
//
//    public void pickGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, IMAGE_GALLERY_CODE);
//
//    }
//
//    public void pickCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        File file = createUniqueFileName();
//
//        //for handling uri exposing exception in api > 24
//        image_uri = FileProvider.getUriForFile(MainActivity.this,
//                BuildConfig.APPLICATION_ID + ".provider", file);
//        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, image_uri);
//        intent.putExtra("return-data", true);
//
//        startActivityForResult(intent, IMAGE_CAMERA_CODE);
//
//
//    }
//
//
//    private File createUniqueFileName() {
//
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "CameraDemo");
//
//        if (!mediaStorageDir.exists()){
//            if (!mediaStorageDir.mkdirs()){
//                return null;
//            }
//        }
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        imageFileName = "IMG_"+ timeStamp + ".jpg";
//        return new File(mediaStorageDir.getPath() + File.separator + imageFileName);
//    }
//
//
//
//    public void requestCameraPermission() {
//
//        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
//
//    }
//
//
//
//
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case CAMERA_REQUEST_CODE:
//                if(grantResults.length > 0) {
//                    boolean cameraAccepted = grantResults[0] ==
//                            PackageManager.PERMISSION_GRANTED;
//                    boolean writeStorageAcceped = grantResults[0] ==
//                            PackageManager.PERMISSION_GRANTED;
//
//                    if(cameraAccepted && writeStorageAcceped) {
//                        pickCamera();
//                    }
//                    else {
//                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//
//            case STORAGE_REQUEST_CODE:
//                if(grantResults.length > 0) {
//                    boolean writeStorageAcceped = grantResults[0] ==
//                            PackageManager.PERMISSION_GRANTED;
//
//                    if(writeStorageAcceped) {
//                        pickGallery();
//                    }
//                    else {
//                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//        }
//    }
//
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//      //  lv_floating_buttons.setVisibility(View.GONE);
//      //  floatingActionButton.show();
//
//        if(resultCode == RESULT_OK)
//        {
//            if(requestCode == IMAGE_GALLERY_CODE){
//                CropImage.activity(data.getData())
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(this);
//
//            }
//            if(requestCode == IMAGE_CAMERA_CODE){
//                CropImage.activity(image_uri)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(this);
//            }
//        }
//
//        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                System.out.println(resultUri.toString());
//                imageHolder.setImageURI(resultUri);
//
//                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageHolder.getDrawable();
//                Bitmap bitmap = bitmapDrawable.getBitmap();
//                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
//
//                if (!recognizer.isOperational()) {
//                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//                } else {
//                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//                    SparseArray<TextBlock> items = recognizer.detect(frame);
//                    StringBuilder sb = new StringBuilder();
//
//                    for (int i = 0; i < items.size(); i++) {
//                        TextBlock myItems = items.valueAt(i);
//                        sb.append(myItems.getValue());
//                        sb.append("\n");
//                    }
//
//                    tv_result.setText(sb.toString());
//
//
//                }
//            }
//            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//
//        }
//
//
//
//
//
//}
}






