package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Sin extends Function{
	public Sin(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.sin(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new Sin(argument);
	}
}