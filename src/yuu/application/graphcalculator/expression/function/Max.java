package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Max extends Function{
	public Max(Expression a, Expression b){
		super(a,b);
	}
	public double evaluate(double x) {
		return Math.max(argument().evaluate(x),argument2().evaluate(x));
	}
	public static  Expression create(Expression a,  Expression b){
		return new Max(a,b);
	}
}