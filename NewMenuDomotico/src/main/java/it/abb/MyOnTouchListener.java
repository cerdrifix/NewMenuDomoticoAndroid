package it.abb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

class MyOnTouchListener implements OnTouchListener {

    public boolean onTouch(View v, MotionEvent e){
    	if(v instanceof ImageButton) {
    		ImageButton ib = (ImageButton)v;
			int tag = Integer.parseInt(ib.getTag(R.id.TAG_IMAGE_ID).toString());
			
			Bitmap Image = BitmapFactory.decodeResource(v.getContext().getResources(), tag);
			Image = Image.copy(Bitmap.Config.ARGB_8888,true);
			Paint paint = new Paint();
			paint.setDither(true);
			paint.setFilterBitmap(true);
			Bitmap glow = BitmapFactory.decodeResource(v.getContext().getResources(), R.drawable.white_glow);
			Bitmap bitmap=Bitmap.createBitmap(Image.getWidth(),Image.getHeight(), Config.ARGB_8888);
			Canvas canvas=new Canvas(bitmap);
			
			int margin = 5;
			
			canvas.drawBitmap(glow, new Rect(0,0,glow.getWidth(),glow.getHeight()), new Rect( (Image.getWidth() - glow.getWidth()) - margin, (Image.getHeight() - glow.getHeight()) - margin - 3, glow.getWidth() + margin,glow.getHeight() + margin),paint);
			canvas.drawBitmap(Image, new Rect(0,0,Image.getWidth(),Image.getHeight()), new Rect(0+4,0+4,Image.getWidth()-4,Image.getHeight()-4),paint); 
			
			ib.setImageBitmap(bitmap);
    	}
    	else if(v instanceof TextView){
    		TextView tv = (TextView)v;
    		
    		tv.setBackgroundDrawable(v.getContext().getResources().getDrawable(R.drawable.custom_shape_3));
    	}
		
		return false;
    }

    public static void resetTouch(View v) {
    	ImageButton ib = (ImageButton)v;
		int tag = Integer.parseInt(ib.getTag(R.id.TAG_IMAGE_ID).toString());
		
		Bitmap Image = BitmapFactory.decodeResource(v.getContext().getResources(), tag);
		Image = Image.copy(Bitmap.Config.ARGB_8888,true);
		Paint paint = new Paint();
		paint.setDither(true);
		paint.setFilterBitmap(true);
		Bitmap bitmap=Bitmap.createBitmap(Image.getWidth(),Image.getHeight(), Config.ARGB_8888);
		Canvas canvas=new Canvas(bitmap);
		
		canvas.drawBitmap(Image, new Rect(0,0,Image.getWidth(),Image.getHeight()), new Rect(0,0,Image.getWidth(),Image.getHeight()),paint);
		
		ib.setImageBitmap(bitmap);
    }
}