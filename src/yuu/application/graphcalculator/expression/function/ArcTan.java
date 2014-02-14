package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class ArcTan extends Function{
	public ArcTan(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.atan(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new ArcTan(argument);
	}
}