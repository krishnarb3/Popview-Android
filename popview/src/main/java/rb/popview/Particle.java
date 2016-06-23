package rb.popview;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by rb on 20/6/16.
 */
public class Particle {
	int color;
	Random random;
	float radius;
	float randSpeed;
	float initialX;
	float initialY;
	float x;
	float y;
	float alpha;

	public void advance (float factor, Rect bound, Bitmap bitmap, int moveFactor) {
		radius = (1f-(factor/40f))*radius;
		alpha = 1f-factor;
		x = x+randSpeed*((initialX - (bound.left+bitmap.getWidth()/2))/(bitmap.getWidth()/2))*(moveFactor);
		y = y+randSpeed*((initialY - (bound.top+bitmap.getHeight()/2))/(bitmap.getHeight()/2))*(moveFactor);
	}

	public void followUp (float factor, Rect bound, Bitmap bitmap, int moveFactor, float startX, float startY) {
		radius = radius/(1f-(factor/40f));
		alpha = factor;
		if(startX>initialX && x>initialX || startX<initialX && x<initialX)
			x = x-randSpeed*((initialX - (bound.left+bitmap.getWidth()/2))/(bitmap.getWidth()/2))*(moveFactor);
		if(startY>initialY && y>initialY || startY<initialY && y<initialY)
			y = y-randSpeed*((initialY - (bound.top+bitmap.getHeight()/2))/(bitmap.getHeight()/2))*(moveFactor);
	}
}