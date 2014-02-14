package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Cosh extends Function{
	public Cosh(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.cosh(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new Cosh(argument);
	}
}