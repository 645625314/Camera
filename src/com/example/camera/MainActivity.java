package com.example.camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.camera.ui.DrawCaptureRect;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
	Camera camera;
	Picture picture;
	int mCurrentOrientation;
	Camera.Parameters parameters;
	Button take_picture;
	ImageView pic_pre;
	TextView countdown;
	int currentTime = 10;
	boolean isclicked = false;
	String imagefilepath = "/sdcard/Pictures/";
	
	private Handler timerUpdateHandler;
	private boolean timerRunning = false;
	private Runnable timerUpdateTask = new Runnable() {
		public void run() {
			if (currentTime > 1) {
				currentTime--;
				timerUpdateHandler.postDelayed(timerUpdateTask, 1000);
			} else {
				camera.autoFocus(autofocus);
				camera.takePicture(null, null, jpeg);
				currentTime = 10;
				timerRunning = false;
				countdown.setVisibility(View.GONE);
			}
			countdown.setText("" + currentTime);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		SurfaceView cameraView = (SurfaceView) this
				.findViewById(R.id.surfaceview);
		SurfaceHolder surfaceHolder = cameraView.getHolder(); // 用surfaceholder将SurfaceView和Camera联系起来
		//surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this); // 告知SurfaceHolder用此activity作为其Callback的实现类
		mCurrentOrientation = getResources().getConfiguration().orientation; // 得到当前手机屏幕显示方向
		take_picture = (Button) findViewById(R.id.take_picture);
		take_picture.setOnClickListener(takePicture);
		pic_pre = (ImageView) findViewById(R.id.pic_pre);
		countdown = (Button) this.findViewById(R.id.count);
		
		pic_pre.setOnClickListener(prepicListener);
		cameraView.setFocusable(true);
		cameraView.setFocusableInTouchMode(true);
		cameraView.setClickable(true);
		cameraView.setOnClickListener(surfaceautofocus);
		

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId())// 得到被点击的item的itemId
		{

		case R.id.original:
			setcolor(Camera.Parameters.EFFECT_NONE);
			break;
		case R.id.black_white:
			setcolor(Camera.Parameters.EFFECT_MONO);
			break;
		case R.id.nagative:
			setcolor(Camera.Parameters.EFFECT_NEGATIVE);
			break;
		case R.id.Sephia:
			setcolor(Camera.Parameters.EFFECT_SEPIA);
			break;

		case R.id.delay_take:
			countdown.setVisibility(View.VISIBLE);
			timerUpdateHandler = new Handler();
			if (!timerRunning) {
				timerRunning = true;
				timerUpdateHandler.post(timerUpdateTask);
			}
		
		case R.id.exit:
			finish();
			break;
		}
		return true;
	}

	private void setcolor(String string) {
		// TODO Auto-generated method stub
		parameters = camera.getParameters();
		parameters.setColorEffect(string);// 黑白效果
		camera.setParameters(parameters);
	}
	
   	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		
		MenuInflater inflater = new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.home_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 在activity里实现SurfaceHolder.Callback接口。这样，我们的activity就能在Surface被创建，
	 * 改变和被销毁时得到通知
	 */

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera = Camera.open();
		try {
			camera.setPreviewDisplay(holder);// 将suface
												// 的预览设置由回调函数传入的SurfaceHolder中显示
			if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
				// Toast.makeText(getApplicationContext(), "竖屏",
				// Toast.LENGTH_SHORT).show();

				camera.setDisplayOrientation(90);
			} else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) { // Toast.makeText(getApplicationContext(),
																						// "横屏",
																						// Toast.LENGTH_SHORT).show();
				camera.setDisplayOrientation(0);
			}

			camera.startPreview();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "IO异常", Toast.LENGTH_SHORT)
					.show();

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

	PictureCallback jpeg = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "开启拍摄", Toast.LENGTH_SHORT)
					.show();

			try {
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						"yyyyMMddhhmmss");
				String date = sDateFormat.format(new java.util.Date());

				imagefilepath = imagefilepath + date + ".jpg";
				File file = new File(imagefilepath);

				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(file));
				Matrix matrix = new Matrix();
				if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
					matrix.postRotate(90);
				} else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {

					matrix.postRotate(0);
				}
				Bitmap rotateBitmap = Bitmap.createBitmap(bm, 0, 0,
						bm.getWidth(), bm.getHeight(), matrix, true);
				// 将图片以JPEG格式压缩到流中
				rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
				pic_pre.setBackgroundDrawable(changeBitmapToDrawable(rotateBitmap));
				// pic_pre.setBackgroundDrawable();
				pic_pre.setTag(imagefilepath);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};
	OnClickListener prepicListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String picpath = (String) v.getTag();
			Intent intent = new Intent();

			intent.putExtra("picpath", picpath);
			intent.setClass(MainActivity.this, Picture.class);
			startActivity(intent);
			finish();

		}
	};

	public BitmapDrawable changeBitmapToDrawable(Bitmap rotateBitmap) {
		// TODO Auto-generated method stub
		int width = rotateBitmap.getWidth();
		int height = rotateBitmap.getHeight();
		int newWidth = 100;
		float scaleWidth = (float) newWidth / width;
		float scaleHeight = scaleWidth;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap newBitmap = Bitmap.createBitmap(rotateBitmap, 0, 0, width,
				height, matrix, true);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(newBitmap);
		return bitmapDrawable;
	}

	AutoFocusCallback autofocus = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub
			if (success) {
				parameters = camera.getParameters();
				parameters.setPictureFormat(PixelFormat.JPEG);
				camera.setParameters(parameters);

			}
		}
	};

	OnClickListener takePicture = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!isclicked) {
				camera.takePicture(null, null, jpeg);
				isclicked = true;
			} else {
				camera.startPreview();
				isclicked = false;
			}
		}
	};
	OnClickListener surfaceautofocus = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			camera.autoFocus(autofocus);
			
		
		}
	};

}
