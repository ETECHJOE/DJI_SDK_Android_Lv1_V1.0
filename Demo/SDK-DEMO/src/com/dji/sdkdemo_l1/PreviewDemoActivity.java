package com.dji.sdkdemo_l1;

import java.util.Timer;
import java.util.TimerTask;

import com.dji.sdkdemo_l1.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.Camera.DJICameraSettingsTypeDef.CameraPreviewResolustionType;
import dji.sdk.interfaces.DJIReceivedVideoDataCallBack;
import dji.sdk.widget.DjiGLSurfaceView;

public class PreviewDemoActivity extends Activity implements OnCheckedChangeListener
{

    private static final String TAG = "PreviewDemoActivity";
    
    private DjiGLSurfaceView mDjiGLSurfaceView;
    private DJIReceivedVideoDataCallBack mReceivedVideoDataCallBack = null;
    
    private RadioGroup mResolutionTypeRadioGroup;
    private TextView mConnectStateTextView;
    private Timer mTimer;
    
    class Task extends TimerTask {
        //int times = 1;

        @Override
        public void run() 
        {
            //Log.d(TAG ,"==========>Task Run In!");
            checkConnectState(); 
        }

    };
    private void checkConnectState(){
        
        PreviewDemoActivity.this.runOnUiThread(new Runnable(){

            @Override
            public void run() 
            {   
                boolean bConnectState = DJIDrone.getDjiCamera().getCameraConnectIsOk();
                if(bConnectState){
                    mConnectStateTextView.setText(R.string.camera_connection_ok);
                }
                else{
                    mConnectStateTextView.setText(R.string.camera_connection_break);
                }
            }
        });
        
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_demo);
        
        mDjiGLSurfaceView = (DjiGLSurfaceView)findViewById(R.id.DjiSurfaceView);
        
        mDjiGLSurfaceView.start();
        
        mReceivedVideoDataCallBack = new DJIReceivedVideoDataCallBack(){

            @Override
            public void onResult(byte[] videoBuffer, int size)
            {
                // TODO Auto-generated method stub
                mDjiGLSurfaceView.setDataToDecoder(videoBuffer, size);
            }

            
        };
        
        DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(mReceivedVideoDataCallBack);
        
        mResolutionTypeRadioGroup = (RadioGroup)findViewById(R.id.ResolutionTypeGroup);
        mResolutionTypeRadioGroup.setOnCheckedChangeListener(this);
        
        mConnectStateTextView = (TextView)findViewById(R.id.ConnectStatePreviewTextView);
    }
    
    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        //mDjiGLSurfaceView.resume();
        
        mTimer = new Timer();
        Task task = new Task();
        mTimer.schedule(task, 0, 500);
        
        super.onResume();
    }
    
    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        //mDjiGLSurfaceView.pause();
        if(mTimer!=null) {            
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        
        super.onPause();
    }
    
    @Override
    protected void onStop()
    {
        // TODO Auto-generated method stub
        
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        mDjiGLSurfaceView.destroy();
        DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(null);
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        // TODO Auto-generated method stub
        switch (group.getCheckedRadioButtonId()) {
            case R.id.ResolutionTypeRadio1:
                mDjiGLSurfaceView.setStreamType(CameraPreviewResolustionType.Resolution_Type_320x240_15fps);         
                break;
                
            case R.id.ResolutionTypeRadio2:
                mDjiGLSurfaceView.setStreamType(CameraPreviewResolustionType.Resolution_Type_320x240_30fps);          
                break;
                
            case R.id.ResolutionTypeRadio3:
                mDjiGLSurfaceView.setStreamType(CameraPreviewResolustionType.Resolution_Type_640x480_15fps); 
                break;

            case R.id.ResolutionTypeRadio4:
                mDjiGLSurfaceView.setStreamType(CameraPreviewResolustionType.Resolution_Type_640x480_30fps); 
                break;

            default:
                break;
         }
    }
    
     
    /** 
     * @Description : RETURN BTN RESPONSE FUNCTION
     * @author      : andy.zhao
     * @date        : 2014年7月28日 下午3:39:35
     * @param view 
     * @return      : void
     */
    public void onReturn(View view){
        Log.d(TAG ,"onReturn");  
        this.finish();
    }


}
