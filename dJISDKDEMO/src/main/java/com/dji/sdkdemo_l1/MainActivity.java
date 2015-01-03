package com.dji.sdkdemo_l1;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.dji.sdkdemo_l1.R;

import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIError;
import dji.sdk.api.DJIDroneTypeDef.DJIDroneType;
import dji.sdk.api.media.DJIMedia;
import dji.sdk.api.Camera.DJICameraSettingsTypeDef.CameraPreviewResolustionType;
import dji.sdk.interfaces.DJIGerneralListener;
import dji.sdk.interfaces.DJIMediaFetchCallBack;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ListView mListView = (ListView)findViewById(R.id.listView); 
        // 添加ListItem，设置事件响应
        mListView.setAdapter(new DemoListAdapter());
        mListView.setOnItemClickListener(new OnItemClickListener() {  
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {  
                onListItemClick(index);
            }  
        }); 
        
        new Thread(){
            public void run() {
                try {
                    DJIDrone.checkPermission(getApplicationContext(), new DJIGerneralListener() {
                        
                        @Override
                        public void onGetPermissionResult(int result) {
                            // TODO Auto-generated method stub
                            Log.e(TAG, "onGetPermissionResult = "+result);
                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

        
        onInitSDK();
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
       
    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        onUnInitSDK();
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }
    
    private void onInitSDK(){
        DJIDrone.initWithType(DJIDroneType.DJIDrone_Vision);
        DJIDrone.connectToDrone();
    }
    
    private void onUnInitSDK(){
        DJIDrone.disconnectToDrone();
    }
    
    private void onListItemClick(int index) {
        Intent intent = null;
        intent = new Intent(MainActivity.this, demos[index].demoClass);
        this.startActivity(intent);
    }
    
    private static final DemoInfo[] demos = {
        new DemoInfo(R.string.demo_title_preview,R.string.demo_desc_preview, PreviewDemoActivity.class),
        new DemoInfo(R.string.demo_title_camera_protocol,R.string.demo_desc_camera_protocol, CameraProtocolDemoActivity.class),
        new DemoInfo(R.string.demo_title_main_controller_protocol,R.string.demo_desc_main_controller_protocol, MainControllerDemoActivity.class),
        new DemoInfo(R.string.demo_title_battery_protocol,R.string.demo_desc_battery_protocol, BatteryInfoDemoActivity.class),
        new DemoInfo(R.string.demo_title_gimbal_protocol,R.string.demo_desc_gimbal_protocol, GimbalDemoActivity.class),
        new DemoInfo(R.string.demo_title_range_extender,R.string.demo_desc_range_extender, RangeExtenderDemoActivity.class),
        new DemoInfo(R.string.demo_title_media_sync,R.string.demo_desc_media_sync, MediaSyncDemoActivity.class)
        
        
    };

    private  class DemoListAdapter extends BaseAdapter {
        public DemoListAdapter() {
            super();
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            convertView = View.inflate(MainActivity.this, R.layout.demo_info_item, null);
            TextView title = (TextView)convertView.findViewById(R.id.title);
            TextView desc = (TextView)convertView.findViewById(R.id.desc);

            title.setText(demos[index].title);
            desc.setText(demos[index].desc);
            return convertView;
        }
        @Override
        public int getCount() {
            return demos.length;
        }
        @Override
        public Object getItem(int index) {
            return  demos[index];
        }

        @Override
        public long getItemId(int id) {
            return id;
        }
    }
    
   private static class DemoInfo{
        private final int title;
        private final int desc;
        private final Class<? extends android.app.Activity> demoClass;

        public DemoInfo(int title , int desc,Class<? extends android.app.Activity> demoClass) {
            this.title = title;
            this.desc  = desc;
            this.demoClass = demoClass;
        }
    }
   private static boolean first = false;
   private Timer ExitTimer = new Timer();
       
   class ExitCleanTask extends TimerTask{

           @Override
           public void run() {
               
               Log.e("ExitCleanTask", "Run in!!!! ");
               first = false;
           }
   }   
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG,"onKeyDown KEYCODE_BACK");
            
            if (first) {
                first = false;
                finish();
            } 
            else 
            {
                first = true;
                Toast.makeText(MainActivity.this, getText(R.string.press_again_exit), Toast.LENGTH_SHORT).show();
                ExitTimer.schedule(new ExitCleanTask(), 2000);
            }
            
            //finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
   
}
