package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Sinh extends Function{
	public Sinh(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.sinh(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new Sinh(argument);
	}
}