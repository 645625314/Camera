package com.example.camera;

import com.example.camera.event.MulitPointTouchListener;




import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ZoomControls;

public class Picture extends Activity  {
	ImageView picture;
	ZoomControls zoom;
	Bitmap bitmap;

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST+1:
			picture.setOnTouchListener(new MulitPointTouchListener());
			break;

		case Menu.FIRST+2:
			picture.setOnTouchListener(null);
						break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, Menu.FIRST+1, 0, "开启缩放和拖拽 ");
		menu.add(Menu.NONE, Menu.FIRST+2, 0, "关闭缩放和拖拽");
		return super.onCreateOptionsMenu(menu);
	}

	final double minsize = 0.8 * 0.8 * 0.8 * 0.8 * 0.8 * 0.8 * 0.8 * 0.8 * 0.8
			* 0.8;
	private float scaleWidth=1;
	private float scaleHeight=1;
//	private int displayWidth;
//	private int displayHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pciture);

		picture = (ImageView) this.findViewById(R.id.picture);
		
		zoom = (ZoomControls) this.findViewById(R.id.zoom);

		zoom.setIsZoomInEnabled(true);
		zoom.setIsZoomOutEnabled(true);

//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		displayWidth = dm.widthPixels;
//		displayHeight = dm.heightPixels - 80;

		Intent intent = getIntent();
		String picpath = (String) intent.getCharSequenceExtra("picpath");
		bitmap = BitmapFactory.decodeFile(picpath, null);
		
		picture.setImageBitmap(bitmap);
        
		zoom.setOnZoomInClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				double scale = 1.25;
				scaleWidth = (float) (scaleWidth * scale);
				scaleHeight = (float) (scaleHeight * scale);
				if (scaleWidth >= 1) {
					// 超出大小后的处理
					scaleWidth = (float) 1.0;
					scaleHeight = (float) 1.0;
				}

				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				picture.setImageBitmap(newBitmap);

			}
		});
		zoom.setOnZoomOutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				double scale = 0.8;
				scaleWidth = (float) (scaleWidth * scale);
				scaleHeight = (float) (scaleHeight * scale);
				if (scaleWidth <= minsize) {
					// 低于大小后的处理
					scaleWidth = (float) minsize;
					scaleHeight = (float) minsize;
				}

				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				picture.setImageBitmap(newBitmap);

			}
		});

	}



}
