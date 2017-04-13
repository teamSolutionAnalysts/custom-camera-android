package com.camera2demo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.util.Arrays;

public class Comine extends AppCompatActivity {

    private AutoFitTextureView textureView;
    private AutoFitTextureView textureView2;
    private String cameraId;
    private int mSensorOrientation;
    private Size imageDimension;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession cameraCaptureSessions;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private String cameraId2;
    private int mSensorOrientation2;
    private Size imageDimension2;
    private CameraDevice cameraDevice2;
    private CaptureRequest.Builder captureRequestBuilder2;
    private CameraCaptureSession cameraCaptureSessions2;
    private Handler mBackgroundHandler2;
    private HandlerThread mBackgroundThread2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comine);
        textureView=(AutoFitTextureView)findViewById(R.id.textureView);
        textureView2=(AutoFitTextureView)findViewById(R.id.textureView1);
//        textureView.setSurfaceTextureListener(textureListener);
        textureView2.setSurfaceTextureListener(textureListener2);
        
    }
    TextureView.SurfaceTextureListener textureListener=new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera(0);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//            if (null != textureView || null == imageDimension) {
//                textureView.setTransform(Utils.configureTransform(width, height,imageDimension,CaptureImage.this));
//            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
    TextureView.SurfaceTextureListener textureListener2=new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera2(1);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//            if (null != textureView || null == imageDimension) {
//                textureView.setTransform(Utils.configureTransform(width, height,imageDimension,CaptureImage.this));
//            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
    private void openCamera2(int cameraType) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e("tag", "is camera open");
        try {
            cameraId2 = manager.getCameraIdList()[cameraType];

            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId2);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mSensorOrientation2 = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            assert map != null;
            imageDimension2 = map.getOutputSizes(SurfaceTexture.class)[0];

            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED  ) {
                ActivityCompat.requestPermissions(Comine.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId2, stateCallback2, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e("tag", "openCamera X");
    }
    private void openCamera(int cameraType) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e("tag", "is camera open");
        try {
            cameraId = manager.getCameraIdList()[cameraType];

            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED  ) {
                ActivityCompat.requestPermissions(Comine.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e("tag", "openCamera X");
    }
    CameraDevice.StateCallback stateCallback2 = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {

            Log.e("tag", "onOpened");
            cameraDevice2 = camera;
            createCameraPreview2();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice2.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice2.close();
            cameraDevice2 = null;
        }
    };

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {

            Log.e("tag", "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    protected void createCameraPreview2() {
        try {
            SurfaceTexture texture = textureView2.getSurfaceTexture();
            assert texture != null;

            texture.setDefaultBufferSize(imageDimension2.getWidth(), imageDimension2.getHeight());

            Surface surface = new Surface(texture);
            captureRequestBuilder2 = cameraDevice2.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder2.addTarget(surface);
            cameraDevice2.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice2) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions2 = cameraCaptureSession;
                    updatePreview2();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(Comine.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;

            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());

            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(Comine.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    protected void updatePreview2() {
        if(null == cameraDevice2) {
            Log.e("tag", "updatePreview error, return");
        }
        captureRequestBuilder2.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions2.setRepeatingRequest(captureRequestBuilder2.build(), null, mBackgroundHandler2);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e("tag", "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void closeCamera() {
        if (null != cameraCaptureSessions) {
            cameraCaptureSessions.close();
            cameraCaptureSessions = null;
        }
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != cameraCaptureSessions2) {
            cameraCaptureSessions2.close();
            cameraCaptureSessions2 = null;
        }
        if (null != cameraDevice2) {
            cameraDevice2.close();
            cameraDevice2 = null;
        }

    }
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

        mBackgroundThread2 = new HandlerThread("Camera Background");
        mBackgroundThread2.start();
        mBackgroundHandler2 = new Handler(mBackgroundThread2.getLooper());
    }
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mBackgroundThread2.quitSafely();
        try {
            mBackgroundThread2.join();
            mBackgroundThread2 = null;
            mBackgroundHandler2 = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(Comine.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("tag", "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera(0);
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
        if (textureView2.isAvailable()) {
            openCamera2(1);
        } else {
            textureView2.setSurfaceTextureListener(textureListener2);
        }
    }
    @Override
    protected void onPause() {
        Log.e("tag", "onPause");
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }
}
