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
		SurfaceHolder surfaceHolder = cameraView.getHolder();  //��surfaceholder��SurfaceView��Camera��ϵ����
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
		surfaceHolder.addCallback(this);    //��֪SurfaceHolder�ô�activity��Ϊ��Callback��ʵ����
	     mCurrentOrientation = getResources().getConfiguration().orientation;  //�õ���ǰ�ֻ���Ļ��ʾ����
	   
		
		
		 
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())//�õ��������item��itemId
        {
        case Menu.FIRST+1: //��Ӧ��ID������add���������趨��Id
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
    	parameters.setColorEffect(string);//�ڰ�Ч��
    	camera.setParameters(parameters);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		//getMenuInflater().inflate(R.menu.options_menu, menu);
		menu.add(Menu.NONE, Menu.FIRST+1, 0, "ԭʼ") ;
		menu.add(Menu.NONE, Menu.FIRST+2, 0, "�ڰ�") ;
		menu.add(Menu.NONE, Menu.FIRST+3, 0, "����") ;
		menu.add(Menu.NONE, Menu.FIRST+4, 0, "Sephia") ;
		return true;
	}
	/**
	 * ��activity��ʵ��SurfaceHolder.Callback�ӿڡ����������ǵ�activity������Surface���������ı�ͱ�����ʱ�õ�֪ͨ
	 */

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera=Camera.open();
		try {
			camera.setPreviewDisplay(holder);//��suface ��Ԥ�������ɻص����������SurfaceHolder����ʾ
		  if (mCurrentOrientation== Configuration.ORIENTATION_PORTRAIT) {
			  //Toast.makeText(getApplicationContext(), "����", Toast.LENGTH_SHORT).show();
			  
			  
			  camera.setDisplayOrientation(90);	
		}
		  else  if (mCurrentOrientation== Configuration.ORIENTATION_LANDSCAPE)
		  {  //Toast.makeText(getApplicationContext(), "����", Toast.LENGTH_SHORT).show();
		 			  camera.setDisplayOrientation(0);
		  }
		   
		     
		     
		            					camera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "IO�쳣", Toast.LENGTH_SHORT).show();
			
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
