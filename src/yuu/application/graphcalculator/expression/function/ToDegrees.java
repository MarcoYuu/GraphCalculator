package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class ToDegrees extends Function{
	public ToDegrees(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.toDegrees(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new ToDegrees(argument);
	}
}