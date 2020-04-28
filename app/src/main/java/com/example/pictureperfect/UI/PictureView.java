package com.example.pictureperfect.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.pictureperfect.Model.Abstractions.IBitmap;
import com.example.pictureperfect.Model.AndroidBitmap;
import com.example.pictureperfect.PictureContracts;
import com.example.pictureperfect.Presentation.PicturePresenter;
import com.example.pictureperfect.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PictureView extends Fragment implements PictureContracts.PictureView {

    private Button btn_take, btn_calculate;
    private ImageView imgView, dominantColorView;
    private ImageView[] dominantColorViews;
    private TextView phototext, dominantColorText;
    private String _currentImagePath;

    private Bitmap imageMap;

    private static final int REQUEST_TAKE_PHOTO = 1;

    private PicturePresenter presenter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pictureview, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_take = view.findViewById(R.id.buttonTake);
        btn_calculate = view.findViewById(R.id.buttonCalculate);

        imgView = view.findViewById(R.id.imageView);

        dominantColorViews = new ImageView[5];
        dominantColorViews[0] = view.findViewById(R.id.dominantColorView00);
        dominantColorViews[1] = view.findViewById(R.id.dominantColorView01);
        dominantColorViews[2] = view.findViewById(R.id.dominantColorView02);
        dominantColorViews[3] = view.findViewById(R.id.dominantColorView03);
        dominantColorViews[4] = view.findViewById(R.id.dominantColorView04);
        dominantColorText = view.findViewById(R.id.dominantColortext);

        dominantColorView = dominantColorViews[0];

        btn_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePicture();
            }
        });

        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    calculateDominantColors();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        presenter = new PicturePresenter(this);
    }

    private void dispatchTakePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;

            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {

                Toast.makeText(this.getActivity(), "Something went wrong. Exception: " + e.toString(), Toast.LENGTH_SHORT).show();
                //Java exception handling? -- Look into ensuring file handles etc are released.
            }

            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this.getActivity(), "com.example.pictureperfect.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void calculateDominantColors() throws ExecutionException, InterruptedException {
        if(imageMap != null){
            Bitmap scaled = Bitmap.createScaledBitmap(imageMap, 250, 250, true);

            IBitmap map = new AndroidBitmap(scaled);

            int[] dominantColors = presenter.getDominantColors(map, 5);

            updateDominantColorsText(dominantColors);
            updateDominantColors(dominantColors);
        }
        else
            updateDominantColorsText(new int[] {-5, -5, -5, -5, -5});
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            imageMap = BitmapFactory.decodeFile(_currentImagePath);

            imgView.setImageBitmap(imageMap);
            //phototext.setText(_currentImagePath);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        _currentImagePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void updateDominantColorText(int color) {
        dominantColorText.setText("Color: " + color);
    }

    @Override
    public void updateDominantColor(int color) {
        dominantColorView.setBackgroundColor(color);
    }

    @Override
    public void updateDominantColorsText(int[] colors) {
        String text = "";
        for(int i = 0; i < colors.length; i++)
            text += "Color " + i + ": " + colors[i] + "%n";
        dominantColorText.setText(text);
    }

    @Override
    public void updateDominantColors(int[] colors) {
        for(int i = 0; i < colors.length; i++)
            dominantColorViews[i].setBackgroundColor(colors[i]);
    }
}
