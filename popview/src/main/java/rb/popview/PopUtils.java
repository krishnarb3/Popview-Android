package rb.popview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by rb on 18/6/16.
 */
public class PopUtils {
	private PopUtils() {
	}

	private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
	private static final Canvas sCanvas = new Canvas();

	public static int dp2Px(int dp) {
		return Math.round(dp * DENSITY);
	}

	public static Bitmap createBitmapFromView(View view) {

		if (view.getMeasuredHeight() <= 0) {
			int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			view.measure(spec, spec);
			view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
			Bitmap bitmap = createBitmapSafely(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888, 1);
			if (bitmap != null) {
				synchronized (sCanvas) {
					Canvas canvas = sCanvas;
					canvas.setBitmap(bitmap);
					view.draw(canvas);
					canvas.setBitmap(null);
				}
			}
			return bitmap;
		}

		view.clearFocus();
		Bitmap bitmap = createBitmapSafely(view.getWidth(),
			view.getHeight(), Bitmap.Config.ARGB_8888, 1);
		if (bitmap != null) {
			synchronized (sCanvas) {
				Canvas canvas = sCanvas;
				canvas.setBitmap(bitmap);
				view.draw(canvas);
				canvas.setBitmap(null);
			}
		}
		return bitmap;
	}

	public static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
		try {
			return Bitmap.createBitmap(width, height, config);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			if (retryCount > 0) {
				System.gc();
				return createBitmapSafely(width, height, config, retryCount - 1);
			}
			return null;
		}
	}
}
