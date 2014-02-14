package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Negater extends Function{
	public Negater(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return -argument().evaluate(x);
	}
	public static Expression create(Expression argument){
		return new Negater(argument);
	}
}