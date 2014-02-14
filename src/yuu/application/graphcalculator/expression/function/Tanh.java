package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Tanh extends Function{
	public Tanh(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.tanh(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new Tanh(argument);
	}
}