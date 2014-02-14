package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Tan extends Function{
	public Tan(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.tan(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new Tan(argument);
	}
}