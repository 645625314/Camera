package com.example.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ZoomControls;

public class Picture extends Activity {
	ImageView picture;
	ZoomControls zoom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pciture);

		picture = (ImageView) this.findViewById(R.id.picture);
		zoom = (ZoomControls) this.findViewById(R.id.zoom);
      zoom.setOnZoomInClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	});
      zoom.setOnZoomOutClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	});
      
	}

}
