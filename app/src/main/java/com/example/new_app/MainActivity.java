package com.example.new_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Date;

import android.os.Bundle;

import com.example.new_app.BitmapConverter.BitmapHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE;


public class MainActivity extends AppCompatActivity {

    /*To check whether users allow phone to access the phone gallery*/
    private static final int PERMISSION_STATUS = 1;



    /*To check whether users allow phone to access the camera and write to the external storage*/
    private static final int PERMISSION_STATUS_CAMERA = 3;

    private static final String TAG ="MainActivity";


    Uri selectedImageUri;
    String currentPhotoPath;

    Button scanbutton;
    Button importbutton;
    Button detectionbutton;
    ImageView imageselection;
    Button resetbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        imageselection = (ImageView) findViewById(R.id.imagePreview);

        detectionbutton = (Button)findViewById(R.id.button_detection);

        importbutton = (Button)findViewById(R.id.button_import);

        resetbutton = (Button)findViewById(R.id.button_reset);

/**
 * The following method
 */
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageselection.setImageBitmap(null);
            }
        });


        importbutton.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                    !=PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this
                    ,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_STATUS_CAMERA);

            }
            else{
                CropImage.startPickImageActivity(MainActivity.this);
            }



        });

        detectionbutton.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if(BitmapHelper.getInstance().getBitmap() == null){
                                                       Toast.makeText(MainActivity.this,"Bitmap can not be null",
                                                               Toast.LENGTH_SHORT).show();
                                                   }
                                                   else{
                                                       Intent intent = new Intent(MainActivity.this, EdgeDetectionActivity.class);
                                                       startActivity(intent);
                                                   }

                                                   setContentView(R.layout.activity_edge_detection);
                                                   Log.i("TAG", "detection successful");
                                               }
                                           });
    }


    private void chooseImage() {
        CropImage.startPickImageActivity(MainActivity.this);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STATUS_CAMERA: {
                if (grantResults.length > 0 && (grantResults[0] + grantResults[1]  == PackageManager.PERMISSION_GRANTED)) {
                    chooseImage();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE &&
        resultCode == Activity.RESULT_OK){
            selectedImageUri = CropImage.getPickImageResultUri(this,data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this,selectedImageUri)){
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_STATUS);
            }
            else{
                startCrop(selectedImageUri);
            }
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
//                imageselection.setImageURI(result.getUri());
                try {
                    InputStream inputStream = getContentResolver().openInputStream(result.getUri());
                    Bitmap bitmap =BitmapFactory.decodeStream(inputStream);
                    imageselection.setImageBitmap(bitmap);
                    BitmapHelper.getInstance().setBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this,"line 244",Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void startCrop(Uri imageUri){
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }


}