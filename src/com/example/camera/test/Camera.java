package com.example.camera.test;

import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.camera.MainActivity;
import com.example.camera.Picture;
public class Camera extends ActivityInstrumentationTestCase2<MainActivity> {

	private static final String TAG = "Camera";
	MainActivity mainActivity;
	
	public Camera() {
		super("com.example.camera",MainActivity.class);
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		mainActivity=(MainActivity)getActivity();
	}
//	public void testSDcard() {
//		// TODO Auto-generated method stub
//       if (Environment.getExternalStorageState()!= null) {
//    	   Log.i(TAG, "SD卡正常");
//	}
//	}
	public void testCamera() {
		// TODO Auto-generated method stub
       if ( mainActivity.camera != null) {
		Log.i(TAG, "相机已经开启");
	}
	}
	
	

}
