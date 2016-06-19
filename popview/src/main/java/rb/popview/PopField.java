package rb.popview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by rb on 18/6/16.
 */
public class PopField extends View{

	private List<PopAnimator> mPopViews = new ArrayList<>();
	private int[] mExpandInset = new int[2];


	public PopField(Context context) {
		super(context);
		init();
	}

	public PopField(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PopField(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		Arrays.fill(mExpandInset, PopUtils.dp2Px(32));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (PopAnimator popView : mPopViews) {
			popView.draw(canvas);
		}
	}

	public void expandPopViewBound(int dx, int dy) {
		mExpandInset[0] = dx;
		mExpandInset[1] = dy;
	}

	public void popView(Bitmap bitmap, Rect bound, long startDelay, long duration) {
		final PopAnimator pop = new PopAnimator(this, bitmap, bound);
		pop.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mPopViews.remove(animation);
			}
		});
		pop.setStartDelay(startDelay);
		pop.setDuration(duration);
		mPopViews.add(pop);
		pop.start();
	}

	public void popView (final View view) {
		Rect r = new Rect();
		view.getGlobalVisibleRect(r);
		int[] location = new int[2];
		getLocationOnScreen(location);
		r.offset(-location[0], -location[1]);
		//r.inset(-mExpandInset[0], -mExpandInset[1]);
		int startDelay = 100;
		ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(150);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			Random random = new Random();

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				view.setTranslationX((random.nextFloat() - 0.5f) * view.getWidth() * 0.05f);
				view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.05f);

			}
		});
		animator.start();
		view.animate().setDuration(150).setStartDelay(startDelay).scaleX(0f).scaleY(0f).alpha(0f).start();
		popView(PopUtils.createBitmapFromView(view), r, startDelay, PopAnimator.DEFAULT_DURATION);
	}

	public void clear() {
		mPopViews.clear();
		invalidate();
	}

	public static PopField attach2Window(Activity activity) {
		ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
		PopField explosionField = new PopField(activity);
		rootView.addView(explosionField, new ViewGroup.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		return explosionField;
	}
}
