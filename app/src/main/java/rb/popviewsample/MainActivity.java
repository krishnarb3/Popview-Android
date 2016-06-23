package rb.popviewsample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rb.popview.PopField;

public class MainActivity extends AppCompatActivity {

	private PopField mPopField;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPopField = PopField.attach2Window(this);
		final TextView sampleTextView = (TextView) findViewById(R.id.textView1);
		sampleTextView.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View addView = layoutInflater.inflate(R.layout.newview, null);
				TextView newTextView =  (TextView)addView.findViewById(R.id.textView1);
				newTextView.setText("New Sample text");
				mPopField.popView(sampleTextView,addView,true);
			}
		});
		//addListener(findViewById(R.id.root));
	}

	private void addListener(View root) {
		if (root instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) root;
			for (int i = 0; i < parent.getChildCount(); i++) {
				addListener(parent.getChildAt(i));
			}
		} else {
			root.setClickable(true);
			root.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mPopField.popView(v);
					v.setOnClickListener(null);
				}
			});
		}
	}
}
