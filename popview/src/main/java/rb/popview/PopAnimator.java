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

	static long DEFAULT_DURATION = 0x500;
	private static final int PARTICLE_COUNT_FACTOR = 20;
	private static final int PARTICLE_MOVE_FACTOR = 10;
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

		int noOfParticlesX =(int) (((float)bitmap.getWidth()/bitmap.getWidth())*(float)PARTICLE_COUNT_FACTOR);
		int noOfParticlesY =(int) (((float)bitmap.getHeight()/bitmap.getWidth())*(float)PARTICLE_COUNT_FACTOR);
		int totalNoOfParticles = noOfParticlesX*noOfParticlesY;
		int bitMapWidth = bitmap.getWidth();
		int bitMapHeight = bitmap.getHeight();
		mParticles = new Particle[totalNoOfParticles];
		defaultRadius = ((bitMapWidth/(2*noOfParticlesX)) + (bitMapHeight/(2*noOfParticlesY)))/2;
		for(int i=0;i<noOfParticlesY;i++) {
			for(int j=0;j<noOfParticlesX;j++) {
				mParticles[ noOfParticlesX*i +j ]
					= generateParticle( bitmap.getPixel((j*bitMapWidth/noOfParticlesX)
						, (i*bitMapHeight/noOfParticlesY))
						, mBound.left+(j*defaultRadius*2)
						, mBound.top+(i*defaultRadius*2), random);

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
		float randSpeed = random.nextFloat()*5;
		particle.randSpeed = randSpeed;
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
		float randSpeed;
		float initialX;
		float initialY;
		float x;
		float y;
		float alpha;

			public void advance (float factor) {
				radius = (1f-(factor/40))*radius;
				alpha = 1f-factor;
					x = x+randSpeed*((initialX - (mBound.left+mBitmap.getWidth()/2))/(mBitmap.getWidth()/2))*(PARTICLE_MOVE_FACTOR);
					y = y+randSpeed*((initialY - (mBound.top+mBitmap.getHeight()/2))/(mBitmap.getHeight()/2))*(PARTICLE_MOVE_FACTOR);
			}
	}
}
