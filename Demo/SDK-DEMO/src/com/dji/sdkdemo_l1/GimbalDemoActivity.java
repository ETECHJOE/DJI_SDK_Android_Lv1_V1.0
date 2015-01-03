
package com.dji.sdkdemo_l1;

import java.util.Timer;
import java.util.TimerTask;

import com.dji.sdkdemo_l1.R;






import dji.sdk.api.DJIDrone;
import dji.sdk.api.Camera.DJICameraSettingsTypeDef.CameraVisionType;
import dji.sdk.api.Gimbal.DJIGimbalAttitude;
import dji.sdk.api.Gimbal.DJIGimbalRotation;
import dji.sdk.interfaces.DJIGimbalErrorCallBack;
import dji.sdk.interfaces.DJIGimbalUpdateAttitudeCallBack;
import dji.sdk.interfaces.DJIReceivedVideoDataCallBack;
import dji.sdk.widget.DjiGLSurfaceView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GimbalDemoActivity extends Activity implements OnClickListener{
    private static final String TAG = "GimbalDemoActivity";
    
    private Button mPitchUpBtn;
    private Button mPitchDownBtn;
    private Button mPitchGoBtn;
    private EditText mPitchGoEditText;
    private Button mYawLeftBtn;
    private Button mYawRightBtn;
    private Button mGimbalAttitudeBtn;
    
    private DjiGLSurfaceView mDjiGLSurfaceView;
    private DJIReceivedVideoDataCallBack mReceivedVideoDataCallBack;    
    private DJIGimbalErrorCallBack mGimbalErrorCallBack;
    private DJIGimbalUpdateAttitudeCallBack mGimbalUpdateAttitudeCallBack;
    
    private static final int SHOWTOAST = 1;
    
    private TextView mConnectStateTextView;
    private Timer mTimer;
    private String AttiudeString = "";
    
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
        
        GimbalDemoActivity.this.runOnUiThread(new Runnable(){

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
    
    private Handler handler = new Handler(new Handler.Callback() {
        
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOWTOAST:
                    setResultToToast((String)msg.obj); 
                    break;

                default:
                    break;
            }
            return false;
        }
    });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gimbal_demo);
        
        mDjiGLSurfaceView = (DjiGLSurfaceView)findViewById(R.id.DjiSurfaceView_gimbal);
        mPitchUpBtn = (Button)findViewById(R.id.PitchUpButton);
        mPitchDownBtn = (Button)findViewById(R.id.PitchDownButton);
        mPitchGoBtn = (Button)findViewById(R.id.PitchGoButton);
        mPitchGoEditText = (EditText)findViewById(R.id.PitchGoEditText);
        mYawLeftBtn = (Button)findViewById(R.id.YawLeftButton);
        mYawRightBtn = (Button)findViewById(R.id.YawRightButton);
        mConnectStateTextView = (TextView)findViewById(R.id.ConnectStateGimbalTextView);     
        mGimbalAttitudeBtn = (Button)findViewById(R.id.GimbalAttitudeButton);
        
        mPitchGoBtn.setOnClickListener(this);
        mPitchGoEditText.setHint(DJIDrone.getDjiGimbal().getGimbalPitchMinAngle() + " ~ " + DJIDrone.getDjiGimbal().getGimbalPitchMaxAngle());
        mYawLeftBtn.setOnClickListener(this);
        mYawRightBtn.setOnClickListener(this);
        
        Minus_Listener minuslisten = new Minus_Listener();
        Plus_Listener Pluslisten = new Plus_Listener();
        
        mPitchUpBtn.setOnTouchListener(Pluslisten);
        mPitchDownBtn.setOnTouchListener(minuslisten); 
        
        mGimbalAttitudeBtn.setEnabled(false);
        mGimbalAttitudeBtn.setClickable(false);
        
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
        
        mGimbalErrorCallBack = new DJIGimbalErrorCallBack(){

            @Override
            public void onError(int error) {
                // TODO Auto-generated method stub
                //Log.d(TAG, "Gimbal error = "+error);
            }
            
        };
        
        mGimbalUpdateAttitudeCallBack = new DJIGimbalUpdateAttitudeCallBack(){

            @Override
            public void onResult(DJIGimbalAttitude attitude) {
                // TODO Auto-generated method stub
                //Log.d(TAG, attitude.toString());
                
                
                StringBuffer sb = new StringBuffer();
                sb.append(getString(R.string.gimbal_attitude)).append("\n");
                sb.append("pitch=").append(attitude.pitch).append("\n");
                sb.append("roll=").append(attitude.roll).append("\n");
                sb.append("yaw=").append(attitude.yaw);
                
                AttiudeString = sb.toString();
                
                GimbalDemoActivity.this.runOnUiThread(new Runnable(){

                    @Override
                    public void run() 
                    {   
                        mGimbalAttitudeBtn.setText(AttiudeString);
                    }
                });
                
                
            }
            
        };
        
        DJIDrone.getDjiGimbal().setGimbalErrorCallBack(mGimbalErrorCallBack);
        DJIDrone.getDjiGimbal().setGimbalUpdateAttitudeCallBack(mGimbalUpdateAttitudeCallBack);
        
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        
        mTimer = new Timer();
        Task task = new Task();
        mTimer.schedule(task, 0, 500);
        
        DJIDrone.getDjiGimbal().startUpdateTimer(1000);
        
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        
        if(mTimer!=null) {            
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        
        DJIDrone.getDjiGimbal().stopUpdateTimer();
        
        super.onPause();
    }
    
    @Override
    protected void onStop() {
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
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            
            case R.id.PitchGoButton:                
                new Thread()
                {
                    public void run()
                    {
                        String vString = mPitchGoEditText.getText().toString();
                                                
                        int pitchGo = 0;
                        try {
                            pitchGo = Integer.parseInt(vString);
                        } catch (Exception e) {
                            // TODO: handle exception
                            pitchGo = 0;
                            
                            GimbalDemoActivity.this.runOnUiThread(new Runnable() {
                                
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    mPitchGoEditText.setText("0");
                                }
                            });  
                            
                        }
                        //Log.e("", "PitchGoButton click");
                        DJIGimbalRotation mPitch = new DJIGimbalRotation(true, false,true, pitchGo);
                        
                        DJIDrone.getDjiGimbal().updateGimbalAttitude(mPitch,null,null);

                    }
                }.start();
                
                break;  
                
            case R.id.YawLeftButton:                
                new Thread()
                {
                    //速度模式
                    DJIGimbalRotation mYaw = new DJIGimbalRotation(true, true,false, 60);
                    DJIGimbalRotation mYaw_stop = new DJIGimbalRotation(true, false,false, 0);
                    public void run()
                    {
                            
                        DJIDrone.getDjiGimbal().updateGimbalAttitude(null,null,mYaw);
                        
                        try 
                        {
                            Thread.sleep(150);
                        } catch (InterruptedException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        DJIDrone.getDjiGimbal().updateGimbalAttitude(null,null,mYaw_stop);
                    }
                }.start();
                
                break;  
                
            case R.id.YawRightButton:                
                new Thread()
                {
                    //速度模式
                    DJIGimbalRotation mYaw = new DJIGimbalRotation(true, false,false, 60);
                    DJIGimbalRotation mYaw_stop = new DJIGimbalRotation(true, false,false, 0);
                    public void run()
                    {
                            
                        DJIDrone.getDjiGimbal().updateGimbalAttitude(null,null,mYaw);
                    
                        try 
                        {
                            Thread.sleep(150);
                        } catch (InterruptedException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        DJIDrone.getDjiGimbal().updateGimbalAttitude(null,null,mYaw_stop);
                    }
                }.start();
                
                break;                  
                
            default:
                break;
        }
    }
    
    private void setResultToToast(String result){
        Toast.makeText(GimbalDemoActivity.this, result, Toast.LENGTH_SHORT).show();
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

    private boolean mIsPitchUp = false;
    private boolean mIsPitchDown = false;
    class Plus_Listener implements OnClickListener, OnTouchListener {
        @Override
        public void onClick(View view) {
            //Log.e("", "plus click");
        }
   
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) 
            {
                mIsPitchUp = true;

                new Thread()
                {
                    public void run()
                    {
                    	//速度模式
                    	DJIGimbalRotation mPitch = null;
                        if(DJIDrone.getDjiCamera().getVisionType() == CameraVisionType.Camera_Type_Plus){
                        	mPitch = new DJIGimbalRotation(true,true,false, 150);
                        }
                        else{
                        	mPitch = new DJIGimbalRotation(true,true,false, 20);
                        }
                        DJIGimbalRotation mPitch_stop = new DJIGimbalRotation(false, false,false, 0);
                        
                        while(mIsPitchUp)
                        {
                            //Log.e("", "A5S plus click");

                            
                            DJIDrone.getDjiGimbal().updateGimbalAttitude(mPitch,null,null);
                        
                            try 
                            {
                                Thread.sleep(50);
                            } catch (InterruptedException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        DJIDrone.getDjiGimbal().updateGimbalAttitude(mPitch_stop,null,null);
                    }
                }.start();
                
            } else if (event.getAction() == MotionEvent.ACTION_UP|| event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_CANCEL) 
            {
            
                mIsPitchUp = false;

            }

            return false;
        }
    };

    class Minus_Listener implements OnClickListener, OnTouchListener {
        @Override
        public void onClick(View view) {
            //Log.e("", "minus click");
        }
   
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) 
            {
                mIsPitchDown = true;

                new Thread()
                {
                    public void run()
                    {
                        //速度模式
                    	DJIGimbalRotation mPitch = null;
                        if(DJIDrone.getDjiCamera().getVisionType() == CameraVisionType.Camera_Type_Plus)
                        {
                        	mPitch = new DJIGimbalRotation(true, false,false, 150); 	
                        }else
                        {
                        	mPitch = new DJIGimbalRotation(true, false,false, 20); 
                        }
                        
                        DJIGimbalRotation mPitch_stop = new DJIGimbalRotation(false, false,false, 0);
                    	
                        while(mIsPitchDown)
                        {
                            //Log.e("", "A5S plus click");

                            
                            DJIDrone.getDjiGimbal().updateGimbalAttitude(mPitch,null,null);
                        
                            try 
                            {
                                Thread.sleep(50);
                            } catch (InterruptedException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        DJIDrone.getDjiGimbal().updateGimbalAttitude(mPitch_stop,null,null);
                    }
                }.start();
                
            } else if (event.getAction() == MotionEvent.ACTION_UP|| event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_CANCEL) 
            {
            
                mIsPitchDown = false;

            }

            return false;
        }
    };
}
