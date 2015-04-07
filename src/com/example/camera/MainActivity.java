package com.example.camera;

import java.io.IOException;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
	Camera camera; 
	int mCurrentOrientation;
	 Camera.Parameters parameters;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  
		
		SurfaceView cameraView = (SurfaceView) this.findViewById(R.id.surfaceview);  
		SurfaceHolder surfaceHolder = cameraView.getHolder();  //用surfaceholder将SurfaceView和Camera联系起来
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
		surfaceHolder.addCallback(this);    //告知SurfaceHolder用此activity作为其Callback的实现类
	     mCurrentOrientation = getResources().getConfiguration().orientation;  //得到当前手机屏幕显示方向
	   
		
		
		 
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())//得到被点击的item的itemId
        {
        case Menu.FIRST+1: //对应的ID就是在add方法中所设定的Id
        	setcolor(Camera.Parameters.EFFECT_NONE);
            break;
        case Menu.FIRST+2:
        	setcolor(Camera.Parameters.EFFECT_MONO);
            break;
        case Menu.FIRST+3:
        	setcolor(Camera.Parameters.EFFECT_NEGATIVE);
            break;
        case Menu.FIRST+4:
        	setcolor(Camera.Parameters.EFFECT_SEPIA);
            break;
        }
        return true;
    }
	
	private void setcolor(String string) {
		// TODO Auto-generated method stub
		parameters = camera.getParameters();
    	parameters.setColorEffect(string);//黑白效果
    	camera.setParameters(parameters);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		//getMenuInflater().inflate(R.menu.options_menu, menu);
		menu.add(Menu.NONE, Menu.FIRST+1, 0, "原始") ;
		menu.add(Menu.NONE, Menu.FIRST+2, 0, "黑白") ;
		menu.add(Menu.NONE, Menu.FIRST+3, 0, "反光") ;
		menu.add(Menu.NONE, Menu.FIRST+4, 0, "Sephia") ;
		return true;
	}
	/**
	 * 在activity里实现SurfaceHolder.Callback接口。这样，我们的activity就能在Surface被创建，改变和被销毁时得到通知
	 */

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera=Camera.open();
		try {
			camera.setPreviewDisplay(holder);//将suface 的预览设置由回调函数传入的SurfaceHolder中显示
		  if (mCurrentOrientation== Configuration.ORIENTATION_PORTRAIT) {
			  //Toast.makeText(getApplicationContext(), "竖屏", Toast.LENGTH_SHORT).show();
			  
			  
			  camera.setDisplayOrientation(90);	
		}
		  else  if (mCurrentOrientation== Configuration.ORIENTATION_LANDSCAPE)
		  {  //Toast.makeText(getApplicationContext(), "横屏", Toast.LENGTH_SHORT).show();
		 			  camera.setDisplayOrientation(0);
		  }
		   
		     
		     
		            					camera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "IO异常", Toast.LENGTH_SHORT).show();
			
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		 camera.stopPreview();    
		   camera.release();  
	}
	
}
