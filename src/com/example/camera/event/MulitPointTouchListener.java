package com.example.camera.event;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MulitPointTouchListener implements OnTouchListener {
	private static final String TAG = "Touch"; 
    // These matrices will be used to move and zoom image 
    Matrix matrix = new Matrix(); 
    Matrix savedMatrix = new Matrix(); 

    // We can be in one of these 3 states 
    static final int NONE = 0; 
    static final int DRAG = 1; 
    static final int ZOOM = 2; 
    int mode = NONE; 

    // Remember some things for zooming 
    PointF start = new PointF(); 
    PointF mid = new PointF(); 
    float oldDist = 1f; 
	public MulitPointTouchListener() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		  ImageView view = (ImageView) v; 
          // Log.e("view_width", 
          // view.getImageMatrix()..toString()+"*"+v.getWidth()); 
          // Dump touch event to log 
         // dumpEvent(event); 

          // Handle touch events here... 
          switch (event.getAction() & MotionEvent.ACTION_MASK) { 
          case MotionEvent.ACTION_DOWN: 

                  matrix.set(view.getImageMatrix()); 
                  savedMatrix.set(matrix); 
                  start.set(event.getX(), event.getY()); 
                  //Log.d(TAG, "mode=DRAG"); 
                  mode = DRAG; 

                 
                  //Log.d(TAG, "mode=NONE"); 
                  break; 
          case MotionEvent.ACTION_POINTER_DOWN: 
                  oldDist = spacing(event); 
                  //Log.d(TAG, "oldDist=" + oldDist); 
                  if (oldDist > 10f) { 
                          savedMatrix.set(matrix); 
                          midPoint(mid, event); 
                          mode = ZOOM; 
                          //Log.d(TAG, "mode=ZOOM"); 
                  } 
                  break; 
          case MotionEvent.ACTION_UP: 
          case MotionEvent.ACTION_POINTER_UP: 
                  mode = NONE; 
                  //Log.e("view.getWidth", view.getWidth() + ""); 
                  //Log.e("view.getHeight", view.getHeight() + ""); 

                  break; 
          case MotionEvent.ACTION_MOVE: 
                  if (mode == DRAG) { 
                          // ... 
                          matrix.set(savedMatrix); 
                          matrix.postTranslate(event.getX() - start.x, event.getY() 
                                          - start.y); 
                  } else if (mode == ZOOM) { 
                          float newDist = spacing(event); 
                          //Log.d(TAG, "newDist=" + newDist); 
                          if (newDist > 10f) { 
                                  matrix.set(savedMatrix); 
                                  float scale = newDist / oldDist; 
                                  matrix.postScale(scale, scale, mid.x, mid.y); 
                          } 
                  } 
                  break; 
          } 

          view.setImageMatrix(matrix); 
          return true; // indicate event was handled 
	}
    private float spacing(MotionEvent event) { 
        float x = event.getX(0) - event.getX(1); 
        float y = event.getY(0) - event.getY(1); 
        return FloatMath.sqrt(x * x + y * y); 
} 


private void midPoint(PointF point, MotionEvent event) { 
        float x = event.getX(0) + event.getX(1); 
        float y = event.getY(0) + event.getY(1); 
        point.set(x / 2, y / 2); 
} 
}
