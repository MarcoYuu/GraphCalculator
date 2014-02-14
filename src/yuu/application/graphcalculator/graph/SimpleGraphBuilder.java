package yuu.application.graphcalculator.graph;


import yuu.application.graphcalculator.expression.Expression;

public class SimpleGraphBuilder extends GraphBuilder {
	public static final int SIMPLE_PATH =0;
	public static final int SIMPLE_BITMAP =1;
	
	public SimpleGraphBuilder(){
	}
	
	public SimpleGraphBuilder(Expression expression){
		setExpression(mExpression);
	}

	@Override
	public Graph createGraph(){
		return new DefaultGraph(
				makeGraphPath(makeCoordinateArray()), 
				makeAxisPath(), 
				makeGridPath(1.0f));
	}
	
	@Override
	public Graph createGraph(int type){
		switch(type){
		case SIMPLE_PATH:
			return new DefaultGraph(
					makeGraphPath(makeCoordinateArray()), 
					makeAxisPath(), 
					makeGridPath(1.0f));
		case SIMPLE_BITMAP:
			return new DefaultBitmapGraph(
					makeGraphPath(makeCoordinateArray()), 
					makeAxisPath(), 
					makeGridPath(1.0f), 
					mWidth, mHeight);
		}
		return null;
	}
}
