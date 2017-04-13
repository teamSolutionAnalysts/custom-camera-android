package com.customcamera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by kinjal.dhamat on 4/6/2017.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Context context;
    private android.hardware.Camera camera;
    private int cameraType;

    public CameraPreview(Context context, android.hardware.Camera camera,int cameraType) {
        super(context);
        this.context = context;
        this.camera = camera;
        this.cameraType=cameraType;
        surfaceHolder = getHolder();
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            camera.setPreviewDisplay(holder);
            setCameraDisplayOrientation((Activity) context,cameraType,camera);

            camera.startPreview();
        } catch (IOException e) {
            Log.d("tag", "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (holder == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            Log.d("tag", "Error setting camera stop: " + e.getMessage());
        }

        try {
            camera.setPreviewDisplay(holder);
//                camera.setDisplayOrientation(90);
            setCameraDisplayOrientation((Activity) context,cameraType,camera);
            camera.startPreview();
        } catch (IOException e) {
            Log.d("tag", "Error setting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.release();
    }
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}