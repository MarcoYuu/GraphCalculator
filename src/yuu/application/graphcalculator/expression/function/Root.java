package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Root extends Function{
	public Root(Expression argument){
		super(argument);
	}
	public double evaluate(double x) {
		return Math.sqrt(argument().evaluate(x));
	}
	public static Expression create(Expression argument){
		return new Root(argument);
	}
}