package com.customcamera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import static com.customcamera.CameraPreview.setCameraDisplayOrientation;

/**
 * Created by kinjal.dhamat on 4/7/2017.
 */

public class CameraPreviewFront extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder holder;
    private Context context;
    private Camera camera;
    private int cameraType;
    public CameraPreviewFront(Context context, Camera camera, int cameraType) {
        super(context);
        this.context=context;
        this.camera=camera;
        this.cameraType=cameraType;
        holder=getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            setCameraDisplayOrientation((Activity) context,cameraType,camera);
            camera.startPreview();

            Camera.Parameters params = camera.getParameters();

            // start face detection only *after* preview has started
            if (params.getMaxNumDetectedFaces() > 0){
                // camera supports face detection, so can start it:
                camera.startFaceDetection();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(camera==null){
            return;
        }

        camera.stopPreview();

        try {
            camera.setPreviewDisplay(holder);
            setCameraDisplayOrientation((Activity) context,cameraType,camera);
            camera.startPreview();

            Camera.Parameters params = camera.getParameters();

            // start face detection only *after* preview has started
            if (params.getMaxNumDetectedFaces() > 0){
                // camera supports face detection, so can start it:
                camera.startFaceDetection();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
