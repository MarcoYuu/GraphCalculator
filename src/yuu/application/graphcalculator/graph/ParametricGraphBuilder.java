/**
 * 
 */
package yuu.application.graphcalculator.graph;

import yuu.application.graphcalculator.expression.Expression;

/**
 * @author Yuu
 *
 */
public class ParametricGraphBuilder extends SimpleGraphBuilder {
	
	public static final int PARAMETRIC_PATH =2;
	public static final int PARAMETRIC_BITMAP =3;
	
	public ParametricGraphBuilder(Expression x, Expression y) {
		setParametricExpression(x, y);
	}
	
	@Override
	public Graph createGraph(int type){
		switch(type){
		case PARAMETRIC_PATH:
			return new DefaultGraph(
					makeGraphPath(makeParametricCoordinateArray()), 
					makeAxisPath(), 
					makeGridPath(1.0f));
		case PARAMETRIC_BITMAP:
			return new DefaultBitmapGraph(
					makeGraphPath(makeParametricCoordinateArray()),
					makeAxisPath(), 
					makeGridPath(1.0f), 
					mWidth, mHeight);
		}
		return super.createGraph(type);
	}
}
