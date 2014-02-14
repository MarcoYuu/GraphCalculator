package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class ArcCos extends Function{
	public ArcCos(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.acos(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new ArcCos(argument);
	}
}