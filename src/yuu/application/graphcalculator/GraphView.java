package yuu.application.graphcalculator;

import yuu.application.graphcalculator.GrobalApplicationData.GraphData;
import yuu.application.graphcalculator.expression.Expression;
import yuu.application.graphcalculator.graph.DefaultBitmapGraph;
import yuu.application.graphcalculator.graph.Graph;
import yuu.application.graphcalculator.graph.ParametricGraphBuilder;
import yuu.application.graphcalculator.util.ScaleableSurfaceView;
import yuu.application.graphcalculator.util.Utils.FpsCounter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

public class GraphView extends ScaleableSurfaceView {

	public static enum GraphMode {
		PATH,
		BITMAP,
	}

	private GraphMode mGraphMode = GraphMode.BITMAP;
	private Graph mGraph = null;
	private FpsCounter mFpsCounter = new FpsCounter();

	public GraphView(Context context) {
		super(context);
	}

	@Override
	public void updateView() {

		SurfaceHolder holder = null;
		Canvas canvas = null;

		try {
			holder = getHolder();
			canvas = holder.lockCanvas();

			synchronized (holder) {
				if (mGraph != null) {

					canvas.drawColor(Color.WHITE);

					fitToScreenCurrentTransform();

					canvas.save();
					canvas.concat(getCurrentTransform());
					mGraph.setScaleFactor(getCurrentScaleFactor());
					mGraph.drawGraph(canvas);
					canvas.restore();

					mFpsCounter.countFPS();
				}
			}

		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void initByDataStruct(GraphData data) {
		Expression expr1 = GrobalApplicationData.getExpression();
		Expression expr2 = GrobalApplicationData.getSecondExpression();

		ParametricGraphBuilder builder = new ParametricGraphBuilder(expr1, expr2);
		builder.setExpression(expr1)
				.setScreenSize(data.window_width, data.window_height)
				.setAxisRange(data.min_axis, data.max_axis)
				.setAnalyzeRange(data.start, data.end, data.stride)
				.setValueAsPositiveInfinity(data.infinity)
				.setScaleAxisY(data.scale_y);

		mGraphMode = data.mode;

		if (expr2 == null) {
			if (mGraphMode == GraphMode.BITMAP)
				mGraph = builder.createGraph(ParametricGraphBuilder.SIMPLE_BITMAP);
			else
				mGraph = builder.createGraph(ParametricGraphBuilder.SIMPLE_PATH);
		} else {
			if (mGraphMode == GraphMode.BITMAP)
				mGraph = builder.createGraph(ParametricGraphBuilder.PARAMETRIC_BITMAP);
			else
				mGraph = builder.createGraph(ParametricGraphBuilder.PARAMETRIC_PATH);
		}

		mGraph.setScaleFactor(3.0f)
				.setLineWidth(data.line_width)
				.setAntiAlias(data.anti_alias);

		if (mGraphMode == GraphMode.BITMAP)
			((DefaultBitmapGraph) mGraph).repaintGraphBitmap(true);
	}

	public void saveGraphToSD(boolean save_as_png) {
		((DefaultBitmapGraph) mGraph).saveGraphToSD(save_as_png);
	}

	public Graph getGraph() {
		return mGraph;
	}

	public GraphMode getGraphMode() {
		return mGraphMode;
	}
}
