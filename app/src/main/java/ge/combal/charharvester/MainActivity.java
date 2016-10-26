package ge.combal.charharvester;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

	Paint mPaint;
	DrawingView dv;
	TextView labelView;
	LinearLayout linearLayout;
	LinearLayout labelLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		linearLayout = (LinearLayout) findViewById(R.id.drawingContainer);
		labelView = (TextView) findViewById(R.id.label);
		labelLayout = (LinearLayout) findViewById(R.id.labelLayout);

		mPaint = new Paint();
		dv = new DrawingView(this, mPaint);
		linearLayout.addView(dv, 0);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(32);

		labelView.setText(LabelGenerator.getNext());

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
				nextChar();
			}
		});
	}

	private void nextChar(){

		if(!isConnectedToInternet()) {
			showToast("No Internet Connection");
			return ;
		}

		if(!dv.isDirty()){
			return;
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			dv.writeTo(baos);
			System.out.println("baos size is : " + baos.size());
			SendImageTask task = new SendImageTask(this, LabelGenerator.getCurrent(), baos);
			task.execute();
			labelView.setText(LabelGenerator.getNext());
			int[] colors = nextColor();
			labelLayout.setBackgroundColor(colors[0]);
			labelView.setTextColor(colors[1]);
		} catch (Exception e){
			e.printStackTrace();
		}
		dv.clean();
	}

	public boolean isConnectedToInternet(){
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {

			return true;
		}
		return false;
	}

	public void showToast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private int[] nextColor(){
		Random rnd = new Random();
		int r = rnd.nextInt(256);
		int g = rnd.nextInt(256);
		int b = rnd.nextInt(256);
		int threshold = 130;
		System.out.println("r: " + r + ", g: " + g + ", b: " + b);
		int textColor = Color.BLACK;
		if(r < threshold && g < threshold && b < threshold){
			textColor = Color.WHITE;
		}
		int[] colors = {Color.argb(255, r, g, b), textColor};
		return colors;
	}
}
