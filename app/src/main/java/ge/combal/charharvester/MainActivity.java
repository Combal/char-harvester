package ge.combal.charharvester;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	Paint mPaint;
	DrawingView dv;
	TextView labelView;
	LinearLayout linearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		linearLayout = (LinearLayout) findViewById(R.id.drawingContainer);
		labelView = (TextView) findViewById(R.id.label);

		mPaint = new Paint();
		dv = new DrawingView(this, mPaint);
		linearLayout.addView(dv, 0);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(12);

		nextChar();

		Button nextBtn = (Button) findViewById(R.id.next);
		Button resetBtn = (Button) findViewById(R.id.reset);

		resetBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dv.clean();
			}
		});

		nextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dv.clean();
				nextChar();
			}
		});
	}

	private void nextChar(){
		// TODO: 10/3/16 save image
		labelView.setText(LabelGenerator.next());
	}
}
