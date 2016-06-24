package rb.popview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

/**
 * Created by rb on 18/6/16.
 */
public class PopField extends View {

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

	private void popView(final Bitmap bitmap, final Rect bound
		, final long startDelay, final long duration) {
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


	private void popView(final Bitmap bitmap, final Bitmap addBitmap
		, final View view, final View addView, final Rect bound
		, final long startDelay, final long duration) {

		final PopAnimator pop = new PopAnimator(this, bitmap, bound);
		final View currentView = this;
		pop.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				PopAnimator popNext = new PopAnimator(currentView, addBitmap, bound, pop.getParticleValues());
				popNext.addListener(new AnimatorListenerAdapter() {
					@Override public void onAnimationEnd(Animator animation) {
						ViewGroup parentViewGroup = (ViewGroup) view.getParent();
						int index = parentViewGroup.indexOfChild(view);
						parentViewGroup.removeView(view);
						parentViewGroup.addView(addView, index);
					}
				});
				popNext.setStartDelay(startDelay);
				popNext.setDuration(duration);
				mPopViews.add(popNext);
				popNext.start();
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
		int startDelay = 0;
		view.animate().setDuration(150).setStartDelay(startDelay)
			.scaleX(0f).scaleY(0f).alpha(0f).start();
		popView(PopUtils.createBitmapFromView(view), r, startDelay, PopAnimator.DEFAULT_DURATION);
	}

	public void popView (final View view, View addView) {
		Rect r = new Rect();
		view.getGlobalVisibleRect(r);
		int[] location = new int[2];
		getLocationOnScreen(location);
		r.offset(-location[0], -location[1]);
		int startDelay = 0;
		view.animate().setDuration(150).setStartDelay(startDelay)
			.scaleX(0f).scaleY(0f).alpha(0f).start();
		popView(PopUtils.createBitmapFromView(view), r, startDelay, PopAnimator.DEFAULT_DURATION);
		ViewGroup parentViewGroup = (ViewGroup) view.getParent();
		int index = parentViewGroup.indexOfChild(view);
		parentViewGroup.removeView(view);
		parentViewGroup.addView(addView, index);
	}

	public void popView (final View view, View addView, boolean isAnimated) {
		if(isAnimated) {
			Rect r = new Rect();
			view.getGlobalVisibleRect(r);
			int[] location = new int[2];
			getLocationOnScreen(location);
			r.offset(-location[0], -location[1]);
			long startDelay = 0;
			view.animate().setDuration(150).setStartDelay(startDelay)
				.scaleX(0f).scaleY(0f).alpha(0f).start();
			popView(PopUtils.createBitmapFromView(view), PopUtils.createBitmapFromView(addView)
				, view, addView, r, startDelay, PopAnimator.DEFAULT_DURATION);
		}
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
