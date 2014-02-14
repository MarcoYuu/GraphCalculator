package yuu.application.graphcalculator.graph;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.os.Environment;
import android.util.Log;

public class DefaultBitmapGraph extends DefaultGraph {

	private Bitmap mBitmap = null;

	private int mWidth = 0;
	private int mHeight = 0;
	private Matrix mScaleFactorMat = new Matrix();

	public DefaultBitmapGraph(Path graph, Path axis, Path grid, int width, int height) {
		super(graph, axis, grid);
		mWidth = width;
		mHeight = height;
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public void saveGraphToSD(boolean saveAsPng) {
		try {
			// SD カード/アプリ名 ディレクトリ生成
			File outDir = new File(Environment.getExternalStorageDirectory(), "GraphCalcurator");
			// パッケージ名のディレクトリが SD カードになければ作成します。
			if (outDir.exists() == false) {
				outDir.mkdir();
			}

			// 日付でファイル名を作成　
			Date mDate = new Date();
			SimpleDateFormat fileName = new SimpleDateFormat("yyyyMMdd_HHmmss");

			// 保存
			if (saveAsPng) {
				FileOutputStream fos = null;
				fos = new FileOutputStream(new File(outDir, "graph_" + fileName.format(mDate)
						+ ".png"));
				mBitmap.compress(CompressFormat.PNG, 100, fos);
				fos.close();
			} else {
				FileOutputStream fos = null;
				fos = new FileOutputStream(new File(outDir, "graph_" + fileName.format(mDate)
						+ ".jpg"));
				mBitmap.compress(CompressFormat.JPEG, 100, fos);
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error", "" + e.toString());
		}
	}

	public void repaintGraphBitmap() {
		repaintGraphBitmap(true);
	}

	public void repaintGraphBitmap(boolean draw_grid) {
		refreshBitmap();
		mBitmap = Bitmap.createBitmap(
				(int) (mWidth * mScaleFactor),
				(int) (mHeight * mScaleFactor),
				Bitmap.Config.ARGB_4444
				);
		Canvas canvas = new Canvas(mBitmap);
		setLineWidth(mLineWidth * mScaleFactor);
		canvas.drawColor(Color.WHITE);

		Matrix matrix = new Matrix();
		matrix.setScale(mScaleFactor, mScaleFactor);
		mAxis.transform(matrix);
		mGraph.transform(matrix);
		mGrid.transform(matrix);
		matrix = null;

		if (draw_grid)
			super.drawGrid(canvas);
		super.drawAxis(canvas);
		super.drawGraph(canvas);

		mScaleFactorMat.setScale(1 / mScaleFactor, 1 / mScaleFactor);

		canvas = null;
		System.gc();
	}

	public void refreshBitmap() {
		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
		System.gc();
	}

	@Override
	public Graph drawGraph(Canvas canvas) {
		canvas.save();
		canvas.drawBitmap(mBitmap, mScaleFactorMat, null);
		canvas.restore();
		return this;
	}

	@Override
	public Graph drawAxis(Canvas canvas) {
		return this;
	}

	@Override
	public Graph drawGrid(Canvas canvas) {
		return this;
	}

}
