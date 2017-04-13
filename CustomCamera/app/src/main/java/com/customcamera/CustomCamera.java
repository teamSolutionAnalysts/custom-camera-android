package com.customcamera;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class CustomCamera extends Activity{

    private FrameLayout frameCamera;
    private Camera camera;
    private CameraPreview cameraPreview;
    private Button btnCapImg;
    private Button btnRCamera;
    private int cameraType=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        frameCamera = (FrameLayout) findViewById(R.id.frameCamera);
        btnCapImg=(Button)findViewById(R.id.btnCapImg);
        btnRCamera=(Button)findViewById(R.id.btnRcamera);

        if (checkCameraHardware()) {

            camera=getCameraInstance(cameraType);
            cameraPreview=new CameraPreview(this,camera,cameraType);
            frameCamera.addView(cameraPreview);
            camera.setFaceDetectionListener(new MyFaceDetectionListener());

            setFocus();
            startFaceDetection();
        } else {
            Toast.makeText(getApplicationContext(), "Device not support camera feature", Toast.LENGTH_SHORT).show();
        }

        btnCapImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(myShutterCallback,null,pictureCallback);
            }
        });

        btnRCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cameraType==0){
                    if(camera!=null){
                        camera.stopPreview();
                        camera.release();
                        cameraType=1;
                        btnRCamera.setText("Back Camera");
                        camera=getCameraInstance(cameraType);
                        camera.startPreview();
                        frameCamera.removeAllViews();
                        cameraPreview=new CameraPreview(CustomCamera.this,camera,cameraType);
                        frameCamera.addView(cameraPreview);
                        setFocus();
                        startFaceDetection();
                    }
                }else {
                    if(camera!=null){
                        camera.stopPreview();
                        camera.release();
                        cameraType=0;
                        btnRCamera.setText("Front Camera");
                        camera=getCameraInstance(cameraType);
                        camera.startPreview();
                        frameCamera.removeAllViews();
                        cameraPreview=new CameraPreview(CustomCamera.this,camera,cameraType);
                        frameCamera.addView(cameraPreview);
                        setFocus();
                        startFaceDetection();
                    }
                }
            }
        });


    }
    public void setFocus(){
        Camera.Parameters params = camera.getParameters();
        if(params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

       camera.setParameters(params);
    }

    public void startFaceDetection(){
        // Try starting Face Detection
        Camera.Parameters params = camera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0){
            // camera supports face detection, so can start it:
            camera.startFaceDetection();
        }
    }
    void setCameraZoom(Camera camera){
        Camera.Parameters parameters = camera.getParameters();
        int zoom=parameters.getMaxZoom();
        parameters.setZoom(5);
        camera.setParameters(parameters);
    }
    private boolean checkCameraHardware() {

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private Camera getCameraInstance(int cameraType) {
        Camera camera = null;
//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        for (int i=0;i<Camera.getNumberOfCameras();i++){
//            Camera.CameraInfo camInfo = new Camera.CameraInfo();
//            Camera.getCameraInfo(i, camInfo);
//
//            if (camInfo.facing==(Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                try {
                    camera = Camera.open(cameraType);
                } catch (Exception e) {
                    Log.d("tag","Error setting camera not open "+ e);
                }
//            }
//        }

        return camera;
    }
    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback(){

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }};
    class MyFaceDetectionListener implements Camera.FaceDetectionListener {

        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            if (faces.length > 0){
                Log.d("FaceDetection", "face detected: "+ faces.length +
                        " Face 1 Location X: " + faces[0].rect.centerX() +
                        "Y: " + faces[0].rect.centerY() );
                Toast.makeText(getApplicationContext(),"face detected: "+ faces.length +
                        " Face 1 Location X: " + faces[0].rect.centerX() +
                        "Y: " + faces[0].rect.centerY(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Camera.PictureCallback pictureCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File file= new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)+"/"+System.currentTimeMillis()+".jpg");
            if (file == null){
                Log.d("tag", "Error creating media file, check storage permissions: ");
                return;
            }

//            int width=getResources().getDisplayMetrics().widthPixels;
//            int hight=getResources().getDisplayMetrics().heightPixels;

            Bitmap bm= BitmapFactory.decodeByteArray(data,0, (data)!=null ? data.length :0);

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
//                Bitmap scaledBm=Bitmap.createScaledBitmap(bm,width,hight,true);
//                int w=scaledBm.getWidth();
//                int h=scaledBm.getHeight();

                Matrix matrix=new Matrix();
                matrix.setRotate(90);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);

            }

            try {
                FileOutputStream fileOutputStream=new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        // if you are using MediaRecorder, release it first
        camera.stopPreview();
    }


}
