package rb.popviewsample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import rb.popview.PopField;

public class MainActivity extends AppCompatActivity {

	private PopField mPopField;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPopField = PopField.attach2Window(this);

		final TextView sampleTextView = (TextView) findViewById(R.id.textview1);
		sampleTextView.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View addView = layoutInflater.inflate(R.layout.sampletextview, null);
				TextView newTextView = (TextView) addView.findViewById(R.id.sampletextview);
				newTextView.setText("New Sample text");
				mPopField.popView(sampleTextView, addView, true);
			}
		});

		final ImageView imageView1 = (ImageView) findViewById(R.id.imageview1);
		imageView1.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				mPopField.popView(imageView1);
			}
		});

		final ImageView imageView2 = (ImageView) findViewById(R.id.imageview2);
		imageView2.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View addView = layoutInflater.inflate(R.layout.sampleimageview, null);
				ImageView newImageView = (ImageView) addView.findViewById(R.id.sampleimageview);
				newImageView.setImageDrawable(getResources().getDrawable(R.drawable.p5));
				mPopField.popView(imageView2, addView);
			}
		});

		final ImageView imageView3 = (ImageView) findViewById(R.id.imageview3);
		imageView3.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View addView = layoutInflater.inflate(R.layout.sampleimageview, null);
				ImageView newImageView = (ImageView) addView.findViewById(R.id.sampleimageview);
				newImageView.setImageDrawable(getResources().getDrawable(R.drawable.p3));
				mPopField.popView(imageView3, addView, true);
			}
		});
	}
}
