package com.customcamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity implements View.OnClickListener{

    private Button btnImageIntent;
    private Button btnCustomCamera;
    private Button btnCapVideo;
    private Button btnCombine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btnImageIntent=(Button)findViewById(R.id.btnImageIntent);
        btnCustomCamera=(Button)findViewById(R.id.btnCustomCamera);
        btnCapVideo=(Button)findViewById(R.id.btnCapVideo);
        btnCombine=(Button)findViewById(R.id.btnCombine);
        btnImageIntent.setOnClickListener(this);
        btnCustomCamera.setOnClickListener(this);
        btnCapVideo.setOnClickListener(this);
        btnCombine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btnImageIntent){
            Intent intent=new Intent(StartActivity.this,ImageCaptureIntent.class);
            startActivity(intent);
        }else if(v.getId()==R.id.btnCustomCamera){
            Intent intent=new Intent(StartActivity.this,CustomCamera.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.btnCapVideo){
            Intent intent=new Intent(StartActivity.this,VideoCapturing.class);
            startActivity(intent);
        }else if(v.getId()==R.id.btnCombine){
            Intent intent=new Intent(StartActivity.this,FrontAndBackCamera.class);
            startActivity(intent);
        }
    }
}
