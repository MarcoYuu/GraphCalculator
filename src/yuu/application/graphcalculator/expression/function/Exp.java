package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Exp extends Function{
	public Exp(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.exp(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new Exp(argument);
	}
}