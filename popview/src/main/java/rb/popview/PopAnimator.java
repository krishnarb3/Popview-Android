package rb.popview;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.Random;

/**
 * Created by rb on 18/6/16.
 */
public class PopAnimator extends ValueAnimator {

	static long DEFAULT_DURATION = 0x500;
	private static final int PARTICLE_COUNT_FACTOR = 30;
	private static final int PARTICLE_MOVE_FACTOR = 10;
	private static final Interpolator DEFAULT_INTERPOLATOR = new DecelerateInterpolator(0.8f);
	private static final Interpolator FOLLOWUP_INTERPOLATOR = new AccelerateInterpolator(0.8f);
	private boolean mIsFollowedUp = false;
	private int defaultRadius;
	private Paint mPaint;
	private Rect mBound;
	private Particle[][] mParticles;
	private Bitmap mBitmap;
	private View mContainer;
	private boolean isFirstTime = true;
	float startX[][];
	float startY[][];
	long totalNoOfDrawsAdvance = DEFAULT_DURATION*60;	//For 60 fps
	long totalNoOfDrawsFollow = totalNoOfDrawsAdvance;
	long noOfDrawsAdvance = 0, noOfDrawsFollow = 0;

	public PopAnimator (View container, Bitmap bitmap, Rect bound) {
		mPaint = new Paint();
		mBound = new Rect(bound);
		Random random = new Random(System.currentTimeMillis());
		int noOfParticlesX =(int) (((float)bitmap.getWidth()/bitmap.getWidth())
													*(float)PARTICLE_COUNT_FACTOR);
		int noOfParticlesY =(int) (((float)bitmap.getHeight()/bitmap.getWidth())
													*(float)PARTICLE_COUNT_FACTOR);
		int bitMapWidth = bitmap.getWidth();
		int bitMapHeight = bitmap.getHeight();
		mParticles = new Particle[noOfParticlesY][noOfParticlesX];
		defaultRadius = ((bitMapWidth/(2*noOfParticlesX)) + (bitMapHeight/(2*noOfParticlesY)))/2;
		for(int i=0;i<noOfParticlesY;i++) {
			for(int j=0;j<noOfParticlesX;j++) {
				mParticles[i][j]
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

	public PopAnimator (View container, Bitmap bitmap, Rect bound, Particle[][] particles) {
		mPaint = new Paint();
		mBound = new Rect(bound);
		mParticles = particles;
		int noOfParticlesY = mParticles.length;
		int noOfParticlesX = mParticles[0].length;
		int bitMapWidth = bitmap.getWidth();
		int bitMapHeight = bitmap.getHeight();
		for(int i=0;i<noOfParticlesY;i++) {
			for(int j=0;j<noOfParticlesX;j++) {
				mParticles[i][j]
					= generateParticle( bitmap.getPixel((j*bitMapWidth/noOfParticlesX)
					, (i*bitMapHeight/noOfParticlesY))
					, mParticles[i][j].initialX
					, mParticles[i][j].initialY, mParticles[i][j]);

			}
		}

		startX = new float[mParticles.length][mParticles[0].length];
		startY = new float[mParticles.length][mParticles[0].length];
		mBitmap = bitmap;
		mContainer = container;
		mIsFollowedUp = true;
		setFloatValues(0f,1f);
		setInterpolator(FOLLOWUP_INTERPOLATOR);
		setDuration(DEFAULT_DURATION);
	}

	public Particle[][] getParticleValues () {
		return mParticles;
	}

	private Particle generateParticle (int color, int initialX, int initialY, Random random) {
		Particle particle = new Particle();
		particle.color = color;
		particle.initialX = initialX;
		particle.initialY = initialY;
		particle.random = random;
		particle.x = initialX;
		particle.y = initialY;
		float randRadius = random.nextFloat();
		float randSpeed = random.nextFloat()*5;
		particle.randSpeed = randSpeed;
		particle.radius = randRadius*defaultRadius;
		return particle;
	}

	private Particle generateParticle (int color, float initialX, float initialY, Particle initialParticle) {
		Particle particle = new Particle();
		particle.alpha = initialParticle.alpha;
		particle.color = color;
		particle.initialX = initialX;
		particle.initialY = initialY;
		particle.x = initialParticle.x;
		particle.y = initialParticle.y;
		particle.randSpeed = initialParticle.randSpeed;
		particle.radius = initialParticle.radius;
		return particle;
	}

	public boolean draw (Canvas canvas) {

		if(!isStarted()) {
			return false;
		}

		for (int i = 0; i < mParticles.length; i++) {
			for (int j = 0; j < mParticles[i].length; j++) {
				if (mIsFollowedUp) {
					noOfDrawsFollow++;
					if(noOfDrawsFollow >totalNoOfDrawsFollow) {
						mContainer.invalidate();
						return true;
					}
					if(isFirstTime) {
						startX[i][j] = mParticles[i][j].x;
						startY[i][j] = mParticles[i][j].y;
					}
					mParticles[i][j].followUp((float) getAnimatedValue(), mBound, mBitmap, PARTICLE_MOVE_FACTOR, startX[i][j], startY[i][j]);
					if (mParticles[i][j].alpha > 0f) {
						mPaint.setColor(mParticles[i][j].color);
						mPaint.setAlpha((int) (Color.alpha(mParticles[i][j].color) * mParticles[i][j].alpha));
						canvas.drawCircle(mParticles[i][j].x, mParticles[i][j].y, mParticles[i][j].radius, mPaint);
					}
				}
				else {
					noOfDrawsAdvance++;
					if(noOfDrawsAdvance> totalNoOfDrawsAdvance) {
						mContainer.invalidate();
						return true;
					}
					mParticles[i][j].advance((float) getAnimatedValue(), mBound, mBitmap, PARTICLE_MOVE_FACTOR);
					if (mParticles[i][j].alpha > 0f) {
						mPaint.setColor(mParticles[i][j].color);
						mPaint.setAlpha((int) (Color.alpha(mParticles[i][j].color) * mParticles[i][j].alpha));
						canvas.drawCircle(mParticles[i][j].x, mParticles[i][j].y, mParticles[i][j].radius, mPaint);
					}
				}
			}
		}

		isFirstTime = false;
		mContainer.invalidate();
		return true;
	}

	@Override
	public void start() {
		super.start();
		mContainer.invalidate(mBound);
	}

}
