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

    /*RequestCode for choosing image action*/
    private static final int CHOOSE_IMAGE = 2;

    /*To check whether users allow phone to access the camera and write to the external storage*/
    private static final int PERMISSION_STATUS_CAMERA = 3;

    /*RequestCode for taking pic and save*/
    private static final int TAKE_PIC_AND_SAVE = 4;
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

        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission
        // .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
        //    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_STATUS);
        // }
        /*Bind the variable with the actual button*/
        imageselection = (ImageView) findViewById(R.id.imagePreview);
        detectionbutton = (Button)findViewById(R.id.button_detection);
        importbutton = (Button)findViewById(R.id.button_import);
        resetbutton = (Button)findViewById(R.id.button_reset);

        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageselection.setImageBitmap(null);
            }
        });

//        cvbutton = (Button)findViewById(R.id.button_cvcamera);

        /*Listener of button_scan*/
//        scanbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
//                        !=PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this
//                        ,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//                {
//                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            PERMISSION_STATUS_CAMERA);
//
//                }
//                else{
//                    dispatchTakePictureIntent();
//                }
//
//
//                try{
//                 Intent intent = new Intent();
//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivity(intent);
//                 }
//                catch (Exception e) {
//                 e.printStackTrace();
//                }
//            }
//        });


        /*Listener of button_import*/
        importbutton.setOnClickListener(v -> {
//            if(checkSelfPermission(
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED)
//            {
//
//                requestPermissions(
//                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
//                        PERMISSION_STATUS);
//            }
//            else{
//                chooseImage();
//            }
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
//                                                   Drawable drawable = imageselection.getDrawable();
//                                                   Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
//                                                   ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                                   bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
//                                                   byte[] bitmapdata = stream.toByteArray();
//                                                   Intent intent = new Intent(MainActivity.this, EdgeDetectionActivity.class);
//                                                   intent.putExtra("bitmapbytes",bitmapdata);
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

//        cvbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setContentView(R.layout.activity_camera_detection);
//                Log.i("TAG","CV successful");
//            }
//        }
//        );
    }



//    private void takePicAndSave(){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//       startActivityForResult(intent,TAKE_PIC_AND_SAVE);
//    }

    private void chooseImage() {

//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(intent,CHOOSE_IMAGE ); needed
        CropImage.startPickImageActivity(MainActivity.this);


        //if(intent.resolveActivity(getPackageManager()) != null){
        //startActivityForResult(intent, CHOOSE_IMAGE);}

        //Toast.makeText(this, "Where is image", Toast.LENGTH_SHORT).show();
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
//            break;
//            case PERMISSION_STATUS_CAMERA: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
//                    Toast.makeText(this, "Camera Permission granted", Toast.LENGTH_SHORT).show();
//                    dispatchTakePictureIntent();
//                } else {
//                    Toast.makeText(this, "Camera part Permission denied", Toast.LENGTH_SHORT).show();
//                }
//            }
//            break;
//        }
//    }

    // @Override
    //public void onRequestPermissionsResult(int request, @NonNull String[] permission, @NonNull int[] results){
    //   switch (request){
    //       case PERMISSION_STATUS:
    //          if (results[0] == PackageManager.PERMISSION_GRANTED) {
    //              Toast.makeText(this, "Have the Permission", Toast.LENGTH_SHORT).show();
    //           }
    //           else{
    //               Toast.makeText(this, "NO Permission", Toast.LENGTH_SHORT).show();
    //               //finish();
    //           }
    //   }
    // }
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
//        switch (requestCode) {
//            case CHOOSE_IMAGE:**
//            case PICK_IMAGE_CHOOSER_REQUEST_CODE:
//                if (resultCode == RESULT_OK) {
//                    if (data != null) {
//                        CropImage.ActivityResult result = CropImage.getActivityResult(data);**
//                        selectedImageUri = data.getData(); needed**
//                          selectedImageUri = CropImage.getPickImageResultUri(this,data);
//                        Toast.makeText(this, "229", Toast.LENGTH_SHORT).show();
//                          startCrop(selectedImageUri);
//
//                              Toast.makeText(this, "230", Toast.LENGTH_SHORT).show();
//                              CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                              if(result == null){
//                                  Toast.makeText(this, "234, it is null", Toast.LENGTH_SHORT).show();
//                              }
//                              else{
//                                  imageselection.setImageURI(result.getUri());
//                                  Toast.makeText(this, "234", Toast.LENGTH_SHORT).show();}
//
//                    }
//
//                }
//                break;
//            case TAKE_PIC_AND_SAVE:
//                if(resultCode == RESULT_OK){
//                    File photo = new File (currentPhotoPath);
//                    imageselection.setImageURI(Uri.fromFile(photo));
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(Uri.fromFile(photo));
//                        Bitmap bitmap =BitmapFactory.decodeStream(inputStream);
//                        imageselection.setImageBitmap(bitmap);
//                        BitmapHelper.getInstance().setBitmap(bitmap);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d(TAG,"Absolute Url" + Uri.fromFile(photo));
//                }
//                break;
//    }

        //switch (requestCode){
        //case CHOOSE_IMAGE:
        //if(resultCode == RESULT_OK){
        // Uri selectedImage = data.getData();
        //String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null );
        // cursor.moveToFirst();
        //int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //String picturePath = cursor.getString(columnIndex);
        //cursor.close();
        //imageselection.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        // }
        //}
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        Toast.makeText(this, "line 234", Toast.LENGTH_SHORT).show();
        File storageDir = getFilesDir();

//        File storageDir = Environment.getDataDirectory() ;
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Toast.makeText(this, "line 246", Toast.LENGTH_SHORT).show();
        return image;
    }

    //static final int REQUEST_TAKE_PHOTO = 5;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "androidx.core.content.FileProvider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, TAKE_PIC_AND_SAVE);
        }

    }

    private void startCrop(Uri imageUri){
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }




}