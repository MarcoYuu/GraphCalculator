package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Cos extends Function{
	public Cos(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.cos(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new Cos(argument);
	}
}