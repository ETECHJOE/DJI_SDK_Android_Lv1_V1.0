
package com.dji.sdkdemo_l1;

import java.util.Timer;
import java.util.TimerTask;

import com.dji.sdkdemo_l1.R;
import com.dji.sdkdemo_l1.MainControllerDemoActivity.Task;

import dji.sdk.api.DJIDrone;
import dji.sdk.api.Battery.DJIBatteryProperty;
import dji.sdk.interfaces.DJIBattryUpdateInfoCallBack;
import dji.sdk.interfaces.DJIReceivedVideoDataCallBack;
import dji.sdk.widget.DjiGLSurfaceView;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BatteryInfoDemoActivity extends Activity {
    private static final String TAG = "BatteryInfoDemoActivity";
    
    private TextView mConnectStateTextView;
    private TextView mBatteryInfoTextView;  
    private DjiGLSurfaceView mDjiGLSurfaceView;
    
    private DJIReceivedVideoDataCallBack mReceivedVideoDataCallBack  = null;    
    private DJIBattryUpdateInfoCallBack mBattryUpdateInfoCallBack = null;
    
    private Timer mTimer;
    private String BatteryInfoString = "";    
    
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
        
        BatteryInfoDemoActivity.this.runOnUiThread(new Runnable(){

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_info_demo);

        mDjiGLSurfaceView = (DjiGLSurfaceView)findViewById(R.id.DjiSurfaceView_battery_info);
        mBatteryInfoTextView = (TextView)findViewById(R.id.BatteryInfoTV);
        mConnectStateTextView = (TextView)findViewById(R.id.ConnectStateBatteryInfoTextView);        
        
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
        
        mBattryUpdateInfoCallBack = new DJIBattryUpdateInfoCallBack(){

            @Override
            public void onResult(DJIBatteryProperty state) {
                // TODO Auto-generated method stub
                StringBuffer sb = new StringBuffer();
                sb.append(getString(R.string.battery_info)).append("\n");
                sb.append("designedVolume=").append(state.designedVolume).append("\n");
                sb.append("fullChargeVolume=").append(state.fullChargeVolume).append("\n");        
                sb.append("currentElectricity=").append(state.currentElectricity).append("\n");
                sb.append("currentVoltage=").append(state.currentVoltage).append("\n");        
                sb.append("currentCurrent=").append(state.currentCurrent).append("\n");
                sb.append("remainLifePercent=").append(state.remainLifePercent).append("\n");
                sb.append("remainPowerPercent=").append(state.remainPowerPercent).append("\n");
                sb.append("batteryTemperature=").append(state.batteryTemperature).append("\n");
                sb.append("dischargeCount=").append(state.dischargeCount);
                BatteryInfoString = sb.toString();

                BatteryInfoDemoActivity.this.runOnUiThread(new Runnable(){

                    @Override
                    public void run() 
                    {   
                        mBatteryInfoTextView.setText(BatteryInfoString);
                    }
                });
            }
            
        };        
        
        DJIDrone.getDjiBattery().setBattryUpdateInfoCallBack(mBattryUpdateInfoCallBack);
        
    }

    @Override
    protected void onResume() {
        
        mTimer = new Timer();
        Task task = new Task();
        mTimer.schedule(task, 0, 500);    
        
        DJIDrone.getDjiBattery().startUpdateTimer(2000);
        // TODO Auto-generated method stub
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
        
        DJIDrone.getDjiBattery().stopUpdateTimer();
        super.onPause();
    }
    
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        mDjiGLSurfaceView.destroy();
        DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(null);
        
        super.onDestroy();
    }
    
    /** 
     * @Description : RETURN BTN RESPONSE FUNCTION
     * @author      : andy.zhao
     * @date        : 2014年7月29日 下午4:53:35
     * @param view 
     * @return      : void
     */
    public void onReturn(View view){
        Log.d(TAG ,"onReturn");  
        this.finish();
    }
    
}
