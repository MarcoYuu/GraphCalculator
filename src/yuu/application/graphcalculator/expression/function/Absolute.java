package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Absolute extends Function{
	public Absolute(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.abs(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new Absolute(argument);
	}
}