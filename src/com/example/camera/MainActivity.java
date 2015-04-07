package com.example.camera;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Bundle;


public class MainActivity extends Activity implements Callback,AutoFocusCallback{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture);
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalidateDrawable(Drawable who) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scheduleDrawable(Drawable who, Runnable what, long when) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unscheduleDrawable(Drawable who, Runnable what) {
		// TODO Auto-generated method stub
		
	}
}
