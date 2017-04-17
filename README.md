# custom-camera-android
We can make custom camera using Camera and Camera2 API.
CustomCamera project make using camera API while Camera2demo make using Camera2 API.
Camera class, which has been deprecated. We recommend using the newer class Camera2, which works on Android 5.0 (API level 21) or greater.

### Manifest declarations

```sh
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-feature android:name="android.hardware.camera" />
```

## Camera API:
To access the primary camera,Use ``` Camera.open(cameraType) ``` method. Here cameraType=0 for back camera of 1 for front camera.

A [CameraPreview](https://github.com/teamSolutionAnalysts/custom-camera-android/blob/master/CustomCamera/app/src/main/java/com/customcamera/CameraPreview.java) class is a SurfaceView that can display the live image data coming from a camera, so users can frame and capture a picture or video.This class implements ```SurfaceHolder.Callback``` in order to capture the callback events for creating and destroying the view.

#### Capturing pictures
In order to capturing Image ``` Camera.takePicture() ``` method is use. In order to receive data in a JPEG format, you must implement an ```Camera.PictureCallback``` interface to receive the image data and write it to a file.

#### Capturing videos
For capturing video follow same step for showing preview. Use MediaRecorder class to record video. [VideoCapturing](https://github.com/teamSolutionAnalysts/custom-camera-android/blob/master/CustomCamera/app/src/main/java/com/customcamera/VideoCapturing.java) Activity contain video capturing code.

## Camera2 API:

Create a custom TextureView class [AutoFitTextureView](https://github.com/teamSolutionAnalysts/custom-camera-android/blob/master/Camera2Demo/app/src/main/java/com/camera2demo/AutoFitTextureView.java) and add it to the layout. The purpose of the custom TextureView is to be able to draw itself according to an aspect ratio.

Implement a ``` TextureView.SurfaceTextureListener ``` on your TextureView, and override its ``` onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) ``` method to calculate the matrix to apply to the TextureView so the camera output fits. Also override ``` onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) ``` for open camera.

Implement a CameraDevice.StateCallback to receive events about changes of the state of the camera device. Get CameraManger object and set necessary Properties of camera.

  ```sh 
  manager.openCamera(cameraId, stateCallback, null)
  ```
  
 In CameraDevice.StateCallback override method ``` onOpened(CameraDevice camera)``` and start camera preview.
 
 #### Capturing pictures
 Method ```takePicture()``` is use for capture image. Use ```setOnImageAvailableListener```  for getting image available call back.

#### Capturing videos
 Use MediaRecorder class to record video. Set up a ```CaptureRequest.Builder``` using ```createCaptureRequest(CameraDevice.TEMPLATE_RECORD)``` on your CameraDevice instance. Use start() and stop() methods on your MediaRecorder instance to actually start and stop the recording. [RecordVideo](https://github.com/teamSolutionAnalysts/custom-camera-android/blob/master/Camera2Demo/app/src/main/java/com/camera2demo/RecordVideo.java) Activity contain code for capturing video.
 
 [CaptureWithVideo](https://github.com/teamSolutionAnalysts/custom-camera-android/blob/master/Camera2Demo/app/src/main/java/com/camera2demo/CaptureWithVideo.java) Activity show  simultaneously capture Image and video. In which Image will capture while video is record.


 
