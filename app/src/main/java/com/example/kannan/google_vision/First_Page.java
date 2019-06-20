package com.example.kannan.google_vision;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class First_Page extends AppCompatActivity {



    String cameraPermission[];
    String storagePermission[];


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_GALLERY_CODE = 1000;
    private static final int IMAGE_CAMERA_CODE = 1001;

    private Uri image_uri;
    private String imageFileName;
    private EditText editText;

    ImageButton galary,camera,search,police;
    final MainActivity obj=new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first__page);
            galary=findViewById(R.id.imageButton);
            camera=findViewById(R.id.imageButton2);
            police=findViewById(R.id.imageButton3);
            search=findViewById(R.id.imageButton4);
            editText=findViewById(R.id.editText);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};




        galary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                   }
                else {
                    pickGallery();
                }

            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickCamera();
                }
            }
        });

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://keralapolice.gov.in/"));
                startActivity(browserIntent);

            }
        });






        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editText.getText().toString();
                if(!number.isEmpty()) {
                    Intent intent = new Intent(First_Page.this, MainActivity.class);
                    intent.putExtra("number", number);
                    startActivity(intent); // go to vehicle search page
                }
                else {
                    Toast.makeText(First_Page.this,"Enter Vehicle number",Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    public boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    public boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }




    public void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_GALLERY_CODE);

    }

    public void pickCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = createUniqueFileName();

        //for handling uri exposing exception in api > 24
        image_uri = FileProvider.getUriForFile(First_Page.this,
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



    public void requestCameraPermission() {

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

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

                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("uri", resultUri.toString());
                startActivity(i);
//                obj.imageHolder.setImageURI(resultUri);
//
//                BitmapDrawable bitmapDrawable = (BitmapDrawable) obj.imageHolder.getDrawable();
//                Bitmap bitmap = bitmapDrawable.getBitmap();
//                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
//
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
//                    obj.tv_result.setText(sb.toString());
//
//
//
//                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }


        }





    }





}
