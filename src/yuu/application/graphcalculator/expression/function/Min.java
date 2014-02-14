package yuu.application.graphcalculator.expression.function;

import yuu.application.graphcalculator.expression.Expression;

public class Min extends Function{
	public Min(Expression a, Expression b){
		super(a,b);
	}
	public double evaluate(double x) {
		return Math.min(argument().evaluate(x),argument2().evaluate(x));
	}
	public static  Expression create(Expression a,  Expression b){
		return new Min(a,b);
	}
}