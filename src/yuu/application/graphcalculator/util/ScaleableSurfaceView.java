package yuu.application.graphcalculator.util;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * @author Yuu Momma
 *
 */
/**
 * @author Yuu Momma
 *
 */
public abstract class ScaleableSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	static final String DEBUG_LOG_TAG = "ScaleGesture";

	// 慣性の再描画間隔と減衰率
	private static final long REDRAW_INTERVAL = 15;
	private static final float INERTIAL_ATTENUATE_RATE = 0.92f;

	// 接触モード
	public static enum TouchMode {
		NONE,
		MOVE,
		SCALE,
	}

	// 非同期アニメーションのためのハンドラ
	private Handler mHandler = new Handler();

	// 現在のタッチスクリーンへの接触モード
	private TouchMode mTouchMode = TouchMode.NONE;

	// 画面幅
	private float mWidth;
	private float mHeight;

	// 現在の変換行列
	private Matrix mCurrentTransform = new Matrix();

	// 平行移動用
	private PointF mTouchedPoint = new PointF();
	private Matrix mSavedPosition = new Matrix();
	private PointF mPrevPoint = new PointF();
	private PointF mVelocity = new PointF();

	// 拡大縮小用
	private PointF mMiddlePoint = new PointF();
	private float mScaleFactor = 1.0f;
	private float mMinScale = 1.0f;
	private float mMaxScale = 10f;
	private ScaleGestureDetector mScaleDetector = null;
	private ScaleListener mGestureListener = new ScaleListener();

	public ScaleableSurfaceView(Context context) {
		super(context);

		mScaleDetector = new ScaleGestureDetector(context, mGestureListener);

		Display disp = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		mWidth = disp.getWidth();
		mHeight = disp.getHeight();

		getHolder().addCallback(this);
	}

	/**
	 * ドラッグおよび拡大縮小時に呼ばれるコールバック
	 */
	public abstract void updateView();

	public void fitToScreenCurrentTransform() {
		PointF point = getCurrentOffset();

		float offset_x = 0.0f;
		float offset_y = 0.0f;

		if (point.x > 0)
			offset_x = -point.x;
		else if (point.x < mWidth - mWidth * mScaleFactor)
			offset_x = (mWidth - mWidth * mScaleFactor) - point.x;

		if (point.y > 0)
			offset_y = -point.y;
		else if (point.y < mHeight - mHeight * mScaleFactor)
			offset_y = (mHeight - mHeight * mScaleFactor) - point.y;

		mCurrentTransform.postTranslate(offset_x, offset_y);
	}

	public void setMaxScale(float scale) {
		mMaxScale = scale;
	}

	public void setMinScale(float scale) {
		mMinScale = scale;
	}

	public float getCurrentScaleFactor() {
		return mHeight;
	}

	public PointF getCurrentOffset() {
		float[] point = {
				0, 0
		};
		mCurrentTransform.mapPoints(point);
		return new PointF(point[0], point[1]);
	}

	public Matrix getCurrentTransform() {
		return mCurrentTransform;
	}

	public TouchMode getCurrentTouchMode() {
		return mTouchMode;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mWidth = width;
		mHeight = height;
		mCurrentTransform.reset();
		mScaleFactor = 1.0f;
		updateView();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		updateView();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureListener.setEvent(event);
		mScaleDetector.onTouchEvent(event);
		drag(event);

		return true;
	}

	private void drag(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			Log.d(DEBUG_LOG_TAG, "onMoveBegin : " + event.getX() + "," + event.getY());
			mVelocity.set(0, 0);
			mPrevPoint.set(event.getX(), event.getY());
			mSavedPosition.set(mCurrentTransform);
			mTouchedPoint.set(event.getX(), event.getY());
			if (mTouchMode != TouchMode.SCALE)
				mTouchMode = TouchMode.MOVE;
			updateView();
			break;
		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP: {
			Log.d(DEBUG_LOG_TAG, "onMoveEnd : " + event.getX() + "," + event.getY());
			mTouchMode = TouchMode.NONE;
	
			mHandler.postDelayed(new Runnable() {
	
				@Override
				public void run() {
					mVelocity.set(mVelocity.x * INERTIAL_ATTENUATE_RATE, mVelocity.y
							* INERTIAL_ATTENUATE_RATE);
					if (-1 < mVelocity.x && mVelocity.x < 1) {
						mVelocity.x = 0;
					}
					if (-1 < mVelocity.y && mVelocity.y < 1) {
						mVelocity.y = 0;
					}
	
					mCurrentTransform.postTranslate(mVelocity.x, mVelocity.y);
					Log.d(DEBUG_LOG_TAG, "onInertial : " + mVelocity.x + "," + mVelocity.y);
	
					updateView();
	
					if (mTouchMode == TouchMode.NONE && mVelocity.x != 0 && mVelocity.y != 0)
						mHandler.postDelayed(this, REDRAW_INTERVAL);
				}
			}, REDRAW_INTERVAL);
	
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (mTouchMode == TouchMode.MOVE) {
				Log.d(DEBUG_LOG_TAG, "onMove : " + event.getX() + "," + event.getY());
				mCurrentTransform.set(mSavedPosition);
				float offset_x = event.getX() - mTouchedPoint.x;
				float offset_y = event.getY() - mTouchedPoint.y;
	
				mCurrentTransform.postTranslate(offset_x, offset_y);
	
				mVelocity.set(event.getX() - mPrevPoint.x, event.getY() - mPrevPoint.y);
				mPrevPoint.set(event.getX(), event.getY());
	
				updateView();
			}
			break;
		}
		}
	}

	private class ScaleListener extends SimpleOnScaleGestureListener {

		MotionEvent event = null;

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			Log.d(DEBUG_LOG_TAG, "onScaleBegin : " + detector.getScaleFactor());
			mTouchMode = TouchMode.SCALE;
			if (event != null)
				mMiddlePoint.set((event.getX(0) + event.getX(1)) / 2,
						(event.getY(0) + event.getY(1)) / 2);
			updateView();
			return super.onScaleBegin(detector);
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			Log.d(DEBUG_LOG_TAG, "onScaleEnd : " + detector.getScaleFactor());
			scaling(detector);

			mTouchMode = TouchMode.NONE;
			super.onScaleEnd(detector);
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			Log.d(DEBUG_LOG_TAG, "onScale : " + detector.getScaleFactor());
			scaling(detector);
			return true;
		}

		/**
		 * 上限下限付の拡大行列計算
		 *
		 * @param detector
		 */
		private void scaling(ScaleGestureDetector detector) {
			float scale = detector.getScaleFactor();
			float considered_scale = mScaleFactor * scale;
			if (considered_scale <= mMaxScale && considered_scale >= mMinScale) {
				mScaleFactor = considered_scale;
			} else if (considered_scale > mMaxScale) {
				scale = mMaxScale / mScaleFactor;
				mScaleFactor = mMaxScale;
			} else if (considered_scale < mMinScale) {
				scale = mMinScale / mScaleFactor;
				mScaleFactor = mMinScale;
			}
			mCurrentTransform.postScale(scale, scale, mMiddlePoint.x, mMiddlePoint.y);
			updateView();
		}

		public void setEvent(MotionEvent event) {
			this.event = event;
		}
	}
}