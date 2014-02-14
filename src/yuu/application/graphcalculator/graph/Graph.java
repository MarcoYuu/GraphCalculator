package yuu.application.graphcalculator.graph;

import android.graphics.Canvas;

public interface Graph {
	public Graph drawGraph(Canvas canvas);
	public Graph drawAxis(Canvas canvas);
	public Graph drawGrid(Canvas canvas);
	
	public Graph setColor(int color);
	public Graph setLineWidth(float width);
	public Graph setScaleFactor(float scale);
	public Graph setAntiAlias(boolean aa);
}
