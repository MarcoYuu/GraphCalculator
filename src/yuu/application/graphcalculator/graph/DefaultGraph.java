package yuu.application.graphcalculator.graph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;

public class DefaultGraph implements Graph {

	private Paint mPaint = new Paint();
	private boolean mAntiAlias = false;

	protected Path mGraph = null;
	protected Path mAxis = null;
	protected Path mGrid = null;

	protected int mColor = Color.BLUE;
	protected float mLineWidth = 1.0f;
	protected float mScaleFactor = 1.0f;

	DefaultGraph(Path graph, Path axis, Path grid) {
		mGraph = graph;
		mAxis = axis;
		mGrid = grid;
	}

	@Override
	public Graph setAntiAlias(boolean aa) {
		mAntiAlias = aa;
		return this;
	}

	@Override
	public Graph drawGraph(Canvas canvas) {
		Paint paint = mPaint;
		paint.reset();
		paint.setStrokeWidth(mLineWidth / mScaleFactor);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeJoin(Join.ROUND);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(mAntiAlias);
		paint.setColor(mColor);

		canvas.drawPath(mGraph, paint);
		return this;
	}

	@Override
	public Graph drawAxis(Canvas canvas) {
		Paint paint = mPaint;
		paint.reset();
		paint.setStrokeWidth(mLineWidth / mScaleFactor);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(mAntiAlias);
		paint.setStyle(Style.STROKE);

		canvas.drawPath(mAxis, paint);
		return this;
	}

	@Override
	public Graph drawGrid(Canvas canvas) {
		Paint paint = mPaint;
		paint.reset();
		paint.setStrokeWidth(mLineWidth * 0.5f / mScaleFactor);
		paint.setColor(Color.LTGRAY);
		paint.setAntiAlias(mAntiAlias);
		paint.setStyle(Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[] {
				30.0f, 10.0f
		}, 0));

		canvas.drawPath(mGrid, paint);
		return this;
	}

	@Override
	public Graph setColor(int color) {
		mColor = color;
		return this;
	}

	@Override
	public Graph setLineWidth(float width) {
		mLineWidth = width;
		return this;
	}

	@Override
	public Graph setScaleFactor(float scale) {
		mScaleFactor = scale;
		return this;
	}
}