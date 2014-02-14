package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class ToRadians extends Function{
	public ToRadians(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.toRadians(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new ToRadians(argument);
	}
}