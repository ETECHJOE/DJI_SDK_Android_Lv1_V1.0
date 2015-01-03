package com.dji.sdkdemo_l1;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;





import com.dji.sdkdemo_l1.R;
import com.dji.sdkdemo.widget.PopupNumberPickerDouble;
import com.dji.sdkdemo.widget.PopupNumberPicker;
import com.dji.sdkdemo.widget.PopupNumberPickerDoubleRecording;

import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIError;
import dji.sdk.api.Camera.DJICameraSettingsTypeDef.*;
import dji.sdk.interfaces.DJIExecuteResultCallback;
import dji.sdk.interfaces.DJIReceivedVideoDataCallBack;
import dji.sdk.util.DjiLocationCoordinate2D;
import dji.sdk.widget.DjiGLSurfaceView;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CameraProtocolDemoActivity extends Activity implements OnClickListener
{
    private static final String TAG = "CameraProtocolDemoActivity";
    
    private Button mStartTakePhotoBtn;
    private Button mStopTakePhotoBtn;
    private Button mStartRecordingBtn;
    private Button mStopRecordingBtn;
    private Button m_camera_set_photosize_btn;
    private Button m_camera_get_photosize_btn;   
    private Button m_camera_set_iso_btn;
    private Button m_camera_get_iso_btn;
    private Button m_camera_set_whitebalance_btn;
    private Button m_camera_get_whitebalance_btn;
    private Button m_camera_set_exposuremetering_btn;
    private Button m_camera_get_exposuremetering_btn;
    private Button m_camera_set_exposurecompensation_btn;
    private Button m_camera_get_exposurecompensation_btn;
    private Button m_camera_set_antiflicker_btn;
    private Button m_camera_get_antiflicker_btn;
    private Button m_camera_set_sharpness_btn;
    private Button m_camera_get_sharpness_btn;
    private Button m_camera_set_contrast_btn;
    private Button m_camera_get_contrast_btn;
    private Button m_camera_set_mutiphotocount_btn;
    private Button m_camera_get_mutiphotocount_btn;
    private Button m_camera_set_cameraaction_btn;
    private Button m_camera_get_cameraaction_btn;
    private Button m_camera_set_photoformat_btn;
    private Button m_camera_get_photoformat_btn;
    private Button m_camera_set_recordingparam_btn;
    private Button m_camera_get_recordingparam_btn;
    private Button m_camera_set_continuousparam_btn;
    private Button m_camera_get_continuousparam_btn;
        
    private Button m_camera_set_mode_btn;
    private Button m_camera_get_version_btn;
    private Button m_camera_get_visiontype_btn;
    private Button m_camera_get_connectisok_btn;
    private Button m_camera_sync_time_btn;
    private Button m_camera_save_config_btn;
    private Button m_camera_restore_default_btn;
    private Button m_camera_format_sdcard_btn;
        
    private DjiGLSurfaceView mDjiGLSurfaceView;
    private DJIReceivedVideoDataCallBack mReceivedVideoDataCallBack = null;
    
    private final int SHOWTOAST = 1;
    
    private TextView mConnectStateTextView;
    private Timer mTimer;
    
    private PopupNumberPicker mPopupNumberPicker = null;    
    private PopupNumberPickerDouble mPopupNumberPickerDouble = null;    
    private PopupNumberPickerDoubleRecording mPopupNumberPickerDoubleRecording = null;
    
    private Context m_context;

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
        
        CameraProtocolDemoActivity.this.runOnUiThread(new Runnable(){

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_protocol_demo);
 
        mDjiGLSurfaceView = (DjiGLSurfaceView)findViewById(R.id.DjiSurfaceView_02);
        
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
        
        
        mStartTakePhotoBtn = (Button)findViewById(R.id.StartTakePhotoButton);
        mStopTakePhotoBtn = (Button)findViewById(R.id.StopTakePhotoButton);
        mStartRecordingBtn = (Button)findViewById(R.id.StartRecordingButton);
        mStopRecordingBtn = (Button)findViewById(R.id.StopRecordingButton);
        m_camera_set_photosize_btn = (Button)findViewById(R.id.camera_set_photosize_btn);
        m_camera_get_photosize_btn = (Button)findViewById(R.id.camera_get_photosize_btn);   
        m_camera_set_iso_btn = (Button)findViewById(R.id.camera_set_iso_btn);
        m_camera_get_iso_btn = (Button)findViewById(R.id.camera_get_iso_btn);
        m_camera_set_whitebalance_btn = (Button)findViewById(R.id.camera_set_whitebalance_btn);
        m_camera_get_whitebalance_btn = (Button)findViewById(R.id.camera_get_whitebalance_btn);
        m_camera_set_exposuremetering_btn = (Button)findViewById(R.id.camera_set_exposuremetering_btn);
        m_camera_get_exposuremetering_btn = (Button)findViewById(R.id.camera_get_exposuremetering_btn);
        m_camera_set_exposurecompensation_btn = (Button)findViewById(R.id.camera_set_exposurecompensation_btn);
        m_camera_get_exposurecompensation_btn = (Button)findViewById(R.id.camera_get_exposurecompensation_btn);
        m_camera_set_antiflicker_btn = (Button)findViewById(R.id.camera_set_antiflicker_btn);
        m_camera_get_antiflicker_btn = (Button)findViewById(R.id.camera_get_antiflicker_btn);
        m_camera_set_sharpness_btn = (Button)findViewById(R.id.camera_set_sharpness_btn);
        m_camera_get_sharpness_btn = (Button)findViewById(R.id.camera_get_sharpness_btn);
        m_camera_set_contrast_btn = (Button)findViewById(R.id.camera_set_contrast_btn);
        m_camera_get_contrast_btn = (Button)findViewById(R.id.camera_get_contrast_btn);
        m_camera_set_mutiphotocount_btn = (Button)findViewById(R.id.camera_set_mutiphotocount_btn);
        m_camera_get_mutiphotocount_btn = (Button)findViewById(R.id.camera_get_mutiphotocount_btn);
        m_camera_set_cameraaction_btn = (Button)findViewById(R.id.camera_set_cameraaction_btn);
        m_camera_get_cameraaction_btn = (Button)findViewById(R.id.camera_get_cameraaction_btn);
        m_camera_set_photoformat_btn = (Button)findViewById(R.id.camera_set_photoformat_btn);
        m_camera_get_photoformat_btn = (Button)findViewById(R.id.camera_get_photoformat_btn);
        m_camera_set_recordingparam_btn = (Button)findViewById(R.id.camera_set_recordingparam_btn);
        m_camera_get_recordingparam_btn = (Button)findViewById(R.id.camera_get_recordingparam_btn);
        m_camera_set_continuousparam_btn = (Button)findViewById(R.id.camera_set_continuousparam_btn);
        m_camera_get_continuousparam_btn = (Button)findViewById(R.id.camera_get_continuousparam_btn);            
        m_camera_set_mode_btn = (Button)findViewById(R.id.camera_set_mode_btn);
        m_camera_get_version_btn = (Button)findViewById(R.id.camera_get_version_btn);
        m_camera_get_visiontype_btn = (Button)findViewById(R.id.camera_get_visiontype_btn);
        m_camera_get_connectisok_btn = (Button)findViewById(R.id.camera_get_connectisok_btn);
        m_camera_sync_time_btn = (Button)findViewById(R.id.camera_sync_time_btn);
        m_camera_save_config_btn = (Button)findViewById(R.id.camera_save_config_btn);
        m_camera_restore_default_btn = (Button)findViewById(R.id.camera_restore_default_btn);
        m_camera_format_sdcard_btn = (Button)findViewById(R.id.camera_format_sdcard_btn);

        mStartTakePhotoBtn.setOnClickListener(this);
        mStopTakePhotoBtn.setOnClickListener(this);
        mStartRecordingBtn.setOnClickListener(this);
        mStopRecordingBtn.setOnClickListener(this);
        m_camera_set_photosize_btn.setOnClickListener(this);
        m_camera_get_photosize_btn.setOnClickListener(this);   
        m_camera_set_iso_btn.setOnClickListener(this);
        m_camera_get_iso_btn.setOnClickListener(this);
        m_camera_set_whitebalance_btn.setOnClickListener(this);
        m_camera_get_whitebalance_btn.setOnClickListener(this);
        m_camera_set_exposuremetering_btn.setOnClickListener(this);
        m_camera_get_exposuremetering_btn.setOnClickListener(this);
        m_camera_set_exposurecompensation_btn.setOnClickListener(this);
        m_camera_get_exposurecompensation_btn.setOnClickListener(this);
        m_camera_set_antiflicker_btn.setOnClickListener(this);
        m_camera_get_antiflicker_btn.setOnClickListener(this);
        m_camera_set_sharpness_btn.setOnClickListener(this);
        m_camera_get_sharpness_btn.setOnClickListener(this);
        m_camera_set_contrast_btn.setOnClickListener(this);
        m_camera_get_contrast_btn.setOnClickListener(this);
        m_camera_set_mutiphotocount_btn.setOnClickListener(this);
        m_camera_get_mutiphotocount_btn.setOnClickListener(this);
        m_camera_set_cameraaction_btn.setOnClickListener(this);
        m_camera_get_cameraaction_btn.setOnClickListener(this);
        m_camera_set_photoformat_btn.setOnClickListener(this);
        m_camera_get_photoformat_btn.setOnClickListener(this);
        m_camera_set_recordingparam_btn.setOnClickListener(this);
        m_camera_get_recordingparam_btn.setOnClickListener(this);
        m_camera_set_continuousparam_btn.setOnClickListener(this);
        m_camera_get_continuousparam_btn.setOnClickListener(this);            
        m_camera_set_mode_btn.setOnClickListener(this);
        m_camera_get_version_btn.setOnClickListener(this);
        m_camera_get_visiontype_btn.setOnClickListener(this);
        m_camera_get_connectisok_btn.setOnClickListener(this);
        m_camera_sync_time_btn.setOnClickListener(this);
        m_camera_save_config_btn.setOnClickListener(this);
        m_camera_restore_default_btn.setOnClickListener(this);
        m_camera_format_sdcard_btn.setOnClickListener(this);
        
        mConnectStateTextView = (TextView)findViewById(R.id.ConnectStateCameraTextView);
     
        m_context = this.getApplicationContext();
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        
        mTimer = new Timer();
        Task task = new Task();
        mTimer.schedule(task, 0, 500);
        
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
    public void onClick(View v)
    {
        List<String> strlist = null;
        List<String> strlist2 = null;
        int TotalStringCnt = 0;
        String[] mSettingStrs = null;
        
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.StartTakePhotoButton:
                DJIDrone.getDjiCamera().startTakePhoto(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub

                        Log.d(TAG, "Start Takephoto errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "Start Takephoto errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                
//                  DjiLocationCoordinate2D mGpsCoordinate2d = new DjiLocationCoordinate2D(22.01234, 113.1234);
//                  DJIDrone.getDjiCamera().setCameraGps(mGpsCoordinate2d,new DJIExecuteResultCallback(){
//    
//                      @Override
//                      public void onResult(DJIError mErr)
//                      {
//                          // TODO Auto-generated method stub
//    
//                          Log.d(TAG, "setCameraGps errorCode = "+ mErr.errorCode);
//                          Log.d(TAG, "setCameraGps errorDescription = "+ mErr.errorDescription);
//                          String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
//                          handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
//                                                 
//                      }
//                      
//                  });                
                  break;

            case R.id.StopTakePhotoButton:
                DJIDrone.getDjiCamera().stopTakePhoto(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "Stop Takephoto errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "Stop Takephoto errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                
//                DJIDrone.getDjiCamera().getCameraGps(new DJIExecuteResultCallback(){
//                    
//                    @Override
//                    public void onResult(DJIError mErr)
//                    {
//                        // TODO Auto-generated method stub
//  
//                        Log.d(TAG, "getCameraGps errorCode = "+ mErr.errorCode);
//                        Log.d(TAG, "getCameraGps errorDescription = "+ mErr.errorDescription);
//                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
//                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
//                                               
//                    }
//                    
//                });  
                
                break;

            case R.id.StartRecordingButton:
                DJIDrone.getDjiCamera().startRecord(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    { 
                        // TODO Auto-generated method stub
                        Log.d(TAG, "Start Recording errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "Start Recording errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                break;

            case R.id.StopRecordingButton:
                DJIDrone.getDjiCamera().stopRecord(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "Stop Recording errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "Stop Recording errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                break;
                
            case R.id.camera_set_photosize_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.photosizeValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.photosizeValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }

                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                DJIDrone.getDjiCamera().setCameraPhotoSize(CameraPhotoSizeType.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraPhotoSize errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraPhotoSize errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                
                break;
            case R.id.camera_get_photosize_btn:
                DJIDrone.getDjiCamera().getCameraPhotoSize(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraPhotoSize errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraPhotoSize errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().photoSize.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                
                break;   
            case R.id.camera_set_iso_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.isoValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.isoValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }
                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;

                                //Log.d(TAG,"iso value = "+CameraISOType.values()[pos1].toString());
                                
                                DJIDrone.getDjiCamera().setCameraISO(CameraISOType.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraISO errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraISO errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                break;
                
            case R.id.camera_get_iso_btn:
                DJIDrone.getDjiCamera().getCameraISO(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraISO errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraISO errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().iso.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                
                break;
                
            case R.id.camera_set_whitebalance_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.whitebalanceValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.whitebalanceValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }
 
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                DJIDrone.getDjiCamera().setCameraWhiteBalance(CameraWhiteBalanceType.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraWhiteBalance errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraWhiteBalance errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                
                break;
            case R.id.camera_get_whitebalance_btn:
                DJIDrone.getDjiCamera().getCameraWhiteBalance(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraWhiteBalance errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraWhiteBalance errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().whiteBalance.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });

                break;
            case R.id.camera_set_exposuremetering_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.exposuremeteringValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.exposuremeteringValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }

                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                DJIDrone.getDjiCamera().setCameraExposureMetering(CameraExposureMeteringType.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraExposureMetering errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraExposureMetering errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                break;
            case R.id.camera_get_exposuremetering_btn:
                DJIDrone.getDjiCamera().getCameraExposureMetering(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraExposureMetering errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraExposureMetering errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().exposureMetering.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });                
                break;
            case R.id.camera_set_exposurecompensation_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.exposurecompensationValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.exposurecompensationValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }

                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                DJIDrone.getDjiCamera().setCameraExposureCompensation(CameraExposureCompensationType.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraExposureCompensation errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraExposureCompensation errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                break;
            case R.id.camera_get_exposurecompensation_btn:
                DJIDrone.getDjiCamera().getCameraExposureCompensation(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraExposureCompensation errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraExposureCompensation errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().exposureCompensation.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });  
                break;
            case R.id.camera_set_antiflicker_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.antiflickerValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.antiflickerValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }

                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                DJIDrone.getDjiCamera().setCameraAntiFlicker(CameraAntiFlickerType.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraAntiFlicker errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraAntiFlicker errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                
                break;
            case R.id.camera_get_antiflicker_btn:
                DJIDrone.getDjiCamera().getCameraAntiFlicker(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraAntiFlicker errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraAntiFlicker errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().antiFlicker.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });  
                break;
            case R.id.camera_set_sharpness_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.sharpnessValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.sharpnessValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }

                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                DJIDrone.getDjiCamera().setCameraSharpness(CameraSharpnessType.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraSharpness errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraSharpness errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                
                break;
            case R.id.camera_get_sharpness_btn:
                DJIDrone.getDjiCamera().getCameraSharpness(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraSharpness errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraSharpness errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().sharpness.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });  
                break;
            case R.id.camera_set_contrast_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.contrastValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.contrastValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }

                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                DJIDrone.getDjiCamera().setCameraContrast(CameraContrastType.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraContrast errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraContrast errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                
                break;
            case R.id.camera_get_contrast_btn:
                DJIDrone.getDjiCamera().getCameraContrast(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraContrast errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraContrast errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().contrast.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                }); 
                break;
            case R.id.camera_set_mutiphotocount_btn:                
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.multishotValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.multishotValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }
                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                Log.d(TAG,"CameraMultiShotCount.values()[pos1].toString() = "+CameraMultiShotCount.values()[pos1].toString());
                                
                                DJIDrone.getDjiCamera().setMutiPhotoCount(CameraMultiShotCount.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setMutiPhotoCount errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setMutiPhotoCount errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                
                break;
            case R.id.camera_get_mutiphotocount_btn:
                DJIDrone.getDjiCamera().getMutiPhotoCount(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getMutiPhotoCount errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getMutiPhotoCount errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().multiShotCount.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                }); 
                break;
            case R.id.camera_set_cameraaction_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.cameraactionValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.cameraactionValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }
                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                //Log.d(TAG,"CameraActionWhenBreak.values()[pos1].toString() = "+CameraActionWhenBreak.values()[pos1].toString());
                                
                                DJIDrone.getDjiCamera().setCameraActionWhenConnectionBroken(CameraActionWhenBreak.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraActionWhenConnectionBroken errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraActionWhenConnectionBroken errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                break;
            case R.id.camera_get_cameraaction_btn:
                DJIDrone.getDjiCamera().getCameraActionWhenConnectionBroken(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraActionWhenConnectionBroken errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraActionWhenConnectionBroken errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().cameraAction.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                }); 
                break;
            case R.id.camera_set_photoformat_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.photoformatValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.photoformatValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }
                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                //Log.d(TAG,"CameraActionWhenBreak.values()[pos1].toString() = "+CameraActionWhenBreak.values()[pos1].toString());
                                
                                DJIDrone.getDjiCamera().setCameraPhotoFormat(CameraPhotoFormatType.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraPhotoFormat errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraPhotoFormat errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                break;
            case R.id.camera_get_photoformat_btn:
                DJIDrone.getDjiCamera().getCameraPhotoFormat(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraPhotoFormat errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraPhotoFormat errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().photoFormat.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                break;
            case R.id.camera_set_recordingparam_btn:
                if (mPopupNumberPickerDoubleRecording != null)
                    mPopupNumberPickerDoubleRecording.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.recordingResolutionValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.recordingResolutionValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }
                    
                strlist2 = new ArrayList<String>();
                
                TotalStringCnt = getResources().getStringArray(R.array.recordingFovValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.recordingFovValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist2.add(mSettingStrs[i]);
                }
                  
                mPopupNumberPickerDoubleRecording = new PopupNumberPickerDoubleRecording(m_context,
                        strlist,null,strlist2,null,                    
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) 
                            {
                                //Log.d(TAG,"pos1 = "+pos1+",pos2 = "+pos2);                               
                                mPopupNumberPickerDoubleRecording.dismiss();
                                mPopupNumberPickerDoubleRecording = null;
                                
                                DJIDrone.getDjiCamera().setCameraRecordingParam(CameraRecordingResolutionType.values()[pos1], CameraRecordingFovType.values()[pos2],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraRecordingParam errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraRecordingParam errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                            }}, 350,200, 0);
                
                mPopupNumberPickerDoubleRecording.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                
                
                break;
            case R.id.camera_get_recordingparam_btn:
                
                DJIDrone.getDjiCamera().getCameraRecordingParam(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getCameraRecordingParam errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getCameraRecordingParam errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().recordingResolution.toString()+","+DJIDrone.getDjiCamera().getDjiCameraProperty().recordingFov.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                
                break;
                
            case R.id.camera_set_continuousparam_btn:
                
                if (mPopupNumberPickerDouble != null)
                    mPopupNumberPickerDouble.dismiss();

                strlist = new ArrayList<String>();
                for (int i = 3; i <= 60; i++) {                             
                    strlist.add(String.valueOf(i));
                }

                    
                strlist2 = new ArrayList<String>();
                
                for (int i = 1; i <= 255; i++) {    
                    if(i== 255){
                        strlist2.add("∞");
                    }
                    else{
                        strlist2.add(String.valueOf(i));
                    }
                }
                  
                mPopupNumberPickerDouble = new PopupNumberPickerDouble(m_context,
                        strlist,null,strlist2,null,                    
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) 
                            {
                                Log.d(TAG,"pos1 = "+pos1+",pos2 = "+pos2);                                 
                                
                                
                                mPopupNumberPickerDouble.dismiss();
                                mPopupNumberPickerDouble = null;
                                
                                DJIDrone.getDjiCamera().setContinuousPhotoParam(pos2+1, pos1+3,new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setContinuousPhotoParam errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setContinuousPhotoParam errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                            }}, 350,200, 0);
                
                mPopupNumberPickerDouble.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                break;
            case R.id.camera_get_continuousparam_btn:
                DJIDrone.getDjiCamera().getContinuousPhotoParam(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "getContinuousPhotoParam errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "getContinuousPhotoParam errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        if(mErr.errorCode == DJIError.RESULT_OK ){
                            result = result +"\n" + "value:"+ DJIDrone.getDjiCamera().getDjiCameraProperty().mContinuousPhotoParam.toString();
                        }
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                break;        
            case R.id.camera_set_mode_btn:
                if (mPopupNumberPicker != null)
                    mPopupNumberPicker.dismiss();

                strlist = new ArrayList<String>();                
                TotalStringCnt = getResources().getStringArray(R.array.cameramodeValues).length;
                mSettingStrs = new String[TotalStringCnt];
                mSettingStrs = getResources().getStringArray(R.array.cameramodeValues);
                
                for (int i = 0; i < TotalStringCnt; i++) {
                    strlist.add(mSettingStrs[i]);
                }
                
                mPopupNumberPicker = new PopupNumberPicker(m_context,
                        strlist,
                        new pickerValueChangeListener(){

                            @Override
                            public void onValueChange(int pos1, int pos2) {
                                //Log.d(TAG,"pos1 = "+ pos1 +", pos2 = "+pos2);
                                mPopupNumberPicker.dismiss();
                                mPopupNumberPicker = null;
                                
                                //Log.d(TAG,"CameraActionWhenBreak.values()[pos1].toString() = "+CameraActionWhenBreak.values()[pos1].toString());
                                
                                DJIDrone.getDjiCamera().setCameraMode(CameraMode.values()[pos1],new DJIExecuteResultCallback(){

                                    @Override
                                    public void onResult(DJIError mErr)
                                    {
                                        // TODO Auto-generated method stub
                                        Log.d(TAG, "setCameraMode errorCode = "+ mErr.errorCode);
                                        Log.d(TAG, "setCameraMode errorDescription = "+ mErr.errorDescription);
                                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                                               
                                    }
                                    
                                });
                                
                                
                            }}, 250,
                        200, 0);
                
                mPopupNumberPicker.showAtLocation(findViewById(R.id.my_content_view),
                        Gravity.CENTER, 0, 0);
                
                break;
            case R.id.camera_get_version_btn:
                String mVersion = "";
                mVersion = DJIDrone.getDjiCamera().getCameraVersion();    
                handler.sendMessage(handler.obtainMessage(SHOWTOAST, "camera version =" + mVersion));
                break;
            case R.id.camera_get_visiontype_btn:                
                CameraVisionType mType;
                mType = DJIDrone.getDjiCamera().getVisionType();    
                handler.sendMessage(handler.obtainMessage(SHOWTOAST, "Vision type = "+mType.toString()));
                break;
            case R.id.camera_get_connectisok_btn:
                boolean flag = false;
                flag = DJIDrone.getDjiCamera().getCameraConnectIsOk();  
                handler.sendMessage(handler.obtainMessage(SHOWTOAST, "connect is ok = "+flag));
                break;
            case R.id.camera_sync_time_btn:
                DJIDrone.getDjiCamera().syncTime(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "syncTime errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "syncTime errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                break;
            case R.id.camera_save_config_btn:
                DJIDrone.getDjiCamera().saveCameraConfig(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "saveCameraConfig errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "saveCameraConfig errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                break;
            case R.id.camera_restore_default_btn:
                DJIDrone.getDjiCamera().restoreCameraDefaultSettings(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "restoreCameraDefaultSettings errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "restoreCameraDefaultSettings errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                break;
            case R.id.camera_format_sdcard_btn:
                DJIDrone.getDjiCamera().formatSDCard(new DJIExecuteResultCallback(){

                    @Override
                    public void onResult(DJIError mErr)
                    {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "formatSDCard errorCode = "+ mErr.errorCode);
                        Log.d(TAG, "formatSDCard errorDescription = "+ mErr.errorDescription);
                        String result = "errorCode =" + mErr.errorCode + "\n"+"errorDescription =" + DJIError.getErrorDescriptionByErrcode(mErr.errorCode);
                        handler.sendMessage(handler.obtainMessage(SHOWTOAST, result));
                                               
                    }
                    
                });
                break;   
            default:
                break;
        }
    }
    
    private void setResultToToast(String result){
        Toast.makeText(CameraProtocolDemoActivity.this, result, Toast.LENGTH_SHORT).show();
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
    
    public interface pickerValueChangeListener {
        void onValueChange(int pos1,int pos2);
    }
    

}
