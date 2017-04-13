package com.customcamera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class FrontAndBackCamera extends Activity {

    private FrameLayout frameBack;
    private FrameLayout frameFrant;

    private Camera cameraBack;
    private CameraPreview previewBack;

    private Camera cameraFront;
    private CameraPreviewFront previewFront;

    private ImageView imgcombine;

    private Button btnClick;

    private Bitmap bitBack;
    private Bitmap bitFront;

    private int cameraType=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_and_back_camera);

        frameBack=(FrameLayout)findViewById(R.id.frameBack);

//        frameFrant=(FrameLayout)findViewById(R.id.frameFront);
//        cameraFront=getCameraInstance(1);
//        previewFront=new CameraPreviewFront(FrontAndBackCamera.this,cameraFront,1);
//        frameFrant.addView(previewFront);

        imgcombine=(ImageView)findViewById(R.id.imgcombine);

        btnClick=(Button)findViewById(R.id.button);

//        cameraBack=getCameraInstance(0);
//        previewBack=new CameraPreview(FrontAndBackCamera.this,cameraBack,0);

        startCamera(cameraType);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cameraType==1){
                    cameraBack.takePicture(null, null, pictureCallback);
                }
                else if(cameraType==0){
                    frameBack.setVisibility(View.VISIBLE);
                    cameraBack.takePicture(null, null, pictureCallback);
                }
                else if(cameraType==2){
                    cameraType=0;
                    frameBack.setVisibility(View.VISIBLE);
                    imgcombine.setVisibility(View.GONE);
                    startCamera(cameraType);

                }
            }
        });


    }

     Camera getCameraInstance(int cameraType){
        Camera camera=null;

        try{
            camera=Camera.open(cameraType);
        }catch (Exception e){
           e.printStackTrace();
        }

        return camera;
    }

    Camera.PictureCallback pictureCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            if(cameraType==0) {
                bitBack = (BitmapFactory.decodeByteArray(data, 0, (data) != null ? data.length : 0)).copy(Bitmap.Config.ARGB_8888,true);
                cameraType=1;
                startCamera(cameraType);
            }else {
                bitFront = (BitmapFactory.decodeByteArray(data, 0, (data) != null ? data.length : 0)).copy(Bitmap.Config.ARGB_8888,true);
                cameraType=2;
                combineImages(bitBack,bitFront);
            }
        }
    };

    private void startCamera(int type){
        if (cameraBack!=null) {
            cameraBack.stopPreview();
            cameraBack.release();
        }
        cameraBack=getCameraInstance(type);
        cameraBack.startPreview();
        frameBack.removeAllViews();
        previewBack=new CameraPreview(FrontAndBackCamera.this,cameraBack,0);
        frameBack.addView(previewBack);

    }

    private void combineImage(Bitmap bitBack,Bitmap bitFront){
        Canvas comboImage = new Canvas(bitBack);
// Then draw the second on top of that
        comboImage.drawBitmap(bitFront, 0f, 0f, null);
//        bitBack.compress(Bitmap.CompressFormat.PNG, 50, os)
        frameBack.setVisibility(View.GONE);
        imgcombine.setImageBitmap(bitFront);

        cameraType=0;

    }
    public void combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);


        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, c.getWidth(), 0f, null);




        File file= new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)+"/"+System.currentTimeMillis()+".jpg");
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            cs.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(file.exists()){

           final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            imgcombine.setVisibility(View.VISIBLE);
            frameBack.setVisibility(View.GONE);
            cameraType=2;
            imgcombine.setImageBitmap(bm);

        }

    }
}
