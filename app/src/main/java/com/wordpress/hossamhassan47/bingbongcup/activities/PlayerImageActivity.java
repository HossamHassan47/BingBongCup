package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.Helper.RoundImage;
import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.fragments.AddPlayerFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PlayerImageActivity extends AppCompatActivity {

    final int PIC_CROP = 1;
    private static final int CAMERA_REQUEST = 1888;
    Uri photoURI;

    int playerId;
    String currentImageSrc;

    ImageView imgPlayerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_image);

        playerId = getIntent().getIntExtra("playerId", 0);
        currentImageSrc = getIntent().getStringExtra("imageSrc");

        imgPlayerImage = findViewById(R.id.image_view_player_image);
        if (currentImageSrc != null) {
            Bitmap bm = BitmapFactory.decodeFile(currentImageSrc);
            RoundImage roundedImage = new RoundImage(bm);
            imgPlayerImage.setImageDrawable(roundedImage);
        }

        ImageView btnSave = findViewById(R.id.image_view_save_player_image);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());

                Player player = db.playerDao().loadPlayerById(playerId);
                player.imageSrc = currentImageSrc;
                int id = db.playerDao().updatePlayer(player);
                if (id > 0) {
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        ImageView btnAddImage = findViewById(R.id.image_view_new_player_image);
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Take Picture
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                    }

                    if (photoFile != null) {
                        photoURI = Uri.fromFile(photoFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                // Perform Crop
                performCrop(photoURI);

            } else if (requestCode == PIC_CROP) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");

                    File output = new File(currentImageSrc);
                    if (output.exists()){
                        output.delete();
                    }

                    Uri uriSavedImage = Uri.fromFile(output);

                    OutputStream imageFileOS;
                    try {
                        imageFileOS = getContentResolver().openOutputStream(uriSavedImage);
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] image=stream.toByteArray();

                        //bitmap image here
                        imageFileOS.write(image);
                        imageFileOS.flush();
                        imageFileOS.close();



                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Bitmap bm = BitmapFactory.decodeFile(currentImageSrc);
                    RoundImage roundedImage = new RoundImage(bm);
                    imgPlayerImage.setImageDrawable(roundedImage);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "BBCup_Player_" + playerId;

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentImageSrc = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");

            // set crop properties here
            cropIntent.putExtra("crop", true);

            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);

            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);

            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            // respond to users whose devices do not support the crop action
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
