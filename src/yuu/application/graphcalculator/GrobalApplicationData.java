package yuu.application.graphcalculator;

import yuu.application.graphcalculator.GraphView.GraphMode;
import yuu.application.graphcalculator.expression.Expression;

class GrobalApplicationData {

	private volatile static Expression mExpression = null;
	private volatile static Expression mSecondExpression = null;
	private volatile static GraphData mGraphData = null;

	private GrobalApplicationData() {
	}

	/**
	 * 現在登録されている式を取得する.
	 *
	 * @return 登録されている式
	 */
	public static Expression getExpression() {
		return mExpression;
	}

	/**
	 * 式の登録.
	 *
	 * @param expression
	 *            登録する式
	 */
	public static void setExpression(Expression expression) {
		mExpression = expression;
	}

	/**
	 * 現在登録されている式を取得する.
	 *
	 * @return 登録されている式
	 */
	public static Expression getSecondExpression() {
		return mSecondExpression;
	}

	/**
	 * 式の登録.
	 *
	 * @param expression
	 *            登録する式
	 */
	public static void setSecondExpression(Expression expression) {
		mSecondExpression = expression;
	}

	/**
	 * 現在登録されている描画設定を取得する.
	 *
	 * @return 登録されている描画設定
	 */
	public static GraphData getGraphData() {
		return mGraphData;
	}

	/**
	 * 描画設定の登録.
	 *
	 * @param expression
	 *            登録する描画設定
	 */
	public static void setGraphData(GraphData graphData) {
		mGraphData = graphData;
	}

	public static class GraphData {

		public GraphMode mode = GraphMode.BITMAP;
		public int window_width = 480;
		public int window_height = 800;
		public float min_axis = -10;
		public float max_axis = 10;
		public float start = -10;
		public float end = 10;
		public float stride = 0.01f;
		public float line_width = 2.0f;
		public float infinity = 10e16f;
		public float scale_y = 1.0f;
		public boolean save_graph = false;
		public boolean save_as_png = false;
		public boolean anti_alias = false;
	}
}
