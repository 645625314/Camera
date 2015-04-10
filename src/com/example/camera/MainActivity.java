package com.example.camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
	Camera camera;
	int mCurrentOrientation;
	Camera.Parameters parameters;
	Button take_picture;
	ImageView pic_pre;
	boolean isclicked = false;
	String imagefilepath = "/sdcard/Pictures/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		SurfaceView cameraView = (SurfaceView) this
				.findViewById(R.id.surfaceview);
		SurfaceHolder surfaceHolder = cameraView.getHolder(); // ��surfaceholder��SurfaceView��Camera��ϵ����
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this); // ��֪SurfaceHolder�ô�activity��Ϊ��Callback��ʵ����
		mCurrentOrientation = getResources().getConfiguration().orientation; // �õ���ǰ�ֻ���Ļ��ʾ����
		take_picture = (Button) findViewById(R.id.take_picture);
		take_picture.setOnClickListener(takePicture);
		pic_pre = (ImageView) findViewById(R.id.pic_pre);
		
		cameraView.setFocusable(true);
		cameraView.setFocusableInTouchMode(true);
		cameraView.setClickable(true);
		cameraView.setOnClickListener(surfaceautofocus);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId())// �õ��������item��itemId
		{
		case Menu.FIRST + 1: // ��Ӧ��ID������add���������趨��Id
			setcolor(Camera.Parameters.EFFECT_NONE);
			break;
		case Menu.FIRST + 2:
			setcolor(Camera.Parameters.EFFECT_MONO);
			break;
		case Menu.FIRST + 3:
			setcolor(Camera.Parameters.EFFECT_NEGATIVE);
			break;
		case Menu.FIRST + 4:
			setcolor(Camera.Parameters.EFFECT_SEPIA);
			break;
		}
		return true;
	}

	private void setcolor(String string) {
		// TODO Auto-generated method stub
		parameters = camera.getParameters();
		parameters.setColorEffect(string);// �ڰ�Ч��
		camera.setParameters(parameters);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		// getMenuInflater().inflate(R.menu.options_menu, menu);
		menu.add(Menu.NONE, Menu.FIRST + 1, 0, "ԭʼ");
		menu.add(Menu.NONE, Menu.FIRST + 2, 0, "�ڰ�");
		menu.add(Menu.NONE, Menu.FIRST + 3, 0, "����");
		menu.add(Menu.NONE, Menu.FIRST + 4, 0, "Sephia");
		return true;
	}

	/**
	 * ��activity��ʵ��SurfaceHolder.Callback�ӿڡ����������ǵ�activity������Surface��������
	 * �ı�ͱ�����ʱ�õ�֪ͨ
	 */

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera = Camera.open();
		try {
			camera.setPreviewDisplay(holder);// ��suface
												// ��Ԥ�������ɻص����������SurfaceHolder����ʾ
			if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
				// Toast.makeText(getApplicationContext(), "����",
				// Toast.LENGTH_SHORT).show();

				camera.setDisplayOrientation(90);
			} else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) { // Toast.makeText(getApplicationContext(),
																						// "����",
																						// Toast.LENGTH_SHORT).show();
				camera.setDisplayOrientation(0);
			}

			camera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "IO�쳣", Toast.LENGTH_SHORT)
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
			Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_SHORT)
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
				// ��ͼƬ��JPEG��ʽѹ��������
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
    OnClickListener prepicListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String picpath=(String)v.getTag();
			Intent intent=new Intent();
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
