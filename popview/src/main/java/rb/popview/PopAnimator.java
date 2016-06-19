package rb.popview;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.Random;

/**
 * Created by rb on 18/6/16.
 */
public class PopAnimator extends ValueAnimator {

	static long DEFAULT_DURATION = 0x4000;
	private static final int PARTICLE_COUNT_FACTOR = 30;
	private static final int PARTICLE_MOVE_FACTOR = 2;
	private static final Interpolator DEFAULT_INTERPOLATOR = new DecelerateInterpolator(0.8f);
	private int defaultRadius;
	private Paint mPaint;
	private Rect mBound;
	private Particle[] mParticles;
	private Bitmap mBitmap;
	private View mContainer;

	public PopAnimator (View container, Bitmap bitmap, Rect bound) {
		mPaint = new Paint();
		mBound = new Rect(bound);
		Random random = new Random(System.currentTimeMillis());

		int noOfParticlesX = bitmap.getWidth()/PARTICLE_COUNT_FACTOR;
		int noOfParticlesY = bitmap.getHeight()/PARTICLE_COUNT_FACTOR;
		int totalNoOfParticles = noOfParticlesX*noOfParticlesY;
		int bitMapWidth = bitmap.getWidth();
		int bitMapHeight = bitmap.getHeight();
		mParticles = new Particle[totalNoOfParticles];
		defaultRadius = ((bitMapWidth/noOfParticlesX) + (bitMapHeight/noOfParticlesY))/2;
		for(int i=0;i<noOfParticlesY;i++) {
			for(int j=0;j<noOfParticlesX;j++) {
				mParticles[ noOfParticlesX*i +j ]
					= generateParticle( bitmap.getPixel((j*bitMapWidth/noOfParticlesX)
						, (i*bitMapHeight/noOfParticlesY))
						, (j*bitMapWidth/noOfParticlesX), (i*bitMapHeight/noOfParticlesY), random);

			}
		}
		mBitmap = bitmap;
		mContainer = container;
		setFloatValues(0f,1f);
		setInterpolator(DEFAULT_INTERPOLATOR);
		setDuration(DEFAULT_DURATION);
	}



	private Particle generateParticle (int color, int initialX, int initialY, Random random) {
		Particle particle = new Particle();
		particle.color = color;
		particle.initialX = initialX;
		particle.initialY = initialY;
		particle.x = initialX;
		particle.y = initialY;
		float randRadius = random.nextFloat();
		if(randRadius<0.5f)
			randRadius = 0.5f+randRadius;
		particle.radius = randRadius*defaultRadius;
		return particle;
	}

	public boolean draw (Canvas canvas) {
		if(!isStarted()) {
			return false;
		}
		for(Particle particle : mParticles) {
			particle.advance((float)getAnimatedValue());
			if(particle.alpha > 0f) {
				mPaint.setColor(particle.color);
				mPaint.setAlpha((int) (Color.alpha(particle.color) * particle.alpha));
				canvas.drawCircle(particle.x, particle.y, particle.radius, mPaint);
			}
		}
		mContainer.invalidate();
		return true;
	}

	@Override
	public void start() {
		super.start();
		mContainer.invalidate(mBound);
	}

	private class Particle {
		int color;
		float radius;
		float initialX;
		float initialY;
		float x;
		float y;
		float alpha;

			public void advance (float factor) {
				radius = (1f-(factor/2))*radius;
				alpha = 1f-factor;
				if(initialX<(mBitmap.getWidth()/2))
					x = x-((mBitmap.getWidth()/2 - initialX)/(mBitmap.getWidth()/2))*(PARTICLE_MOVE_FACTOR);
				else if(initialX>((mBitmap.getWidth()/2)))
					x = x+((initialX - (mBitmap.getWidth()/2))/(mBitmap.getWidth()/2))*(PARTICLE_MOVE_FACTOR);
				if(initialY<(mBitmap.getHeight()/2))
					y = y-((mBitmap.getHeight()/2 - initialY)/(mBitmap.getHeight()/2))*(PARTICLE_MOVE_FACTOR);
				else if(initialY>(mBitmap.getHeight()/2))
					y = y+((initialY - mBitmap.getHeight()/2)/(mBitmap.getHeight()/2))*(PARTICLE_MOVE_FACTOR);
			}
	}
}
