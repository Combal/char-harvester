package ge.combal.charharvester;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import java.io.OutputStream;

/**
 * Created by vano on 10/3/16.
 */

public class DrawingView extends View {
	public int width;
	public int height;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	Context context;
	private Paint circlePaint;
	private Path circlePath;
	private Paint mPaint;
	private static boolean dirty = false;

	public DrawingView(Context c, Paint mPaint) {
		super(c);
		context = c;
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		circlePaint = new Paint();
		circlePath = new Path();
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(Color.BLUE);
		circlePaint.setStyle(Paint.Style.STROKE);
		circlePaint.setStrokeJoin(Paint.Join.MITER);
		circlePaint.setStrokeWidth(4f);
		this.mPaint = mPaint;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		mCanvas = new Canvas(mBitmap);
		clean();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath(mPath, mPaint);
		canvas.drawPath(circlePath, circlePaint);
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;

			circlePath.reset();
			circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		circlePath.reset();
		// commit the path to our offscreen
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				dirty = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
		}
		return true;
	}

	public void clean(){
		mCanvas.drawColor(Color.WHITE);
		this.requestLayout();
		dirty = false;
	}

	public Bitmap getBitmap(){
		return this.mBitmap;
	}

	public void writeTo(OutputStream os){
		if(mBitmap == null) return ;
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}
}
