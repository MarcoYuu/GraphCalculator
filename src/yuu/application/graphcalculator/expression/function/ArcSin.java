package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class ArcSin extends Function{
	public ArcSin(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.asin(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new ArcSin(argument);
	}
}