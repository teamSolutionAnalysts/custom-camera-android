package com.camera2demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCamera;
    private Button btnFrontBack;
    private Button btnRecording;
    private Button btnCamera2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnCamera = (Button) findViewById(R.id.button2);
        btnFrontBack = (Button) findViewById(R.id.button3);
        btnRecording = (Button) findViewById(R.id.button4);
        btnCamera2=(Button)findViewById(R.id.button5);

        btnCamera.setOnClickListener(this);
        btnFrontBack.setOnClickListener(this);
        btnRecording.setOnClickListener(this);
        btnCamera2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button2:
                intent = new Intent(StartActivity.this, CaptureImage.class);
                break;
            case R.id.button3:
                intent = new Intent(StartActivity.this, FrontBackFlashCaptureImage.class);
                break;
            case R.id.button4:
                intent = new Intent(StartActivity.this, RecordVideo.class);
                break;

            case R.id.button5:
                intent = new Intent(StartActivity.this, CaptureWithVideo.class);
                break;

            default:
                break;
        }
        startActivity(intent);
    }
}
